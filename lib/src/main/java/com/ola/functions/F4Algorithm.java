package com.ola.functions;

import com.ola.number.Numeric;
import com.ola.structures.MacaulayMatrix;
import com.ola.structures.Monomial;
import com.ola.structures.Polynomial;
import com.ola.utils.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unchecked")
public final class F4Algorithm {
    private F4Algorithm() {
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

    private static <T extends Numeric> List<Polynomial<T>> reduction(
            List<Pair<Polynomial<T>, Polynomial<T>>> pairs,
            List<Polynomial<T>> currentBasis
    ) {
        var list = symbolicPreprocessing(pairs, currentBasis);
        var leadingMonomials = new HashSet<Monomial<T>>();
        for (var polynomial : list) {
            leadingMonomials.add(polynomial.leadingMonomial());
        }

        var macaulayMatrix = new MacaulayMatrix<>(list);
        macaulayMatrix.rowEchelonReduction();
        var polynomials = macaulayMatrix.polynomials();
        var results = new ArrayList<Polynomial<T>>(polynomials.size());
        for (var polynomial : polynomials) {
            if (!leadingMonomials.contains(polynomial.leadingMonomial())) {
                results.add(polynomial);
            }
        }

        return results;
    }

    private static <T extends Numeric> List<Polynomial<T>> symbolicPreprocessing(
            List<Pair<Polynomial<T>, Polynomial<T>>> pairs,
            List<Polynomial<T>> currentBasis
    ) {
        var one = (T) currentBasis.getFirst().leadingCoefficient().one();
        var list = sPolynomialsHalves(pairs);
        var done = new HashSet<Monomial<T>>();
        var allMonomials = new HashSet<Monomial<T>>();
        for (var polynomial : list) {
            done.add(polynomial.leadingMonomial());

            for (var monomial : polynomial.monomials()) {
                allMonomials.add(monomial.withCoefficient(one));
            }
        }

        var ordering = currentBasis.getFirst().ordering();
        while (!done.equals(allMonomials)) {
            Monomial<T> largestMonomial = null;
            for (var monomial : allMonomials) {
                if (!done.contains(monomial)) {
                    if (largestMonomial == null || ordering.compare(monomial, largestMonomial) > 0) {
                        largestMonomial = monomial;
                    }
                }
            }

            assert largestMonomial != null;
            done.add(largestMonomial);
            for (var polynomial : currentBasis) {
                var divisionResult = largestMonomial.divide(polynomial.leadingMonomial());
                if (!divisionResult.coefficient().equals(divisionResult.coefficient().zero())) {
                    var polynomialToAdd = polynomial.multiply(divisionResult);
                    list.add(polynomialToAdd);

                    for (var monomial : polynomialToAdd.monomials()) {
                        allMonomials.add(monomial.withCoefficient(one));
                    }

                    break;
                }
            }
        }

        return list;
    }

    private static <T extends Numeric> List<Polynomial<T>> sPolynomialsHalves(
            List<Pair<Polynomial<T>, Polynomial<T>>> pairs
    ) {
        var result = new ArrayList<Polynomial<T>>(pairs.size());
        for (var pair : pairs) {
            var lcm = MonomialFunctions.lcm(pair.first().leadingMonomial(), pair.second().leadingMonomial());
            var leftQuotient = lcm.divide(pair.first().leadingTerm());
            var rightQuotient = lcm.divide(pair.second().leadingTerm());
            result.add(pair.first().multiply(leftQuotient));
            result.add(pair.second().multiply(rightQuotient));
        }

        return result;
    }
}
