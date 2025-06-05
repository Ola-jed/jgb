package com.ola.structures;

import com.ola.number.Numeric;
import com.ola.ordering.MonomialOrdering;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a polynomial over a polynomial ring {@code T}, consisting of a list of monomials.
 * <p>
 * The polynomial maintains a list of monomials sorted according to a specified monomial ordering.
 * It supports operations respecting the algebraic structure defined by the numeric type {@code T}.
 * </p>
 *
 * @param <T> the field of the coefficients
 */
@SuppressWarnings("unchecked")
public final class Polynomial<T extends Numeric> {
    private final List<Monomial<T>> monomials;
    private final int fieldSize;
    private final MonomialOrdering<T> ordering;
    private final int length;
    private final int degree;

    public Polynomial(List<Monomial<T>> monomials, int fieldSize, MonomialOrdering<T> ordering) {
        monomials.sort(ordering);
        // Merge monomials with equal exponents
        List<Monomial<T>> merged = new ArrayList<>();
        var maxDegree = 0;
        for (var monomial : monomials) {
            maxDegree = Math.max(maxDegree, monomial.degree());
            if (!merged.isEmpty() && monomial.exponentsEqual(merged.getLast())) {
                Monomial<T> last = merged.removeLast();
                T newCoefficient = (T) last.coefficient().add(monomial.coefficient());
                merged.add(last.withCoefficient(newCoefficient));
            } else {
                merged.add(monomial);
            }
        }

        // Remove zero coefficient monomials
        List<Monomial<T>> cleaned = new ArrayList<>();
        for (var monomial : merged) {
            if (!monomial.isZero()) {
                cleaned.add(monomial);
            }
        }

        this.monomials = cleaned;
        this.fieldSize = fieldSize;
        this.ordering = ordering;
        this.length = cleaned.size();
        this.degree = maxDegree;
    }

    public Polynomial(Monomial<T> monomial, MonomialOrdering<T> ordering) {
        this.monomials = List.of(monomial);
        this.fieldSize = monomial.fieldSize();
        this.ordering = ordering;
        this.length = 1;
        this.degree = monomial.degree();
    }

    public Polynomial(int fieldSize, MonomialOrdering<T> ordering) {
        this.monomials = List.of();
        this.fieldSize = fieldSize;
        this.ordering = ordering;
        this.length = 0;
        this.degree = 0;
    }

    // Constructor to avoid sorting when initializing after doing some operations because the monomials are already sorted
    private Polynomial(List<Monomial<T>> monomials, int fieldSize, MonomialOrdering<T> ordering, int length) {
        this.monomials = monomials;
        this.fieldSize = fieldSize;
        this.ordering = ordering;
        this.length = length;
        var maxDegree = 0;
        for (var monomial : monomials) {
            maxDegree = Math.max(maxDegree, monomial.degree());
        }

        this.degree = maxDegree;
    }

