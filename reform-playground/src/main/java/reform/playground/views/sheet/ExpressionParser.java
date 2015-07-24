package reform.playground.views.sheet;

import reform.components.expression.ExpressionEditor;
import reform.data.sheet.*;
import reform.data.sheet.expression.*;
import reform.data.syntax.Lexer;
import reform.data.syntax.Parser;
import reform.data.syntax.Token;
import reform.evented.core.EventedSheet;
import reform.identity.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class ExpressionParser implements ExpressionEditor.Parser
{
	private final Sheet _sheet;
	private final Lexer _lexer = getLexer();
	private final Parser.ParserDelegate _delegate;
	private final Parser<Expression> _parser;
	private final HashSet<String> _functionNames = new HashSet<>();


	public ExpressionParser(EventedSheet sheet)
	{

		Calculator.Function[] functions = Calculator.Function.values();

		for(int i=0;i<functions.length;i++) {
			_functionNames.add(functions[i].name().toLowerCase());
		}
		_sheet = sheet.getRaw();
		_delegate = new Delegate(sheet.getRaw(), _functionNames);
		_parser = new Parser(_delegate);
	}

	@Override
	public Expression parse(CharSequence charSeq) {
		try {
			return _parser.parse(_lexer.tokenize(charSeq));
		} catch (Parser.ParsingException e) {
			return new InvalidExpression(charSeq);
		} catch (Lexer.LexingException e) {
			return new InvalidExpression(charSeq);
		}
	}



	public String getUniqueNameFor(String wantedName, Definition def) {
		String testName = wantedName;
		int postfix=0;
		Definition otherDef = _sheet.findDefinitionWithName(testName);
		while(otherDef != null && otherDef != def || _functionNames.contains(testName)) {
			testName = wantedName + ++postfix;
			otherDef = _sheet.findDefinitionWithName(testName);
		}

		if(postfix > 0) {
			return testName;
		} else {
			return wantedName;
		}
	}

	private static Lexer getLexer()
	{
		Lexer.Generator generator = new Lexer.Generator();

		generator.ignore("\\s+");
		generator.ignore("\\u00A0+");

		generator.add(Token.Type.ParenthesisLeft, "\\(");
		generator.add(Token.Type.ParenthesisRight, "\\)");


		generator.add(Token.Type.Operator, "\\-");
		generator.add(Token.Type.Operator, "\\+");
		generator.add(Token.Type.Operator, "\\/");
		generator.add(Token.Type.Operator, "\\*");
		generator.add(Token.Type.Operator, "\\%");
		generator.add(Token.Type.Operator, "\\^");


		generator.add(Token.Type.Operator, "~");
		generator.add(Token.Type.Operator, "\\&\\&");
		generator.add(Token.Type.Operator, "\\|\\|");


		generator.add(Token.Type.Operator, "<");
		generator.add(Token.Type.Operator, "<=");
		generator.add(Token.Type.Operator, ">");
		generator.add(Token.Type.Operator, ">=");

		generator.add(Token.Type.Operator, "==");

		generator.add(Token.Type.ArgumentSeparator, ",");
		generator.add(Token.Type.LiteralValue, "(0|([1-9][0-9]*))(\\.[0-9]*)?");
		generator.add(Token.Type.LiteralValue, "#[0-9a-fA-F]{6,8}");
		generator.add(Token.Type.LiteralValue, "\"[^\"]+\"");
		generator.add(Token.Type.LiteralValue, "(true|false)");
		generator.add(Token.Type.Identifier, "[a-zA-Z_][_a-zA-Z0-9]*");
		generator.add(Token.Type.LiteralValue, "\"[^\"]*\"");

		return generator.getLexer();
	}

	private final static class Delegate implements Parser.ParserDelegate<Expression>
	{

		private static char[] UNARY_OPERATORS = new char[]{'+', '-', '~'};
		private static char[] BINARY_OPERATORS = new char[]{'+', '-', '*', '/', '%'};

		private final Sheet _sheet;
		private final HashSet<String> _functionNames;

		public Delegate(Sheet sheet, HashSet<String> functionNames)
		{
			_sheet = sheet;
			_functionNames = functionNames;
		}

		@Override
		public Expression variableTokenToNode(Token token)
		{
			String name = token.value.toString();
			Definition def = _sheet.findDefinitionWithName(name);
			if (def == null)
			{
				return new ReferenceExpression(new Identifier(-1), name);
			}
			return new ReferenceExpression(def.getId(), name);
		}

		@Override
		public boolean hasFunctionOfName(final Token left)
		{
			return _functionNames.contains(left.value.toString());
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
		private static Pattern colorPattern = Pattern.compile("^#[0-9a-fA-F]{6}$");
		private static Pattern colorPatternAlpha = Pattern.compile("^#[0-9a-fA-F]{8}$");

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
			else if (colorPattern.matcher(token.value).find())
			{
				return new ConstantExpression(new Value(0xff000000 | Integer.parseInt(token.value.subSequence(1, token.value.length()).toString(), 16),
				                                        true));
			}
			else if (colorPatternAlpha.matcher(token.value).find())
			{
				return new ConstantExpression(new Value((int)Long.parseLong(token.value.subSequence(1, token.value.length()).toString(), 16),
				                                        true));
			}
			else
			{
				throw new Parser.UnexpectedTokenException(token);
			}
		}
	}
}
