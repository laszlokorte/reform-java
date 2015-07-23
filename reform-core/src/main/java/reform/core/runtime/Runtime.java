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

	Runtime getSubroutine(Identifier<? extends Picture> pictureId);

	DataSet getDataSet();

	Vec2i getSize();
}
