package com.ola.utils;

public final class ArithmeticUtils {
    private ArithmeticUtils() {
    }

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

    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }

        if (n == 2 || n == 3) {
            return true;
        }

        if (n % 2 == 0 || n % 3 == 0) {
            for (int i = 5; i <= Math.sqrt(n); i = i + 6) {
                if (n % i == 0 || n % (i + 2) == 0) {
                    return false;
                }
            }
        }

        return true;
    }
}
