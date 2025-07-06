package com.ola.utils;

/**
 * Utility class providing common arithmetic operations.
 * <p>
 * This class is final is intended to group
 * static utility methods related to arithmetic operations.
 * </p>
 */
public final class ArithmeticUtils {
    private ArithmeticUtils() {
    }

    /**
     * Computes the greatest common divisor (GCD) of two integers using
     * the binary GCD algorithm (Stein's algorithm).
     *
     * <p>This method handles zero inputs and negative values correctly.
     * The result is always non-negative unless an overflow occurs.</p>
     *
     * @param p the first integer
     * @param q the second integer
     * @return the greatest common divisor of {@code p} and {@code q}
     * @throws ArithmeticException if the GCD is 2^31, causing overflow
     */
    public static int gcd(int p, int q) {
        int a = p > 0 ? -p : p;
        int b = q > 0 ? -q : q;
        int negatedGcd;
        if (a == 0) {
            negatedGcd = b;
        } else if (b == 0) {
            negatedGcd = a;
        } else {
            int aTwos = Integer.numberOfTrailingZeros(a);
            int bTwos = Integer.numberOfTrailingZeros(b);
            a >>= aTwos;
            b >>= bTwos;

            int shift;
            for (shift = Math.min(aTwos, bTwos); a != b; a >>= Integer.numberOfTrailingZeros(a)) {
                int delta = a - b;
                b = Math.max(a, b);
                a = delta > 0 ? -delta : delta;
            }

            negatedGcd = a << shift;
        }

        if (negatedGcd == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow: gcd(%d, %d) is 2^31".formatted(p, q));
        } else {
            return -negatedGcd;
        }
    }

    /**
     * Computes the greatest common divisor (GCD) of two long integers using
     * the binary GCD algorithm (Stein's algorithm).
     *
     * <p>This method handles zero inputs and negative values correctly.
     * The result is always non-negative unless an overflow occurs.</p>
     *
     * @param p the first long integer
     * @param q the second long integer
     * @return the greatest common divisor of {@code p} and {@code q}
     * @throws ArithmeticException if the GCD is 2^63, causing overflow
     */
    public static long gcd(long p, long q) {
        long a = p > 0L ? -p : p;
        long b = q > 0L ? -q : q;
        long negatedGcd;
        if (a == 0L) {
            negatedGcd = b;
        } else if (b == 0L) {
            negatedGcd = a;
        } else {
            int aTwos = Long.numberOfTrailingZeros(a);
            int bTwos = Long.numberOfTrailingZeros(b);
            a >>= aTwos;
            b >>= bTwos;
            int shift = Math.min(aTwos, bTwos);

            while (true) {
                long delta = a - b;
                if (delta == 0L) {
                    negatedGcd = a << shift;
                    break;
                }

                b = Math.max(a, b);
                a = delta > 0L ? -delta : delta;
                a >>= Long.numberOfTrailingZeros(a);
            }
        }

        if (negatedGcd == Long.MIN_VALUE) {
            throw new ArithmeticException("overflow: gcd(%d, %d) is 2^63".formatted(p, q));
        } else {
            return -negatedGcd;
        }
    }

    /**
     * Determines if a given integer is a prime number.
     *
     * <p>This implementation uses a deterministic check optimized by:
     * <ul>
     *   <li>Quickly ruling out numbers less than 2 and small primes 2 and 3</li>
     *   <li>Eliminating even numbers and multiples of 3</li>
     *   <li>Testing divisors of the form 6k ± 1 up to {@code sqrt(n)}</li>
     * </ul>
     * </p>
     *
     * @param n the integer to test for primality
     * @return {@code true} if {@code n} is prime; {@code false} otherwise
     */
    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }

        if (n == 2 || n == 3) {
            return true;
        }

        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }

        for (var i = 5; i <= Math.sqrt(n); i = i + 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }

        return true;
    }
}
