package com.ola.functions.algorithms;

import com.ola.functions.MonomialFunctions;
import com.ola.number.Numeric;
import com.ola.structures.Monomial;
import com.ola.structures.Polynomial;
import com.ola.utils.Pair;
import com.ola.utils.Triple;

import java.util.*;

@SuppressWarnings("unchecked")
public final class M4GBAlgorithm {
    private M4GBAlgorithm() {
    }

    public static <T extends Numeric> List<Polynomial<T>> compute(List<Polynomial<T>> polynomials) {
        var polynomialsToWorkWith = new ArrayList<>(polynomials);
        List<Monomial<T>> monomials = new ArrayList<>();
        List<Polynomial<T>> basis = new ArrayList<>();
        List<Pair<Monomial<T>, Monomial<T>>> pairs = new ArrayList<>();
        var identityMonomial = MonomialFunctions.one(polynomials.getFirst().leadingMonomial());

        for (var i = 0; i < polynomialsToWorkWith.size(); i++) {
            var polynomial = polynomialsToWorkWith.get(i);
            var multiplyFullReduced = multiplyFullReduce(monomials, basis, identityMonomial, polynomial);
            basis = multiplyFullReduced.first();
            polynomialsToWorkWith.set(i, multiplyFullReduced.second());

            var updatedReduced = updateReduce(monomials, basis, pairs, polynomial);
            monomials = updatedReduced.first();
            basis = updatedReduced.second();
            pairs = updatedReduced.third();
        }

        var ordering = polynomials.getFirst().ordering();
        var pq = new PriorityQueue<>((Comparator<Pair<Monomial<T>, Monomial<T>>>) (x, y) -> ordering.compare(x.first(), y.first()));
        pq.addAll(pairs);
        while (!pq.isEmpty()) {
            var selectedPair = pq.poll();
            Polynomial<T> f = null;
            Polynomial<T> g = null;
            for (var polynomial : basis) {
                if (polynomial.leadingMonomial().equals(selectedPair.first())) {
                    f = polynomial;
                }

                if (polynomial.leadingMonomial().equals(selectedPair.second())) {
                    g = polynomial;
                }
            }

            assert f != null && g != null;
            var lcm = MonomialFunctions.lcm(selectedPair.first(), selectedPair.second());
            var mulFullReduce1 = multiplyFullReduce(monomials, basis, lcm.divide(f.leadingTerm()), f.tail());
            var mulFullReduce2 = multiplyFullReduce(monomials, basis, lcm.divide(g.leadingTerm()), g.tail());
            var h = mulFullReduce1.second().subtract(mulFullReduce2.second());
            if (!h.monomials().isEmpty()) {
                var updatedReduced = updateReduce(monomials, basis, new ArrayList<>(pq.stream().toList()), h);
                monomials = updatedReduced.first();
                basis = updatedReduced.second();
                pq.clear();
                pq.addAll(updatedReduced.third());
            }
        }

        var monomialsSet = new HashSet<>(monomials);
        var basisToReturn = new ArrayList<Polynomial<T>>(basis.size());
        for (var polynomial : basis) {
            if (monomialsSet.contains(polynomial.leadingMonomial())) {
                basisToReturn.add(polynomial);
            }
        }

        return basisToReturn;
    }

