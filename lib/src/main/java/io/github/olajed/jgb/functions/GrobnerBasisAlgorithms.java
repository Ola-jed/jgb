package io.github.olajed.jgb.functions;

import io.github.olajed.jgb.number.Numeric;
import io.github.olajed.jgb.structures.Polynomial;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Utility class providing algorithms related to Grobner bases.
 * <p>
 * This class contains static methods for operations such as reducing and minimizing
 * a Grobner basis of polynomials
 * </p>
 */
public final class GrobnerBasisAlgorithms {
    private GrobnerBasisAlgorithms() {
    }

    /**
     * Reduces a Grobner basis by ensuring each polynomial is reduced with respect to the others.
     *
     * <p>This process makes the Grobner basis reduced, meaning no polynomial can be further simplified by
     * any other polynomial in the basis.</p>
     *
     * @param polynomials the Grobner basis to reduce
     * @param <T>         the numeric type of the polynomial coefficients
     * @return a reduced Grobner basis
     */
    public static <T extends Numeric> List<Polynomial<T>> reduceGrobnerBasis(List<Polynomial<T>> polynomials) {
        var minimized = minimizeGrobnerBasis(polynomials);
        var size = minimized.size();
        var result = new ArrayList<Polynomial<T>>(size);

        if (size <= 1) {
            return new ArrayList<>(minimized);
        }

        for (var i = 0; i < size; i++) {
            var current = minimized.get(i);
            var reducers = new ArrayList<>(minimized);
            reducers.remove(i);
            result.add(current.reduce(reducers));
        }

        return result;
    }

    /**
     * Minimizes a given Grobner basis by removing redundant polynomials.
     *
     * @param polynomials the initial Grobner basis to minimize
     * @param <T>         the numeric type of the polynomial coefficients
     * @return a minimized Grobner basis with redundant polynomials removed
     */
    public static <T extends Numeric> List<Polynomial<T>> minimizeGrobnerBasis(List<Polynomial<T>> polynomials) {
        var size = polynomials.size();
        var monicBasis = new ArrayList<Polynomial<T>>(size);
        for (Polynomial<T> polynomial : polynomials) {
            monicBasis.add(polynomial.divide(polynomial.leadingCoefficient()));
        }

        var toKeep = new BitSet(size);
        toKeep.set(0, size);
        for (var i = 0; i < size; i++) {
            if (!toKeep.get(i)) {
                continue;
            }

            for (var j = 0; j < size; j++) {
                if (i != j && toKeep.get(j)) {
                    var divisionResult = monicBasis.get(i)
                            .leadingMonomial()
                            .divide(monicBasis.get(j).leadingMonomial())
                            .coefficient();

                    if (!divisionResult.equals(divisionResult.zero())) {
                        toKeep.clear(i);
                        break;
                    }
                }
            }
        }

        var result = new ArrayList<Polynomial<T>>();
        for (int i = 0; i < size; i++) {
            if (toKeep.get(i)) {
                result.add(monicBasis.get(i));
            }
        }

        return result;
    }

    /**
     * Checks whether a given list of polynomials forms a Gröbner basis using Buchberger's criterion.
     *
     * <p>
     * This method verifies if the provided set of polynomials constitutes a Gröbner basis
     * for the ideal they generate. It applies Buchberger's criterion by computing all
     * S-polynomials for each distinct pair of polynomials and reducing them modulo the set.
     * If any S-polynomial does not reduce to zero, the given set is not a Gröbner basis.
     * </p>
     *
     * @param polynomials the list of polynomials to check
     * @param <T>         the numeric type of the polynomial coefficients,
     *                    which must extend {@link Numeric}
     * @return {@code true} if the polynomials form a Gröbner basis;
     * {@code false} otherwise
     * @see Polynomial
     */
    public static <T extends Numeric> boolean isGrobnerBasis(List<Polynomial<T>> polynomials) {
        for (var i = 0; i < polynomials.size(); i++) {
            for (var j = i + 1; j < polynomials.size(); j++) {
                var sPolynomial = PolynomialFunctions.sPolynomial(polynomials.get(i), polynomials.get(j));
                var reductionResult = sPolynomial.reduce(polynomials);

                if (!reductionResult.isZero()) {
                    return false;
                }
            }
        }

        return true;
    }
}

