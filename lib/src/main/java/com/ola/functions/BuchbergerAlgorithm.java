package com.ola.functions;

import com.ola.enums.PairSelectionStrategy;
import com.ola.number.Numeric;
import com.ola.structures.Monomial;
import com.ola.structures.Polynomial;
import com.ola.utils.Pair;
import com.ola.utils.Triple;

import java.util.*;

public final class BuchbergerAlgorithm {
    private BuchbergerAlgorithm() {
    }

    public static <T extends Numeric> List<Polynomial<T>> compute(
            List<Polynomial<T>> polynomials
    ) {
        return computeNormalSelectionStrategy(polynomials);
    }

    public static <T extends Numeric> List<Polynomial<T>> compute(
            List<Polynomial<T>> polynomials,
            PairSelectionStrategy selectionStrategy
    ) {
        if (selectionStrategy == PairSelectionStrategy.FIRST) {
            return computeFirstSelectionStrategy(polynomials);
        } else if (selectionStrategy == PairSelectionStrategy.DEGREE) {
            return computeDegreeSelectionStrategy(polynomials);
        } else if (selectionStrategy == PairSelectionStrategy.NORMAL) {
            return computeNormalSelectionStrategy(polynomials);
        } else {
            return computeSugarSelectionStrategy(polynomials);
        }
    }

    private static <T extends Numeric> List<Polynomial<T>> computeFirstSelectionStrategy(List<Polynomial<T>> polynomials) {
        var basis = new ArrayList<>(polynomials);
        var criticalPairs = new ArrayDeque<>(Pair.generatePairs(polynomials));

        while (!criticalPairs.isEmpty()) {
            var selectedPair = criticalPairs.removeFirst();
            var sPolynomial = PolynomialFunctions.sPolynomial(selectedPair.first(), selectedPair.second());
            var reduction = sPolynomial.reduce(basis);

            if (!reduction.monomials().isEmpty()) {
                criticalPairs.addAll(Pair.cartesianProduct(basis, reduction));
                basis.add(reduction);
            }
        }

        return basis;
    }

    private static <T extends Numeric> List<Polynomial<T>> computeDegreeSelectionStrategy(List<Polynomial<T>> polynomials) {
        var basis = new ArrayList<>(polynomials);
        var pq = new PriorityQueue<>(
                Comparator.comparingInt((Triple<Polynomial<T>, Polynomial<T>, Integer> x) -> x.third())
        );

        for (var i = 0; i < basis.size(); i++) {
            for (var j = i + 1; j < basis.size(); j++) {
                var p1 = basis.get(i);
                var p2 = basis.get(j);
                pq.add(new Triple<>(p1, p2, MonomialFunctions.lcm(p1.leadingMonomial(), p2.leadingMonomial()).degree()));
            }
        }

        while (!pq.isEmpty()) {
            var min = pq.poll();
            var selectedPair = new Pair<>(min.first(), min.second());
            var sPolynomial = PolynomialFunctions.sPolynomial(selectedPair.first(), selectedPair.second());
            var reduction = sPolynomial.reduce(basis);

            if (!reduction.monomials().isEmpty()) {
                basis.add(reduction);

                var lastIndex = basis.size() - 1;
                for (int i = 0; i < lastIndex; i++) {
                    var existingPoly = basis.get(i);
                    var degree = MonomialFunctions.lcm(existingPoly.leadingMonomial(), reduction.leadingMonomial()).degree();
                    pq.add(new Triple<>(existingPoly, reduction, degree));
                }
            }
        }

        return basis;
    }

    private static <T extends Numeric> List<Polynomial<T>> computeNormalSelectionStrategy(List<Polynomial<T>> polynomials) {
        var ordering = polynomials.getFirst().ordering();
        var basis = new ArrayList<>(polynomials);
        var pq = new PriorityQueue<>(
                (Comparator<Triple<Polynomial<T>, Polynomial<T>, Monomial<T>>>) (x, y) -> ordering.compare(x.third(), y.third())
        );

        for (var i = 0; i < basis.size(); i++) {
            for (var j = i + 1; j < basis.size(); j++) {
                var p1 = basis.get(i);
                var p2 = basis.get(j);
                pq.add(new Triple<>(p1, p2, MonomialFunctions.lcm(p1.leadingMonomial(), p2.leadingMonomial())));
            }
        }

        while (!pq.isEmpty()) {
            var min = pq.poll();
            var selectedPair = new Pair<>(min.first(), min.second());
            var sPolynomial = PolynomialFunctions.sPolynomial(selectedPair.first(), selectedPair.second());
            var reduction = sPolynomial.reduce(basis);

            if (!reduction.monomials().isEmpty()) {
                basis.add(reduction);

                var lastIndex = basis.size() - 1;
                for (int i = 0; i < lastIndex; i++) {
                    var existingPoly = basis.get(i);
                    var lcm = MonomialFunctions.lcm(existingPoly.leadingMonomial(), reduction.leadingMonomial());
                    pq.add(new Triple<>(existingPoly, reduction, lcm));
                }
            }
        }

        return basis;
    }

    private static <T extends Numeric> List<Polynomial<T>> computeSugarSelectionStrategy(List<Polynomial<T>> polynomials) {
        var basis = new ArrayList<>(polynomials);
        var pq = new PriorityQueue<>(
                Comparator.comparingInt((Triple<Polynomial<T>, Polynomial<T>, Integer> x) -> x.third())
        );

        for (var i = 0; i < basis.size(); i++) {
            for (var j = i + 1; j < basis.size(); j++) {
                var p1 = basis.get(i);
                var p2 = basis.get(j);
                var s1 = p1.degree();
                var s2 = p2.degree();
                var firstPart = Math.max(s1 - p1.leadingMonomial().degree(), s2 - p2.leadingMonomial().degree());
                var secondPart = MonomialFunctions.lcm(p1.leadingMonomial(), p2.leadingMonomial()).degree();
                pq.add(new Triple<>(p1, p2, firstPart + secondPart));
            }
        }

        while (!pq.isEmpty()) {
            var min = pq.poll();
            var selectedPair = new Pair<>(min.first(), min.second());
            var sPolynomial = PolynomialFunctions.sPolynomial(selectedPair.first(), selectedPair.second());
            var reduction = sPolynomial.reduce(basis);

            if (!reduction.monomials().isEmpty()) {
                basis.add(reduction);

                var lastIndex = basis.size() - 1;
                for (int i = 0; i < lastIndex; i++) {
                    var existingPoly = basis.get(i);
                    var lcm = MonomialFunctions.lcm(existingPoly.leadingMonomial(), reduction.leadingMonomial());
                    var s1 = existingPoly.degree();
                    var s2 = reduction.degree();
                    var firstPart = Math.max(s1 - existingPoly.leadingMonomial().degree(), s2 - reduction.leadingMonomial().degree());
                    var secondPart = lcm.degree();
                    pq.add(new Triple<>(existingPoly, reduction, firstPart + secondPart));
                }
            }
        }

        return basis;
    }
}
