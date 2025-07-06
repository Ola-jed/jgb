package com.ola.enums;

import com.ola.dsl.tokens.TokenType;

/**
 * Enumeration of numeric types supported in polynomial computations.
 *
 * <p>Includes common numeric domains such as real numbers, complex numbers,
 * rational numbers, and finite fields (Galois fields).</p>
 */
public enum NumericType {
    GaloisField,
    Rational,
    Real,
    Complex;

    /**
     * Maps a {@link TokenType} to the corresponding {@code NumericType}.
     *
     * @param tokenType the token type representing a numeric domain
     * @return the matching {@code NumericType}, or {@code null} if no match
     */
    public static NumericType fromTokenType(TokenType tokenType) {
        return switch (tokenType) {
            case R -> Real;
            case C -> Complex;
            case Q -> Rational;
            case GF -> GaloisField;
            default -> null;
        };
    }
}

