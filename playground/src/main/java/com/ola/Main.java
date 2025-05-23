package com.ola;

import com.ola.dsl.ast.AstNode;
import com.ola.dsl.lexer.Lexer;
import com.ola.dsl.parser.Parser;
import com.ola.dsl.tokens.Token;
import com.ola.enums.PairSelectionStrategy;
import com.ola.functions.GrobnerBasisAlgorithms;
import com.ola.functions.algorithms.BuchbergerAlgorithm;
import com.ola.functions.algorithms.F4Algorithm;
import com.ola.functions.algorithms.ImprovedF4Algorithm;
import com.ola.functions.algorithms.M4GBAlgorithm;
import com.ola.number.GaloisFieldElement;
import com.ola.providers.ReimerGenerator;
import com.ola.structures.PolynomialRing;

public class Main {
    public static void main(String[] args) {
        try {
            var lexer = new Lexer();
            var tokens = lexer.scan("""
                        @variables(x, y, z)
                        @ordering(grlex)
                        @dense
                        @field(GF[5])
                    
                    """);

            System.out.println("=========================================");
            System.out.println("Tokens");
            for (Token token : tokens) {
                System.out.println(token);
            }

            System.out.println("=========================================");
            System.out.println("AST");
            var parser = new Parser(tokens);
            var ast = parser.parse();
            for (AstNode astNode : ast) {
                System.out.println(astNode);
            }

            System.exit(0);

            var ring = new PolynomialRing(GaloisFieldElement.class, new String[]{"x", "y", "z"});
            var polynomials = ReimerGenerator.get(3);
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