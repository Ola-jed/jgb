package io.github.olajed.jgb.number;

import io.github.olajed.jgb.utils.ArithmeticUtils;

/**
 * Immutable representation of a rational number as a numerator and denominator.
 *
 * <p>Implements the {@link Numeric} interface for numeric operations.</p>
 *
 * <p>Provides constants for zero and one.</p>
 */
public final class Rational implements Numeric {
    public static final Rational ZERO = new Rational(0);
    public static final Rational ONE = new Rational(1);
    private final int numerator;
    private final int denominator;

    public Rational(int num, int den) {
        if (den == 0) {
            throw new ArithmeticException("Denominator must be different from 0");
        } else {
            if (num == den) {
                this.numerator = 1;
                this.denominator = 1;
            } else {
                int p;
                int q;
                if (((num | den) & 1) == 0) {
                    p = num >> 1;
                    q = den >> 1;
                } else {
                    p = num;
                    q = den;
                }

                int d = ArithmeticUtils.gcd(p, q);
                this.numerator = p / d;
                this.denominator = q / d;
            }
        }
    }

    public Rational(int num) {
        this.numerator = num;
        this.denominator = 1;
    }

    public static Rational of(int num) {
        return num == 0 ? ZERO : new Rational(num);
    }

    public static Rational of(int num, int den) {
        return num == 0 ? ZERO : new Rational(num, den);
    }

    public Rational zero() {
        return ZERO;
    }

    public boolean isZero() {
        return this.numerator == 0;
    }

    public Rational one() {
        return ONE;
    }

    public boolean isOne() {
        return this.numerator == this.denominator;
    }

    public int signum() {
        return Integer.signum(this.numerator) * Integer.signum(this.denominator);
    }

    public Rational negate() {
        return this.numerator == Integer.MIN_VALUE
                ? new Rational(this.numerator, -this.denominator)
                : new Rational(-this.numerator, this.denominator);
    }

    public Rational add(int value) {
        if (value == 0) {
            return this;
        } else if (this.isZero()) {
            return new Rational(value);
        } else {
            var num = (long) value * (long) this.denominator;
            return of(Math.toIntExact((long) this.numerator + num), this.denominator);
        }
    }

    @Override
    public Numeric add(Numeric other) {
        if (!(other instanceof Rational value)) {
            throw new IllegalArgumentException("Cannot add different types");
        }

        return this.addSub(value, true);
    }

    public Rational subtract(int value) {
        if (value == 0) {
            return this;
        } else if (this.isZero()) {
            return value == Integer.MIN_VALUE ? new Rational(Integer.MIN_VALUE, -1) : new Rational(-value);
        } else {
            var num = (long) value * (long) this.denominator;
            return of(Math.toIntExact((long) this.numerator - num), this.denominator);
        }
    }

    @Override
    public Numeric subtract(Numeric other) {
        if (!(other instanceof Rational value)) {
            throw new IllegalArgumentException("Cannot subtract different types");
        }

        return this.addSub(value, false);
    }

    private Rational addSub(Rational value, boolean isAdd) {
        if (value.isZero()) {
            return this;
        } else if (this.isZero()) {
            return isAdd ? value : value.negate();
        } else {
            int d1 = ArithmeticUtils.gcd(this.denominator, value.denominator);
            long uvp = (long) this.numerator * (long) (value.denominator / d1);
            long upv = (long) value.numerator * (long) (this.denominator / d1);
            long t = isAdd ? uvp + upv : uvp - upv;
            long d2 = ArithmeticUtils.gcd(t, (long) d1);
            return of(Math.toIntExact(t / d2), Math.multiplyExact(this.denominator / d1, value.denominator / (int) d2));
        }
    }

    public Rational multiply(int value) {
        if (value != 0 && !this.isZero()) {
            var d2 = ArithmeticUtils.gcd(value, this.denominator);
            return new Rational(Math.multiplyExact(this.numerator, value / d2), this.denominator / d2);
        } else {
            return ZERO;
        }
    }

    @Override
    public Numeric multiply(Numeric other) {
        if (!(other instanceof Rational value)) {
            throw new IllegalArgumentException("Cannot multiply different types");
        }

        return !value.isZero() && !this.isZero() ? this.multiply(value.numerator, value.denominator) : ZERO;
    }

    private Rational multiply(int num, int den) {
        var d1 = ArithmeticUtils.gcd(this.numerator, den);
        var d2 = ArithmeticUtils.gcd(num, this.denominator);
        return new Rational(Math.multiplyExact(this.numerator / d1, num / d2), Math.multiplyExact(this.denominator / d2, den / d1));
    }

    public Rational divide(int value) {
        if (value == 0) {
            throw new ArithmeticException("The value to divide by must not be zero");
        } else if (this.isZero()) {
            return ZERO;
        } else {
            var d1 = ArithmeticUtils.gcd(this.numerator, value);
            return new Rational(this.numerator / d1, Math.multiplyExact(this.denominator, value / d1));
        }
    }

    @Override
    public Numeric divide(Numeric other) {
        if (!(other instanceof Rational value)) {
            throw new IllegalArgumentException("Cannot divide different types");
        }

        if (value.isZero()) {
            throw new ArithmeticException("The value to divide by must not be zero");
        } else {
            return this.isZero() ? ZERO : this.multiply(value.denominator, value.numerator);
        }
    }

    @Override
    public Numeric inverse() {
        return new Rational(denominator, numerator);
    }

    public double get() {
        return numerator * 1.0 / denominator;
    }

    public String toString() {
        String str;
        if (this.isZero()) {
            str = "0";
        } else if (this.denominator == 1) {
            str = Integer.toString(this.numerator);
        } else {
            str = this.numerator + " / " + this.denominator;
        }

        return str;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            if (other instanceof Rational rhs) {
                if (this.signum() == rhs.signum()) {
                    return Math.abs(this.numerator) == Math.abs(rhs.numerator) && Math.abs(this.denominator) == Math.abs(rhs.denominator);
                }
            }

            return false;
        }
    }

    @Override
    public int hashCode() {
        var numS = Integer.signum(this.numerator);
        var denS = Integer.signum(this.denominator);
        return (31 * (31 + this.numerator * numS) + this.denominator * denS) * numS * denS;
    }
}
