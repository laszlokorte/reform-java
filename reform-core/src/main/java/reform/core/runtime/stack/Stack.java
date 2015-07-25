package reform.core.runtime.stack;

import reform.core.forms.Form;
import reform.identity.FastIterable;
import reform.identity.IdentifiableList;
import reform.identity.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Stack implements Iterable<Identifier<? extends Form>>,
		FastIterable<Identifier<? extends Form>>
{
	private final static int INITIAL_CAPACITY = 100;

	private final IdentifiableList<Form> _content = new IdentifiableList<>();
	private final ArrayList<Frame> _frames = new ArrayList<>();
	private final Map<Identifier<? extends Form>, Integer> _offsets = new HashMap<>();
	private long[] _data = new long[INITIAL_CAPACITY];
	private int _dataSize = 0;

	public Stack()
	{

	}

	public void pushFrame()
	{
		_frames.add(new Frame(this));
	}

	public void popFrame()
	{
		final Frame frame = _frames.remove(_frames.size() - 1);
		frame.popped();
	}

	public void declare(final Form element)
	{
		final Frame topFrame = _frames.get(_frames.size() - 1);
		topFrame.declare(element);

		_content.add(element);
		_offsets.put(element.getId(), _dataSize);
		_dataSize += element.getSizeOnStack();

		growIfNeeded();
	}

	private void growIfNeeded()
	{
		if (_data.length < _dataSize)
		{

			final long[] oldData = _data;
			final int oldLength = oldData.length;
			_data = new long[_data.length * 2];
			System.arraycopy(oldData, 0, _data, 0, oldLength);
		}
	}

	public long getData(final Identifier<? extends Form> id, final int offset)
	{
		// if (!_content.contains(id)
		// || offset >= _content.getById(id).getSizeOnStack()) {
		// throw new IllegalStateException("Invalid stack access.");
		// }
		return _data[_offsets.get(id) + offset];
	}

	public void setData(final Identifier<? extends Form> id, final int offset, final
	long value)
	{
		// if (!_content.contains(id)
		// || offset >= _content.getById(id).getSizeOnStack()) {
		// throw new IllegalStateException(
		// "Invalid stack access:" + id + " at offset: " + offset);
		// }
		_data[_offsets.get(id) + offset] = value;
	}

	void remove(final Identifier<? extends Form> id)
	{
		final int size = _content.getById(id).getSizeOnStack();
		_content.removeById(id);
		for (int i = 1; i <= size; i++)
		{
			_data[_dataSize - i] = 0;
		}
		_dataSize -= size;
		_offsets.remove(id);
	}

	public void clear()
	{
		_content.clear();
		_frames.clear();
		_offsets.clear();
		_dataSize = 0;
	}

	public boolean isEmpty()
	{
		return _dataSize == 0 && _frames.size() == 0;
	}

	public Form get(final Identifier<? extends Form> id)
	{
		return _content.getById(id);
	}

	public FastIterable<Identifier<? extends Form>> getForms()
	{
		return _frames.get(_frames.size() - 1)._content;
	}

	@Override
	public Iterator<Identifier<? extends Form>> iterator()
	{
		return _content.iterator();
	}

	@Override
	public int size()
	{
		return _content.size();
	}

	@Override
	public Identifier<? extends Form> get(final int i)
	{
		return _content.getIdAtIndex(i);
	}
}
