package reform.stage.tooling.tools;

import reform.evented.core.EventedPicture;
import reform.math.Vec2;
import reform.math.Vec2i;
import reform.stage.elements.CropPoint;
import reform.stage.tooling.Input;
import reform.stage.tooling.Tool;
import reform.stage.tooling.ToolState;
import reform.stage.tooling.cursor.Cursor;

public class CropTool implements Tool
{

	private enum State
	{
		Idle, Hover, Pressed
	}

	private final ToolState _toolState;
	private final Cursor _cursor;
	private final EventedPicture _eventedPicture;

	private State _state = State.Idle;
	private CropPoint _currentPoint = null;
	private final Vec2 _pressOffset = new Vec2();
	private final Vec2i _oldSize = new Vec2i();

	public CropTool(final ToolState toolState, final Cursor cursor, final EventedPicture ePicture)
	{
		_toolState = toolState;
		_cursor = cursor;
		_eventedPicture = ePicture;
	}

	@Override
	public void setUp()
	{
		_toolState.setViewState(ToolState.ViewState.Crop);
		_toolState.setSelectionState(ToolState.SelectionState.CropPoint);

		_toolState.setDescription("Resize Canvas");

		_state = State.Idle;
	}

	@Override
	public void tearDown()
	{
		_state = State.Idle;
	}

	@Override
	public void cancel()
	{
		if (_state == State.Pressed)
		{
			_eventedPicture.setSize(_oldSize);
		}
		_state = State.Idle;
		_currentPoint = null;
		_toolState.setActiveCropPoint(null);
	}

	@Override
	public void press()
	{
		if (_state == State.Hover)
		{
			_state = State.Pressed;
			_oldSize.set(_eventedPicture.getSize());
			final Vec2 cursorPos = _cursor.getPosition();
			_pressOffset.set(cursorPos.x - _currentPoint.getX(), cursorPos.y - _currentPoint.getY());
		}
	}

	@Override
	public void release()
	{
		final CropPoint p = _cursor.getCropPoint();
		if (p != null)
		{
			_currentPoint = p;
			_state = State.Hover;
		}
		else
		{
			_currentPoint = null;
			_state = State.Idle;
		}

		_eventedPicture.setSize(_eventedPicture.getSize());
		_toolState.setActiveCropPoint(_currentPoint);

	}

	@Override
	public void refresh()
	{

	}

	@Override
	public void toggleOption()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void cycle()
	{
		if (_state == State.Hover)
		{
			_cursor.cycleNextCropPoint();
		}
	}

	@Override
	public void input(final Input input)
	{
		switch (_state)
		{
			case Idle:
			{
				final CropPoint p = _cursor.getCropPoint();
				if (p != null)
				{
					_state = State.Hover;
				}
				_currentPoint = p;
				break;
			}
			case Hover:
			{
				final CropPoint p = _cursor.getCropPoint();
				if (p == null)
				{

					_state = State.Idle;
				}
				_currentPoint = p;

				break;
			}
			case Pressed:
			{
				_eventedPicture.setSize(calcSize(input.getShiftModifier().isActive()));
			}
		}

		_toolState.setActiveCropPoint(_currentPoint);
	}

	@Override
	public void focusChanged()
	{

	}

	private Vec2i calcSize(final boolean keepRatio)
	{
		final Vec2 pos = _cursor.getPosition();

		final int deltaX = (int) (pos.x - _currentPoint.getX() - _pressOffset.x) * _currentPoint.getOffsetX();
		final int deltaY = (int) (pos.y - _currentPoint.getY() - _pressOffset.y) * _currentPoint.getOffsetY();

		final int newWidth = Math.max(_eventedPicture.getSize().x + deltaX, 10);
		final int newHeight = Math.max(_eventedPicture.getSize().y + deltaY, 10);

		if (keepRatio && _currentPoint.isCorner())
		{
			final double ratio = 1.0 * _oldSize.y / _oldSize.x;
			final double min = Math.min(newWidth, newHeight / ratio);

			return new Vec2i((int) Math.round(min), (int) Math.round(min * ratio));
		}
		else
		{
			return new Vec2i(newWidth, newHeight);
		}

	}

	public boolean isActive() {
		return _state == State.Pressed;
	}

}
