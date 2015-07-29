package reform.core.graphics;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

public class ColoredShape
{
	private final GeneralPath.Double _path = new GeneralPath.Double();
	private Stroke _stroke = new BasicStroke(1);
	private Color _backgroundColor;
	private Color _strokeColor;
	private ArrayList<ColoredShape> _subShapes = new ArrayList<>();
	private final AffineTransform _childTransform = new AffineTransform();


	public Stroke getStroke()
	{
		return _stroke;
	}

	public Color getBackgroundColor()
	{
		return _backgroundColor;
	}

	public void setBackgroundColor(final int color)
	{
		_backgroundColor = color != 0 ? new Color(color, true) : null;
	}

	public GeneralPath.Double getPath()
	{
		return _path;
	}

	public void setStrokeColor(final int color)
	{
		_strokeColor = color != 0 ? new Color(color, true) : null;
	}

	public void reset()
	{
		_backgroundColor = null;
		_strokeColor = null;
		_subShapes.clear();
		_path.reset();
		_childTransform.setToIdentity();
	}

	private float opacity = 1;

	public void draw(final Graphics2D g2)
	{
		if (_backgroundColor != null)
		{
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,opacity));
			g2.setColor(_backgroundColor);
			g2.fill(_path);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		} else {
			opacity -= 0.3;
			AffineTransform oldTransform = g2.getTransform();
			g2.transform(_childTransform);
			for(int i=0,j=_subShapes.size();i<j;i++) {
				_subShapes.get(i).draw(g2);
			}
			g2.setTransform(oldTransform);
			opacity += 0.3;
		}

		if (_stroke != null && _strokeColor != null)
		{
			g2.setColor(_strokeColor);
			g2.setStroke(_stroke);
			g2.draw(_path);
		}

	}

	public void setStrokeWidth(final double strokeWidth)
	{
		_stroke = strokeWidth > 0 ? new BasicStroke((float)strokeWidth) : null;
	}

	public void addSubShape(final ColoredShape subShape)
	{
		_subShapes.add(subShape);
	}

	public void setChildTransform(final AffineTransform transform)
	{
		_childTransform.setTransform(transform);
	}

	public void addSubShapesFrom(final ColoredShape subShapesFrom)
	{
		_subShapes.addAll(subShapesFrom._subShapes);
	}
}
