package reform.rendering.canvas;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComponent;

public class Canvas extends JComponent {

	private static final long serialVersionUID = 1L;

	private final CanvasAdapter _adapter;
	private final HashMap<RenderingHints.Key, Object> _renderingHits = new HashMap<>();
	private final ArrayList<CanvasRenderer> _rederers = new ArrayList<>();

	public Canvas(final CanvasAdapter adapter) {
		_adapter = adapter;

		_renderingHits.put(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		_renderingHits.put(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);

		final Graphics2D g2 = (Graphics2D) g.create();
		final int width = getWidth();
		final int height = getHeight();

		g2.setRenderingHints(_renderingHits);

		for (int i = 0; i < _rederers.size(); i++) {
			_rederers.get(i).render(g2, width, height);
		}

		g2.dispose();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(_adapter.getWidth(), _adapter.getHeight());
	}

	public void addRenderer(final CanvasRenderer renderer) {
		_rederers.add(renderer);
	}
}
