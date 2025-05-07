package com.ola;

import com.ola.enums.PairSelectionStrategy;
import com.ola.functions.BuchbergerAlgorithm;
import com.ola.functions.F4Algorithm;
import com.ola.functions.GrobnerBasisAlgorithms;
import com.ola.number.GaloisFieldElement;
import com.ola.number.Real;
import com.ola.providers.ReimerGenerator;
import com.ola.structures.DenseMonomial;
import com.ola.structures.Monomial;
import com.ola.structures.PolynomialRing;
import com.ola.structures.SparseMonomial;

public class Main {
    public static void main(String[] args) {
        try {
            var ring = new PolynomialRing(GaloisFieldElement.class, new String[]{"x", "y", "z", "t"});

            var a = new SparseMonomial<>(new int[]{1, 2, 3, 3}, new GaloisFieldElement(3, 5));
            System.out.println(ring.format(a));
            for (Monomial<GaloisFieldElement> div : a.divisors()) {
                System.out.println(ring.format(div));
            }
            System.exit(0);


            var polynomials = ReimerGenerator.get(4, false);
            for (var polynomial : polynomials) {
                System.out.println(ring.format(polynomial));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}