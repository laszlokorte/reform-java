package reform.rendering.icons;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class ActionBranchIcon implements Icon
{

	private final Area _shape = new Area();

	public ActionBranchIcon()
	{

		final AffineTransform t = AffineTransform.getRotateInstance(-Math.PI / 4);
		final AffineTransform t2 = AffineTransform.getTranslateInstance(0, 200);
		_shape.add(new Area(new Rectangle2D.Double(-60, -400, 120, 800)));

		final Area tip = new Area(
				t.createTransformedShape(new Rectangle2D.Double(-175, -175, 350, 350)));
		tip.subtract(new Area(new Rectangle2D.Double(-300, -300, 600, 300)));

		_shape.add(tip.createTransformedArea(t2));

		final Area branch = new Area(new Ellipse2D.Double(-400, -400, 800, 800));
		branch.subtract(new Area(new Ellipse2D.Double(-300, -300, 600, 600)));
		branch.subtract(new Area(new Rectangle2D.Double(-500, -500, 1000, 500)));
		branch.subtract(new Area(new Rectangle2D.Double(-500, -500, 500, 1000)));

		_shape.add(branch.createTransformedArea(
				AffineTransform.getTranslateInstance(-350, -400)));

		final AffineTransform t3 = AffineTransform.getTranslateInstance(-320, -60);
		t3.rotate(Math.PI / 2);
		_shape.add(tip.createTransformedArea(t3));

		_shape.add(new Area(new Rectangle2D.Double(190, -350, 350, 120)));
		_shape.add(new Area(new Rectangle2D.Double(305, -465, 120, 350)));

	}

	@Override
	public void draw(final Graphics2D g, final int x, final int y, final int width)
	{
		final AffineTransform t = AffineTransform.getScaleInstance(width / 1000.0,
		                                                           width / 1000.0);
		g.translate(x, y);
		g.fill(t.createTransformedShape(_shape));
		g.translate(-x, -y);
	}
}