    private static <T extends Numeric> Triple<List<Monomial<T>>, List<Polynomial<T>>, List<Pair<Monomial<T>, Monomial<T>>>> updateReduce(
            List<Monomial<T>> monomials,
            List<Polynomial<T>> polynomials,
            List<Pair<Monomial<T>, Monomial<T>>> pairs,
            Polynomial<T> polynomial
    ) {
        var leadingCoefficient = polynomial.leadingCoefficient();
        var leadingMonomial = polynomial.leadingMonomial();
        var ordering = polynomial.ordering();

        var currentPolynomials = new ArrayList<Polynomial<T>>();
        currentPolynomials.add(polynomial.multiply((T) leadingCoefficient.inverse()));

        var currentPolynomialsLeadingMonomials = new HashSet<Monomial<T>>(currentPolynomials.size());
        currentPolynomialsLeadingMonomials.add(currentPolynomials.getFirst().leadingMonomial());

        while (true) {
            var polynomialsToTest = new ArrayList<>(polynomials);
            polynomialsToTest.addAll(currentPolynomials);

            var allMonomials = monomialsInTails(polynomialsToTest);
            allMonomials.removeAll(currentPolynomialsLeadingMonomials);

            Monomial<T> selectedMonomial = null;
            for (var monomial : allMonomials) {
                var divisionResult = monomial.divide(leadingMonomial);
                if (!divisionResult.isZero()) {
                    if (selectedMonomial == null || ordering.compare(monomial, selectedMonomial) > 0) {
                        selectedMonomial = monomial;
                    }
                }
            }

            if (selectedMonomial == null) {
                break;
            }

            var result = multiplyFullReduce(
                    monomials,
                    polynomials,
                    selectedMonomial.divide(polynomial.leadingTerm()),
                    polynomial.tail()
            );

            polynomials = result.first();
            var newPolynomial = result.second().add(new Polynomial<>(selectedMonomial, ordering));
            currentPolynomials.add(newPolynomial);
            currentPolynomialsLeadingMonomials.add(newPolynomial.leadingMonomial());
        }

        while (!currentPolynomials.isEmpty()) {
            // Selecting the polynomial with the minimal leading monomial
            Polynomial<T> minPolynomial = null;
            for (var currentPolynomial : currentPolynomials) {
                if (minPolynomial == null) {
                    minPolynomial = currentPolynomial;
                } else if (ordering.compare(currentPolynomial.leadingMonomial(), minPolynomial.leadingMonomial()) > 0) {
                    minPolynomial = currentPolynomial;
                }
            }

            assert minPolynomial != null;

            // Update H
            var zero = (T) leadingCoefficient.zero();
            for (var i = 0; i < currentPolynomials.size(); i++) {
                var g = currentPolynomials.get(i);
                var coefficient = zero;
                for (var monomial : g.tail().monomials()) {
                    if (monomial.exponentsEqual(minPolynomial.leadingMonomial())) {
                        coefficient = monomial.coefficient();
                        break;
                    }
                }

                currentPolynomials.set(i, g.subtract(minPolynomial.multiply(coefficient)));
            }

            // Update M
            for (var i = 0; i < polynomials.size(); i++) {
                var g = polynomials.get(i);
                var coefficient = zero;
                for (var monomial : g.tail().monomials()) {
                    if (monomial.exponentsEqual(minPolynomial.leadingMonomial())) {
                        coefficient = monomial.coefficient();
                        break;
                    }
                }

                polynomials.set(i, g.subtract(minPolynomial.multiply(coefficient)));
            }

            polynomials.add(minPolynomial);
            currentPolynomials.remove(minPolynomial);
        }

        var updateResult = update(monomials, pairs, polynomial.leadingMonomial());
        return new Triple<>(updateResult.first(), polynomials, updateResult.second());
    }

    private static <T extends Numeric> Pair<List<Polynomial<T>>, Polynomial<T>> multiplyFullReduce(
            List<Monomial<T>> monomials,
            List<Polynomial<T>> polynomials,
            Monomial<T> term,
            Polynomial<T> polynomial
    ) {
        var ordering = polynomial.ordering();
        var resultingPolynomial = new Polynomial<>(term.fieldSize(), ordering);
        for (var polynomialTerm : polynomial.monomials()) {
            var product = term.multiply(polynomialTerm);
            if (monomialReducibleByMonomials(monomials, product)) {
                var reductors = getReductor(monomials, polynomials, product);
                polynomials = reductors.first();
                var reductorPolynomial = reductors.second();
                var rLt = reductorPolynomial.leadingTerm();
                var rTail = reductorPolynomial.tail();
                resultingPolynomial = resultingPolynomial.subtract(rTail.multiply(product.divide(rLt)));
            } else {
                resultingPolynomial = resultingPolynomial.add(new Polynomial<>(product, ordering));
            }
        }

        return new Pair<>(polynomials, resultingPolynomial);
    }

    private static <T extends Numeric> Pair<List<Polynomial<T>>, Polynomial<T>> getReductor(
            List<Monomial<T>> monomials,
            List<Polynomial<T>> polynomials,
            Monomial<T> term
    ) {
        for (var polynomial : polynomials) {
            if (polynomial.leadingMonomial().exponentsEqual(term)) {
                return new Pair<>(polynomials, polynomial);
            }
        }

        var f = reduceSel(polynomials, term).orElseThrow();
        var fullReduced = multiplyFullReduce(monomials, polynomials, term.divide(f.leadingTerm()), f.tail());
        var polynomialAdded = fullReduced.second().add(new Polynomial<>(term, f.ordering()));
        var polynomialsToReturn = fullReduced.first();
        polynomialsToReturn.add(polynomialAdded);
        return new Pair<>(polynomialsToReturn, polynomialAdded);
    }

