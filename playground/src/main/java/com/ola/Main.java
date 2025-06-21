package com.ola;

import com.ola.functions.algorithms.FGLMAlgorithm;
import com.ola.number.Rational;
import com.ola.structures.PolynomialRing;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            var ring = new PolynomialRing(Rational.class, new String[]{"x", "y", "z"});
            var p1 = ring.parse("980 * z^2 - 18 * y - 201 * z + 13");
            var p2 = ring.parse("35 * y * z - 4 * y + 2 * z - 1");
            var p3 = ring.parse("10 * y^2 - y - 12 * z + 1");
            var p4 = ring.parse("5 * x^2 - 4 * y + 2 * z - 1");


            var gb = Arrays.asList(p1, p2, p3, p4);
            System.out.println("-------------------------------------------------");

            var lexGb = FGLMAlgorithm.compute(gb);
            System.out.println("-------------------------------------------------");

            System.out.println("Lex gb after FGLM");
            for (var polynomial : lexGb) {
                System.out.println(ring.format(polynomial));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}