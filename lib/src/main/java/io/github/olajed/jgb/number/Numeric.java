package io.github.olajed.jgb.number;

/**
 * Represents a numeric type supporting basic arithmetic operations.
 *
 * <p>This sealed interface restricts implementations to a fixed set of numeric types:
 * {@link GaloisFieldElement}, {@link Rational}, {@link Real}, and {@link Complex}.</p>
 */
public sealed interface Numeric permits GaloisFieldElement, Rational, Real, Complex {
    /**
     * Adds this numeric value to another.
     *
     * @param other the numeric value to add
     * @return the sum of this and {@code other}
     */
    Numeric add(Numeric other);

    /**
     * Subtracts another numeric value from this one.
     *
     * @param other the numeric value to subtract
     * @return the difference between this and {@code other}
     */
    Numeric subtract(Numeric other);

    /**
     * Multiplies this numeric value by another.
     *
     * @param other the numeric value to multiply by
     * @return the product of this and {@code other}
     */
    Numeric multiply(Numeric other);

    /**
     * Divides this numeric value by another.
     *
     * @param other the numeric value to divide by
     * @return the quotient of this divided by {@code other}
     * @throws ArithmeticException if division by zero occurs
     */
    Numeric divide(Numeric other);

    /**
     * Returns the additive inverse (negation) of this numeric value.
     *
     * @return the negation of this value
     */
    Numeric negate();

    /**
     * Returns the multiplicative inverse of this numeric value.
     *
     * @return the inverse of this value
     * @throws ArithmeticException if this value has no multiplicative inverse (e.g., zero)
     */
    Numeric inverse();

    /**
     * Returns the multiplicative identity value for this numeric type.
     *
     * @return the numeric value representing one
     */
    Numeric one();

    /**
     * Returns the additive identity value for this numeric type.
     *
     * @return the numeric value representing zero
     */
    Numeric zero();
}

