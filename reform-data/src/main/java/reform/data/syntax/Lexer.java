package reform.data.syntax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

public class Lexer
{
	private final List<Rule> _rules = new ArrayList<>();
	private final List<Rule> _ignoreRules = new ArrayList<>();

	public Lexer(final List<Rule> rules, final List<Rule> ignoreRules)
	{
		_rules.addAll(rules);
		_ignoreRules.addAll(ignoreRules);
	}

	public Iterable<Token> tokenize(final CharSequence input)
	{
		return new Tokens(this, input);
	}

	public static class Generator
	{

		private final ArrayList<Rule> _rules = new ArrayList<>();

		private final ArrayList<Rule> _ignores = new ArrayList<>();

		/**
		 * Add a new token pattern to this configuration.
		 * <p>
		 * The name is meant for the parser to recognize a token.
		 * The pattern is a part of a regular expression describing the expected input.
		 * <p>
		 * lexerGen.add('number', '-?[0-9]+')
		 */
		public void add(final Token.Type type, final String pattern)
		{
			_rules.add(new Rule(type, pattern, _rules.size()));
		}

		/**
		 * Configure a pattern to be ignored.
		 * <p>
		 * The pattern is a part of a regular expression describing ignored input.
		 * <p>
		 * lexerGen.ignore('\s+')
		 */
		public void ignore(final String pattern)
		{
			_ignores.add(new Rule(Token.Type.Ignore, pattern));
		}

		public Lexer getLexer()
		{
			return new Lexer(_rules, _ignores);
		}

	}

	private static class Tokens implements Iterable<Token>
	{

		private final Lexer _lexer;
		private final CharSequence _input;

		public Tokens(final Lexer lexer, final CharSequence input)
		{
			_lexer = lexer;
			_input = input;
		}

		@Override
		public Iterator<Token> iterator()
		{
			return new TokenIterator(_lexer, _input);
		}
	}

	private static class TokenIterator implements Iterator<Token>
	{

		private final Lexer _lexer;
		private final CharSequence _input;
		int _index = 1;
		int _line = 1;
		int _column = 1;
		char _accFirst;
		final StringBuffer _accumulator;
		final Queue<Character> _inputQueue;
		private int _currentPos = 0;
		private boolean _hasNext = true;

		TokenIterator(final Lexer lexer, final CharSequence input)
		{
			_lexer = lexer;
			_input = input;
			_accumulator = new StringBuffer();
			_inputQueue = new LinkedBlockingQueue<>();
		}

		@Override
		public boolean hasNext()
		{
			return _hasNext;
		}

		@Override
		public Token next()
		{
			Rule currentRule = null;
			int currentPrio = 0;
			int currentColumn = _column;
			int currentLine = _line;

			outer:
			while (true)
			{
				if (_inputQueue.isEmpty())
				{
					if (_currentPos < _input.length())
					{
						_inputQueue.add(_input.charAt(_currentPos++));
					}
					else
					{
						break;
					}
				}
				else
				{
					for (int i = 0, j = this._lexer._ignoreRules.size(); i < j; i++)
					{
						final Rule rule = this._lexer._ignoreRules.get(i);
						if (rule.matches(this._accumulator.toString()))
						{
							currentColumn = _column;
							currentLine = _line;
							this._index += this._accumulator.length();
							this._accumulator.setLength(0);
							continue outer;
						}
					}

					boolean any = false;
					for (int i = 0, j = this._lexer._rules.size(); i < j; i++)
					{
						final Rule rule = this._lexer._rules.get(i);
						final int prio = j - rule._inversePriority;

						if (prio >= currentPrio && rule.matches(
								this._accumulator.toString() + this._inputQueue.peek()))
						{
							currentRule = rule;
							currentPrio = prio;
							any = true;
						}
					}
					if (!any)
					{
						if (currentRule != null)
						{
							break;
						}
						currentRule = null;
						currentPrio = 0;
					}
					_consume();
				}

			}

			if (currentRule != null)
			{
				final Position sourcePos = new Position(_index, currentLine,
				                                        currentColumn);
				final Token current = new Token(currentRule.type,
				                                this._accumulator.toString(), sourcePos);
				this._index += this._accumulator.length();
				this._accumulator.setLength(0);

				return current;
			}

			if (_accumulator.length() != 0)
			{
				_hasNext = false;
				throw new LexingException(_accFirst, new Position(_index, currentLine,
				                                                  currentColumn));
			}

			_hasNext = false;
			return new Token(Token.Type.EOF, "",
			                 new Position(_index, currentLine, currentColumn));
		}

		void _consume()
		{
			if (this._accumulator.length() == 0)
			{
				_accFirst = this._inputQueue.peek();
			}

			if (this._inputQueue.peek() == '\n')
			{
				_column = 1;
				_line++;
			}
			else
			{
				_column++;
			}

			this._accumulator.append(this._inputQueue.poll());
		}


	}

	private static class Rule
	{

		private final Token.Type type;

		private final Pattern _pattern;

		private final int _inversePriority;

		Rule(final Token.Type type, final String pattern, final int invPrio)
		{
			this.type = type;
			_pattern = Pattern.compile("^(" + pattern + ")$");
			_inversePriority = invPrio;
		}

		Rule(final Token.Type type, final String pattern)
		{
			this(type, pattern, 0);
		}

		public boolean matches(final CharSequence input)
		{
			return _pattern.matcher(input).find();
		}

	}

	public static class LexingException extends RuntimeException
	{
		public final Position sourcePosition;
		public final char character;

		LexingException(final char character, final Position sourcePosition)
		{
			super(String.valueOf(character));
			this.character = character;
			this.sourcePosition = sourcePosition;
		}
	}
}
