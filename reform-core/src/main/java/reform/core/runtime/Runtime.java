package reform.core.runtime;

import reform.core.forms.Form;
import reform.core.project.Picture;
import reform.data.ExpressionContext;
import reform.identity.FastIterable;
import reform.identity.Identifier;
import reform.math.Vec2i;

public interface Runtime {
	public void begin();

	public void finish();

	public void beforeEval(Evaluatable instruction);

	public void afterEval(Evaluatable instruction);

	public void pushScope();

	public void popScope();

	public void declare(Form form);

	public Form get(Identifier<? extends Form> id);

	public long get(Identifier<? extends Form> id, int offset);

	public void set(Identifier<? extends Form> id, int offset, long value);

	public FastIterable<Identifier<? extends Form>> getStackIterator();

	public void reportError(Evaluatable instruction, Error error);

	public boolean shouldStop();

	public Runtime getSubroutine(Identifier<? extends Picture> pictureId);

	public ExpressionContext getExpressionContext();

	public Vec2i getSize();
}
