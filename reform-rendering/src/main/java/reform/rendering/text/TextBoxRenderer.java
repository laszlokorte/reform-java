package reform.rendering.text;

import java.awt.Graphics2D;

public class TextBoxRenderer {
	public float draw(final Graphics2D g2, final int posX, final int posY,
			final String text, final int width) {
		g2.drawString(text, posX, posY + 20);
		return 20;

		// final AttributedString styledText = new AttributedString(text);
		// final AttributedCharacterIterator iterator =
		// styledText.getIterator();
		// final int start = iterator.getBeginIndex();
		// final int end = iterator.getEndIndex();
		//
		// final FontRenderContext frc = g2.getFontRenderContext();
		//
		// final LineBreakMeasurer measurer = new LineBreakMeasurer(iterator,
		// frc);
		// measurer.setPosition(start);
		//
		// float y = posY;
		// final float x = posX;
		// while (measurer.getPosition() < end) {
		// final TextLayout layout = measurer.nextLayout(width);
		//
		// y += layout.getAscent() * 1.3;
		// final float dx = layout.isLeftToRight() ? 0 : width
		// - layout.getAdvance();
		//
		// layout.draw(g2, x + dx, y);
		// y += (layout.getDescent() + layout.getLeading()) * 1.3;
		// }
		//
		// return y - posY;
	}
}
