package io.github.olajed.jgb.structures;

import io.github.olajed.jgb.number.Numeric;

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
    /**
     * Returns the total degree of the monomial, defined as the sum of all exponents.
     *
     * @return the total degree
     */
    public abstract int degree();

    /**
     * Returns the coefficient of the monomial.
     *
     * @return the coefficient
     */
    public abstract T coefficient();

    /**
     * Returns the number of indeterminates (variables) in the polynomial ring.
     *
     * @return the number of variables
     */
    public abstract int fieldSize();

    /**
     * Returns the exponent of the variable at the specified index.
     *
     * @param indeterminateIndex the index of the variable
     * @return the corresponding exponent
     */
    public abstract int getExponent(int indeterminateIndex);

    /**
     * Accumulates a result by applying a transformation and aggregation over all exponents.
     *
     * @param <U>          the type of external data associated with each variable
     * @param <V>          the result type of accumulation
     * @param elements     an array of external data, one per variable
     * @param combination  a function combining an exponent and its corresponding data
     * @param initialValue the starting value for accumulation
     * @param aggregation  a function combining successive results
     * @return the final accumulated value
     */
    public abstract <U, V> V accumulate(U[] elements,
                                        BiFunction<Integer, U, V> combination,
                                        V initialValue,
                                        BiFunction<V, V, V> aggregation);

    /**
     * Accumulates a result by applying a transformation and aggregation over all exponents.
     *
     * @param elements     an array of integer values, one per variable
     * @param combination  a function combining an exponent and its corresponding value
     * @param initialValue the starting value for accumulation
     * @param aggregation  a function combining successive results
     * @return the final accumulated value
     */
    public abstract int accumulate(int[] elements,
                                   IntBinaryOperator combination,
                                   int initialValue,
                                   IntBinaryOperator aggregation);

    /**
     * Returns the product of this monomial and another.
     * Coefficients are multiplied and exponents are added componentwise.
     *
     * @param other the monomial to multiply
     * @return the resulting product monomial
     */
    public abstract Monomial<T> multiply(Monomial<T> other);

    /**
     * Returns a new monomial with its coefficient multiplied by a scalar,
     * and the exponents unchanged.
     *
     * @param factor the scalar multiplier
     * @return the resulting monomial
     */
    public abstract Monomial<T> multiply(T factor);

    /**
     * Returns the result of dividing this monomial by another.
     * Assumes exact division (i.e., no negative exponents).
     *
     * @param other the divisor monomial
     * @return the resulting quotient monomial
     */
    public abstract Monomial<T> divide(Monomial<T> other);

    /**
     * Returns a copy of this monomial with the coefficient replaced.
     *
     * @param newCoefficient the new coefficient
     * @return the updated monomial
     */
    public abstract Monomial<T> withCoefficient(T newCoefficient);

    /**
     * Returns all monomials that divide this monomial, including the identity and itself.
     *
     * @return an iterable over the divisors
     */
    public abstract Iterable<Monomial<T>> divisors();

    /**
     * Returns {@code true} if this monomial shares no variables with non-zero exponents
     * with another monomial.
     *
     * @param other the monomial to compare with
     * @return {@code true} if disjoint, {@code false} otherwise
     */
    public abstract boolean disjointWith(Monomial<T> other);

    /**
     * Returns {@code true} if this monomial has the same exponent vector as another.
     *
     * @param other the monomial to compare with
     * @return {@code true} if exponents are equal, {@code false} otherwise
     */
    public abstract boolean exponentsEqual(Monomial<T> other);

    /**
     * Returns {@code true} if this monomial is a power of another monomial.
     * That is, if there exists an integer k such that each exponent in this monomial
     * is equal to k times the corresponding exponent in the other.
     *
     * @param other the base monomial
     * @return {@code true} if this is a power of {@code other}, {@code false} otherwise
     */
    public abstract boolean isPowerOf(Monomial<T> other);

    /**
     * Returns {@code true} if the coefficient is zero.
     *
     * @return {@code true} if this is the zero monomial
     */
    public abstract boolean isZero();

    /**
     * Returns {@code true} if the coefficient is one and all exponents are zero.
     *
     * @return {@code true} if this is the multiplicative identity
     */
    public abstract boolean isOne();
}
