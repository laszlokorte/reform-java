package reform.playground.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import reform.playground.presenter.PicturePresenter;

public class SetPreviewAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private final PicturePresenter _presenter;
	private final boolean _enable;

	public SetPreviewAction(final PicturePresenter presenter,
			final boolean enable) {
		_presenter = presenter;
		_enable = enable;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		_presenter.setPreviewMode(_enable);
	}

}
