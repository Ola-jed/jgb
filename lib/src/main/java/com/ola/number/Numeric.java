package com.ola.number;

/**
 * Base structure for numbers that are part of a field
 * The fields supported are Q (Rational numbers), R (Real numbers) and C (Complex numbers)
 */
public interface Numeric {
    Numeric add(Numeric other);

    Numeric subtract(Numeric other);

    Numeric multiply(Numeric other);

    Numeric divide(Numeric other);

    Numeric negate();

    Numeric one();

    Numeric zero();
}
