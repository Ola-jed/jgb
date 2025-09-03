package io.github.olajed.jgb.dsl.parser;

import io.github.olajed.jgb.dsl.ast.*;
import io.github.olajed.jgb.dsl.exceptions.ParsingException;
import io.github.olajed.jgb.dsl.tokens.Token;
import io.github.olajed.jgb.dsl.tokens.TokenType;
import io.github.olajed.jgb.enums.NumericType;
import io.github.olajed.jgb.enums.OrderingType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    private Token t(TokenType type, Object value) {
        return new Token(type, value, 1);
    }

    private Token t(TokenType type) {
        return new Token(type, null, 1);
    }

    private List<AstNode> parse(Token... tokens) {
        List<Token> list = new ArrayList<>(List.of(tokens));
        list.add(new Token(TokenType.EOF, null, 1));
        Parser parser = new Parser(list);
        return parser.parse();
    }

    @Test
    void testVariablesDefinition() {
        var nodes = parse(
                t(TokenType.AT),
                t(TokenType.VARIABLES_DEFINITION),
                t(TokenType.LEFT_PARENTHESIS),
                t(TokenType.INDETERMINATE, "x"),
                t(TokenType.COMMA),
                t(TokenType.INDETERMINATE, "y"),
                t(TokenType.RIGHT_PARENTHESIS)
        );

        assertEquals(1, nodes.size());
        assertInstanceOf(VariablesConfigurationNode.class, nodes.getFirst());

        var vars = ((VariablesConfigurationNode) nodes.getFirst()).getVariables();
        assertEquals(List.of("x", "y"), vars);
    }

    @Test
    void testFieldDefinitionGF() {
        var nodes = parse(
                t(TokenType.AT),
                t(TokenType.FIELD_DEFINITION),
                t(TokenType.LEFT_PARENTHESIS),
                t(TokenType.GF),
                t(TokenType.LEFT_SQUARE_BRACKET),
                t(TokenType.NUMBER, 7),
                t(TokenType.RIGHT_SQUARE_BRACKET),
                t(TokenType.RIGHT_PARENTHESIS)
        );

        assertEquals(1, nodes.size());
        assertInstanceOf(FieldConfigurationNode.class, nodes.getFirst());

        var field = (FieldConfigurationNode) nodes.getFirst();
        assertEquals(NumericType.GaloisField, field.getElementsType());
        assertEquals(7, field.getModulo());
    }

    @Test
    void testOrderingDefinition() {
        var nodes = parse(
                t(TokenType.AT),
                t(TokenType.ORDERING_DEFINITION),
                t(TokenType.LEFT_PARENTHESIS),
                t(TokenType.GREVLEX),
                t(TokenType.RIGHT_PARENTHESIS)
        );

        assertEquals(1, nodes.size());
        assertInstanceOf(OrderingConfigurationNode.class, nodes.getFirst());

        var ordering = ((OrderingConfigurationNode) nodes.getFirst()).getOrderingType();
        assertEquals(OrderingType.GREVLEX, ordering);
    }

    @Test
    void testPolynomial() {
        var nodes = parse(
                t(TokenType.NUMBER, 2),
                t(TokenType.TIMES),
                t(TokenType.INDETERMINATE, "x"),
                t(TokenType.EXPONENT),
                t(TokenType.NUMBER, 2),
                t(TokenType.PLUS),
                t(TokenType.NUMBER, 3),
                t(TokenType.TIMES),
                t(TokenType.INDETERMINATE, "y")
        );

        assertEquals(1, nodes.size());
        assertInstanceOf(PolynomialNode.class, nodes.getFirst());
        var poly = (PolynomialNode) nodes.getFirst();
        assertEquals(2, poly.getMonomials().size());
    }

    @Test
    void testVariablesDefinitionMissingParenThrows() {
        assertThrows(ParsingException.class, () -> parse(
                t(TokenType.AT),
                t(TokenType.VARIABLES_DEFINITION),
                t(TokenType.LEFT_PARENTHESIS),
                t(TokenType.INDETERMINATE, "x")
        ));
    }
}

