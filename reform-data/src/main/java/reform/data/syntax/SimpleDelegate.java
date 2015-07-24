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
public class SimpleDelegate implements Parser.ParserDelegate<Expression>
{

	private static char[] UNARY_OPERATORS = new char[]{'+', '-', '~'};
	private static char[] BINARY_OPERATORS = new char[]{'+', '-', '*', '/', '%'};

	private final Sheet _sheet;

	public SimpleDelegate(Sheet sheet)
	{
		_sheet = sheet;
	}

	@Override
	public Expression variableTokenToNode(Token token)
	{
		String name = token.value.toString().substring(1);
		Definition def = _sheet.findDefinitionWithName(name);
		if (def == null)
		{
			return new ReferenceExpression(new Identifier(-1), name);
		}
		return new ReferenceExpression(def.getId(), name);
	}

	@Override
	public Expression emptyNode()
	{
		return new ConstantExpression(new Value(0));
	}

	@Override
	public Expression unaryOperatorToNode(Token operator, Expression operand)
	{
		if (operator.value.equals("-"))
		{
			return UnaryMinusExpression.wrapOrSimplify(operand);
		}
		else if (operator.value.equals("+"))
		{
			return operand;
		}
		else if (operator.value.equals("~"))
		{
			return LogicNegateExpression.wrapOrSimplify(operand);
		}
		else
		{
			throw new Parser.UnknownOperatorException(operator, Parser.Arity.Unary);
		}
	}

	@Override
	public Expression binaryOperatorToNode(Token operator, Expression leftHand, Expression rightHand)
	{
		if (operator.value.equals("+"))
		{
			return new AdditionExpression(leftHand, rightHand);
		}
		else if (operator.value.equals("-"))
		{
			return new SubtractionExpression(leftHand, rightHand);
		}
		else if (operator.value.equals("*"))
		{
			return new MultiplicationExpression(leftHand, rightHand);
		}
		else if (operator.value.equals("/"))
		{
			return new DivisionExpression(leftHand, rightHand);
		}
		else if (operator.value.equals("%"))
		{
			return new ModuloExpression(leftHand, rightHand);
		}
		else if (operator.value.equals("^"))
		{
			return new ExponentialExpression(leftHand, rightHand);
		}
		else if (operator.value.equals("&&"))
		{
			return new LogicAndExpression(leftHand, rightHand);
		}
		else if (operator.value.equals("||"))
		{
			return new LogicOrExpression(leftHand, rightHand);
		}
		else if (operator.value.equals("<"))
		{
			return new LessThanExpression(leftHand, rightHand);
		}
		else if (operator.value.equals("<="))
		{
			return new LessThanEqualExpression(leftHand, rightHand);
		}
		else if (operator.value.equals(">"))
		{
			return new GreaterThanExpression(leftHand, rightHand);
		}
		else if (operator.value.equals(">="))
		{
			return new GreaterThanEqualExpression(leftHand, rightHand);
		}
		else if (operator.value.equals("=="))
		{
			return new EqualExpression(leftHand, rightHand);
		}
		else if (operator.value.equals("!="))
		{
			return new EqualExpression(leftHand, rightHand);
		}
		else
		{
			throw new Parser.UnknownOperatorException(operator, Parser.Arity.Unary);
		}
	}

	@Override
	public Expression functionTokenToNode(Token function, List<Expression> args)
	{
		Expression[] params = new Expression[args.size()];
		args.toArray(params);
		String funcName = function.value.toString();
		String enumName = funcName.substring(0, 1).toUpperCase() + funcName.substring(1);
		return new FunctionCallExpression(Calculator.Function.valueOf(enumName), params);
	}

	@Override
	public boolean hasBinaryOperator(Token operator)
	{
		switch (operator.value.toString())
		{
			case "+":
			case "-":
			case "*":
			case "/":
			case "%":
			case "||":
			case "&&":
			case "<":
			case "<=":
			case ">":
			case ">=":
			case "==":
			case "!=":
			case "^":
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean hasUnaryOperator(Token operator)
	{
		switch (operator.value.toString())
		{
			case "-":
			case "+":
			case "~":
				return true;
			default:
				return false;
		}
	}

	@Override
	public Parser.Associativity assocOfOperator(Token token)
	{
		switch (token.value.toString())
		{
			case "^":
				return Parser.Associativity.Right;
			default:
				return Parser.Associativity.Left;
		}
	}

	@Override
	public int precedenceOfOperator(Token token, boolean actsAsUnary)
	{
		switch (token.value.toString())
		{
			case "^":
				return 500;
			case "*":
			case "/":
			case "%":
				return 400;
			case "+":
			case "-":
				if (actsAsUnary)
				{
					return 450;
				}
				else
				{
					return 300;
				}
			case "<":
			case "<=":
			case ">":
			case ">=":
				return 200;
			case "==":
			case "!=":
				return 100;
			case "&&":
				return 80;
			case "||":
				return 50;
			default:
				throw new Parser.UnknownOperatorException(token, Parser.Arity.Binary);

		}
	}

	@Override
	public Parser.Context<Expression> newContext()
	{
		return new Parser.Context<>();
	}

	private static Pattern stringPattern = Pattern.compile("^\"[^\"]*\"$");
	private static Pattern intPattern = Pattern.compile("^(0|[1-9][0-9]*)$");
	private static Pattern doublePattern = Pattern.compile("^(0|[1-9][0-9]*)?\\.[0-9]*$");

	@Override
	public Expression literalTokenToNode(Token token)
	{
		if (token.value.equals("true"))
		{
			return new ConstantExpression(new Value(true));
		}
		else if (token.value.equals("false"))
		{
			return new ConstantExpression(new Value(false));
		}
		else if (intPattern.matcher(token.value).find())
		{
			return new ConstantExpression(new Value(Integer.parseInt(token.value.toString(), 10)));
		}
		else if (doublePattern.matcher(token.value).find())
		{
			return new ConstantExpression(new Value(Double.parseDouble(token.value.toString())));
		}
		else if (stringPattern.matcher(token.value).find())
		{
			return new ConstantExpression(new Value(token.value.subSequence(1, token.value.length() - 1).toString()));
		}
		else
		{
			throw new Parser.UnexpectedTokenException(token);
		}
	}
}
