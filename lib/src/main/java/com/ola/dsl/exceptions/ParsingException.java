package com.ola.dsl.exceptions;

import com.ola.dsl.tokens.Token;

public class ParsingException extends RuntimeException {
    public ParsingException(Token token, String message) {
        super("Error at line %d : %s".formatted(token.line(), message));
    }
}
