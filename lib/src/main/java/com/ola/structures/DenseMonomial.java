package com.ola.structures;

import com.ola.number.Numeric;

import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;

/**
 * Represents a dense implementation of a mathematical monomial in a polynomial ring.
 * Stores the exponents of all indeterminates as an array,
 * including zero exponents for indeterminates not present in the monomial.
 *
 * @param <T> The type of the coefficient, which must extend {@link Numeric}.
 */
@SuppressWarnings("unchecked")
public final class DenseMonomial<T extends Numeric> extends Monomial<T> {
    private final T coefficient;
    private final int[] exponents;
    private final int fieldSize;
    private int degree;

    public DenseMonomial(int[] exponents, T coefficient) {
        this.exponents = exponents;
        this.coefficient = coefficient;
        this.fieldSize = exponents.length;
        this.degree = 0;

        for (int exponent : exponents) {
            degree += exponent;
        }
    }

    @Override
    public int degree() {
        return degree;
    }

    @Override
    public T coefficient() {
        return coefficient;
    }

    @Override
    public int fieldSize() {
        return fieldSize;
    }

    @Override
    public int getExponent(int indeterminateIndex) {
        return exponents[indeterminateIndex];
    }

    @Override
    public <U, V> V accumulate(U[] elements, BiFunction<Integer, U, V> combination, V initialValue, BiFunction<V, V, V> aggregation) {
        var result = initialValue;
        for (var i = 0; i < fieldSize; i++) {
            result = aggregation.apply(result, combination.apply(exponents[i], elements[i]));
        }

        return result;
    }

    @Override
    public int accumulate(int[] elements, IntBinaryOperator combination, int initialValue, IntBinaryOperator aggregation) {
        var result = initialValue;
        for (var i = 0; i < fieldSize; i++) {
            result = aggregation.applyAsInt(result, combination.applyAsInt(exponents[i], elements[i]));
        }

        return result;
    }

    @Override
    public Monomial<T> multiply(Monomial<T> other) {
        if (!(other instanceof DenseMonomial<T> dense)) {
            throw new IllegalArgumentException("Expected a DenseMonomial instance.");
        }

        var resultingExponents = new int[fieldSize];
        var resultCoefficient = (T) coefficient.multiply(dense.coefficient);
        for (int i = 0; i < fieldSize; i++) {
            resultingExponents[i] = exponents[i] + dense.exponents[i];
        }

        return new DenseMonomial<>(resultingExponents, resultCoefficient);
    }

    @Override
    public Monomial<T> multiply(T factor) {
        return new DenseMonomial<>(this.exponents, (T) this.coefficient.multiply(factor));
    }

    @Override
    public Monomial<T> divide(Monomial<T> other) {
        if (!(other instanceof DenseMonomial<T> dense)) {
            throw new IllegalArgumentException("Expected a DenseMonomial instance.");
        }

        var resultingExponents = new int[fieldSize];
        for (int i = 0; i < fieldSize; i++) {
            resultingExponents[i] = exponents[i] - dense.exponents[i];

            if (resultingExponents[i] < 0) {
                // Division is not possible, return the zero monomial
                return new DenseMonomial<>(new int[fieldSize], (T) coefficient.zero());
            }
        }

        return new DenseMonomial<>(resultingExponents, (T) coefficient.divide(dense.coefficient));
    }

    @Override
    public Monomial<T> withCoefficient(T newCoefficient) {
        return new DenseMonomial<>(exponents, newCoefficient);
    }

    @Override
    public Iterable<Monomial<T>> divisors() {
        return () -> new MonomialDivisionIterator<>(this);
    }

    @Override
    public boolean disjointWith(Monomial<T> other) {
        if (!(other instanceof DenseMonomial<T> dense)) {
            throw new IllegalArgumentException("Expected a DenseMonomial instance.");
        }

        for (var i = 0; i < fieldSize; i++) {
            if (exponents[i] != 0 && dense.exponents[i] != 0) {
                return false;
            }
        }

        return degree != 0;
    }

    @Override
    public boolean exponentsEqual(Monomial<T> other) {
        if (!(other instanceof DenseMonomial<T> dense)) {
            throw new IllegalArgumentException("Expected a DenseMonomial instance.");
        }

        for (int i = 0; i < fieldSize; i++) {
            if (exponents[i] != dense.exponents[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        var other = (DenseMonomial<?>) obj;
        if (fieldSize != other.fieldSize) {
            return false;
        }

        if (!coefficient.equals(other.coefficient)) {
            return false;
        }

        for (int i = 0; i < fieldSize; i++) {
            if (exponents[i] != other.exponents[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        var result = coefficient.hashCode();
        for (int i = 0; i < fieldSize; i++) {
            result = 31 * result + exponents[i];
        }

        return result;
    }
}
