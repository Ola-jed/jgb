package com.ola.functions;

import com.ola.number.Numeric;
import com.ola.structures.DenseMonomial;
import com.ola.structures.Monomial;
import com.ola.structures.Polynomial;
import com.ola.structures.SparseMonomial;
import com.ola.utils.Pair;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public final class ImprovedF4Algorithm {
    private ImprovedF4Algorithm() {
    }

    public static <T extends Numeric> List<Polynomial<T>> compute(List<Polynomial<T>> polynomials) {
        var basis = new ArrayList<>(polynomials);
        var criticalPairs = Pair.generatePairs(polynomials);
        while (!criticalPairs.isEmpty()) {
            var selectedPairs = selection(criticalPairs);
            criticalPairs.removeAll(selectedPairs);
            var newBasis = reduction(selectedPairs, basis);
            for (var polynomial : newBasis) {
                criticalPairs.addAll(Pair.cartesianProduct(basis, polynomial));
                basis.add(polynomial);
            }
        }

        return basis;
    }

    private static <T extends Numeric> List<Pair<Polynomial<T>, Polynomial<T>>> selection(
            List<Pair<Polynomial<T>, Polynomial<T>>> pairs
    ) {
        var size = pairs.size();
        var lcmDegrees = new int[size];
        var minDegree = Integer.MAX_VALUE;
        for (var i = 0; i < size; i++) {
            var pair = pairs.get(i);
            var degree = MonomialFunctions.lcm(pair.first().leadingMonomial(), pair.second().leadingMonomial()).degree();
            minDegree = Math.min(minDegree, degree);
            lcmDegrees[i] = degree;
        }

        var selected = new ArrayList<Pair<Polynomial<T>, Polynomial<T>>>(size);
        for (var i = 0; i < size; i++) {
            if (lcmDegrees[i] == minDegree) {
                selected.add(pairs.get(i));
            }
        }

        return selected;
    }

    // TODO
    private static <T extends Numeric> List<Polynomial<T>> reduction(
            List<Pair<Polynomial<T>, Polynomial<T>>> pairs,
            List<Polynomial<T>> currentBasis
    ) {
        throw new RuntimeException();
    }

    // TODO
    private static <T extends Numeric> List<Polynomial<T>> symbolicPreprocessing(
            List<Pair<Polynomial<T>, Polynomial<T>>> pairs,
            List<Polynomial<T>> currentBasis,
            List<List<Polynomial<T>>> polynomialSets
    ) {
        var one = (T) currentBasis.getFirst().leadingCoefficient().one();
        var list = sPolynomialsHalves(pairs);
        var polynomials = new ArrayList<Polynomial<T>>();
        throw new RuntimeException();
    }

    // OK
    private static <T extends Numeric> List<Pair<Monomial<T>,Polynomial<T>>> sPolynomialsHalves(
            List<Pair<Polynomial<T>, Polynomial<T>>> pairs
    ) {
        var result = new ArrayList<Pair<Monomial<T>,Polynomial<T>>>(pairs.size());
        for (var pair : pairs) {
            var lcm = MonomialFunctions.lcm(pair.first().leadingMonomial(), pair.second().leadingMonomial());
            var leftQuotient = lcm.divide(pair.first().leadingTerm());
            var rightQuotient = lcm.divide(pair.second().leadingTerm());
            result.add(new Pair<>(leftQuotient, pair.first()));
            result.add(new Pair<>(rightQuotient, pair.second()));
        }

        return result;
    }

    // OK
    private static <T extends Numeric> Pair<Monomial<T>, Polynomial<T>> simplify(
            Monomial<T> term,
            Polynomial<T> polynomial,
            List<List<Polynomial<T>>> polynomialSets
    ) {
        Monomial<T> one;
        if (term instanceof DenseMonomial<T>) {
            one = new DenseMonomial<>(new int[term.fieldSize()], (T) term.coefficient().one());
        } else {
            one = new SparseMonomial<>(new int[term.fieldSize()], (T) term.coefficient().one());
        }

        for (var divisor : term.divisors()) {
            var product = polynomial.multiply(divisor);
            for (var polynomialSet : polynomialSets) {
                for (var p : polynomialSet) {
                    if (p.leadingTerm().equals(product.leadingTerm())) {
                        if (divisor.equals(term)) {
                            return new Pair<>(one, p);
                        } else {
                            return simplify(term.divide(divisor), polynomial, polynomialSets);
                        }
                    }
                }
            }
        }

        return new Pair<>(term, polynomial);
    }
}
