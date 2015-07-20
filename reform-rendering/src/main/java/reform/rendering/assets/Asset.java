package reform.rendering.assets;

import java.awt.*;

/**
 * An {@link Asset} is an object which can be rendered into a {@link Graphics}
 * object at a given position.
 * <p>
 * For example a simple UI element like a snap point on a canvas.
 */
public interface Asset
{
	/**
	 * Draw the asset at the given position.
	 *
	 * @param g The Graphics context to draw the asset it.
	 * @param x the x coordinate to draw the asset at.
	 * @param y the y coordinate to draw the asset at.
	 */
	void drawAt(Graphics g, double x, double y);
}
