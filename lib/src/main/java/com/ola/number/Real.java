package com.ola.number;

/**
 * Wrapper for real numbers
 * <p>
 * Stored as double
 */
public final class Real implements Numeric {
    private final double internal;
    public static final Real ONE = new Real(1.0);
    public static final Real ZERO = new Real(0.0);

    public Real(double value) {
        this.internal = value;
    }

    public double get() {
        return internal;
    }

    @Override
    public Numeric add(Numeric other) {
        if (!(other instanceof Real otherReal)) {
            throw new IllegalArgumentException("Cannot add different types");
        }

        return new Real(this.internal + otherReal.internal);
    }

    @Override
    public Numeric subtract(Numeric other) {
        if (!(other instanceof Real otherReal)) {
            throw new IllegalArgumentException("Cannot subtract different types");
        }

        return new Real(this.internal - otherReal.internal);
    }

    @Override
    public Numeric multiply(Numeric other) {
        if (!(other instanceof Real otherReal)) {
            throw new IllegalArgumentException("Cannot multiply different types");
        }

        return new Real(this.internal * otherReal.internal);
    }

    @Override
    public Numeric divide(Numeric other) {
        if (!(other instanceof Real otherReal)) {
            throw new IllegalArgumentException("Cannot divide different types");
        }

        if (otherReal.internal == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }

        return new Real(this.internal / otherReal.internal);
    }

    @Override
    public Numeric negate() {
        return new Real(-internal);
    }

    @Override
    public Numeric inverse() {
        return new Real(1 / internal);
    }

    @Override
    public Numeric one() {
        return ONE;
    }

    @Override
    public Numeric zero() {
        return ZERO;
    }

    @Override
    public String toString() {
        return Double.toString(internal);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        var real = (Real) o;
        return internal == real.internal;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(internal);
    }
}
