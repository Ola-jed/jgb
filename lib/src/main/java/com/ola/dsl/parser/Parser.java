package com.ola.dsl.parser;

import com.ola.dsl.ast.*;
import com.ola.dsl.exceptions.ParsingException;
import com.ola.dsl.tokens.Token;
import com.ola.dsl.tokens.TokenType;
import com.ola.enums.MonomialType;
import com.ola.enums.NumericType;
import com.ola.enums.OrderingType;
import com.ola.number.Complex;
import com.ola.number.Numeric;
import com.ola.number.Rational;
import com.ola.number.Real;
import com.ola.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code Parser} class is responsible for parsing a list of tokens
 * It converts a flat list of lexical tokens into blocks for our polynomials.
 */
public class Parser {
    private final List<Token> tokens;
    private int current;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        current = 0;
    }

    /**
     * Parses the token list into a list of {@link AstNode} instances.
     *
     * <p>Processes the token stream line by line, skipping empty lines.</p>
     *
     * @return list of AST nodes representing parsed polynomial blocks and configurations
     */
    public List<AstNode> parse() {
        var nodes = new ArrayList<AstNode>();
        skipLineBreaks();
        while (!isAtEnd()) {
            nodes.add(parseLine());
            skipLineBreaks();
        }

        return nodes;
    }

    private AstNode parseLine() {
        if (match(TokenType.AT)) {
            return configuration();
        }

        return polynomial();
    }

    private AstNode configuration() {
        if (match(TokenType.VARIABLES_DEFINITION)) {
            return variablesDefinition();
        } else if (match(TokenType.FIELD_DEFINITION)) {
            return fieldDefinition();
        } else if (match(TokenType.ORDERING_DEFINITION)) {
            return orderingDefinition();
        } else if (match(TokenType.DENSE) || match(TokenType.SPARSE)) {
            return structureDefinition();
        }

        throw new ParsingException(peek(), "Expected either variables, field, ordering or structure related configuration after '@'");
    }

    private AstNode variablesDefinition() {
        consume(TokenType.LEFT_PARENTHESIS, "Expected '(' after @variables");
        consume(TokenType.INDETERMINATE, "Expected at least one indeterminate in variables definition");
        var variables = new ArrayList<String>();
        variables.add((String) previous().value());
        while (match(TokenType.COMMA)) {
            consume(TokenType.INDETERMINATE, "Expected indeterminate after ','");
            variables.add((String) previous().value());
        }

        consume(TokenType.RIGHT_PARENTHESIS, "Expected ')' at the end of variables definition");
        return new VariablesConfigurationNode(variables);
    }

    private AstNode fieldDefinition() {
        consume(TokenType.LEFT_PARENTHESIS, "Expected '(' after @field");
        var type = TokenType.EOF;
        var modulo = 0;
        if (match(TokenType.C)) {
            type = TokenType.C;
        } else if (match(TokenType.R)) {
            type = TokenType.R;
        } else if (match(TokenType.Q)) {
            type = TokenType.Q;
        } else if (match(TokenType.GF)) {
            type = TokenType.GF;
            consume(TokenType.LEFT_SQUARE_BRACKET, "Expected '[' after 'GF'");
            var maybeInteger = consume(TokenType.NUMBER, "Expected integer in Galois Field definition")
                    .value();
            if (!(maybeInteger instanceof Integer)) {
                throw new ParsingException(
                        peek(),
                        "Expected integer in Galois Field definition, got value %s".formatted(maybeInteger)
                );
            }

            modulo = (int) maybeInteger;
            consume(TokenType.RIGHT_SQUARE_BRACKET, "Expected ']' after Galois Field definition");
        } else {
            throw new ParsingException(peek(), "Unexpected token %s in field definition".formatted(peek().value()));
        }

        consume(TokenType.RIGHT_PARENTHESIS, "Expected ')' after field definition");
        return new FieldConfigurationNode(NumericType.fromTokenType(type), modulo);
    }

    private AstNode orderingDefinition() {
        consume(TokenType.LEFT_PARENTHESIS, "Expected '(' after @ordering");
        TokenType tokenType;
        if (match(TokenType.LEX)) {
            tokenType = TokenType.LEX;
        } else if (match(TokenType.GRLEX)) {
            tokenType = TokenType.GRLEX;
        } else if (match(TokenType.GREVLEX)) {
            tokenType = TokenType.GREVLEX;
        } else {
            throw new ParsingException(peek(), "Unexpected token in ordering definition");
        }

        consume(TokenType.RIGHT_PARENTHESIS, "Expected ')' after ordering definition");
        return new OrderingConfigurationNode(OrderingType.fromTokenType(tokenType));
    }

    private AstNode structureDefinition() {
        var type = previous().type() == TokenType.DENSE ? TokenType.DENSE : TokenType.SPARSE;
        return new MonomialConfigurationNode(MonomialType.fromTokenType(type));
    }

    private AstNode polynomial() {
        var monomials = new ArrayList<Pair<Numeric, Map<String, Integer>>>();
        var expectOperator = false;
        do {
            monomials.add(monomial(expectOperator));
            expectOperator = true;
        } while (!isAtEnd() && !peekType(TokenType.LINE_BREAK));

        return new PolynomialNode(monomials);
    }

    private Pair<Numeric, Map<String, Integer>> monomial(boolean operatorExpected) {
        if (operatorExpected && !(peekType(TokenType.PLUS) || peekType(TokenType.MINUS))) {
            throw new ParsingException(peek(), "Unexpected '+' or '-'.");
        }

        if (peekType(TokenType.LEFT_PARENTHESIS) || peekType(TokenType.NUMBER) || peekType(TokenType.I)) {
            // There is indeed a coefficient
            var coefficient = coefficient(peekType(TokenType.LEFT_PARENTHESIS));
            if (match(TokenType.TIMES)) {
                var factors = factors();
                return new Pair<>(coefficient, factors);
            }

            // No indeterminates, 0-degree monomial
            return new Pair<>(coefficient, Map.of());
        } else if (match(TokenType.MINUS)) {
            // Negative here, we negate the monomial existing
            var unsignedMonomial = monomial(false);
            return new Pair<>(unsignedMonomial.first().negate(), unsignedMonomial.second());
        } else {
            if (match(TokenType.PLUS) && (peekType(TokenType.LEFT_PARENTHESIS) || peekType(TokenType.NUMBER) || peekType(TokenType.I))) {
                return monomial(false);
            }

            var factors = factors(); // No explicit coefficient, we assume 1
            return new Pair<>(new Real(1), factors);
        }
    }

    private Numeric coefficient(boolean inGroup) {
        if (match(TokenType.LEFT_PARENTHESIS)) {
            var content = coefficient(inGroup);
            consume(TokenType.RIGHT_PARENTHESIS, "Expected ')' after grouping.");
            return content;
        }

        // Handling the edge case where we have the imaginary unit
        if (match(TokenType.I)) {
            return new Complex(0, 1);
        }

        // We are dealing with either GF, Q, C or R.
        // R and GF have single-token elements
        // If we encounter a '/', we consider we are in Q
        // If we encounter a 'I', we consider we are in C, and then we look for another coefficient
        // If we are in a ground and if we get a + or a -, we continue looking, we are in C
        // We read the first number
        var number = consume(TokenType.NUMBER, "Expected number.").value();
        var first = ((Number) number).doubleValue();
        if (match(TokenType.DIVIDE)) {
            // A rational number, we try to get the denominator
            var numerator = (int) first;
            var denominator = (Number) consume(TokenType.NUMBER, "Expected number after '/'.").value();
            return new Rational(numerator, denominator.intValue());
        } else if (match(TokenType.I)) {
            return new Complex(0, first);
        } else if (inGroup && peekType(TokenType.PLUS)) {
            // We check if this can be expressed as (x + yI) or (x + I)
            if (!peekType(TokenType.I, 1) && !(peekType(TokenType.NUMBER, 1) && peekType(TokenType.I, 2))) {
                return new Real(first);
            }

            advance(); // We are indeed parsing a complex number, consume the +
            // We assume we are in a complex number, we are now looking for the imaginary part
            if (match(TokenType.I)) {
                return new Complex(first, 1);
            }

            var imaginaryPart = (Number) consume(TokenType.NUMBER, "Expected number after '+'.").value();
            consume(TokenType.I, "Expected 'I' after imaginary part.");
            return new Complex(first, imaginaryPart.doubleValue());
        } else if (inGroup && peekType(TokenType.MINUS)) {
            // We check if this can be expressed as (x - yI) or (x - I)
            if (!peekType(TokenType.I, 1) && !(peekType(TokenType.NUMBER, 1) && peekType(TokenType.I, 2))) {
                return new Real(first);
            }

            advance(); // We are indeed parsing a complex number, consume the -
            // We assume we are in a complex number, we are now looking for the imaginary part
            var imaginaryPart = (Number) consume(TokenType.NUMBER, "Expected number after '-'.").value();
            consume(TokenType.I, "Expected 'I' after imaginary part.");
            return new Complex(first, -imaginaryPart.doubleValue());
        } else {
            return new Real(first);
        }
    }

    private Map<String, Integer> factors() {
        var content = new HashMap<String, Integer>();
        while (peekType(TokenType.INDETERMINATE)) {
            var variable = (String) consume(TokenType.INDETERMINATE, "Expected indeterminate.").value();
            var exponent = 1;
            if (match(TokenType.EXPONENT)) {
                var exp = consume(TokenType.NUMBER, "Expected number after '^'.");
                if (!(exp.value() instanceof Integer)) {
                    throw new ParsingException(exp, "Exponent value should be an integer");
                }

                exponent = (int) exp.value();
            }

            content.put(variable, exponent);
            match(TokenType.TIMES);
        }

        return content;
    }

    private Token consume(TokenType tokenType, String message) {
        if (tokens.get(current).type() == tokenType) {
            return tokens.get(current++);
        }

        throw new ParsingException(peek(), message);
    }

    private boolean match(TokenType... tokenTypes) {
        for (var tokenType : tokenTypes) {
            if (peekType(tokenType)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean peekType(TokenType type) {
        return current < tokens.size() && tokens.get(current).type() == type;
    }

    private boolean peekType(TokenType type, int distance) {
        return (current + distance) < tokens.size() && tokens.get(current + distance).type() == type;
    }

    private void advance() {
        if (!isAtEnd()) {
            current++;
        }
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().type() == TokenType.EOF;
    }

    private void skipLineBreaks() {
        while (peekType(TokenType.LINE_BREAK)) {
            advance();
        }
    }
}
