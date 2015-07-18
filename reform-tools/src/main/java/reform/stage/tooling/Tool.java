package reform.stage.tooling;

public interface Tool {
	void setUp();

	void tearDown();

	void cancle();

	void press();

	void release();

	void refresh();

	void toggleOption();

	void cycle();

	void input(Input input);
}
