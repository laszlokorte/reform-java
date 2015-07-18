package reform.data;

public interface Expression {

	public Value evaluate(ExpressionContext context) throws CycleException,
			SemanticException;
}
