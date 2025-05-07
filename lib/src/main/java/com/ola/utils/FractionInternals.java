package com.ola.utils;


public final class FractionInternals {
    public static final FractionInternals ZERO = new FractionInternals(0);
    public static final FractionInternals ONE = new FractionInternals(1);
    private final int numerator;
    private final int denominator;

    private FractionInternals(int num, int den) {
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

    private FractionInternals(int num) {
        this.numerator = num;
        this.denominator = 1;
    }

    private FractionInternals(double value, double epsilon, int maxDenominator, int maxIterations) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Not finite: " + value);
        } else {
            double absValue = Math.abs(value);
            double r0 = absValue;
            long a0 = (long)Math.floor(absValue);
            if (a0 > 2147483648L) {
                throw new ArithmeticException("Overflow trying to convert %s to fraction (%d/%d)".formatted(value, a0, 1));
            } else if (absValue - (double)a0 <= epsilon) {
                int num = (int)a0;
                int den = 1;
                if ((double)Math.signum((float)num) != Math.signum(value)) {
                    if (num == Integer.MIN_VALUE) {
                        den = -den;
                    } else {
                        num = -num;
                    }
                }

                this.numerator = num;
                this.denominator = den;
            } else {
                long maxDen = Math.abs((long)maxDenominator);
                long p0 = 1L;
                long q0 = 0L;
                long p1 = a0;
                long q1 = 1L;
                int n = 0;
                boolean stop = false;

                long p2;
                long q2;
                do {
                    ++n;
                    double r1 = (double)1.0F / (r0 - (double)a0);
                    long a1 = (long)Math.floor(r1);
                    p2 = a1 * p1 + p0;
                    q2 = a1 * q1 + q0;
                    if (Long.compareUnsigned(p2, 2147483648L) > 0 || Long.compareUnsigned(q2, 2147483648L) > 0) {
                        if (epsilon != (double)0.0F) {
                            throw new ArithmeticException("Overflow trying to convert %s to fraction (%d/%d)".formatted(value, p2, q2));
                        }

                        p2 = p1;
                        q2 = q1;
                        break;
                    }

                    double convergent = (double)p2 / (double)q2;
                    if (n < maxIterations && Math.abs(convergent - absValue) > epsilon && q2 < maxDen) {
                        p0 = p1;
                        p1 = p2;
                        q0 = q1;
                        q1 = q2;
                        a0 = a1;
                        r0 = r1;
                    } else {
                        stop = true;
                    }
                } while(!stop);

                if (n >= maxIterations) {
                    throw new ArithmeticException("Unable to convert %s to fraction after %d iterations".formatted(value, maxIterations));
                } else {
                    int den;
                    int num;
                    if (q2 <= maxDen) {
                        num = (int)p2;
                        den = (int)q2;
                    } else {
                        num = (int)p1;
                        den = (int)q1;
                    }

                    if ((double)(Math.signum((float)num) * Math.signum((float)den)) != Math.signum(value)) {
                        if (num == Integer.MIN_VALUE) {
                            den = -den;
                        } else {
                            num = -num;
                        }
                    }

                    this.numerator = num;
                    this.denominator = den;
                }
            }
        }
    }

    public static FractionInternals of(int num) {
        return num == 0 ? ZERO : new FractionInternals(num);
    }

    public static FractionInternals of(int num, int den) {
        return num == 0 ? ZERO : new FractionInternals(num, den);
    }

    public FractionInternals zero() {
        return ZERO;
    }

    public boolean isZero() {
        return this.numerator == 0;
    }

    public FractionInternals one() {
        return ONE;
    }

    public boolean isOne() {
        return this.numerator == this.denominator;
    }

    public int signum() {
        return Integer.signum(this.numerator) * Integer.signum(this.denominator);
    }

    public FractionInternals negate() {
        return this.numerator == Integer.MIN_VALUE ? new FractionInternals(this.numerator, -this.denominator) : new FractionInternals(-this.numerator, this.denominator);
    }

    public FractionInternals add(int value) {
        if (value == 0) {
            return this;
        } else if (this.isZero()) {
            return new FractionInternals(value);
        } else {
            long num = (long)value * (long)this.denominator;
            return of(Math.toIntExact((long)this.numerator + num), this.denominator);
        }
    }

    public FractionInternals add(FractionInternals value) {
        return this.addSub(value, true);
    }

    public FractionInternals subtract(int value) {
        if (value == 0) {
            return this;
        } else if (this.isZero()) {
            return value == Integer.MIN_VALUE ? new FractionInternals(Integer.MIN_VALUE, -1) : new FractionInternals(-value);
        } else {
            long num = (long)value * (long)this.denominator;
            return of(Math.toIntExact((long)this.numerator - num), this.denominator);
        }
    }

    public FractionInternals subtract(FractionInternals value) {
        return this.addSub(value, false);
    }

    private FractionInternals addSub(FractionInternals value, boolean isAdd) {
        if (value.isZero()) {
            return this;
        } else if (this.isZero()) {
            return isAdd ? value : value.negate();
        } else {
            int d1 = ArithmeticUtils.gcd(this.denominator, value.denominator);
            long uvp = (long)this.numerator * (long)(value.denominator / d1);
            long upv = (long)value.numerator * (long)(this.denominator / d1);
            long t = isAdd ? uvp + upv : uvp - upv;
            long d2 = ArithmeticUtils.gcd(t, (long)d1);
            return of(Math.toIntExact(t / d2), Math.multiplyExact(this.denominator / d1, value.denominator / (int)d2));
        }
    }

    public FractionInternals multiply(int value) {
        if (value != 0 && !this.isZero()) {
            int d2 = ArithmeticUtils.gcd(value, this.denominator);
            return new FractionInternals(Math.multiplyExact(this.numerator, value / d2), this.denominator / d2);
        } else {
            return ZERO;
        }
    }

    public FractionInternals multiply(FractionInternals value) {
        return !value.isZero() && !this.isZero() ? this.multiply(value.numerator, value.denominator) : ZERO;
    }

    private FractionInternals multiply(int num, int den) {
        int d1 = ArithmeticUtils.gcd(this.numerator, den);
        int d2 = ArithmeticUtils.gcd(num, this.denominator);
        return new FractionInternals(Math.multiplyExact(this.numerator / d1, num / d2), Math.multiplyExact(this.denominator / d2, den / d1));
    }

    public FractionInternals divide(int value) {
        if (value == 0) {
            throw new ArithmeticException("The value to divide by must not be zero");
        } else if (this.isZero()) {
            return ZERO;
        } else {
            int d1 = ArithmeticUtils.gcd(this.numerator, value);
            return new FractionInternals(this.numerator / d1, Math.multiplyExact(this.denominator, value / d1));
        }
    }

    public FractionInternals divide(FractionInternals value) {
        if (value.isZero()) {
            throw new ArithmeticException("The value to divide by must not be zero");
        } else {
            return this.isZero() ? ZERO : this.multiply(value.denominator, value.numerator);
        }
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

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            if (other instanceof FractionInternals rhs) {
                if (this.signum() == rhs.signum()) {
                    return Math.abs(this.numerator) == Math.abs(rhs.numerator) && Math.abs(this.denominator) == Math.abs(rhs.denominator);
                }
            }

            return false;
        }
    }

    public int hashCode() {
        int numS = Integer.signum(this.numerator);
        int denS = Integer.signum(this.denominator);
        return (31 * (31 + this.numerator * numS) + this.denominator * denS) * numS * denS;
    }
}
