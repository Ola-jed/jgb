package com.ola.number;

/**
 * Base structure for numbers that are part of a field
 * The fields supported are Q (Rational numbers), R (Real numbers), C (Complex numbers) and Z/pZ (Galois Fields)
 */
public sealed interface Numeric permits Complex, GaloisFieldElement, Rational, Real {
    Numeric add(Numeric other);

    Numeric subtract(Numeric other);

    Numeric multiply(Numeric other);

    Numeric divide(Numeric other);

    Numeric negate();

    Numeric inverse();

    Numeric one();

    Numeric zero();
}
