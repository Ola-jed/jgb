package com.ola.enums;

import com.ola.dsl.tokens.TokenType;

/**
 * Enumeration of monomial ordering types used in polynomial rings, supported by the DSL.
 *
 * <p>Supports common orderings: lexicographic (LEX), graded lexicographic (GRLEX),
 * and graded reverse lexicographic (GREVLEX).</p>
 *
 * <p>More niche orderings are deliberately not supported here.</p>
 */
public enum OrderingType {
    LEX,
    GRLEX,
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
