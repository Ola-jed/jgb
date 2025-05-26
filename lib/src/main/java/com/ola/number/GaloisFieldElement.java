package com.ola.number;

import com.ola.utils.ArithmeticUtils;

/**
 * Represents an element in a Galois Field (also known as a finite field).
 * <p>
 * This implementation supports prime fields GF(p), where p is a prime number.
 * Each element is represented by an integer modulo the prime value.
 */
public final class GaloisFieldElement implements Numeric {
    private final int primeModulus;

    private final int element;

    public GaloisFieldElement(int element, int primeModulus) {
        if (!ArithmeticUtils.isPrime(primeModulus)) {
            throw new IllegalArgumentException("The modulus for the Galois field must be a prime number.");
        }

        this.primeModulus = primeModulus;
        this.element = (element % primeModulus + primeModulus) % primeModulus;
    }

    public int get() {
        return element;
    }

    public int modulo() {
        return primeModulus;
    }

    @Override
    public Numeric add(Numeric other) {
        if (!(other instanceof GaloisFieldElement otherElement)) {
            throw new IllegalArgumentException("Cannot add different types");
        }

        checkSameModulus(otherElement);
        var result = (element + otherElement.element) % primeModulus;
        return new GaloisFieldElement(result, primeModulus);
    }

    @Override
    public Numeric subtract(Numeric other) {
        if (!(other instanceof GaloisFieldElement otherElement)) {
            throw new IllegalArgumentException("Cannot subtract different types");
        }

        checkSameModulus(otherElement);
        var result = (element - otherElement.element + primeModulus) % primeModulus;
        return new GaloisFieldElement(result, primeModulus);
    }

    @Override
    public Numeric multiply(Numeric other) {
        if (!(other instanceof GaloisFieldElement otherElement)) {
            throw new IllegalArgumentException("Cannot multiply different types");
        }

        checkSameModulus(otherElement);
        var result = (element * otherElement.element) % primeModulus;
        return new GaloisFieldElement(result, primeModulus);
    }

    @Override
    public Numeric divide(Numeric other) {
        if (!(other instanceof GaloisFieldElement otherElement)) {
            throw new IllegalArgumentException("Cannot divide different types");
        }

        checkSameModulus(otherElement);
        var inverse = modInverse(otherElement.element, primeModulus);
        var result = (element * inverse) % primeModulus;
        return new GaloisFieldElement(result, primeModulus);
    }

    @Override
    public Numeric negate() {
        var result = (primeModulus - this.element) % primeModulus;
        return new GaloisFieldElement(result, primeModulus);
    }

    @Override
    public Numeric inverse() {
        return new GaloisFieldElement(modInverse(element, primeModulus), primeModulus);
    }

    private void checkSameModulus(GaloisFieldElement other) {
        if (this.primeModulus != other.primeModulus) {
            throw new IllegalArgumentException("Elements must be from the same Galois Field");
        }
    }

    private int modInverse(int a, int m) {
        int m0 = m, t, q;
        int x0 = 0, x1 = 1;

        if (m == 1) {
            return 0;
        }

        while (a > 1) {
            q = a / m;
            t = m;

            m = a % m;
            a = t;

            t = x0;
            x0 = x1 - q * x0;
            x1 = t;
        }

        if (x1 < 0) {
            x1 += m0;
        }

        return x1;
    }


    @Override
    public Numeric one() {
        return new GaloisFieldElement(1, primeModulus);
    }

    @Override
    public Numeric zero() {
        return new GaloisFieldElement(0, primeModulus);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GaloisFieldElement that)) {
            return false;
        }

        return primeModulus == that.primeModulus && element == that.element;
    }

    @Override
    public int hashCode() {
        var result = primeModulus;
        result = 31 * result + element;
        return result;
    }

    @Override
    public String toString() {
        return Integer.toString(element);
    }
}
