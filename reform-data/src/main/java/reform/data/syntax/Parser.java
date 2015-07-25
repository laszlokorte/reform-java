package reform.data.syntax;

import java.util.*;

public class Parser<E>
{

	private final ParserDelegate<E> _delegate;

	public Parser(final ParserDelegate<E> delegate)
	{
		this._delegate = delegate;
	}

	/**
	 * Shunting-yard algorythm
	 */
	public E parse(final Iterable<Token> lexerResult)
	{
		final Context<E> context = _delegate.newContext();
		boolean needOpen = false;

		outer:
		for (final Token token : lexerResult)
		{
			if (needOpen && !_delegate.isOpeningToken(token))
			{
				throw new UnexpectedTokenException(token,
				                                   "Expected Opening Parenthesis.");
			}
			needOpen = false;
			switch (token.type)
			{
				case EOF:
					break outer;

				case Identifier:
					if (context.lastTokenAtom)
					{
						throw new UnexpectedTokenException(token);
					}
					if (_delegate.hasFunctionOfName(token))
					{
						context.stack.push(token);
						context.argCount.push(0);

						if (!context.wereValues.isEmpty())
						{
							context.wereValues.pop();
							context.wereValues.push(true);
						}
						context.wereValues.push(false);
						needOpen = true;
					}
					else
					{
						context.lastTokenAtom = true;

						context.output.push(_delegate.variableTokenToNode(token));

						if (!context.wereValues.isEmpty())
						{
							context.wereValues.pop();
							context.wereValues.push(true);
						}
					}
					break;

				case LiteralValue:
					if (context.lastTokenAtom)
					{
						throw new UnexpectedTokenException(token);
					}
					context.lastTokenAtom = true;

					context.output.push(_delegate.literalTokenToNode(token));

					if (!context.wereValues.isEmpty())
					{
						context.wereValues.pop();
						context.wereValues.push(true);
					}

					break;

				case ArgumentSeparator:
					while (!context.stack.isEmpty() && !_delegate.isOpeningToken(
							context.stack.peek()))
					{
						context.output.push(_pipe(context.stack.pop(), context));
					}
					if (context.stack.isEmpty() || context.wereValues.isEmpty())
					{
						throw new UnexpectedTokenException(token);
					}
					if (context.wereValues.pop())
					{
						context.argCount.push(context.argCount.pop() + 1);
					}
					context.wereValues.push(true);
					context.lastTokenAtom = false;

					break;

				case Operator:
					if (isOperator(context.prevToken) && _delegate.hasUnaryOperator(
							token))
					{
						if (context.lastTokenAtom)
						{
							throw new UnexpectedTokenException(token);
						}
						context.unaries.add(token);
						context.stack.push(token);
					}
					else
					{

						if (!context.stack.isEmpty() && context.stack.peek().type ==
								Token.Type.Identifier)
						{
							context.output.push(_pipe(context.stack.pop(), context));
						}

						while ((!context.stack.isEmpty() && (context.stack.peek().type
								== Token.Type.Operator) &&
								(_delegate.precedenceOfOperator(token,
								                                actsAsUnary(context,
								                                            token)) <
										_delegate.precedenceOfOperator(
										context.stack.peek(), actsAsUnary(context,
										                                  context.stack
												                                  .peek())) || (_delegate.assocOfOperator(
										token) == Associativity.Left && _delegate
										.precedenceOfOperator(token,
										                      actsAsUnary(context,
										                                  token)) ==
										_delegate
										.precedenceOfOperator(context.stack.peek(),
										                      actsAsUnary(context,
										                                  context.stack.peek()))))))
						{
							context.output.push(_pipe(context.stack.pop(), context));
						}

						///if(forceParentheses && (context.stack.isEmpty || !_delegate
						// .isOpeningToken(context.stack
						// .peek()
						// ))) {
						///	throw new AmbiguousParsingException(token);
						///}

						context.stack.push(token);
						context.lastTokenAtom = false;
					}
					break;

				default:
					if (_delegate.isOpeningToken(token))
					{
						if (context.lastTokenAtom)
						{
							throw new UnexpectedTokenException(token);
						}
						context.stack.push(token);
					}
					else if (_delegate.isClosingToken(token))
					{
						while (!context.stack.isEmpty() && !_delegate.isMatchingPair(
								context.stack.peek(), token))
						{
							context.output.push(_pipe(context.stack.pop(), context));
						}

						if (!context.stack.isEmpty())
						{
							context.stack.pop();
						}
						else
						{
							throw new MismatchedTokenException(token, false);
						}

						if (!context.stack.isEmpty() && context.stack.peek().type ==
								Token.Type.Identifier)
						{
							///if(forceParentheses && (context.wereValues.isEmpty() ||
							// !context.wereValues.peek())) {
							///	throw new RedundancyParsingException(stackScopeBottom);
							///}
							context.output.push(_pipe(context.stack.pop(), context));
						}
					}
					else
					{
						_delegate.unknownToken(token, context, this::finalize);
					}
			}

			context.prevToken = token;
		}

		return _delegate.finish(this::finalize, context);
	}

