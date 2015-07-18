package reform.data;

public interface Expression {

	Value evaluate(ExpressionContext context) throws CycleException,
			SemanticException;
}
