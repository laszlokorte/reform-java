package reform.playground.views.sheet;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.util.regex.Pattern;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class SheetIdTextField extends JTextField
{
	private static final Pattern whiteSpace = Pattern.compile("\\s+");
	private int limit;

	public SheetIdTextField(int limit) {
		super();
		this.limit = limit;
	}


	@Override
	protected Document createDefaultModel() {
		return new LimitDocument();
	}

	private class LimitDocument extends PlainDocument
	{

		@Override
		public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException
		{
			if (str == null) return;

			String noSpaces = whiteSpace.matcher(str).replaceAll("");

			if ((getLength() + noSpaces.length()) <= limit) {
				super.insertString(offset, noSpaces, attr);
			}
		}

	}
}
