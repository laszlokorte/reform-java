package reform.data.syntax;


import reform.data.sheet.Calculator;
import reform.data.sheet.Definition;
import reform.data.sheet.Sheet;
import reform.data.sheet.Value;
import reform.data.sheet.expression.*;
import reform.identity.Identifier;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by laszlokorte on 22.07.15.
 */
public class SimpleDelegate implements Parser.ParserDelegate<Expression> {

	private static char[] UNARY_OPERATORS = new char[]{'+', '-'};
	private static char[] BINARY_OPERATORS = new char[]{'+', '-', '*', '/', '%'};

	private final Sheet _sheet;

	public SimpleDelegate(Sheet sheet) {
		_sheet = sheet;
	}

	@Override
	public Expression variableTokenToNode(Token token) {
		String name = token.value.toString();
		Definition def = _sheet.findDefinitionWithName(name.substring(1));
		return new ReferenceExpression(def.getId(), name);
	}

	@Override
	public Expression emptyNode() {
		return new ConstantExpression(new Value(0));
	}

	@Override
	public Expression unaryOperatorToNode(Token operator, Expression operand) {
		if(operator.value.equals("-")) {
			return UnaryMinusExpression.wrapOrSimplify(operand);
		} else if(operator.value.equals("+")) {
			return operand;
		} else {
			throw new Parser.UnknownOperatorException(operator, Parser.Arity.Unary);
		}
	}

	@Override
	public Expression binaryOperatorToNode(Token operator, Expression leftHand, Expression rightHand) {
		if(operator.value.equals("+")) {
			return new AdditionExpression(leftHand, rightHand);
		} else if(operator.value.equals("-")) {
			return new SubtractionExpression(leftHand, rightHand);
		} else if(operator.value.equals("*")) {
			return new MultiplicationExpression(leftHand, rightHand);
		} else if(operator.value.equals("/")) {
			return new DivisionExpression(leftHand, rightHand);
		} else if(operator.value.equals("%")) {
			return new ModuloExpression(leftHand, rightHand);
		} else if(operator.value.equals("^")) {
			return new ExponentialExpression(leftHand, rightHand);
		} else {
			throw new Parser.UnknownOperatorException(operator, Parser.Arity.Unary);
		}
	}

	@Override
	public Expression functionTokenToNode(Token function, List<Expression> args) {
		Expression[] params = new Expression[args.size()];
		args.toArray(params);
		String funcName = function.value.toString();
		String enumName = funcName.substring(0,1).toUpperCase() + funcName.substring(1);
		return new FunctionCallExpression(Calculator.Function.valueOf(enumName), params);
	}

	@Override
	public boolean hasBinaryOperator(Token operator) {
		return operator.value.equals("+") || operator.value.equals("-") || operator.value.equals("*") || operator.value.equals("/") ||
				operator.value.equals("%") || operator.value.equals("^");
	}

	@Override
	public boolean hasUnaryOperator(Token operator) {
		return operator.value.equals("-") || operator.value.equals("+");
	}

	@Override
	public Parser.Associativity assocOfOperator(Token token) {
		return token.value.equals("^") ? Parser.Associativity.Right : Parser.Associativity.Left;
	}

	@Override
	public int precedenceOfOperator(Token token, boolean actsAsUnary) {
		if(token.value.equals("^")) {
			return 200;
		} else if(token.value.equals("*") || token.value.equals("/") || token.value.equals("%")) {
			return 100;
		} else if(token.value.equals("+") || token.value.equals("-")) {
			if(actsAsUnary) {
				return 150;
			} else {
				return 20;
			}
		} else {
			throw new Parser.UnknownOperatorException(token, Parser.Arity.Binary);
		}
	}

	@Override
	public Parser.Context<Expression> newContext() {
		return new Parser.Context<>();
	}

	private static Pattern stringPattern = Pattern.compile("^\"[^\"]*\"$");
	private static Pattern intPattern = Pattern.compile("^(0|[1-9][0-9]*)$");
	private static Pattern doublePattern = Pattern.compile("^(0|[1-9][0-9]*)?\\.[0-9]*$");

	@Override
	public Expression literalTokenToNode(Token token) {
		if(intPattern.matcher(token.value).find()) {
			return new ConstantExpression(new Value(Integer.parseInt(token.value.toString(), 10)));
		} else if(doublePattern.matcher(token.value).find()) {
			return new ConstantExpression(new Value(Double.parseDouble(token.value.toString())));
		} else if(stringPattern.matcher(token.value).find()) {
			return new ConstantExpression(new Value(token.value.subSequence(1, token.value.length()-1).toString()));
		} else {
			throw new Parser.UnexpectedTokenException(token);
		}
	}
}
