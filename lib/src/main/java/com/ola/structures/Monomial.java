package com.ola.structures;

import com.ola.number.Numeric;

import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;

/**
 * Represents a mathematical monomial in a polynomial ring.
 * A monomial consists of a coefficient and a set of indeterminates
 * raised to specific powers.
 *
 * @param <T> The type of the coefficient, which must extend {@link Numeric}.
 */
public abstract sealed class Monomial<T extends Numeric> permits DenseMonomial, SparseMonomial {
    public abstract int degree();

    public abstract T coefficient();

    public abstract int fieldSize();

    public abstract int getExponent(int indeterminateIndex);

    public abstract <U, V> V accumulate(U[] elements, BiFunction<Integer, U, V> combination, V initialValue, BiFunction<V, V, V> aggregation);

    public abstract int accumulate(int[] elements, IntBinaryOperator combination, int initialValue, IntBinaryOperator aggregation);

    public abstract Monomial<T> multiply(Monomial<T> other);

    public abstract Monomial<T> multiply(T factor);

    public abstract Monomial<T> divide(Monomial<T> other);

    public abstract Monomial<T> withCoefficient(T newCoefficient);

    public abstract Iterable<Monomial<T>> divisors();

    public abstract boolean disjointWith(Monomial<T> other);
}