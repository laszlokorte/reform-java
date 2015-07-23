package reform.playground.views.sheet;

import reform.components.expression.ExpressionEditor;
import reform.data.sheet.DataSet;
import reform.data.sheet.Solver;
import reform.data.sheet.expression.Expression;
import reform.data.sheet.expression.InvalidExpression;
import reform.data.syntax.Lexer;
import reform.data.syntax.Parser;
import reform.data.syntax.SimpleDelegate;
import reform.data.syntax.Token;
import reform.evented.core.EventedSheet;

/**
 * Created by laszlokorte on 23.07.15.
 */
public class ExpressionParser implements ExpressionEditor.Parser
{
	private final Lexer _lexer = getLexer();
	private final Parser.ParserDelegate _delegate;
	private final Parser<Expression> _parser;

	public ExpressionParser(EventedSheet sheet)
	{
		_delegate = new SimpleDelegate(sheet.getRaw());
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

		generator.add(Token.Type.ArgumentSeparator, ",");
		generator.add(Token.Type.LiteralValue, "(0|([1-9][0-9]*))(\\.[0-9]*)?");
		generator.add(Token.Type.LiteralValue, "\"[^\"]+\"");
		generator.add(Token.Type.Identifier, "\\$[a-zA-Z_][_a-zA-Z0-9]*");
		generator.add(Token.Type.FunctionName, "[a-z]+");
		generator.add(Token.Type.LiteralValue, "\"[^\"]*\"");

		return generator.getLexer();
	}

}
