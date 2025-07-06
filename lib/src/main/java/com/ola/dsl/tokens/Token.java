package com.ola.dsl.tokens;

/**
 * Immutable representation of a lexical token.
 *
 * <p>Each token has a {@link TokenType}, an associated value (e.g., identifier name,
 * numeric literal), and the line number where it appears in the source.</p>
 */
public record Token(TokenType type, Object value, int line) {
}