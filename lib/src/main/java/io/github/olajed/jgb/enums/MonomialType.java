package io.github.olajed.jgb.enums;

import io.github.olajed.jgb.dsl.tokens.TokenType;

/**
 * Enumeration representing monomial storage types used in polynomial representations.
 *
 * <p>DENSE indicates monomials with explicit coefficients for all variables,
 * while SPARSE indicates monomials storing only nonzero exponents.</p>
 */
public enum MonomialType {
    /** Represents monomials with all exponents stored explicitly, including zeros. */
    DENSE,

    /** Represents monomials storing only non-zero exponents for efficiency. */
    SPARSE;

    /**
     * Converts a {@link TokenType} to the corresponding {@code MonomialType}.
     *
     * @param tokenType the token type to convert
     * @return the matching {@code MonomialType}, or {@code null} if no match
     */
    public static MonomialType fromTokenType(TokenType tokenType) {
        return switch (tokenType) {
            case TokenType.DENSE -> DENSE;
            case TokenType.SPARSE -> SPARSE;
            default -> null;
        };
    }
}
