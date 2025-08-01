package io.github.olajed.jgb.enums;

import io.github.olajed.jgb.dsl.tokens.TokenType;

/**
 * Enumeration of numeric types supported in polynomial computations.
 *
 * <p>Includes common numeric domains such as real numbers, complex numbers,
 * rational numbers, and finite fields (Galois fields).</p>
 */
public enum NumericType {
    /** Represents elements of a finite field (Galois field). */
    GaloisField,

    /** Represents rational numbers (fractions of integers). */
    Rational,

    /** Represents real numbers with floating-point precision. */
    Real,

    /** Represents complex numbers with real and imaginary parts. */
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