	private boolean actsAsUnary(final Context<E> context, final Token token)
	{
		return context.unaries.contains(token);
	}

	E finalize(final Context<E> context)
	{
		while (!context.stack.isEmpty())
		{
			if (_delegate.isOpeningToken(context.stack.peek()))
			{
				throw new MismatchedTokenException(context.stack.peek(), true);
			}
			if (_delegate.isClosingToken(context.stack.peek()))
			{
				throw new MismatchedTokenException(context.stack.peek(), false);
			}
			context.output.push(_pipe(context.stack.pop(), context));
		}

		if (!context.output.isEmpty())
		{
			final E result = context.output.pop();
			if (!context.output.isEmpty())
			{
				throw new ParsingException(String.format(
						"Unexpecteded parse error. Parser is in invalid state. %s " +
								"%s",
						context.output.peek(), result), new Token(Token.Type.EOF, "E", new Position(1, 1,
				                                                               0)));
			}
			return result;
		}
		else
		{
			return _delegate.emptyNode();
		}
	}

	E _pipe(final Token op, final Context<E> context)
	{
		switch (op.type)
		{
			case Identifier:
				final boolean w = context.wereValues.pop();
				int argCount = context.argCount.pop();
				final ArrayList<E> temp = new ArrayList<>();

				while (argCount-- > 0 && !context.output.isEmpty())
				{
					temp.add(context.output.pop());
				}
				if (w && !context.output.isEmpty())
				{
					temp.add(context.output.pop());
				}
				else if (w)
				{
					throw new ParsingException(String.format(
							"Unexpected end of argument list for function '%s'.",
							op.value), op);
				}
				Collections.reverse(temp);
				return _delegate.functionTokenToNode(op, temp);

			case Operator:
				if (context.unaries.contains(op))
				{
					final E operand;

					if (!_delegate.hasUnaryOperator(op))
					{
						throw new UnknownOperatorException(op, Arity.Unary);
					}

					if (!context.output.isEmpty())
					{
						operand = context.output.pop();
					}
					else
					{
						throw new MissingOperandException(op, Arity.Unary, 1);
					}

					return _delegate.unaryOperatorToNode(op, operand);
				}
				else
				{
					final E leftHand;
					final E rightHand;

					if (!_delegate.hasBinaryOperator(op))
					{
						throw new UnknownOperatorException(op, Arity.Binary);
					}

					if (!context.output.isEmpty())
					{
						rightHand = context.output.pop();
					}
					else
					{
						throw new MissingOperandException(op, Arity.Binary, 2);
					}

					if (!context.output.isEmpty())
					{
						leftHand = context.output.pop();
					}
					else
					{
						throw new MissingOperandException(op, Arity.Binary, 1);
					}

					return _delegate.binaryOperatorToNode(op, leftHand, rightHand);
				}
			default:
				return _delegate.unknownOperator(op, context, this::finalize);
		}
	}

	private boolean isOperator(final Token t)
	{
		return t == null || t.type == Token.Type.Operator || t.type == Token.Type
				.ArgumentSeparator || _delegate.isOpeningToken(
				t);
	}

