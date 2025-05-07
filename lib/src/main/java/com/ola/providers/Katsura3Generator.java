package com.ola.providers;

import com.ola.number.GaloisFieldElement;
import com.ola.ordering.GrlexOrdering;
import com.ola.ordering.MonomialOrdering;
import com.ola.structures.DenseMonomial;
import com.ola.structures.Monomial;
import com.ola.structures.Polynomial;
import com.ola.structures.SparseMonomial;

import java.util.ArrayList;
import java.util.List;

public final class Katsura3Generator {
    private Katsura3Generator() {
    }

    public static List<Polynomial<GaloisFieldElement>> get() {
        return get(new GrlexOrdering<>(), true);
    }

    public static List<Polynomial<GaloisFieldElement>> get(MonomialOrdering<GaloisFieldElement> ordering) {
        return get(ordering, true);
    }

    public static List<Polynomial<GaloisFieldElement>> get(boolean dense) {
        return get(new GrlexOrdering<>(), dense);
    }

    public static List<Polynomial<GaloisFieldElement>> get(MonomialOrdering<GaloisFieldElement> ordering, boolean dense) {
        var polynomials = new ArrayList<Polynomial<GaloisFieldElement>>(3);
        var coefficients = coefficients();
        var monomials = List.of(
                List.of(new int[]{1, 0, 0}, new int[]{0, 1, 0}, new int[]{0, 0, 1}, new int[]{0, 0, 0}),
                List.of(new int[]{2, 0, 0}, new int[]{0, 2, 0}, new int[]{0, 0, 2}, new int[]{1, 0, 0}),
                List.of(new int[]{1, 1, 0}, new int[]{0, 1, 1}, new int[]{0, 1, 0})
        );

        for (int i = 0; i < monomials.size(); i++) {
            var exponentsLists = monomials.get(i);
            var tempMonomials = new ArrayList<Monomial<GaloisFieldElement>>(monomials.size());
            for (int j = 0; j < exponentsLists.size(); j++) {
                if (dense) {
                    tempMonomials.add(new DenseMonomial<>(exponentsLists.get(j), coefficients.get(i).get(j)));
                } else {
                    tempMonomials.add(new SparseMonomial<>(exponentsLists.get(j), coefficients.get(i).get(j)));
                }
            }

            polynomials.add(new Polynomial<>(tempMonomials, 3, ordering));
        }

        return polynomials;
    }

    private static List<List<GaloisFieldElement>> coefficients() {
        var gf1 = new GaloisFieldElement(1, 5);
        var gf2 = new GaloisFieldElement(2, 5);
        var gfNeg1 = new GaloisFieldElement(-1, 5);
        return List.of(
                List.of(gf1, gf2, gf2, gfNeg1),
                List.of(gf1, gf2, gf2, gfNeg1),
                List.of(gf2, gf2, gfNeg1)
        );
    }
}
