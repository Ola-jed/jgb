package com.ola.dsl.parser;

import com.ola.dsl.ast.*;
import com.ola.dsl.exceptions.ParsingException;
import com.ola.dsl.tokens.Token;
import com.ola.dsl.tokens.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        current = 0;
    }

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
        return new FieldConfigurationNode(type, modulo);
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
        return new OrderingConfigurationNode(tokenType);
    }

    private AstNode structureDefinition() {
        var type = previous().type() == TokenType.DENSE ? TokenType.DENSE : TokenType.SPARSE;
        return new MonomialConfigurationNode(type);
    }

    private AstNode polynomial() {
        // TODO : Implement this
        throw new RuntimeException();
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

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }

        return previous();
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
