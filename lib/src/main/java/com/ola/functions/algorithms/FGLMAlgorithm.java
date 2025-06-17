package com.ola.functions.algorithms;

import com.ola.enums.MonomialType;
import com.ola.number.Numeric;
import com.ola.number.Real;
import com.ola.ordering.GrevlexOrdering;
import com.ola.ordering.LexOrdering;
import com.ola.structures.*;
import com.ola.utils.Pair;

import java.util.*;

// For conversion to lex basis only
@SuppressWarnings("unchecked")
public final class FGLMAlgorithm {
    public static <T extends Numeric> List<Polynomial<T>> compute(List<Polynomial<T>> basis) {
        /*
         * Given G, a reduced GB
         * We return G'
         * We initialize a B = emptyset()
         * We initialize G' = G
         * X = 1
         * Compute r(X)
         * if r(X) = 0
         *      add X to B
         *      increment power of X for the current indeterminate
         * else if we have a linear combination st r(X) := sum(elements of B) = A
         *      // we can say that we reduce X by G then by B
         *      then g = X - A
         *      add g to G'
         *      if LT(g) = power_of(x) where x is the maximal variable according to the new ordering
         *          exit
         *      else
         *          replace X by the next monomial not divisible by any of the LT of polynomials in G
         * return G'
         */

        var ring = new PolynomialRing(Real.class, new String[]{"x", "y", "z"});
        var initialOrdering = new GrevlexOrdering<T>();
        var ordering = new LexOrdering<T>();
        var firstPoly = basis.getFirst();
        var fieldSize = firstPoly.fieldSize();
        var one = (T) firstPoly.leadingCoefficient().one();
        var type = firstPoly.leadingTerm() instanceof DenseMonomial<T> ? MonomialType.DENSE : MonomialType.SPARSE;
        var newBasis = new ArrayList<Polynomial<T>>();
        var monomialsAndRemainders = new TreeMap<Monomial<T>, Polynomial<T>>(initialOrdering);
        var maxExponents = new int[fieldSize];
        maxExponents[0] = 1;
        var maxVariable = monomial(maxExponents, type, one);
        var exponents = new int[fieldSize];
        var monomial = monomial(new int[fieldSize], type, one);
        var polynomial = new Polynomial<>(monomial, ordering);
        var remainder = polynomial.reduce(basis);
        var currentVariableIndex = fieldSize - 1;
        while (true) {
            System.out.printf("%s, remainder = %s", ring.format(polynomial), ring.format(remainder));
            var dependenceData = checkLinearDependence(remainder, monomialsAndRemainders);
            if (!dependenceData.first()) {
                System.out.println(" : linearly independent");
                monomialsAndRemainders.put(monomial, remainder);
                exponents = incrementPowerForExponent(exponents, currentVariableIndex);
                monomial = monomial(exponents, type, one);
                polynomial = new Polynomial<>(monomial, initialOrdering);
                remainder = polynomial.reduce(basis);
            } else {
                System.out.println(" : linearly dependent");
                var result = polynomial;
                for (var entry : dependenceData.second().entrySet()) {
                    result = result.subtract(new Polynomial<>(entry.getKey().multiply(entry.getValue()), ordering));
                }

                newBasis.add(result);
                if (!result.isZero() && result.leadingTerm().isPowerOf(maxVariable)) {
                    break;
                } else {
                    exponents = new int[fieldSize];
                    currentVariableIndex--;
                    exponents[currentVariableIndex] = 1;
                    monomial = monomial(exponents, type, one);
                    polynomial = new Polynomial<>(monomial, initialOrdering);
                    remainder = polynomial.reduce(basis);
                }
            }
        }

        return newBasis;
    }

    private static int[] incrementPowerForExponent(int[] current, int currentVariable) {
        current[currentVariable]++;
        return current;
    }

    private static <T extends Numeric> Pair<Boolean, Map<Monomial<T>, T>> checkLinearDependence(
            Polynomial<T> polynomial,
            Map<Monomial<T>, Polynomial<T>> polynomials
    ) {
        // FIXME
        var coefficients = new HashMap<Monomial<T>, T>();
        while (!polynomial.isZero()) {
            var leadingCoefficient = polynomial.leadingCoefficient();
            var leadingMonomial = polynomial.leadingMonomial();
            var divided = false;
            for (var candidate : polynomials.entrySet()) {
                var candidateMonomial = candidate.getKey();
                var candidatePolynomial = candidate.getValue();
                var candidateLeadingCoefficient = candidatePolynomial.leadingCoefficient();
                var candidateLeadingMonomial = candidatePolynomial.leadingMonomial();
                if (leadingMonomial.equals(candidateLeadingMonomial)) {
                    var divisionResult = (T) leadingCoefficient.divide(candidateLeadingCoefficient);
                    polynomial = polynomial.subtract(candidatePolynomial.multiply(divisionResult));
                    if (coefficients.containsKey(candidateMonomial)) {
                        coefficients.put(candidateMonomial, (T) coefficients.get(candidateMonomial).add(divisionResult));
                    } else {
                        coefficients.put(candidateMonomial, divisionResult);
                    }

                    divided = true;
                    break;
                }
            }

            if (!divided) {
                return new Pair<>(false, Map.of());
            }
        }

        return new Pair<>(true, coefficients);
    }

    private static <T extends Numeric> Monomial<T> monomial(int[] exponents, MonomialType type, T one) {
        if (type == MonomialType.DENSE) {
            return new DenseMonomial<>(exponents, one);
        } else {
            return new SparseMonomial<>(exponents, one);
        }
    }
}
