package reform.data.syntax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

public class Lexer {
	private final List<Rule> _rules = new ArrayList<>();
	private final List<Rule> _ignoreRules = new ArrayList<>();

	public Lexer(List<Rule> rules, List<Rule> ignoreRules) {
		_rules.addAll(rules);
		_ignoreRules.addAll(ignoreRules);
	}

	public Iterable<Token> tokenize(CharSequence input) {
		return new Tokens(this, input);
	}

	public static class Generator {

		private final ArrayList<Rule> _rules = new ArrayList<Rule>();

		private final ArrayList<Rule> _ignores = new ArrayList<Rule>();

		/**
		 * Add a new token pattern to this configuration.
		 *
		 * The name is meant for the parser to recognize a token.
		 * The pattern is a part of a regular expression describing the expected input.
		 *
		 *     lexerGen.add('number', '-?[0-9]+')
		 */
		public void add(Token.Type type, String pattern) {
			_rules.add(new Rule(type, pattern));
		}

		/**
		 * Configure a pattern to be ignored.
		 *
		 * The pattern is a part of a regular expression describing ignored input.
		 *
		 *     lexerGen.ignore('\s+')
		 */
		public void ignore(String pattern) {
			_ignores.add(new Rule(Token.Type.Ignore, pattern));
		}

		public Lexer getLexer() {
			return new Lexer(_rules, _ignores);
		}

	}

	private static class Tokens implements Iterable<Token> {

		private final Lexer _lexer;
		private final CharSequence _input;

		public Tokens(Lexer lexer, CharSequence input) {
			_lexer = lexer;
			_input = input;
		}

		@Override
		public Iterator<Token> iterator() {
			return new TokenIterator(_lexer, _input);
		}
	}

	private static class TokenIterator implements Iterator<Token> {

		private final Lexer _lexer;
		private final CharSequence _input;
		private int _currentPos = 0;

		int _index = 1;
		int _line = 1;
		int _column = 1;

		char _accFirst;
		StringBuffer _accumulator;
		Queue<Character> _inputQueue;

		private boolean _hasNext = true;

		TokenIterator(Lexer lexer, CharSequence input) {
			_lexer = lexer;
			_input = input;
			_accumulator = new StringBuffer();
			_inputQueue = new LinkedBlockingQueue<>();
		}

		@Override
		public boolean hasNext() {
			return _hasNext;
		}

		@Override
		public Token next() {
			Rule currentRule = null;
			int currentColumn = _column;
			int currentLine = _line;

			while (true) {
				if (_inputQueue.isEmpty()) {
					if (_currentPos < _input.length()) {
						_inputQueue.add(_input.charAt(_currentPos++));
					} else {
						break;
					}
				} else if (currentRule != null) {
					if (currentRule.matches(this._accumulator.toString() + this._inputQueue.peek())) {
						_consume();
					} else {
						break;
					}
				} else {
					_consume();
					for (int i = 0, j = this._lexer._ignoreRules.size(); i < j; i++) {
						Rule rule = this._lexer._ignoreRules.get(i);
						if (rule.matches(this._accumulator.toString())) {
							currentColumn = _column;
							currentLine = _line;
							this._index += this._accumulator.length();
							this._accumulator.setLength(0);
							break;
						}
					}

					for (int i = 0, j = this._lexer._rules.size(); i < j; i++) {
						Rule rule = this._lexer._rules.get(i);

						if (rule.matches(this._accumulator.toString())) {
							currentRule = rule;
							break;
						}
					}
				}

			}

			if (currentRule != null) {
				Position sourcePos = new Position(_index, currentLine, currentColumn);
				Token current = new Token(currentRule.type, this._accumulator.toString(), sourcePos);
				this._index += this._accumulator.length();
				this._accumulator.setLength(0);

				return current;
			}

			if (_accumulator.length() != 0) {
				_hasNext = false;
				throw new LexingException(_accFirst, new Position(_index, currentLine, currentColumn));
			}

			_hasNext = false;
			return new Token(Token.Type.EOF, "", new Position(_index, currentLine, currentColumn));
		}

		void _consume() {
			if (this._accumulator.length() == 0) {
				_accFirst = this._inputQueue.peek();
			}

			if (this._inputQueue.peek() == '\n') {
				_column = 1;
				_line++;
			} else {
				_column++;
			}

			this._accumulator.append(this._inputQueue.poll());
		}


	}

	private static class Rule {

		private final Token.Type type;

		private final Pattern _pattern;

		Rule(Token.Type type, String pattern) {
			this.type = type;
			_pattern = Pattern.compile("^(" + pattern + ")$");
		}

		public boolean matches(CharSequence input) {
			return _pattern.matcher(input).find();
		}

	}

	public static class LexingException extends RuntimeException {
		public final Position sourcePosition;
		public final char character;

		LexingException(char character, Position sourcePosition) {
			super(String.valueOf(character));
			this.character = character;
			this.sourcePosition = sourcePosition;
		}
	}
}