    private static <T extends Numeric> boolean monomialReducibleByMonomials(List<Monomial<T>> monomials, Monomial<T> monomial) {
        for (var maybeDivisor : monomials) {
            if (!monomial.divide(maybeDivisor).isZero()) {
                return true;
            }
        }

        return false;
    }

    private static <T extends Numeric> Optional<Polynomial<T>> reduceSel(List<Polynomial<T>> basis, Monomial<T> monomial) {
        for (var polynomial : basis) {
            if (!monomial.divide(polynomial.leadingMonomial()).isZero()) {
                return Optional.of(polynomial);
            }
        }

        return Optional.empty();
    }

    private static <T extends Numeric> Set<Monomial<T>> monomialsInTails(List<Polynomial<T>> polynomials) {
        T one = null;
        if (!polynomials.isEmpty()) {
            one = (T) polynomials.getFirst().leadingCoefficient().one();
        }

        var monomials = new HashSet<Monomial<T>>();
        for (var polynomial : polynomials) {
            for (var monomial : polynomial.tail().monomials()) {
                monomials.add(monomial.withCoefficient(one));
            }
        }

        return monomials;
    }

    // Rework of the Buchberger update function to handle monomials
    private static <T extends Numeric> Pair<List<Monomial<T>>, List<Pair<Monomial<T>, Monomial<T>>>> update(
            List<Monomial<T>> monomials,
            List<Pair<Monomial<T>, Monomial<T>>> oldPairs,
            Monomial<T> monomial
    ) {
        var pairs = new ArrayList<Pair<Monomial<T>, Monomial<T>>>(monomials.size());
        for (var p : monomials) {
            pairs.add(new Pair<>(monomial, p));
        }

        var savedPairs = new ArrayList<Pair<Monomial<T>, Monomial<T>>>();
        while (!pairs.isEmpty()) {
            var selectedPair = pairs.removeLast();
            if (monomial.disjointWith(selectedPair.second())) {
                savedPairs.add(selectedPair);
                continue;
            }

            var predicateForPairs = pairs.stream().allMatch(x -> MonomialFunctions
                    .lcm(monomial, selectedPair.second())
                    .divide(MonomialFunctions.lcm(monomial, x.second()))
                    .isZero());

            var predicateForSavedPairs = savedPairs.stream().allMatch(x -> MonomialFunctions
                    .lcm(monomial, selectedPair.second())
                    .divide(MonomialFunctions.lcm(monomial, x.second()))
                    .isZero());

            if (predicateForPairs && predicateForSavedPairs) {
                savedPairs.add(selectedPair);
            }
        }

        var otherPairs = new ArrayList<Pair<Monomial<T>, Monomial<T>>>();
        while (!savedPairs.isEmpty()) {
            var selectedPair = savedPairs.removeLast();
            if (!monomial.disjointWith(selectedPair.second())) {
                otherPairs.add(selectedPair);
            }
        }

        var newPairs = new ArrayList<Pair<Monomial<T>, Monomial<T>>>();
        while (!oldPairs.isEmpty()) {
            var selectedPair = oldPairs.removeLast();
            var x = selectedPair.first();
            var y = selectedPair.second();
            var lcm = MonomialFunctions.lcm(x, y);
            if (lcm.divide(monomial).isZero() || MonomialFunctions.lcm(x, monomial).equals(lcm)
                    || MonomialFunctions.lcm(monomial, y).equals(lcm)
            ) {
                newPairs.add(selectedPair);
            }
        }

        newPairs.addAll(otherPairs);
        var newMonomials = new ArrayList<Monomial<T>>(monomials.size());
        while (!monomials.isEmpty()) {
            var candidate = monomials.removeLast();
            if (candidate.divide(monomial).isZero()) {
                newMonomials.add(candidate);
            }
        }

        newMonomials.add(monomial);
        return new Pair<>(newMonomials, newPairs);
    }
}