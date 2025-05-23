package com.ola.dsl.lexer;

import com.ola.dsl.tokens.Token;
import com.ola.dsl.tokens.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final List<Token> tokens = new ArrayList<>();
    private int start;
    private int current;
    private int currentLine;
    private String source;

    public List<Token> scan(String source) {
        this.source = source;
        tokens.clear();
        currentLine = 1;
        start = 0;
        current = 0;
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        addToken(TokenType.EOF, null);
        return tokens;
    }

    private void scanToken() {
        var character = advance();

        if (character == '#') {
            // Comment, we can just skip the whole line
            skipLine();
        } else if (TokenType.tokenMapppings.containsKey(character)) {
            addToken(TokenType.tokenMapppings.get(character), String.valueOf(character));
        } else if (character == '\n') {
            addToken(TokenType.LINE_BREAK, null);
            currentLine++;
        } else if (Character.isWhitespace(character)) {
            // Nothing to do
            return;
        } else if (Character.isDigit(character)) {
            number();
        } else if (matchKeyword("dense", TokenType.DENSE)) {
            // Use dense representation for monomials
            return;
        } else if (matchKeyword("sparse", TokenType.SPARSE)) {
            // Use dense representation for monomials
            return;
        } else if (matchKeyword("GF", TokenType.GF)) {
            // Galois Field
            return;
        } else if (matchKeyword("lex", TokenType.LEX)) {
            // Lexical ordering
            return;
        } else if (matchKeyword("grlex", TokenType.GRLEX)) {
            // Graded lexical ordering
            return;
        } else if (matchKeyword("grevlex", TokenType.GREVLEX)) {
            // Graded reverse lexical ordering
            return;
        } else if (matchKeyword("variables", TokenType.VARIABLES_DEFINITION)) {
            // @variables
            return;
        } else if (matchKeyword("field", TokenType.FIELD_DEFINITION)) {
            // @field
            return;
        } else if (matchKeyword("ordering", TokenType.ORDERING_DEFINITION)) {
            // @ordering
            return;
        } else if ((Character.isLowerCase(character) && Character.isLetter(character)) || character == '_') {
            indeterminate();
        } else {
            throw new IllegalArgumentException("Unexpected character '%s' on line %d".formatted(character, currentLine));
        }
    }

    private void number() {
        while (Character.isDigit(peek())) {
            advance();
        }

        if (peek() == '.' && Character.isDigit(peekNext())) {
            do {
                advance();
            } while (Character.isDigit(peek()));
            var token = new Token(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)), currentLine);
            tokens.add(token);
        } else {
            var token = new Token(TokenType.NUMBER, Integer.parseInt(source.substring(start, current)), currentLine);
            tokens.add(token);
        }
    }

    private void indeterminate() {
        while (isValidIndeterminateCharacter(peek())) {
            advance();
        }

        var text = source.substring(start, current);
        addToken(TokenType.INDETERMINATE, text);
    }

    private char advance() {
        skip(1);
        return source.charAt(current - 1);
    }

    private void skip(int count) {
        current += count;
    }

    private void skipLine() {
        while (!isAtEnd() && peek() != '\n') {
            skip(1);
        }
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }

        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) {
            return '\0';
        }

        return source.charAt(current + 1);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private boolean matchKeyword(String keyword, TokenType type) {
        var suffix = keyword.substring(1);
        if (source.startsWith(suffix, current)) {
            var end = current + suffix.length();
            // Check that the next character (if any) is NOT a valid identifier char
            if (end < source.length()) {
                var nextChar = source.charAt(end);
                if (Character.isLetterOrDigit(nextChar) || nextChar == '_') {
                    return false;
                }
            }

            skip(suffix.length());
            addToken(type, keyword);
            return true;
        }

        return false;
    }

    private void addToken(TokenType type, String lexeme) {
        tokens.add(new Token(type, lexeme, currentLine));
    }

    private boolean isValidIndeterminateCharacter(char c) {
        return Character.isLetter(c) || Character.isDigit(c) || c == '_';
    }
}
