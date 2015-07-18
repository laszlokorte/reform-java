package reform.core.runtime;

import reform.core.forms.Form;
import reform.core.project.Picture;
import reform.data.ExpressionContext;
import reform.identity.FastIterable;
import reform.identity.Identifier;
import reform.math.Vec2i;

public interface Runtime {
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

	void reportError(Evaluable instruction, Error error);

	boolean shouldStop();

	Runtime getSubroutine( Identifier<? extends Picture> pictureId);

	ExpressionContext getExpressionContext();

	Vec2i getSize();
}
