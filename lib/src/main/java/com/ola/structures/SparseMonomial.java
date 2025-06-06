package com.ola.structures;

import com.ola.number.Numeric;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;

/**
 * Represents a sparse implementation of a mathematical monomial in a polynomial ring.
 * Stores only the non-zero exponents of the indeterminates,
 * using a bitset.
 *
 * @param <T> The type of the coefficient, which must extend {@link Numeric}.
 */
@SuppressWarnings("unchecked")
public final class SparseMonomial<T extends Numeric> extends Monomial<T> {
    private final T coefficient;
    private final BitSet bitset;
    private final int[] exponents;
    private final int fieldSize;
    private int degree;

    public SparseMonomial(int[] exponents, T coefficient) {
        this.coefficient = coefficient;
        this.bitset = new BitSet(exponents.length);
        this.fieldSize = exponents.length;
        var nonZero = 0;
        degree = 0;
        for (var i = 0; i < fieldSize; i++) {
            if (exponents[i] != 0) {
                nonZero++;
                bitset.set(i);
                degree += exponents[i];
            }
        }

        this.exponents = new int[nonZero];
        var ptr = 0;
        for (var i = 0; i < fieldSize; i++) {
            if (exponents[i] != 0) {
                this.exponents[ptr] = exponents[i];
                ptr++;
            }
        }
    }

    private SparseMonomial(int[] exponents, T coefficient, BitSet bitset, int fieldSize, int degree) {
        this.coefficient = coefficient;
        this.bitset = bitset;
        this.exponents = exponents;
        this.fieldSize = fieldSize;
        this.degree = degree;
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

    public BitSet bitset() {
        return bitset;
    }

    @Override
    public int getExponent(int indeterminateIndex) {
        var correspondingBit = bitset.get(indeterminateIndex);
        if (!correspondingBit) {
            return 0;
        }

        var ptr = 0;
        for (var i = 0; i <= indeterminateIndex; i++) {
            if (bitset.get(i)) {
                ptr++;
            }
        }

        return exponents[ptr - 1];
    }

    public int getExponentAtPosition(int position) {
        // Raw version of getExponent
        return exponents[position];
    }

    @Override
    public <U, V> V accumulate(U[] elements, BiFunction<Integer, U, V> combination, V initialValue, BiFunction<V, V, V> aggregation) {
        var result = initialValue;
        var ptr = 0;
        for (var i = 0; i < fieldSize; i++) {
            if (bitset.get(i)) {
                result = aggregation.apply(result, combination.apply(exponents[ptr], elements[i]));
                ptr++;
            } else {
                result = aggregation.apply(result, combination.apply(0, elements[i]));
            }
        }

        return result;
    }

    @Override
    public int accumulate(int[] elements, IntBinaryOperator combination, int initialValue, IntBinaryOperator aggregation) {
        var result = initialValue;
        var ptr = 0;
        for (var i = 0; i < fieldSize; i++) {
            if (bitset.get(i)) {
                result = aggregation.applyAsInt(result, combination.applyAsInt(exponents[ptr], elements[i]));
                ptr++;
            } else {
                result = aggregation.applyAsInt(result, combination.applyAsInt(0, elements[i]));
            }
        }

        return result;
    }

    @Override
    public Monomial<T> multiply(Monomial<T> other) {
        if (!(other instanceof SparseMonomial<T> sparse)) {
            throw new IllegalArgumentException("Expected a SparseMonomial instance.");
        }

        var ptr1 = 0;
        var ptr2 = 0;
        var resultingExponents = new int[fieldSize];
        for (var i = 0; i < fieldSize; i++) {
            var currentExponent = 0;
            if (bitset.get(i)) {
                currentExponent += exponents[ptr1];
                ptr1++;
            }

            if (sparse.bitset.get(i)) {
                currentExponent += sparse.exponents[ptr2];
                ptr2++;
            }

            resultingExponents[i] = currentExponent;
        }

        return new SparseMonomial<>(resultingExponents, (T) coefficient.multiply(sparse.coefficient));
    }

    @Override
    public Monomial<T> multiply(T factor) {
        return new SparseMonomial<>(exponents, (T) this.coefficient.multiply(factor), bitset, fieldSize, degree);
    }

    @Override
    public Monomial<T> divide(Monomial<T> other) {
        if (!(other instanceof SparseMonomial<T> sparse)) {
            throw new IllegalArgumentException("Expected a SparseMonomial instance.");
        }

        var ptr1 = 0;
        var ptr2 = 0;
        var resultingExponents = new int[fieldSize];
        for (var i = 0; i < fieldSize; i++) {
            var currentResult = 0;
            if (bitset.get(i)) {
                currentResult += exponents[ptr1];
                ptr1++;
            }

            if (sparse.bitset.get(i)) {
                currentResult -= sparse.exponents[ptr2];
                ptr2++;
            }

            if (currentResult < 0) {
                // Division is not possible, return the zero monomial
                return new SparseMonomial<>(new int[fieldSize], (T) coefficient.zero());
            }

            resultingExponents[i] = currentResult;
        }

        return new SparseMonomial<>(resultingExponents, (T) coefficient.divide(sparse.coefficient));
    }

    @Override
    public Monomial<T> withCoefficient(T newCoefficient) {
        return new SparseMonomial<>(exponents, newCoefficient, bitset, fieldSize, degree);
    }

    @Override
    public Iterable<Monomial<T>> divisors() {
        return () -> new MonomialDivisionIterator<>(this);
    }

    @Override
    public boolean disjointWith(Monomial<T> other) {
        if (!(other instanceof SparseMonomial<T> sparse)) {
            throw new IllegalArgumentException("Expected a SparseMonomial instance.");
        }

        for (var i = 0; i < fieldSize; i++) {
            if (bitset.get(i) && sparse.bitset.get(i)) {
                return false;
            }
        }

        return degree != 0;
    }

    @Override
    public boolean exponentsEqual(Monomial<T> other) {
        if (!(other instanceof SparseMonomial<T> sparse)) {
            throw new IllegalArgumentException("Expected a SparseMonomial instance.");
        }

        if (!bitset.equals(sparse.bitset)) {
            return false;
        }

        var ptr = 0;
        for (var i = 0; i < fieldSize; i++) {
            if (bitset.get(i)) {
                if (exponents[ptr] != sparse.exponents[ptr]) {
                    return false;
                }

                ptr++;
            }
        }

        return true;
    }

    @Override
    public boolean isZero() {
        return coefficient.equals(coefficient.zero());
    }

    @Override
    public boolean isOne() {
        return degree == 0 && coefficient.equals(coefficient.one());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        var other = (SparseMonomial<?>) obj;

        if (fieldSize != other.fieldSize) {
            return false;
        }

        if (!coefficient.equals(other.coefficient)) {
            return false;
        }

        if (!bitset.equals(other.bitset)) {
            return false;
        }

        var ptr = 0;
        for (var i = 0; i < fieldSize; i++) {
            if (bitset.get(i)) {
                if (exponents[ptr] != other.exponents[ptr]) {
                    return false;
                }

                ptr++;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coefficient, bitset, Arrays.hashCode(exponents));
    }
}
