package io.github.olajed.jgb.dsl.tokens;

/**
 * Immutable representation of a lexical token.
 *
 * <p>Each token has a {@link TokenType}, an associated value (e.g., identifier name,
 * numeric literal), and the line number where it appears in the source.</p>
 *
 * @param type the type of token (e.g., identifier, keyword, number)
 * @param value the token's associated value (such as the literal text or parsed number)
 * @param line the line number where the token appears in the source code
 */
public record Token(TokenType type, Object value, int line) {
}
