package com.ola.number;

/**
 * Immutable representation of a complex number with real and imaginary parts.
 *
 * <p>Implements the {@link Numeric} interface for numeric operations.</p>
 *
 * <p>Provides common constants: {@code I} (imaginary unit), {@code one}, and {@code zero}.</p>
 */
public final class Complex implements Numeric {
    public static final Complex I = new Complex(0.0F, 1.0F);
    public static final Complex one = new Complex(1.0F, 0.0F);
    public static final Complex zero = new Complex(0.0F, 0.0F);
    private final double imaginary;
    private final double real;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Numeric one() {
        return one;
    }

    public Numeric zero() {
        return zero;
    }

    public double real() {
        return this.real;
    }

    public double imaginary() {
        return this.imaginary;
    }

    public Complex negate() {
        return new Complex(-this.real, -this.imaginary);
    }

    @Override
    public Numeric add(Numeric other) {
        if (!(other instanceof Complex addend)) {
            throw new IllegalArgumentException("Cannot add different types");
        }

        return new Complex(this.real + addend.real, this.imaginary + addend.imaginary);
    }

    public Complex add(double addend) {
        return new Complex(this.real + addend, this.imaginary);
    }

    @Override
    public Numeric subtract(Numeric other) {
        if (!(other instanceof Complex subtrahend)) {
            throw new IllegalArgumentException("Cannot subtract different types");
        }

        return new Complex(this.real - subtrahend.real, this.imaginary - subtrahend.imaginary);
    }

    public Complex subtract(double subtrahend) {
        return new Complex(this.real - subtrahend, this.imaginary);
    }

    @Override
    public Numeric multiply(Numeric other) {
        if (!(other instanceof Complex factor)) {
            throw new IllegalArgumentException("Cannot multiply different types");
        }

        return multiply(this.real, this.imaginary, factor.real, factor.imaginary);
    }

    private static Complex multiply(double re1, double im1, double re2, double im2) {
        double a = re1;
        double b = im1;
        double c = re2;
        double d = im2;
        double ac = re1 * re2;
        double bd = im1 * im2;
        double ad = re1 * im2;
        double bc = im1 * re2;
        double x = ac - bd;
        double y = ad + bc;
        if (Double.isNaN(x) && Double.isNaN(y)) {
            boolean recalc = false;
            if ((Double.isInfinite(re1) || Double.isInfinite(im1)) && isNotZero(re2, im2)) {
                a = boxInfinity(re1);
                b = boxInfinity(im1);
                c = changeNaNtoZero(re2);
                d = changeNaNtoZero(im2);
                recalc = true;
            }

            if ((Double.isInfinite(c) || Double.isInfinite(d)) && isNotZero(a, b)) {
                c = boxInfinity(c);
                d = boxInfinity(d);
                a = changeNaNtoZero(a);
                b = changeNaNtoZero(b);
                recalc = true;
            }

            if (!recalc && (Double.isInfinite(ac) || Double.isInfinite(bd) || Double.isInfinite(ad) || Double.isInfinite(bc))) {
                a = changeNaNtoZero(a);
                b = changeNaNtoZero(b);
                c = changeNaNtoZero(c);
                d = changeNaNtoZero(d);
                recalc = true;
            }

            if (recalc) {
                x = Double.POSITIVE_INFINITY * (a * c - b * d);
                y = Double.POSITIVE_INFINITY * (a * d + b * c);
            }
        }

        return new Complex(x, y);
    }

    private static double boxInfinity(double component) {
        return Math.copySign(Double.isInfinite(component) ? (double) 1.0F : (double) 0.0F, component);
    }

    private static boolean isNotZero(double real, double imaginary) {
        return real != (double) 0.0F || imaginary != (double) 0.0F;
    }

    private static double changeNaNtoZero(double value) {
        return Double.isNaN(value) ? Math.copySign(0.0F, value) : value;
    }

    public Complex multiply(double factor) {
        return new Complex(this.real * factor, this.imaginary * factor);
    }

    @Override
    public Numeric divide(Numeric other) {
        if (!(other instanceof Complex divisor)) {
            throw new IllegalArgumentException("Cannot multiply different types");
        }

        return divide(this.real, this.imaginary, divisor.real, divisor.imaginary);
    }

    private static Complex divide(double re1, double im1, double re2, double im2) {
        double a = re1;
        double b = im1;
        double c = re2;
        double d = im2;
        int ilogbw = 0;
        int exponent = getScale(re2, im2);
        if (exponent <= 1023) {
            ilogbw = exponent;
            c = Math.scalb(re2, -exponent);
            d = Math.scalb(im2, -exponent);
        }

        double denom = c * c + d * d;
        if (getMaxExponent(re1, im1) > 1021) {
            ilogbw -= 2;
            a = re1 / (double) 4.0F;
            b = im1 / (double) 4.0F;
        }

        double x = Math.scalb((a * c + b * d) / denom, -ilogbw);
        double y = Math.scalb((b * c - a * d) / denom, -ilogbw);
        if (Double.isNaN(x) && Double.isNaN(y)) {
            if (denom != (double) 0.0F || Double.isNaN(a) && Double.isNaN(b)) {
                if ((Double.isInfinite(a) || Double.isInfinite(b)) && Double.isFinite(c) && Double.isFinite(d)) {
                    a = boxInfinity(a);
                    b = boxInfinity(b);
                    x = Double.POSITIVE_INFINITY * (a * c + b * d);
                    y = Double.POSITIVE_INFINITY * (b * c - a * d);
                } else if ((Double.isInfinite(c) || Double.isInfinite(d)) && Double.isFinite(a) && Double.isFinite(b)) {
                    c = boxInfinity(c);
                    d = boxInfinity(d);
                    x = (double) 0.0F * (a * c + b * d);
                    y = (double) 0.0F * (b * c - a * d);
                }
            } else {
                x = Math.copySign(Double.POSITIVE_INFINITY, c) * a;
                y = Math.copySign(Double.POSITIVE_INFINITY, c) * b;
            }
        }

        return new Complex(x, y);
    }

    public Complex divide(double divisor) {
        return new Complex(this.real / divisor, this.imaginary / divisor);
    }

    @Override
    public Numeric inverse() {
        return one.divide(this);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof Complex c)) {
            return false;
        } else {
            return equals(this.real, c.real) && equals(this.imaginary, c.imaginary);
        }
    }

    public int hashCode() {
        return 31 * (31 + Double.hashCode(this.real)) + Double.hashCode(this.imaginary);
    }

    public String toString() {
        return "(" + this.real + ',' + this.imaginary + ')';
    }

    private static boolean equals(double x, double y) {
        return Double.doubleToLongBits(x) == Double.doubleToLongBits(y);
    }

    private static int getScale(double a, double b) {
        var x = Double.doubleToRawLongBits(a) & Long.MAX_VALUE;
        var y = Double.doubleToRawLongBits(b) & Long.MAX_VALUE;
        var bits = Math.max(x, y);
        var exp = (int) (bits >>> 52) - 1023;
        if (exp == -1023) {
            if (bits == 0L) {
                return 1024;
            }

            var mantissa = bits & 4503599627370495L;
            exp -= Long.numberOfLeadingZeros(mantissa << 12);
        }

        return exp;
    }

    private static int getMaxExponent(double a, double b) {
        return Math.max(Math.getExponent(a), Math.getExponent(b));
    }
}