	public enum Associativity
	{
		Left, Right
	}

	public enum Arity
	{
		Unary, Binary
	}

	public interface ParserDelegate<E>
	{

		default boolean isOpeningToken(final Token token)
		{
			return token.type == Token.Type.ParenthesisLeft;
		}

		default boolean isClosingToken(final Token token)
		{
			return token.type == Token.Type.ParenthesisRight;
		}

		default boolean isMatchingPair(final Token left, final Token right)
		{
			return left.type == Token.Type.ParenthesisLeft && right.type == Token.Type
					.ParenthesisRight;
		}

		default boolean hasFunctionOfName(final Token left)
		{
			return false;
		}

		E variableTokenToNode(Token token);

		E emptyNode();

		E unaryOperatorToNode(Token operator, E operand);

		E binaryOperatorToNode(Token operator, E leftHand, E rightHand);

		E functionTokenToNode(Token function, List<E> args);

		boolean hasBinaryOperator(Token operator);

		boolean hasUnaryOperator(Token operator);

		Associativity assocOfOperator(Token token);

		int precedenceOfOperator(Token token, boolean unary);

		default void unknownToken(final Token token, final Context<E> context, final
		Finalizer<E> finalizer)
		{
			throw new UnexpectedTokenException(token);
		}

		default E unknownOperator(final Token op, final Context<E> context, final
		Finalizer<E> finalizer)
		{
			throw new UnexpectedTokenException(op);
		}

		Context<E> newContext();

		default E finish(final Finalizer<E> finalizer, final Context<E> context)
		{
			return finalizer.finalizy(context);
		}

		E literalTokenToNode(Token token);
	}

	interface Finalizer<E>
	{
		E finalizy(Context<E> context);
	}

	public static class Context<E>
	{

		final Stack<Token> stack = new Stack<>();
		final Stack<E> output = new Stack<>();

		final Stack<Boolean> wereValues = new Stack<>();
		final Stack<Integer> argCount = new Stack<>();
		final Set<Token> unaries = new HashSet<>();

		Token prevToken = null;

		boolean lastTokenAtom = false;
	}

	/**
	 * Superclass for all Exceptions thrown when parsing fails.
	 */
	public static class ParsingException extends RuntimeException
	{
		private final Token _token;

		public ParsingException(final String message, final Token token)
		{
			super(message);
			_token = token;
		}

		public Token getToken()
		{
			return _token;
		}
	}

	/**
	 * Exception thrown when the parser reads an token it did not expect.
	 */
	public static class UnexpectedTokenException extends ParsingException
	{

		public UnexpectedTokenException(final Token t)
		{
			super(String.format("Unexpected token %s (%s).", t.value, t.type), t);
		}

		public UnexpectedTokenException(final Token t, final String explain)
		{
			super(String.format("Unexpected token %s (%s). %s", t.value, t.type,
			                    explain),
			      t);
		}

	}

	/**
	 * Exception thrown when two tokens which must occure in pairs do not match.
	 */
	public static class MismatchedTokenException extends ParsingException
	{
		final boolean open;

		public MismatchedTokenException(final Token t, final boolean open)
		{
			super(String.format(
					open ? "Token '%s' (%s) has to be closed." : "Unxpected closing " +
							"token '%s' (%s).",
					t.value, t.type), t);
			this.open = open;
		}

	}

	/**
	 * Exception thrown when the parser reads an operator token but can not find
	 * matching operands.
	 */
	public static class MissingOperandException extends ParsingException
	{
		final Arity arity;
		final int missing;

		public MissingOperandException(final Token t, final Arity arity, final int
				missing)
		{
			super(String.format("Missing %d operand for %s operator '%s'.", missing,
			                    arity.name(), t.value), t);
			this.arity = arity;
			this.missing = missing;
		}
	}

	/**
	 * Exception thrown when the parser does not know the operand.
	 */
	public static class UnknownOperatorException extends ParsingException
	{
		final Arity arity;

		public UnknownOperatorException(final Token t, final Arity arity)
		{
			super(String.format("Unknown %s operator '%s'.", arity.name(), t.value), t);
			this.arity = arity;
		}

	}

}
