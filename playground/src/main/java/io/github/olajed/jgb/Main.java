package io.github.olajed.jgb;

import io.github.olajed.jgb.enums.PairSelectionStrategy;
import io.github.olajed.jgb.functions.GrobnerBasisAlgorithms;
import io.github.olajed.jgb.functions.algorithms.BuchbergerAlgorithm;
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

            System.out.println("Is original GB : "+ GrobnerBasisAlgorithms.isGrobnerBasis(polynomials));
            System.out.println("Is after Buchberger GB : "+ GrobnerBasisAlgorithms.isGrobnerBasis(gb));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}