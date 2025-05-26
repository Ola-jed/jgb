package com.ola.dsl.exceptions;

import com.ola.dsl.tokens.Token;

import java.io.Serial;

public class ParsingException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ParsingException(Token token, String message) {
        super("Error at line %d [Token '%s']: %s".formatted(token.line(), token.value(), message));
    }
}