    @Override
    public Polynomial<T> clone() {
        try {
            // Since the class is immutable, shallow copies are enough
            return (Polynomial<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            // Should not happen but
            return new Polynomial<>(monomials, fieldSize, ordering, length);
        }
    }

    public List<Monomial<T>> monomials() {
        return monomials;
    }

    public int degree() {
        return degree;
    }

    public int fieldSize() {
        return fieldSize;
    }

    public MonomialOrdering<T> ordering() {
        return ordering;
    }

    public int length() {
        return length;
    }

    public T leadingCoefficient() {
        return monomials.get(length - 1).coefficient();
    }

    public Monomial<T> leadingTerm() {
        return monomials.get(length - 1);
    }

    public Monomial<T> leadingMonomial() {
        var leadTerm = monomials.get(length - 1);
        return leadTerm.withCoefficient((T) leadTerm.coefficient().one());
    }

    public int[] multidegree() {
        var leadTerm = monomials.get(length - 1);
        var exponents = new int[fieldSize];
        for (var i = 0; i < fieldSize; i++) {
            exponents[i] = leadTerm.getExponent(i);
        }

        return exponents;
    }

    public Polynomial<T> add(Polynomial<T> other) {
        if (fieldSize != other.fieldSize) {
            throw new IllegalArgumentException("Both polynomials should be defined in the same ring.");
        }

        if (ordering.orderId() != other.ordering.orderId()) {
            throw new IllegalArgumentException("Both polynomials should be defined using the same ordering.");
        }

        // Edge cases
        if (length == 0) {
            return other;
        }

        if (other.length == 0) {
            return this;
        }

        var resultingMonomials = new ArrayList<Monomial<T>>(Math.max(length, other.length));
        var ptr1 = 0;
        var ptr2 = 0;
        while (ptr1 < length && ptr2 < other.length) {
            var currentMonomial = monomials.get(ptr1);
            var otherMonomial = other.monomials.get(ptr2);
            var comparisonResult = ordering.compare(currentMonomial, otherMonomial);

            if (comparisonResult < 0) {
                resultingMonomials.add(currentMonomial);
                ptr1++;
            } else if (comparisonResult > 0) {
                resultingMonomials.add(otherMonomial);
                ptr2++;
            } else {
                // Sum the coefficients
                var sumCoefficient = currentMonomial.coefficient().add(otherMonomial.coefficient());

                // Add if not zero
                if (!sumCoefficient.equals(sumCoefficient.zero())) {
                    var newMonomial = currentMonomial.withCoefficient((T) sumCoefficient);
                    resultingMonomials.add(newMonomial);
                }

                ptr1++;
                ptr2++;
            }
        }

        if (ptr1 < length) {
            resultingMonomials.addAll(monomials.subList(ptr1, length));
        } else if (ptr2 < other.length) {
            resultingMonomials.addAll(other.monomials.subList(ptr2, other.length));
        }

        return new Polynomial<>(resultingMonomials, fieldSize, ordering, resultingMonomials.size());
    }

    public Polynomial<T> subtract(Polynomial<T> other) {
        if (fieldSize != other.fieldSize) {
            throw new IllegalArgumentException("Both polynomials should be defined in the same ring.");
        }

        if (ordering.orderId() != other.ordering.orderId()) {
            throw new IllegalArgumentException("Both polynomials should be defined using the same ordering.");
        }

        // Edge cases
        if (other.length == 0) {
            return this;
        }

        var resultingMonomials = new ArrayList<Monomial<T>>(Math.max(length, other.length));
        resultingMonomials.ensureCapacity(length);
        var ptr1 = 0;
        var ptr2 = 0;
        while (ptr1 < length && ptr2 < other.length) {
            var currentMonomial = monomials.get(ptr1);
            var otherMonomial = other.monomials.get(ptr2);
            var comparisonResult = ordering.compare(currentMonomial, otherMonomial);

            if (comparisonResult < 0) {
                resultingMonomials.add(currentMonomial);
                ptr1++;
            } else if (comparisonResult > 0) {
                var negatedCoefficient = (T) otherMonomial.coefficient().negate();
                resultingMonomials.add(otherMonomial.withCoefficient(negatedCoefficient));
                ptr2++;
            } else {
                // Subtract the coefficient
                var differenceCoefficient = currentMonomial.coefficient().subtract(otherMonomial.coefficient());

                // Add if not zero
                if (!differenceCoefficient.equals(differenceCoefficient.zero())) {
                    var newMonomial = currentMonomial.withCoefficient((T) differenceCoefficient);
                    resultingMonomials.add(newMonomial);
                }

                ptr1++;
                ptr2++;
            }
        }

        if (ptr1 < length) {
            resultingMonomials.addAll(monomials.subList(ptr1, length));
        } else if (ptr2 < other.length) {
            for (int i = ptr2; i < other.length; i++) {
                var otherMonomial = other.monomials.get(i);
                var negatedCoefficient = (T) otherMonomial.coefficient().negate();
                resultingMonomials.add(otherMonomial.withCoefficient(negatedCoefficient));
            }
        }

        return new Polynomial<>(resultingMonomials, fieldSize, ordering, resultingMonomials.size());
    }

    public Polynomial<T> multiply(T factor) {
        // Handle 0 or empty polynomial
        if (factor.equals(factor.zero()) || length == 0) {
            return new Polynomial<>(new ArrayList<>(), fieldSize, ordering, 0);
        }

        // Handle 1
        if (factor.equals(factor.one())) {
            return this;
        }

        var resultMonomials = new ArrayList<Monomial<T>>(length);
        for (int i = 0; i < length; i++) {
            var monomial = monomials.get(i);
            var newCoefficient = (T) monomial.coefficient().multiply(factor);
            if (!newCoefficient.equals(newCoefficient.zero())) {
                resultMonomials.add(monomial.withCoefficient(newCoefficient));
            }
        }

        return new Polynomial<>(resultMonomials, fieldSize, ordering, resultMonomials.size());
    }

    public Polynomial<T> multiply(Monomial<T> factor) {
        // Handle 0-coefficient for the monomial or empty polynomial
        if (factor.coefficient().equals(factor.coefficient().zero()) || length == 0) {
            return new Polynomial<>(new ArrayList<>(), fieldSize, ordering, 0);
        }

        var resultMonomials = new ArrayList<Monomial<T>>(length);
        for (int i = 0; i < length; i++) {
            var monomial = monomials.get(i);
            var resultingMonomial = monomial.multiply(factor);
            if (!resultingMonomial.coefficient().equals(resultingMonomial.coefficient().zero())) {
                resultMonomials.add(resultingMonomial);
            }
        }

        return new Polynomial<>(resultMonomials, fieldSize, ordering, resultMonomials.size());
    }

    public Polynomial<T> divide(T divisor) {
        // Check for division by zero
        if (divisor.equals(divisor.zero())) {
            throw new ArithmeticException("Cannot divide polynomial by zero");
        }

        // Handle 0 polynomial
        if (length == 0) {
            return new Polynomial<>(new ArrayList<>(), fieldSize, ordering, 0);
        }

        // Handle division by 1
        if (divisor.equals(divisor.one())) {
            return this;
        }

        var resultMonomials = new ArrayList<Monomial<T>>(length);
        for (int i = 0; i < length; i++) {
            var monomial = monomials.get(i);
            var newCoefficient = (T) monomial.coefficient().divide(divisor);
            if (!newCoefficient.equals(newCoefficient.zero())) {
                resultMonomials.add(monomial.withCoefficient(newCoefficient));
            }
        }

        return new Polynomial<>(resultMonomials, fieldSize, ordering, resultMonomials.size());
    }

    public Polynomial<T> reduce(List<Polynomial<T>> polynomials) {
        var polynomial = this;
        var remainder = new Polynomial<>(new ArrayList<>(), fieldSize, ordering, 0);
        while (!polynomial.monomials().isEmpty()) {
            var leadingTerm = polynomial.leadingTerm();
            var divided = false;
            for (var candidatePolynomial : polynomials) {
                var candidateLeadingTerm = candidatePolynomial.leadingTerm();
                var divisionResult = leadingTerm.divide(candidateLeadingTerm);

                if (!divisionResult.coefficient().equals(divisionResult.coefficient().zero())) {
                    polynomial = polynomial.subtract(candidatePolynomial.multiply(divisionResult));
                    divided = true;
                    break;
                }
            }

            if (!divided) {
                var leadingTermPolynomial = new Polynomial<>(leadingTerm, ordering);
                remainder = remainder.add(leadingTermPolynomial);
                polynomial = polynomial.tail();
            }
        }

        return remainder;
    }

    public Polynomial<T> tail() {
        var monomialsToKeep = new ArrayList<>(monomials.subList(0, monomials.size() - 1));
        return new Polynomial<>(monomialsToKeep, fieldSize, ordering, length - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Polynomial<?> other)) {
            return false;
        }

        if (fieldSize != other.fieldSize) {
            return false;
        }

        if (ordering.orderId() != other.ordering().orderId()) {
            return false;
        }

        return Objects.equals(monomials, other.monomials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monomials, fieldSize, ordering);
    }

    public boolean isOne() {
        return monomials.size() == 1 && monomials.getFirst().isOne();
    }

    public boolean isZero() {
        for (var monomial : monomials) {
            if (!monomial.isZero()) {
                return false;
            }
        }

        return true;
    }
}
