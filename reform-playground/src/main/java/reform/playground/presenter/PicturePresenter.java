package reform.playground.presenter;

import reform.core.analyzer.ProjectAnalyzer;
import reform.core.forms.Form;
import reform.core.graphics.DrawingType;
import reform.core.pool.Pool;
import reform.core.pool.SimplePool;
import reform.core.procedure.instructions.Instruction;
import reform.core.procedure.instructions.NullInstruction;
import reform.core.runtime.Evaluable;
import reform.core.runtime.ProjectRuntime;
import reform.core.runtime.errors.RuntimeError;
import reform.evented.core.EventedPicture;
import reform.evented.core.EventedProcedure;
import reform.identity.FastIterable;
import reform.identity.Identifier;
import reform.identity.IdentifierEmitter;
import reform.math.Vec2i;
import reform.playground.actions.*;
import reform.playground.listener.FocusAdjustmentProcedureListener;
import reform.playground.listener.SelectionAdjustmentProcedureListener;
import reform.playground.views.procedure.ProcedureView;
import reform.playground.views.sheet.SheetPresenter;
import reform.rendering.icons.*;
import reform.rendering.icons.swing.SwingIcon;
import reform.stage.Stage;
import reform.stage.StageCollector;
import reform.stage.elements.Entity;
import reform.stage.elements.EntityPoint;
import reform.stage.elements.SnapPoint;
import reform.stage.elements.outline.IntersectionSnapPoint;
import reform.stage.hittest.HitTester;
import reform.stage.tooling.*;
import reform.stage.tooling.cursor.Cursor;
import reform.stage.tooling.factory.Builders;
import reform.stage.tooling.factory.FormFactory;
import reform.stage.tooling.tools.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class PicturePresenter
{
	public interface Preview
	{
		void draw(Graphics2D g2);

		int getWidth();

		int getHeight();
	}

	public interface Listener
	{
		void onPreviewChange(PicturePresenter presenter);
	}

	private final ArrayList<Listener> _listeners = new ArrayList<>();

	private final EventedPicture _picture;
	private final InstructionFocus _focus = new InstructionFocus();
	private final FocusAdjustmentProcedureListener _focusAdjustment = new FocusAdjustmentProcedureListener(_focus);
	private final FormSelection _selection = new FormSelection();
	private final SelectionAdjustmentProcedureListener _selectionAdjustment = new SelectionAdjustmentProcedureListener(
			_selection);
	// private final Commander _commander = new Commander();
	private final Stage _stage = new Stage();
	private final HitTester _hitTester = new HitTester(_stage, new HitTesterAdapter(_selection));
	private final Cursor _cursor = new Cursor(_hitTester);
	private final ToolState _toolState = new ToolState();
	private final ToolController _toolController = new ToolController(_cursor);
	private final ProjectAnalyzer _analyzer = new ProjectAnalyzer();
	private final ProjectRuntime _runtime;
	private final PreviewCollector _previewCollector = new PreviewCollector();
	private final StageCollector _stageCollector = new StageCollector(_stage, new StageAdapter(_focus), _analyzer);
	private final StepSnapshotCollector _stepCollector = new StepSnapshotCollector(new Vec2i(100, 60));


	private final JSplitPane _splitPane;
	private final StagePresenter _stagePresenter;
	private final ProcedureView _procedureView;
	private final JScrollPane _stageScroller;

	private final Vec2i _oldStageSize = new Vec2i();

	private final SwingIcon _cursorIcon = new SwingIcon(new ToolCursorIcon());
	private final SwingIcon _cropIcon = new SwingIcon(new ToolCropIcon(), false);
	private final SwingIcon _previewIcon = new SwingIcon(new EyeIcon());
	private final SwingIcon _exportIcon = new SwingIcon(new ActionExportIcon());
	private final SwingIcon _repairIcon = new SwingIcon(new ToolRepairIcon());
	private final SwingIcon _lineIcon = new SwingIcon(new ShapeLineIcon());
	private final SwingIcon _rectIcon = new SwingIcon(new ShapeRectangleIcon(), false);
	private final SwingIcon _circleIcon = new SwingIcon(new ShapeCircleIcon());
	private final SwingIcon _pieIcon = new SwingIcon(new ShapePieIcon());
	private final SwingIcon _arcIcon = new SwingIcon(new ShapeArcIcon());
	private final SwingIcon _textIcon = new SwingIcon(new ShapeTextIcon());
	private final SwingIcon _pictureIcon = new SwingIcon(new ShapePictureIcon());

	private final SwingIcon _translateIcon = new SwingIcon(new ToolTranslateIcon());
	private final SwingIcon _scaleIcon = new SwingIcon(new ToolScaleIcon());
	private final SwingIcon _rotateIcon = new SwingIcon(new ToolRotateIcon());
	private final SwingIcon _morphIcon = new SwingIcon(new ToolMorphIcon());

	private final SwingIcon _trashIcon = new SwingIcon(new ActionTrashIcon(), 22, false);
	private final SwingIcon _loopIcon = new SwingIcon(new ActionLoopIcon(), true);
	private final SwingIcon _branchIcon = new SwingIcon(new ActionBranchIcon(), true);

	public PicturePresenter(final EventedPicture picture, final IdentifierEmitter idEmitter)
	{
		_picture = picture;
		_runtime = new ProjectRuntime(_picture.getProject(), _picture.getSize());

		final EventedProcedure eProcedure = picture.getEventedProcedure();
		eProcedure.addListener(_focusAdjustment);
		eProcedure.addListener(_selectionAdjustment);
		_procedureView = new ProcedureView(new ProcedureViewAdapter(_analyzer, _focus, _stepCollector, eProcedure));
		_stagePresenter = new StagePresenter(_stage, _selection, _toolState, _analyzer, _cursor);


		_stepCollector.addListener(_procedureView);

		final JScrollPane procedureScroller = new JScrollPane(_procedureView,
		                                                      ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		                                                      ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		procedureScroller.setMinimumSize(_procedureView.getMinimumSize());

		final JPanel rightSide = new JPanel(new BorderLayout());
		final JPanel leftSide = new JPanel(new BorderLayout());

		final JPanel dataBox = new JPanel(new BorderLayout());
		final JPanel procedureBox = new JPanel(new BorderLayout());
		final JPanel measureBox = new JPanel(new BorderLayout());

		{
			SheetPresenter sheetPresenter = new SheetPresenter();
			JScrollPane dataScroller = new JScrollPane(sheetPresenter.getComponent());
			dataScroller.getVerticalScrollBar().setUnitIncrement(5);
			dataScroller.setPreferredSize(new Dimension(300, 100));
			dataBox.add(dataScroller, BorderLayout.CENTER);
		}
		{
			final JTable table = new JTable(3, 2);
			table.setFocusable(false);
			table.setSelectionBackground(Color.LIGHT_GRAY);
			table.setSelectionForeground(Color.BLACK);
			measureBox.add(new JLabel("Measurements"), BorderLayout.PAGE_START);
			measureBox.add(table, BorderLayout.CENTER);
		}
		final JPanel headBarLeft = new JPanel();
		headBarLeft.setLayout(new BorderLayout());
		final JLabel label = new JLabel("Procedure");
		headBarLeft.add(label, BorderLayout.WEST);
		final JToolBar procedureToolbar = new JToolBar();
		procedureToolbar.setFloatable(false);
		procedureToolbar.setRollover(false);
		headBarLeft.add(procedureToolbar, BorderLayout.EAST);
		{

			final InstructionsOptionPanel instructionOptionPanel = new InstructionsOptionPanel(eProcedure, _focus);

			procedureToolbar.add(instructionOptionPanel.getComponent());

			procedureToolbar.addSeparator();
			{
				final JButton branchButton = new JButton(new WrapInIfAction(_focus, eProcedure));
				branchButton.setFocusable(false);

				branchButton.setIcon(_branchIcon);
				branchButton.setHideActionText(true);

				procedureToolbar.add(branchButton);
			}

			{
				final JButton loopButton = new JButton(new WrapInLoopAction(_focus, eProcedure));
				loopButton.setFocusable(false);

				loopButton.setIcon(_loopIcon);
				loopButton.setHideActionText(true);

				procedureToolbar.add(loopButton);
			}

			procedureToolbar.addSeparator();

			{
				final JButton deleteInstructionButton = new JButton(new RemoveInstructionAction(_focus, eProcedure));
				deleteInstructionButton.setFocusable(false);

				deleteInstructionButton.setIcon(_trashIcon);
				deleteInstructionButton.setHideActionText(true);

				procedureToolbar.add(deleteInstructionButton);
			}

		}

		procedureBox.add(headBarLeft, BorderLayout.PAGE_START);
		procedureBox.add(procedureScroller, BorderLayout.CENTER);

		leftSide.add(dataBox, BorderLayout.PAGE_START);
		leftSide.add(procedureBox, BorderLayout.CENTER);
		leftSide.add(measureBox, BorderLayout.PAGE_END);

		final JToolBar toolBarRight = new JToolBar();
		toolBarRight.setFloatable(false);
		toolBarRight.setRollover(false);

		final SelectionTool selectionTool = new SelectionTool(_toolState, _selection, _cursor, _stage);
		final MoveFormTool translationTool = new MoveFormTool(selectionTool, _toolState, _cursor, _hitTester, _focus,
		                                                      eProcedure);
		final ScaleFormTool scaleTool = new ScaleFormTool(selectionTool, _toolState, _cursor, _hitTester, _focus,
		                                                  eProcedure);
		final RotateFormTool rotateTool = new RotateFormTool(selectionTool, _toolState, _cursor, _hitTester, _focus,
		                                                     eProcedure);
		final MorphFormTool morphTool = new MorphFormTool(selectionTool, _toolState, _cursor, _hitTester, _focus,
		                                                  eProcedure);
		final CropTool cropTool = new CropTool(_toolState, _cursor, _picture);
		final PreviewTool previewTool = new PreviewTool(_toolState);
		final RepairInstructionTool repairInstructionTool = new RepairInstructionTool(_toolState);
		{
			final CreateFormTool createLineTool = new CreateFormTool(selectionTool,
			                                               new FormFactory<>("Line", idEmitter, Builders.Line),
			                                               _toolState, _cursor, _hitTester, _focus, eProcedure);

			final JButton button = new JButton(new SelectToolAction(_toolController, createLineTool, "Create Line"));
			button.setFocusable(false);

			button.setIcon(_lineIcon);
			button.setHideActionText(true);
			toolBarRight.add(button);
		}

		{
			final CreateFormTool createRectTool = new CreateFormTool(selectionTool,
			                                                         new FormFactory<>("Rectangle", idEmitter,
			                                                                           Builders.Rectangle), _toolState,
			                                                         _cursor, _hitTester, _focus, eProcedure);
			createRectTool.setDiagonalDirection(true);

			final JButton button = new JButton(
					new SelectToolAction(_toolController, createRectTool, "Create " + "Rectangle"));
			button.setFocusable(false);

			button.setIcon(_rectIcon);
			button.setHideActionText(true);
			toolBarRight.add(button);
		}

		{
			final CreateFormTool createCircleTool = new CreateFormTool(selectionTool,
			                                                           new FormFactory<>("Circle", idEmitter,
			                                                                             Builders.Circle), _toolState,
			                                                           _cursor, _hitTester, _focus, eProcedure);
			createCircleTool.setAutoCenter(true);

			final JButton button = new JButton(
					new SelectToolAction(_toolController, createCircleTool, "Create " + "Circle"));
			button.setFocusable(false);

			button.setIcon(_circleIcon);
			button.setHideActionText(true);
			toolBarRight.add(button);
		}

		{
			final CreateFormTool createPieTool = new CreateFormTool(selectionTool,
			                                                        new FormFactory<>("Pie", idEmitter, Builders.Pie),
			                                                        _toolState, _cursor, _hitTester, _focus,
			                                                        eProcedure);
			createPieTool.setAutoCenter(true);

			final JButton button = new JButton(
					new SelectToolAction(_toolController, createPieTool, "Create Pie " + "Segment"));
			button.setFocusable(false);

			button.setIcon(_pieIcon);
			button.setHideActionText(true);
			toolBarRight.add(button);
		}

		{
			final Tool createArcTool = new CreateFormTool(selectionTool,
			                                              new FormFactory<>("Arc", idEmitter, Builders.Arc),
			                                              _toolState,
			                                              _cursor, _hitTester, _focus, eProcedure);

			final JButton button = new JButton(new SelectToolAction(_toolController, createArcTool, "Create Arc"));
			button.setFocusable(false);

			button.setIcon(_arcIcon);
			button.setHideActionText(true);
			toolBarRight.add(button);
		}

		{
			final JButton button = new JButton();
			button.setFocusable(false);

			button.setIcon(_textIcon);
			button.setHideActionText(true);
			toolBarRight.add(button);
		}

		{
			final JButton button = new JButton();
			button.setFocusable(false);

			button.setIcon(_pictureIcon);
			button.setHideActionText(true);
			toolBarRight.add(button);
		}

		toolBarRight.addSeparator();

		{
			final JButton button = new JButton(new SelectToolAction(_toolController, selectionTool, "Select Form"));
			button.setFocusable(false);

			button.setIcon(_cursorIcon);
			button.setHideActionText(true);
			toolBarRight.add(button);
		}

		{
			final JButton button = new JButton(new SelectToolAction(_toolController, translationTool, "Move Form"));
			button.setFocusable(false);

			button.setIcon(_translateIcon);
			button.setHideActionText(true);
			toolBarRight.add(button);
		}

		{
			final JButton button = new JButton(new SelectToolAction(_toolController, scaleTool, "Scale Form"));
			button.setFocusable(false);

			button.setIcon(_scaleIcon);
			button.setHideActionText(true);
			toolBarRight.add(button);
		}

		{
			final JButton button = new JButton(new SelectToolAction(_toolController, rotateTool, "Rotate Form"));
			button.setFocusable(false);

			button.setIcon(_rotateIcon);
			button.setHideActionText(true);
			toolBarRight.add(button);
		}

		{
			final JButton button = new JButton(new SelectToolAction(_toolController, morphTool, "Morph Form"));
			button.setFocusable(false);

			button.setIcon(_morphIcon);
			button.setHideActionText(true);
			toolBarRight.add(button);
		}

		toolBarRight.addSeparator();

		{
			final JButton button = new JButton(new SelectToolAction(_toolController, cropTool, "Crop"));
			button.setFocusable(false);

			button.setIcon(_cropIcon);
			button.setHideActionText(true);
			toolBarRight.add(button);
		}

		{
			final JButton button = new JButton(new SelectToolAction(_toolController, previewTool, "Preview"));
			button.setFocusable(false);

			button.setIcon(_previewIcon);
			button.setHideActionText(true);
			toolBarRight.add(button);
		}

		toolBarRight.addSeparator();

		{
			final JButton button = new JButton(new ExportImageAction(_stage, _toolController, previewTool));

			button.setIcon(_exportIcon);
			button.setHideActionText(true);
			button.setFocusable(false);

			toolBarRight.add(button);
		}

		toolBarRight.addSeparator();

		{
			final JButton button = new JButton(new SelectToolAction(_toolController, repairInstructionTool, "Repair Insruction"));

			button.setIcon(_repairIcon);
			button.setHideActionText(true);
			button.setFocusable(false);

			toolBarRight.add(button);
		}

		toolBarRight.addSeparator();


		{
			final FormOptionPanel formOptionPanelPanel = new FormOptionPanel(eProcedure, _analyzer, _selection);

			toolBarRight.add(formOptionPanelPanel.getComponent());

		}

		rightSide.add(toolBarRight, BorderLayout.PAGE_START);
		_stageScroller = new JScrollPane(_stagePresenter.getView(), ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		                                 ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		rightSide.add(_stageScroller, BorderLayout.CENTER);

		_splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftSide, rightSide);

		_runtime.addListener(_previewCollector);
		_runtime.addListener(_stageCollector);
		_runtime.addListener(_stepCollector);

		_runtime.addListener(new ProjectRuntime.Listener()
		{

			@Override
			public void onPopScope(final ProjectRuntime runtime, final FastIterable<Identifier<? extends Form>> ids)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinishEvaluation(final ProjectRuntime runtime)
			{
				if(_selection.isSet() && _stage.getEntityForId(_selection.getSelected()) == null) {
					_selection.reset();
				}
				_toolController.refresh();
				for (int i = 0; i < _listeners.size(); i++)
				{
					_listeners.get(i).onPreviewChange(PicturePresenter.this);
				}

				final Vec2i newSize = runtime.getSize();
				if (!newSize.equals(_oldStageSize))
				{
					if(!cropTool.isActive()) {
						_stagePresenter.updateSize();
						_oldStageSize.set(newSize);
					}
				}
			}

			@Override
			public void onEvalInstruction(final ProjectRuntime runtime, final Evaluable instruction)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(final ProjectRuntime runtime, final Evaluable instruction, final RuntimeError error)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onBeginEvaluation(final ProjectRuntime runtime)
			{
				// TODO Auto-generated method stub

			}
		});

		_analyzer.addListener(new ProjectAnalyzer.Listener()
		{

			// TODO(Laszlo): find a cleaner solution.
			// If the procedure changes, but the focus is not changed
			// the index of the focused instruction may change. But the
			// procedureView has still the old index stored, because setFocus is
			// only called if the focused instruction changes, not if just the
			// index changes
			// So we have memorize the index and compare it each time the
			// procedure changes. First we compare just the size to avoid linear
			// search indexOf()
			// private int _focusIndex = -1;
			private int _oldSize = -1;

			@Override
			public void onFinishAnalysis(final ProjectAnalyzer analyzer)
			{
				_procedureView.requireUpdate();
				_stepCollector.requireRedraw();
				if (_oldSize != analyzer.getNodeCount() && _focus.isSet())
				{
					final int index = analyzer.indexOf(_focus.getFocused());
					final int oldFocusIndex = _procedureView.getFocus();
					if (index != oldFocusIndex)
					{
						_procedureView.setFocus(index);
					}
					_oldSize = analyzer.getNodeCount();
				}

				evaluateProcedure();
			}
		});

		_focus.setFocus(picture.getEventedProcedure().getRoot().get(0));
		_procedureView.setFocus(0);

		_focus.addListener(new InstructionFocus.Listener()
		{

			@Override
			public void onFocusChanged(final InstructionFocus focus)
			{
				if (focus.isSet())
				{
					final int index = _analyzer.indexOf(focus.getFocused());
					_procedureView.setFocus(index);

					Identifier<?extends Form> form = _focus.getFocused().getTarget();
					if(_analyzer.getForm(form) != null)
					{
						_selection.setSelection(form);
					}
				}
				else
				{
					_procedureView.setFocus(-1);
					_selection.setSelection(null);
				}
				evaluateProcedure();
			}
		});

		_selection.addListener(focus -> _stagePresenter.update());

		_toolController.selectTool(selectionTool);

		final JComponent stage = _stagePresenter.getView();

		stage.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(final MouseEvent e)
			{
				super.mousePressed(e);
				_toolController.press();
			}

			@Override
			public void mouseReleased(final MouseEvent e)
			{
				// TODO Auto-generated method stub
				super.mouseReleased(e);
				_toolController.release();
			}
		});

		stage.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseMoved(final MouseEvent e)
			{
				super.mouseMoved(e);

				final int x = e.getX() - (e.getComponent().getWidth() - _stage.getSize().x) / 2;
				final int y = e.getY() - (e.getComponent().getHeight() - _stage.getSize().y) / 2;

				_toolController.moveTo(x, y);
			}

			@Override
			public void mouseDragged(final MouseEvent e)
			{
				super.mouseMoved(e);

				final int x = e.getX() - (e.getComponent().getWidth() - _stage.getSize().x) / 2;
				final int y = e.getY() - (e.getComponent().getHeight() - _stage.getSize().y) / 2;

				_toolController.moveTo(x, y);
			}
		});

		final InputMap inputMapStage = stage.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		final ActionMap actionMapStage = stage.getActionMap();

		final InputMap inputMapProcedure = _procedureView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		final ActionMap actionMapProcedure = _procedureView.getActionMap();

		inputMapProcedure.put(KeyStroke.getKeyStroke("UP"), "up");
		inputMapProcedure.put(KeyStroke.getKeyStroke("DOWN"), "down");

		inputMapStage.put(KeyStroke.getKeyStroke("TAB"), "cycle");
		inputMapStage.put(KeyStroke.getKeyStroke("shift TAB"), "cycle");
		inputMapStage.put(KeyStroke.getKeyStroke("alt shift TAB"), "cycle");
		inputMapStage.put(KeyStroke.getKeyStroke("alt TAB"), "cycle");

		inputMapStage.put(KeyStroke.getKeyStroke("W"), "toggleToolOption");
		inputMapStage.put(KeyStroke.getKeyStroke("shift W"), "toggleToolOption");
		inputMapStage.put(KeyStroke.getKeyStroke("alt shift W"), "toggleToolOption");
		inputMapStage.put(KeyStroke.getKeyStroke("alt  W"), "toggleToolOption");

		inputMapStage.put(KeyStroke.getKeyStroke("G"), "toggleGuide");
		inputMapStage.put(KeyStroke.getKeyStroke("shift G"), "toggleGuide");
		inputMapStage.put(KeyStroke.getKeyStroke("alt shift G"), "toggleGuide");
		inputMapStage.put(KeyStroke.getKeyStroke("alt G"), "toggleGuide");

		inputMapStage.put(KeyStroke.getKeyStroke("shift pressed SHIFT"), "shiftModifierOn");

		inputMapStage.put(KeyStroke.getKeyStroke("alt shift pressed SHIFT"), "shiftModifierOn");

		inputMapStage.put(KeyStroke.getKeyStroke("released SHIFT"), "shiftModifierOff");

		inputMapStage.put(KeyStroke.getKeyStroke("alt released SHIFT"), "shiftModifierOff");

		inputMapStage.put(KeyStroke.getKeyStroke("alt pressed ALT"), "altModifierOn");

		inputMapStage.put(KeyStroke.getKeyStroke("shift alt pressed ALT"), "altModifierOn");

		inputMapStage.put(KeyStroke.getKeyStroke("released ALT"), "altModifierOff");

		inputMapStage.put(KeyStroke.getKeyStroke("shift released ALT"), "altModifierOff");

		inputMapStage.put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
		inputMapStage.put(KeyStroke.getKeyStroke("SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("ctrl SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("shift SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("shift ctrl SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("alt SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("alt ctrl SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("alt shift SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("alt shift ctrl SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta shift SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta ctrl SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta alt SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta shift ctrl SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta alt ctrl SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta alt shift SPACE"), "showPreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta alt shift ctrl SPACE"), "showPreview");

		inputMapStage.put(KeyStroke.getKeyStroke("released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("ctrl released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("shift released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("shift ctrl released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("alt released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("alt ctrl released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("alt shift released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("alt shift ctrl released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta shift released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta ctrl released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta alt released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta shift ctrl released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta alt ctrl released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta alt shift released SPACE"), "hidePreview");
		inputMapStage.put(KeyStroke.getKeyStroke("meta alt shift ctrl released SPACE"), "hidePreview");

		actionMapStage.put("showPreview", new SetPreviewAction(this, true));
		actionMapStage.put("hidePreview", new SetPreviewAction(this, false));
		actionMapStage.put("cancel", new CancelToolAction(_toolController));

		actionMapStage.put("cycle", new CycleToolAction(_toolController));
		actionMapStage.put("toggleGuide", new ToggleGuideAction(eProcedure, _analyzer, _selection));

		actionMapStage.put("toggleToolOption", new ToggleToolOptionAction(_toolController));

		actionMapStage.put("shiftModifierOn", new ShiftModifierAction(_toolController, true));
		actionMapStage.put("shiftModifierOff", new ShiftModifierAction(_toolController, false));

		actionMapStage.put("altModifierOn", new AltModifierAction(_toolController, true));
		actionMapStage.put("altModifierOff", new AltModifierAction(_toolController, false));

		actionMapProcedure.put("up", new FocusPreviousInstructionAction(_focus));

		actionMapProcedure.put("down", new FocusNextInstructionAction(_focus));
	}

	public void addListener(final Listener listener)
	{
		_listeners.add(listener);
	}

	public void removeListener(final Listener listener)
	{
		_listeners.remove(listener);
	}

	public void update()
	{
		_picture.getEventedProcedure().analyze(_analyzer);
	}

	private void evaluateProcedure()
	{
		_runtime.stop();
		_runtime.setSize(_picture.getSize());
		_picture.getEventedProcedure().evaluate(_runtime);
	}

	public Preview getPreview()
	{
		return _previewCollector;
	}

	public void appendTo(final Container container)
	{
		container.removeAll();
		container.add(_splitPane, BorderLayout.CENTER);

		container.revalidate();
		container.repaint();
	}

	private static class PreviewCollector implements Preview, ProjectRuntime.Listener
	{

		private final Pool<GeneralPath.Double> _pathPool = new SimplePool<>(Path2D.Double::new);

		private final CopyOnWriteArrayList<Shape> _collectedShapes = new CopyOnWriteArrayList<>();
		private final Vec2i _size = new Vec2i();

		@Override
		public void draw(final Graphics2D g2)
		{
			g2.setColor(Color.DARK_GRAY);
			for (int i = 0, j = _collectedShapes.size(); i < j; i++)
			{
				g2.fill(_collectedShapes.get(i));
				g2.draw(_collectedShapes.get(i));
			}
		}

		@Override
		public int getWidth()
		{
			return _size.x;
		}

		@Override
		public int getHeight()
		{
			return _size.y;
		}

		@Override
		public void onEvalInstruction(final ProjectRuntime runtime, final Evaluable evaluable)
		{

		}

		@Override
		public void onError(final ProjectRuntime runtime, final Evaluable instruction, final RuntimeError error)
		{

		}

		@Override
		public void onBeginEvaluation(final ProjectRuntime runtime)
		{
			_collectedShapes.clear();
			_size.set(runtime.getSize());
		}

		@Override
		public void onFinishEvaluation(final ProjectRuntime runtime)
		{
			_pathPool.release();
		}

		@Override
		public void onPopScope(final ProjectRuntime runtime, final FastIterable<Identifier<? extends Form>> poppedIds)
		{
			for (int i = 0, j = poppedIds.size(); i < j; i++)
			{
				final Identifier<? extends Form> id = poppedIds.get(i);
				final Form form = runtime.get(id);
				if (form.getType() == DrawingType.Draw)
				{
					final GeneralPath.Double shape = _pathPool.take();
					shape.reset();
					form.appendToPathForRuntime(runtime, shape);
					_collectedShapes.add(shape);
				}
			}
		}

	}

	private static class ProcedureViewAdapter implements ProcedureView.Adapter
	{

		private final ProjectAnalyzer _analyzer;
		private final StepSnapshotCollector _stepCollector;
		private final InstructionFocus _focus;
		private final EventedProcedure _procedure;

		public ProcedureViewAdapter(final ProjectAnalyzer analyzer, final InstructionFocus focus, final
		StepSnapshotCollector stepCollector, final EventedProcedure procedure)
		{
			_analyzer = analyzer;
			_stepCollector = stepCollector;
			_focus = focus;
			_procedure = procedure;
		}

		@Override
		public void onSelect(final int index)
		{
			_focus.setFocus((Instruction) _analyzer.getNode(index).getSource());
		}

		@Override
		public BufferedImage getImageAt(final int index)
		{
			return _stepCollector.getImageOf((Instruction) _analyzer.getNode(index).getSource());
		}

		@Override
		public int getImageWidth()
		{
			return _stepCollector.getWidth();
		}

		@Override
		public int getImageHeight()
		{
			return _stepCollector.getHeight();
		}

		@Override
		public boolean hasFailed(final int index)
		{
			return _stepCollector.hasFailed((Instruction) _analyzer.getNode(index).getSource());
		}

		@Override
		public boolean hasBeenEvaluated(final int index)
		{
			return _stepCollector.hasBeenEvaluated((Instruction) _analyzer.getNode(index).getSource());
		}

		@Override
		public RuntimeError getError(final int index)
		{
			return _stepCollector.getError((Instruction) _analyzer.getNode(index).getSource());
		}

		@Override
		public String getDescription(final int index)
		{
			return _analyzer.getNode(index).getLabel();
		}

		@Override
		public void removeInstruction(final int index)
		{
			_procedure.removeInstruction((Instruction) _analyzer.getNode(index).getSource());
		}

		@Override
		public int getSize()
		{
			return _analyzer.getNodeCount();
		}

		@Override
		public int getIndentation(final int i)
		{
			return _analyzer.getNode(i).getIndentation();
		}

		@Override
		public boolean isEmptySlot(final int index)
		{
			return _analyzer.getNode(index).getSource() instanceof NullInstruction;
		}

		@Override
		public boolean isGroup(final int index)
		{
			return _analyzer.getNode(index).isGroup();
		}

	}

	public static class StageAdapter implements StageCollector.Adapter
	{

		private final InstructionFocus _focus;

		public StageAdapter(final InstructionFocus focus)
		{
			_focus = focus;
		}

		@Override
		public boolean isInFocus(final Evaluable instruction)
		{
			return instruction.equals(_focus.getFocused());
		}

	}

	private static class HitTesterAdapter implements HitTester.Adapter
	{
		private final FormSelection _selection;

		public HitTesterAdapter(final FormSelection selection)
		{
			_selection = selection;
		}

		@Override
		public boolean isSelected(final Entity entity)
		{
			return entity.getId().equals(_selection.getSelected());
		}

		@Override
		public boolean belongsToSelected(final SnapPoint snapPoint)
		{
			if (snapPoint instanceof EntityPoint)
			{
				return ((EntityPoint) snapPoint).getFormId().equals(_selection.getSelected());
			}

			return snapPoint instanceof IntersectionSnapPoint && (((IntersectionSnapPoint) snapPoint).getFormIdA().equals(
					_selection.getSelected()) || ((IntersectionSnapPoint) snapPoint).getFormIdB().equals(
					_selection.getSelected()));

		}
	}

	public void setPreviewMode(final boolean b)
	{
		_stagePresenter.setPreview(b);
	}
}
