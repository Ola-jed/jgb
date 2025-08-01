package io.github.olajed.jgb.dsl.exceptions;

import io.github.olajed.jgb.dsl.tokens.Token;

import java.io.Serial;

/**
 * Exception thrown when a syntactic or semantic error is encountered during DSL parsing.
 * <p>
 * Includes the offending {@link Token} and a human-readable explanation.
 * <p>
 * This exception is unchecked and can be allowed to propagate through visitor-based parsing.
 *
 * @see Token for token metadata
 */
public class ParsingException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a parsing exception at a specific token, with diagnostic context.
     *
     * @param token   the token at which the error occurred
     * @param message human-readable error explanation
     */
    public ParsingException(Token token, String message) {
        super("Error at line %d [Token '%s']: %s".formatted(token.line(), token.value(), message));
    }
}
