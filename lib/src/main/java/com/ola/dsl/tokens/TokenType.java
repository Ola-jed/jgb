package com.ola.dsl.tokens;

import java.util.Map;

public enum TokenType {
    R,
    C,
    Q,
    GF,
    MINUS,
    PLUS,
    TIMES,
    DIVIDE,
    EXPONENT,
    NUMBER,
    INDETERMINATE,
    LEFT_PARENTHESIS,
    RIGHT_PARENTHESIS,
    LEFT_SQUARE_BRACKET,
    RIGHT_SQUARE_BRACKET,
    LINE_BREAK,
    AT,
    HASH,
    I,
    VARIABLES_DEFINITION,
    ORDERING_DEFINITION,
    FIELD_DEFINITION,
    DENSE,
    SPARSE,
    LEX,
    GRLEX,
    GREVLEX,
    COMMA,
    EOF;

    public static final Map<Character, TokenType> tokenMapppings = Map.ofEntries(
            Map.entry('+', PLUS),
            Map.entry('-', MINUS),
            Map.entry('/', DIVIDE),
            Map.entry('*', TIMES),
            Map.entry('^', EXPONENT),
            Map.entry('(', LEFT_PARENTHESIS),
            Map.entry(')', RIGHT_PARENTHESIS),
            Map.entry('[', LEFT_SQUARE_BRACKET),
            Map.entry(']', RIGHT_SQUARE_BRACKET),
            Map.entry('@', AT),
            Map.entry('#', HASH),
            Map.entry('I', I),
            Map.entry('R', R),
            Map.entry('C', C),
            Map.entry('Q', Q),
            Map.entry(',', COMMA)
    );
}
