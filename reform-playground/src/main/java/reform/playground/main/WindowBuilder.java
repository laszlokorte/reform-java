package reform.playground.main;

import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import reform.core.project.Picture;
import reform.core.project.Project;
import reform.evented.core.EventedProject;
import reform.identity.Identifier;
import reform.identity.IdentifierEmitter;
import reform.playground.actions.CloseAction;
import reform.playground.actions.NewProjectAction;
import reform.playground.actions.OpenAction;
import reform.playground.actions.OpenExampleAction;
import reform.playground.actions.SaveAction;
import reform.playground.actions.SaveAsAction;
import reform.playground.presenter.ProjectPresenter;

public class WindowBuilder {

	private static class Window {
		private final JFrame _frame;
		private boolean _fresh = false;

		public Window(final WindowBuilder windowBuilder, final File file,
				final Project project, final IdentifierEmitter idEmitter,
				final boolean fresh) {
			final EventedProject eProject = new EventedProject(project);
			_frame = new JFrame("Reform Playground");
			_fresh = fresh;
			_frame.setFocusable(true);

			_frame.setMinimumSize(new Dimension(600, 500));

			final ProjectPresenter projectPresenter = new ProjectPresenter(
					eProject, idEmitter);

			// final GlasLayerUI layerUI = new GlasLayerUI();
			// final JLayer<JComponent> layer = new JLayer<>(
			// projectPresenter.getView(), layerUI);

			_frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

			final JMenuBar menubar = new JMenuBar();

			final SaveAsAction saveAsAction = new SaveAsAction(windowBuilder,
					project, file, idEmitter);

			final JMenu fileMenu = new JMenu("File");
			fileMenu.add(new JMenuItem(
					new NewProjectAction(windowBuilder, idEmitter)));
			fileMenu.add(new JMenuItem(new OpenAction(windowBuilder)));
			fileMenu.add(new JMenuItem(new OpenExampleAction(windowBuilder)));
			fileMenu.add(new JMenuItem(
					new SaveAction(project, file, idEmitter, saveAsAction)));
			fileMenu.add(new JMenuItem(saveAsAction));
			fileMenu.addSeparator();
			fileMenu.add(new JMenuItem(new CloseAction(windowBuilder)));

			menubar.add(fileMenu);

			_frame.setJMenuBar(menubar);
			_frame.getContentPane().add(projectPresenter.getView());
			_frame.pack();

			_frame.setLocationRelativeTo(null);
			_frame.setVisible(true);

			eProject.addListener(new EventedProject.Listener() {

				@Override
				public void onPictureRemoved(final EventedProject project,
						final Identifier<? extends Picture> pictureId) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPictureChanged(final EventedProject project,
						final Identifier<? extends Picture> pictureId) {
					_fresh = false;
					eProject.removeListener(this);
				}

				@Override
				public void onPictureAdded(final EventedProject project,
						final Identifier<? extends Picture> pictureId) {
					// TODO Auto-generated method stub

				}
			});

			_frame.addWindowListener(new WindowListener() {

				@Override
				public void windowOpened(final WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowIconified(final WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowDeiconified(final WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowDeactivated(final WindowEvent e) {
					windowBuilder._currentWindow = null;
				}

				@Override
				public void windowClosing(final WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowClosed(final WindowEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void windowActivated(final WindowEvent e) {
					windowBuilder._currentWindow = Window.this;
				}
			});

			{

				final Set<KeyStroke> forwardKeys = new HashSet<KeyStroke>(1);
				forwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,
						InputEvent.CTRL_MASK));
				_frame.setFocusTraversalKeys(
						KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
						forwardKeys);

				final Set<KeyStroke> backwardKeys = new HashSet<KeyStroke>(1);
				backwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,
						InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
				_frame.setFocusTraversalKeys(
						KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
						backwardKeys);
			}
		}
	}

	private final ArrayList<Window> _windows = new ArrayList<>();
	private Window _currentWindow = null;

	JFileChooser _fileChooser = new JFileChooser();

	public WindowBuilder() {
		_fileChooser.setFileFilter(new FileNameExtensionFilter("JSON", "json"));

	}

	public void open(final File file, final Project project,
			final IdentifierEmitter idEmitter, final boolean fresh) {

		if (_currentWindow != null && _currentWindow._fresh) {
			closeCurrentWindow();
		}
		_windows.add(new Window(this, file, project, idEmitter, fresh));
	}

	public void closeCurrentWindow() {
		_currentWindow._frame.dispatchEvent(new WindowEvent(
				_currentWindow._frame, WindowEvent.WINDOW_CLOSING));
	}

	public JFileChooser getFileChooser() {
		return _fileChooser;
	}
}
