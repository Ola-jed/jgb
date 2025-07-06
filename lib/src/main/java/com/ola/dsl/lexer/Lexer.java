package com.ola.dsl.lexer;

import com.ola.dsl.tokens.Token;
import com.ola.dsl.tokens.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The {@code Lexer} class is responsible for lexical analysis, converting
 * a raw source string into a list of {@link Token} objects. It identifies
 * lexical units such as configuration, indeterminates, numbers, and operators.
 */
public class Lexer {
    private final List<Token> tokens = new ArrayList<>();
    private int start;
    private int current;
    private int currentLine;
    private char[] sourceChars;
    private int sourceLength;

    /**
     * Scans the input source string and returns a list of tokens.
     * <p>
     * Iteratively processes characters until the end of input is reached.
     * Adds an EOF token at the end to signal the end of input stream.
     * </p>
     *
     * @param source raw source code string to tokenize
     * @return list of {@link Token} objects representing lexical units
     */
    public List<Token> scan(String source) {
        sourceChars = source.toCharArray();
        sourceLength = sourceChars.length;
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
        char character = advance();

        switch (character) {
            case '#': // Comment, skip the whole line
                skipLine();
                break;
            case '\n':
                addToken(TokenType.LINE_BREAK, "\\n");
                currentLine++;
                break;
            default:
                if (TokenType.tokenMappings.containsKey(character)) {
                    // Cache the string to avoid repeated String.valueOf calls
                    addToken(TokenType.tokenMappings.get(character), String.valueOf(character));
                } else if (Character.isWhitespace(character)) {
                    // Skip whitespace - no action needed
                } else if (Character.isDigit(character)) {
                    number();
                } else if (Character.isLetter(character) || character == '_') {
                    // Handle both keywords and indeterminates in one method
                    identifierOrKeyword();
                } else {
                    throw new IllegalArgumentException("Unexpected character '%s' on line %d".formatted(character, currentLine));
                }

                break;
        }
    }

    private void number() {
        // Consume all digits
        while (current < sourceLength && Character.isDigit(sourceChars[current])) {
            current++;
        }

        // Checking for decimal point
        if (current < sourceLength && sourceChars[current] == '.' && current + 1 < sourceLength && Character.isDigit(sourceChars[current + 1])) {
            current++; // consume '.'
            while (current < sourceLength && Character.isDigit(sourceChars[current])) {
                current++;
            }

            // Parse as double
            var numberStr = new String(sourceChars, start, current - start);
            tokens.add(new Token(TokenType.NUMBER, Double.parseDouble(numberStr), currentLine));
        } else {
            // Parse as integer
            var numberStr = new String(sourceChars, start, current - start);
            tokens.add(new Token(TokenType.NUMBER, Integer.parseInt(numberStr), currentLine));
        }
    }

    private void identifierOrKeyword() {
        // Consume all valid identifier characters
        while (current < sourceLength && isValidIdentifierCharacter(sourceChars[current])) {
            current++;
        }

        var text = new String(sourceChars, start, current - start);
        var keywordType = TokenType.keywords.get(text);
        addToken(Objects.requireNonNullElse(keywordType, TokenType.INDETERMINATE), text);
    }

    private char advance() {
        return sourceChars[current++];
    }

    private void skipLine() {
        while (current < sourceLength && sourceChars[current] != '\n') {
            current++;
        }
    }

    private boolean isAtEnd() {
        return current >= sourceLength;
    }

    private void addToken(TokenType type, String lexeme) {
        tokens.add(new Token(type, lexeme, currentLine));
    }

    private boolean isValidIdentifierCharacter(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }
}
