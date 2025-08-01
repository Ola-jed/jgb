package io.github.olajed.jgb;

import io.github.olajed.jgb.enums.PairSelectionStrategy;
import io.github.olajed.jgb.functions.GrobnerBasisAlgorithms;
import io.github.olajed.jgb.functions.algorithms.BuchbergerAlgorithm;
import io.github.olajed.jgb.functions.algorithms.F4Algorithm;
import io.github.olajed.jgb.functions.algorithms.ImprovedF4Algorithm;
import io.github.olajed.jgb.functions.algorithms.M4GBAlgorithm;
import io.github.olajed.jgb.number.GaloisFieldElement;
import io.github.olajed.jgb.providers.KatsuraGenerator;
import io.github.olajed.jgb.structures.PolynomialRing;

public class Main {
    public static void main(String[] args) {
        try {
            var ring = new PolynomialRing(GaloisFieldElement.class, new String[]{"x1", "x2", "x3", "x4", "x5"});
            var polynomials = KatsuraGenerator.get(4);
            for (var poly : polynomials) {
                System.out.println(ring.format(poly));
            }

            System.out.println("##### The grobner basis (first selection strategy) is");
            var gb = BuchbergerAlgorithm.compute(polynomials, PairSelectionStrategy.FIRST);
            for (var p : gb) {
                System.out.println(ring.format(p));
            }

            System.out.println("##### The grobner basis (degree selection strategy) is");
            gb = BuchbergerAlgorithm.compute(polynomials, PairSelectionStrategy.DEGREE);
            for (var p : gb) {
                System.out.println(ring.format(p));
            }

            System.out.println("##### The grobner basis (normal selection strategy) is");
            gb = BuchbergerAlgorithm.compute(polynomials, PairSelectionStrategy.NORMAL);
            for (var p : gb) {
                System.out.println(ring.format(p));
            }

            System.out.println("##### The grobner basis (sugar selection strategy) is");
            gb = BuchbergerAlgorithm.compute(polynomials, PairSelectionStrategy.SUGAR);
            for (var p : gb) {
                System.out.println(ring.format(p));
            }

            System.out.println("The reduced grobner basis is");
            var reduced = GrobnerBasisAlgorithms.reduceGrobnerBasis(gb);
            for (var p : reduced) {
                System.out.println(ring.format(p));
            }

            System.out.println("##### The grobner basis (F4 algorithm) is");
            gb = F4Algorithm.compute(polynomials);
            for (var p : gb) {
                System.out.println(ring.format(p));
            }

            System.out.println("The reduced grobner basis (after F4) is");
            reduced = GrobnerBasisAlgorithms.reduceGrobnerBasis(gb);
            for (var p : reduced) {
                System.out.println(ring.format(p));
            }

            System.out.println("##### The grobner basis (Improved F4 algorithm) is");
            gb = ImprovedF4Algorithm.compute(polynomials);
            for (var p : gb) {
                System.out.println(ring.format(p));
            }

            System.out.println("The reduced grobner basis (after Improved F4) is");
            reduced = GrobnerBasisAlgorithms.reduceGrobnerBasis(gb);
            for (var p : reduced) {
                System.out.println(ring.format(p));
            }

            System.out.println("##### The grobner basis (M4GB algorithm) is");
            gb = M4GBAlgorithm.compute(polynomials);
            for (var p : gb) {
                System.out.println(ring.format(p));
            }

            System.out.println("The reduced grobner basis (after M4GB) is");
            reduced = GrobnerBasisAlgorithms.reduceGrobnerBasis(gb);
            for (var p : reduced) {
                System.out.println(ring.format(p));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}