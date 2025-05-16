package com.ola.number;

import java.math.BigInteger;

/**
 * Wrapper for rational numbers
 * <p>
 * Represented by x = a / b where a and b are arbitrary precision integers,
 * b nonzero and gcd(a, b) = 1
 */
public final class Rational implements Numeric {
    private final BigInteger num;
    private final BigInteger den;
    public static final Rational one = new Rational(BigInteger.ONE, BigInteger.ONE);
    public static final Rational zero = new Rational(BigInteger.ZERO, BigInteger.ONE);

    public Rational(BigInteger num, BigInteger den) {
        if (den.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Denominator is zero");
        }

        BigInteger g = num.gcd(den);
        BigInteger n = num.divide(g);
        BigInteger d = den.divide(g);

        if (d.compareTo(BigInteger.ZERO) < 0) {
            this.den = d.negate();
            this.num = n.negate();
        } else {
            this.den = d;
            this.num = n;
        }
    }

    public Rational(BigInteger num) {
        this.num = num;
        this.den = BigInteger.ONE;
    }

    public Rational(int num, int den) {
        this(BigInteger.valueOf(num), BigInteger.valueOf(den));
    }

    public Rational(int num) {
        this.num = BigInteger.valueOf(num);
        this.den = BigInteger.ONE;
    }

    private static BigInteger lcm(BigInteger m, BigInteger n) {
        return m.multiply(n.divide(m.gcd(n)));
    }

    @Override
    public Numeric add(Numeric other) {
        if (!(other instanceof Rational otherRational)) {
            throw new IllegalArgumentException("Cannot add different types");
        }

        if (this.equals(zero)) return other;
        if (otherRational.equals(zero)) return this;

        BigInteger f = this.num.gcd(otherRational.num);
        BigInteger g = this.den.gcd(otherRational.den);

        BigInteger newNum = this.num.divide(f).multiply(otherRational.den.divide(g))
                .add(otherRational.num.divide(f).multiply(this.den.divide(g)));
        BigInteger newDen = lcm(this.den, otherRational.den);

        newNum = newNum.multiply(f);
        return new Rational(newNum, newDen);
    }

    @Override
    public Numeric subtract(Numeric other) {
        if (!(other instanceof Rational otherRational)) {
            throw new IllegalArgumentException("Cannot subtract different types");
        }
        return this.add(otherRational.negate());
    }

    @Override
    public Numeric multiply(Numeric other) {
        if (!(other instanceof Rational otherRational)) {
            throw new IllegalArgumentException("Cannot multiply different types");
        }

        Rational c = new Rational(this.num, otherRational.den);
        Rational d = new Rational(otherRational.num, this.den);
        return new Rational(c.num.multiply(d.num), c.den.multiply(d.den));
    }

    @Override
    public Numeric divide(Numeric other) {
        if (!(other instanceof Rational otherRational)) {
            throw new IllegalArgumentException("Cannot divide different types");
        }

        if (otherRational.equals(zero)) {
            throw new ArithmeticException("division by zero");
        }

        return this.multiply(new Rational(otherRational.den, otherRational.num));
    }

    @Override
    public Numeric negate() {
        return new Rational(num.negate(), den);
    }

    @Override
    public Numeric inverse() {
        return new Rational(den, num);
    }

    @Override
    public Numeric one() {
        return one;
    }

    @Override
    public Numeric zero() {
        return zero;
    }

    // return { -1, 0, +1 } if this < other, this = other, or this > other
    public int compareTo(Rational other) {
        // Cross multiply to compare
        BigInteger lhs = this.num.multiply(other.den);
        BigInteger rhs = this.den.multiply(other.num);
        return lhs.compareTo(rhs);
    }

    @Override
    public String toString() {
        if (den.equals(BigInteger.ONE)) return num.toString();
        else return num + "/" + den;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rational other = (Rational) o;
        return compareTo(other) == 0;
    }

    @Override
    public int hashCode() {
        return num.hashCode() * 31 + den.hashCode();
    }
}