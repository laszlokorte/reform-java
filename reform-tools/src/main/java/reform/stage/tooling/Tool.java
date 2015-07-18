package reform.stage.tooling;

public interface Tool {
	public void setUp();

	public void tearDown();

	public void cancle();

	public void press();

	public void release();

	public void refresh();

	public void toggleOption();

	public void cycle();

	public void input(Input input);
}
