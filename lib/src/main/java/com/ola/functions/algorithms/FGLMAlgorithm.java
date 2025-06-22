package com.ola.functions.algorithms;

import com.ola.enums.MonomialType;
import com.ola.number.Numeric;
import com.ola.ordering.GrevlexOrdering;
import com.ola.ordering.LexOrdering;
import com.ola.structures.DenseMonomial;
import com.ola.structures.Monomial;
import com.ola.structures.Polynomial;
import com.ola.structures.SparseMonomial;
import com.ola.utils.MatrixSolver;
import com.ola.utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public final class FGLMAlgorithm {
    /**
     * Converts a reduced Gröbner basis from graded reverse lexicographic (grevlex) order
     * to lexicographic (lex) order using the FGLM algorithm.
     *
     * <p>
     * Given a reduced Gröbner basis <code>G</code> computed with respect to the grevlex order,
     * this method computes the reduced Gröbner basis <code>G'</code> with respect to the lex order.
     * </p>
     *
     * <p>Algorithm overview:</p>
     * <ol>
     *   <li>Initialize an empty set <code>B</code> to hold new basis elements (corresponding to standard monomials).</li>
     *   <li>Initialize <code>G'</code> as a copy of the input basis <code>G</code>.</li>
     *   <li>Start with the monomial <code>X = 1</code> (the multiplicative identity).</li>
     *   <li>Compute the normal form <code>r(X)</code> of <code>X</code> modulo <code>G</code>.</li>
     *   <li>
     *     If <code>r(X) = 0</code>:
     *     <ul>
     *       <li>Add <code>X</code> to <code>B</code> (it corresponds to a new standard monomial).</li>
     *       <li>Increment <code>X</code> to the next monomial power in the current indeterminate.</li>
     *     </ul>
     *   </li>
     *   <li>
     *     Otherwise, if <code>r(X)</code> can be expressed as a linear combination of elements in <code>B</code>:
     *     <ul>
     *       <li>Let <code>A</code> be this linear combination, then define <code>g = X - A</code>.</li>
     *       <li>Add <code>g</code> to <code>G'</code> as a new polynomial in the lex Gröbner basis.</li>
     *       <li>If the leading term of <code>g</code> is a pure power of the maximal variable under the lex order, the algorithm terminates.</li>
     *       <li>Otherwise, update <code>X</code> to the next monomial not divisible by any leading term in <code>G'</code>.</li>
     *     </ul>
     *   </li>
     *   <li>Return the new Gröbner basis <code>G'</code> in lex order.</li>
     * </ol>
     *
     * @param <T>   the numeric type used in the polynomial coefficients, extending {@code Numeric}
     * @param basis the reduced Gröbner basis under grevlex ordering
     * @return the reduced Gröbner basis under lex ordering
     */
    public static <T extends Numeric> List<Polynomial<T>> compute(List<Polynomial<T>> basis) {
        var initialOrdering = new GrevlexOrdering<T>();
        var ordering = new LexOrdering<T>();
        var firstPoly = basis.getFirst();
        var fieldSize = firstPoly.fieldSize();
        var one = (T) firstPoly.leadingCoefficient().one();
        var type = firstPoly.leadingTerm() instanceof DenseMonomial<T> ? MonomialType.DENSE : MonomialType.SPARSE;
        var newBasis = new ArrayList<Polynomial<T>>();
        var monomialsAndRemainders = new ArrayList<Pair<Monomial<T>, Polynomial<T>>>();
        var maxExponents = new int[fieldSize];
        maxExponents[0] = 1;
        var maxVariable = monomial(maxExponents, type, one);
        var exponents = new int[fieldSize];
        var monomial = monomial(new int[fieldSize], type, one);
        var polynomial = new Polynomial<>(monomial, initialOrdering);
        var remainder = polynomial.reduce(basis);
        var currentVariableIndex = fieldSize - 1;
        while (true) {
            var dependenceData = checkLinearDependence(remainder, monomialsAndRemainders);
            if (!dependenceData.first()) {
                monomialsAndRemainders.add(new Pair<>(monomial, remainder));
                exponents = incrementPowerForExponent(exponents, currentVariableIndex);
                monomial = monomial(exponents, type, one);
                polynomial = new Polynomial<>(monomial, initialOrdering);
                remainder = polynomial.reduce(basis);
            } else {
                var result = polynomial.changeOrdering(ordering);
                for (var pair : dependenceData.second()) {
                    result = result.subtract(new Polynomial<>(pair.first().multiply(pair.second()), ordering));
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
        var result = Arrays.copyOf(current, current.length);
        result[currentVariable]++;
        return result;
    }

    private static <T extends Numeric> Pair<Boolean, List<Pair<Monomial<T>, T>>> checkLinearDependence(
            Polynomial<T> polynomial, List<Pair<Monomial<T>, Polynomial<T>>> polynomials
    ) {
        var ordering = new LexOrdering<T>().reversed();
        var zero = (T) polynomial.leadingCoefficient().zero();
        var one = (T) polynomial.leadingCoefficient().one();

        // Collect all unique monomials
        Set<Monomial<T>> allMonomials = new LinkedHashSet<>();
        for (var entry : polynomials) {
            for (var monomial : entry.second().monomials()) {
                allMonomials.add(monomial.withCoefficient(one));
            }
        }

        // Sort the monomials
        var sortedMonomials = new ArrayList<>(allMonomials);
        sortedMonomials.sort(ordering);

        // Create a map for fast monomial lookups
        Map<Monomial<T>, Integer> monomialIndices = new HashMap<>(sortedMonomials.size());
        for (var i = 0; i < sortedMonomials.size(); i++) {
            monomialIndices.put(sortedMonomials.get(i), i);
        }

        // Initialize the transposed coefficient matrix
        var rows = sortedMonomials.size();
        var cols = polynomials.size();
        List<List<T>> matrix = new ArrayList<>(rows);
        for (var i = 0; i < rows; i++) {
            List<T> row = new ArrayList<>(cols);
            for (var j = 0; j < cols; j++) {
                row.add(zero);
            }

            matrix.add(row);
        }

        // Fill the transposed matrix
        var columnIndex = 0;
        for (var entry : polynomials) {
            var currentPolynomial = entry.second();
            for (var monomial : currentPolynomial.monomials()) {
                var lookupMonomial = monomial.withCoefficient(one);
                var rowIndex = monomialIndices.get(lookupMonomial);
                matrix.get(rowIndex).set(columnIndex, monomial.coefficient());
            }

            columnIndex++;
        }

        List<T> polynomialCoefficients = new ArrayList<>();
        var coefficientMap = polynomial
                .monomials()
                .stream()
                .collect(Collectors.toMap(m -> m.withCoefficient(one), Monomial::coefficient));

        for (var monomial : coefficientMap.keySet()) {
            if (!allMonomials.contains(monomial)) {
                return new Pair<>(false, null);
            }
        }

        for (var monomial : sortedMonomials) {
            var coefficient = coefficientMap.getOrDefault(monomial, zero);
            polynomialCoefficients.add(coefficient);
        }

        var solver = new MatrixSolver<>(matrix, polynomialCoefficients);
        var results = solver.solve();
        if (results == null) {
            return new Pair<>(false, null);
        }

        var coefficients = new ArrayList<Pair<Monomial<T>, T>>();
        var i = 0;
        for (var pair : polynomials) {
            coefficients.add(new Pair<>(pair.first(), results.get(i)));
            i++;
        }

        return new Pair<>(true, coefficients);
    }

    private static <T extends Numeric> Monomial<T> monomial(int[] exponents, MonomialType type, T one) {
        return switch (type) {
            case DENSE -> new DenseMonomial<>(exponents, one);
            case SPARSE -> new SparseMonomial<>(exponents, one);
            case null -> throw new IllegalArgumentException("MonomialType cannot be null");
        };
    }
}
