package io.github.olajed.jgb.dsl.lexer;

import io.github.olajed.jgb.dsl.tokens.Token;
import io.github.olajed.jgb.dsl.tokens.TokenType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {
    private final Lexer lexer = new Lexer();

    @Test
    void testEmptyInput() {
        List<Token> tokens = lexer.scan("");
        assertEquals(1, tokens.size());
        assertEquals(TokenType.EOF, tokens.getFirst().type());
    }

    @Test
    void testIntegerValue() {
        List<Token> tokens = lexer.scan("42");
        assertEquals(TokenType.NUMBER, tokens.getFirst().type());
        assertEquals(42, tokens.getFirst().value());
        assertEquals(TokenType.EOF, tokens.get(1).type());
    }

    @Test
    void testDecimalValue() {
        List<Token> tokens = lexer.scan("3.14");
        assertEquals(TokenType.NUMBER, tokens.getFirst().type());
        assertEquals(3.14, (double) tokens.getFirst().value(), 0.0001);
    }

    @Test
    void testIdentifierBecomesIndeterminate() {
        List<Token> tokens = lexer.scan("x1");
        assertEquals(TokenType.INDETERMINATE, tokens.getFirst().type());
        assertEquals("x1", tokens.getFirst().value());
    }

    @Test
    void testKeywordDense() {
        List<Token> tokens = lexer.scan("dense");
        assertEquals(TokenType.DENSE, tokens.getFirst().type());
        assertEquals("dense", tokens.getFirst().value());
    }

    @Test
    void testKeywordGF() {
        List<Token> tokens = lexer.scan("GF");
        assertEquals(TokenType.GF, tokens.getFirst().type());
        assertEquals("GF", tokens.getFirst().value());
    }

    @Test
    void testOperatorsFromTokenMappings() {
        List<Token> tokens = lexer.scan("+-*/^");
        assertEquals(TokenType.PLUS, tokens.getFirst().type());
        assertEquals(TokenType.MINUS, tokens.get(1).type());
        assertEquals(TokenType.TIMES, tokens.get(2).type());
        assertEquals(TokenType.DIVIDE, tokens.get(3).type());
        assertEquals(TokenType.EXPONENT, tokens.get(4).type());
    }

    @Test
    void testParenthesesAndBrackets() {
        List<Token> tokens = lexer.scan("()[]");
        assertEquals(TokenType.LEFT_PARENTHESIS, tokens.getFirst().type());
        assertEquals(TokenType.RIGHT_PARENTHESIS, tokens.get(1).type());
        assertEquals(TokenType.LEFT_SQUARE_BRACKET, tokens.get(2).type());
        assertEquals(TokenType.RIGHT_SQUARE_BRACKET, tokens.get(3).type());
    }

    @Test
    void testLineBreakIncreasesLineCount() {
        List<Token> tokens = lexer.scan("x\ny");
        assertEquals(TokenType.INDETERMINATE, tokens.getFirst().type());
        assertEquals(1, tokens.getFirst().line());

        assertEquals(TokenType.LINE_BREAK, tokens.get(1).type());

        assertEquals(TokenType.INDETERMINATE, tokens.get(2).type());
        assertEquals(2, tokens.get(2).line());
    }

    @Test
    void testCommentIsSkipped() {
        List<Token> tokens = lexer.scan("# comment\nx");
        assertEquals(TokenType.LINE_BREAK, tokens.getFirst().type());
        assertEquals(TokenType.INDETERMINATE, tokens.get(1).type());
        assertEquals("x", tokens.get(1).value());
    }

    @Test
    void testWhitespaceIsIgnored() {
        List<Token> tokens = lexer.scan("   99   ");
        assertEquals(TokenType.NUMBER, tokens.getFirst().type());
        assertEquals(99, tokens.getFirst().value());
    }

    @Test
    void testUnexpectedCharacterThrows() {
        assertThrows(IllegalArgumentException.class, () -> lexer.scan("$"));
    }

    @Test
    void testComplexExpression() {
        String src = "GF(x, y) ordering lex\nsparse";
        List<Token> tokens = lexer.scan(src);

        assertEquals(TokenType.GF, tokens.getFirst().type());
        assertEquals(TokenType.LEFT_PARENTHESIS, tokens.get(1).type());
        assertEquals(TokenType.INDETERMINATE, tokens.get(2).type());
        assertEquals(TokenType.COMMA, tokens.get(3).type());
        assertEquals(TokenType.INDETERMINATE, tokens.get(4).type());
        assertEquals(TokenType.RIGHT_PARENTHESIS, tokens.get(5).type());
        assertEquals(TokenType.ORDERING_DEFINITION, tokens.get(6).type());
        assertEquals(TokenType.LEX, tokens.get(7).type());
        assertEquals(TokenType.LINE_BREAK, tokens.get(8).type());
        assertEquals(TokenType.SPARSE, tokens.get(9).type());
    }
}
