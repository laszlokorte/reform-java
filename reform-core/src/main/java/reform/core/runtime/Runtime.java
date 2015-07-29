package reform.core.runtime;

import reform.core.forms.Form;
import reform.core.project.Picture;
import reform.core.runtime.errors.RuntimeError;
import reform.data.sheet.DataSet;
import reform.identity.FastIterable;
import reform.identity.Identifier;
import reform.math.Vec2i;

public interface Runtime
{
	int MAX_DEPTH = 3;

	Picture subCall(Identifier<? extends Picture> id, int width, int height, boolean fit);

	void subEnd();

	void begin();

	void finish();

	void beforeEval(Evaluable instruction);

	void afterEval(Evaluable instruction);

	void pushScope();

	void popScope();

	void declare(Form form);

	Form get(Identifier<? extends Form> id);

	long get(Identifier<? extends Form> id, int offset);

	void set(Identifier<? extends Form> id, int offset, long value);

	FastIterable<Identifier<? extends Form>> getStackIterator();

	void reportError(Evaluable instruction, RuntimeError error);

	boolean shouldStop();

	DataSet getDataSet();

	Vec2i getSize();

	void addListener(final Listener listener);

	void removeListener(final Listener listener);

	int getDepth();

	interface Listener
	{
		void onBeginEvaluation(Runtime runtime);

		void onFinishEvaluation(Runtime runtime);

		void onEvalInstruction(Runtime runtime, Evaluable instruction);

		void onPopScope(Runtime runtime, FastIterable<Identifier<? extends Form>>
				ids);

		void onError(Runtime runtime, Evaluable instruction, RuntimeError error);
	}
}
