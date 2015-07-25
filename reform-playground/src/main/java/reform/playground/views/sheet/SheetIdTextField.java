package reform.playground.views.sheet;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

public class SheetIdTextField extends JTextField
{
	private static final Pattern whiteSpace = Pattern.compile("\\s+");
	private final int limit;

	public SheetIdTextField(final int limit)
	{
		super();
		this.limit = limit;
	}


	@Override
	protected Document createDefaultModel()
	{
		return new LimitDocument();
	}

	private class LimitDocument extends PlainDocument
	{

		@Override
		public void insertString(final int offset, final String str, final AttributeSet attr) throws
				BadLocationException
		{
			if (str == null)
			{
				return;
			}

			final String noSpaces = whiteSpace.matcher(str).replaceAll("");

			if ((getLength() + noSpaces.length()) <= limit)
			{
				super.insertString(offset, noSpaces, attr);
			}
		}

	}

	@Override
	protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed)
	{
		super.processKeyBinding(ks, e, condition, pressed);
		return true;
	}
}
