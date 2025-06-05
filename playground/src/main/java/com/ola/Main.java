package com.ola;

import com.ola.dsl.ast.AstNode;
import com.ola.dsl.generator.PolynomialGenerator;
import com.ola.dsl.lexer.Lexer;
import com.ola.dsl.parser.Parser;
import com.ola.dsl.tokens.Token;
import com.ola.enums.PairSelectionStrategy;
import com.ola.functions.GrobnerBasisAlgorithms;
import com.ola.functions.algorithms.BuchbergerAlgorithm;
import com.ola.functions.algorithms.F4Algorithm;
import com.ola.functions.algorithms.ImprovedF4Algorithm;
import com.ola.functions.algorithms.M4GBAlgorithm;
import com.ola.graphs.GraphColoringProblem;
import com.ola.number.Complex;
import com.ola.number.GaloisFieldElement;
import com.ola.providers.KatsuraGenerator;
import com.ola.structures.Polynomial;
import com.ola.structures.PolynomialRing;

public class Main {
    public static void main(String[] args) {
        try {
            var x = new PolynomialRing(GaloisFieldElement.class, new String[]{"x1", "x2", "x3", "x4", "x5", "x6", "x7"});
            var xx = KatsuraGenerator.get(6);
            for (Polynomial<GaloisFieldElement> xxx : xx) {
                System.out.println(x.format(xxx));
            }


            System.exit(0);

            var r = new PolynomialRing(Complex.class, new String[]{"a", "b", "c", "d", "e"});
            var w = new GraphColoringProblem("C:\\Users\\olabi\\Projects\\IdeaProjects\\jgb\\examples\\graphs\\simple");

            System.out.println(w.isKColorable(3));


            var ring = new PolynomialRing(Complex.class, new String[]{"x", "y", "z"});
            var lexer = new Lexer();
            var tokens = lexer.scan("""
                    @variables(x, y, z)
                    @field(GF[5])
                    @ordering(grevlex)
                    @dense
                    
                    4 + 2 * z^2 + 3 * y^2 + 2 * x^2
                    4 + 2 * z^3 + 3 * y^3 + 2 * x^3
                    4 + 2 * z^4 + 3 * y^4 + 2 * x^4
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

            System.out.println("=========================================");
            System.out.println("POLYNOMIALS");
            var generator = new PolynomialGenerator();
            var polynomials = generator.generate(ast);
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