package com.ola.number;

import com.ola.utils.ComplexInternals;

/**
 * Wrapper for complex numbers
 * <p>
 * Represented by z = a + ib where a (real part), b (imaginary part) are real numbers (doubles)
 */
public final class Complex implements Numeric {
    private final ComplexInternals internal;
    public static final Complex zero = new Complex(ComplexInternals.ZERO);
    public static final Complex one = new Complex(ComplexInternals.ONE);

    public Complex(double real, double imaginary) {
        this.internal = ComplexInternals.ofCartesian(real, imaginary);
    }

    private Complex(ComplexInternals representation) {
        this.internal = representation;
    }

    @Override
    public Numeric add(Numeric other) {
        if (!(other instanceof Complex otherComplex)) {
            throw new IllegalArgumentException("Cannot add different types");
        }

        var result = internal.add(otherComplex.internal);
        return new Complex(result);
    }

    @Override
    public Numeric subtract(Numeric other) {
        if (!(other instanceof Complex otherComplex)) {
            throw new IllegalArgumentException("Cannot subtract different types");
        }

        var result = internal.subtract(otherComplex.internal);
        return new Complex(result);
    }

    @Override
    public Numeric multiply(Numeric other) {
        if (!(other instanceof Complex otherComplex)) {
            throw new IllegalArgumentException("Cannot multiply different types");
        }

        var result = internal.multiply(otherComplex.internal);
        return new Complex(result);
    }

    @Override
    public Numeric divide(Numeric other) {
        if (!(other instanceof Complex otherComplex)) {
            throw new IllegalArgumentException("Cannot divide different types");
        }

        var result = internal.divide(otherComplex.internal);
        return new Complex(result);
    }

    @Override
    public Numeric negate() {
        return new Complex(this.internal.negate());
    }

    @Override
    public Numeric inverse() {
        return new Complex(ComplexInternals.ONE.divide(this.internal));
    }

    @Override
    public Numeric one() {
        return one;
    }

    @Override
    public Numeric zero() {
        return zero;
    }

    @Override
    public String toString() {
        return "(%s + %si)".formatted(internal.real(), internal.imag());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        var complex = (Complex) o;
        return internal.equals(complex.internal);
    }

    @Override
    public int hashCode() {
        return internal.hashCode();
    }
}
