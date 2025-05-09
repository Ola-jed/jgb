package com.ola.functions;

import com.ola.number.Numeric;
import com.ola.structures.*;
import com.ola.utils.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unchecked")
public final class ImprovedF4Algorithm {
    private ImprovedF4Algorithm() {
    }

    public static <T extends Numeric> List<Polynomial<T>> compute(List<Polynomial<T>> polynomials) {
        List<Polynomial<T>> basis = new ArrayList<>();
        List<Pair<Polynomial<T>, Polynomial<T>>> criticalPairs = new ArrayList<>();
        var polynomialsCopy = new ArrayList<>(polynomials);
        while (!polynomialsCopy.isEmpty()) {
            var selectedPolynomial = polynomialsCopy.removeFirst();
            var updated = update(basis, criticalPairs, selectedPolynomial);
            basis = updated.first();
            criticalPairs = updated.second();
        }

        List<List<Polynomial<T>>> polynomialsSets = new ArrayList<>();
        while (!criticalPairs.isEmpty()) {
            var selectedPairs = selection(criticalPairs);
            criticalPairs.removeAll(selectedPairs);
            var reductionResult = reduction(selectedPairs, basis, polynomialsSets);
            var polys = reductionResult.first();
            polynomialsSets.add(reductionResult.second());
            for (var p : polys) {
                var updateResult = update(basis, criticalPairs, p);
                basis = updateResult.first();
                criticalPairs = updateResult.second();
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

    private static <T extends Numeric> Pair<List<Polynomial<T>>, List<Polynomial<T>>> reduction(
            List<Pair<Polynomial<T>, Polynomial<T>>> pairs,
            List<Polynomial<T>> currentBasis,
            List<List<Polynomial<T>>> polynomialSets
    ) {
        var preprocessed = symbolicPreprocessing(pairs, currentBasis, polynomialSets);
        var preprocessedLeadingTerms = new HashSet<Monomial<T>>(preprocessed.size());
        for (var polynomial : preprocessed) {
            preprocessedLeadingTerms.add(polynomial.leadingTerm());
        }

        var macaulayMatrix = new MacaulayMatrix<>(preprocessed);
        macaulayMatrix.rowEchelonReduction();
        var reducedPolynomials = macaulayMatrix.polynomials();
        var polynomials = new ArrayList<Polynomial<T>>(reducedPolynomials.size());
        for (var reducedPolynomial : reducedPolynomials) {
            if (!preprocessedLeadingTerms.contains(reducedPolynomial.leadingTerm())) {
                polynomials.add(reducedPolynomial);
            }
        }

        return new Pair<>(polynomials, preprocessed);
    }

    private static <T extends Numeric> List<Polynomial<T>> symbolicPreprocessing(
            List<Pair<Polynomial<T>, Polynomial<T>>> pairs,
            List<Polynomial<T>> currentBasis,
            List<List<Polynomial<T>>> polynomialSets
    ) {
        var list = sPolynomialsHalves(pairs);
        var polynomials = new ArrayList<Polynomial<T>>(list.size());
        for (var pair : list) {
            var simplified = simplify(pair.first(), pair.second(), polynomialSets);
            polynomials.add(simplified.second().multiply(simplified.first()));
        }

        var done = new HashSet<Monomial<T>>();
        var allMonomials = new HashSet<Monomial<T>>();
        for (var polynomial : polynomials) {
            done.add(polynomial.leadingTerm());
            allMonomials.addAll(polynomial.monomials());
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
                var divisionResult = largestMonomial.divide(polynomial.leadingTerm());
                if (!divisionResult.coefficient().equals(divisionResult.coefficient().zero())) {
                    var simplificationResult = simplify(divisionResult, polynomial, polynomialSets);
                    var polynomialToAdd = simplificationResult.second().multiply(simplificationResult.first());
                    polynomials.add(polynomialToAdd);
                    allMonomials.addAll(polynomialToAdd.monomials());
                    break;
                }
            }
        }

        return polynomials;
    }

    private static <T extends Numeric> List<Pair<Monomial<T>, Polynomial<T>>> sPolynomialsHalves(
            List<Pair<Polynomial<T>, Polynomial<T>>> pairs
    ) {
        var result = new ArrayList<Pair<Monomial<T>, Polynomial<T>>>(pairs.size() * 2);
        for (var pair : pairs) {
            var lcm = MonomialFunctions.lcm(pair.first().leadingMonomial(), pair.second().leadingMonomial());
            var leftQuotient = lcm.divide(pair.first().leadingTerm());
            var rightQuotient = lcm.divide(pair.second().leadingTerm());
            result.add(new Pair<>(leftQuotient, pair.first()));
            result.add(new Pair<>(rightQuotient, pair.second()));
        }

        return result;
    }

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

    private static <T extends Numeric> Pair<List<Polynomial<T>>, List<Pair<Polynomial<T>, Polynomial<T>>>> update(
            List<Polynomial<T>> oldBasis,
            List<Pair<Polynomial<T>, Polynomial<T>>> oldPairs,
            Polynomial<T> polynomial
    ) {
        var zero = (T) polynomial.leadingCoefficient().zero();
        var pairs = new ArrayList<Pair<Polynomial<T>, Polynomial<T>>>();
        var polynomialLm = polynomial.leadingMonomial();
        for (var p : oldBasis) {
            pairs.add(new Pair<>(polynomial, p));
        }

        var savedPairs = new ArrayList<Pair<Polynomial<T>, Polynomial<T>>>();
        while (!pairs.isEmpty()) {
            var selectedPair = pairs.removeFirst();
            var lm = selectedPair.second().leadingMonomial();

            if (polynomialLm.disjointWith(lm)) {
                savedPairs.add(selectedPair);
                continue;
            }

            var predicateForPairs = pairs.stream().allMatch(x -> MonomialFunctions
                    .lcm(polynomialLm, lm)
                    .divide(MonomialFunctions.lcm(polynomialLm, x.second().leadingMonomial()))
                    .coefficient()
                    .equals(zero));

            var predicateForSavedPairs = savedPairs.stream().allMatch(x -> MonomialFunctions
                    .lcm(polynomialLm, lm)
                    .divide(MonomialFunctions.lcm(polynomialLm, x.second().leadingMonomial()))
                    .coefficient()
                    .equals(zero));


            if (predicateForPairs && predicateForSavedPairs) {
                savedPairs.add(selectedPair);
            }
        }

        var otherPairs = new ArrayList<Pair<Polynomial<T>, Polynomial<T>>>();
        while (!savedPairs.isEmpty()) {
            var selectedPair = savedPairs.removeFirst();
            var lm = selectedPair.second().leadingMonomial();

            if (!polynomialLm.disjointWith(lm)) {
                otherPairs.add(selectedPair);
            }
        }

        var newPairs = new ArrayList<Pair<Polynomial<T>, Polynomial<T>>>();
        while (!oldPairs.isEmpty()) {
            var selectedPair = oldPairs.removeFirst();
            var lm1 = selectedPair.first().leadingMonomial();
            var lm2 = selectedPair.second().leadingMonomial();
            var lcm = MonomialFunctions.lcm(lm1, lm2);

            if (lcm.divide(polynomialLm).coefficient().equals(zero)
                    || MonomialFunctions.lcm(polynomialLm, lm1).equals(lcm)
                    || MonomialFunctions.lcm(polynomialLm, lm2).equals(lcm)
            ) {
                newPairs.add(selectedPair);
            }
        }

        newPairs.addAll(otherPairs);
        var newBasis = new ArrayList<Polynomial<T>>();
        while (!oldBasis.isEmpty()) {
            var poly = oldBasis.removeFirst();
            if (poly.leadingMonomial().divide(polynomialLm).coefficient().equals(zero)) {
                newBasis.add(poly);
            }
        }

        newBasis.add(polynomial);
        return new Pair<>(newBasis, newPairs);
    }
}
