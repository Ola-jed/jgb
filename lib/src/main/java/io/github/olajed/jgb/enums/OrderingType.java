package io.github.olajed.jgb.enums;

import io.github.olajed.jgb.dsl.tokens.TokenType;

/**
 * Enumeration of monomial ordering types used in polynomial rings, supported by the DSL.
 *
 * <p>Supports common orderings: lexicographic (LEX), graded lexicographic (GRLEX),
 * and graded reverse lexicographic (GREVLEX).</p>
 *
 * <p>More niche orderings are deliberately not supported here.</p>
 */
public enum OrderingType {
    /** Lexicographic ordering: compares exponents from left to right. */
    LEX,

    /** Graded lexicographic ordering: compares total degree first, then uses lexicographic order as a tie-breaker. */
    GRLEX,

    /** Graded reverse lexicographic ordering: compares total degree first, then uses reverse lexicographic order. */
    GREVLEX;

    /**
     * Converts a {@link TokenType} to the corresponding {@code OrderingType}.
     *
     * @param tokenType the token type representing an ordering
     * @return the matching {@code OrderingType}, or {@code null} if no match
     */
    public static OrderingType fromTokenType(TokenType tokenType) {
        return switch (tokenType) {
            case LEX -> LEX;
            case GRLEX -> GRLEX;
            case GREVLEX -> GREVLEX;
            default -> null;
        };
    }
}
