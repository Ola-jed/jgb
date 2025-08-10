package io.github.olajed.jgb;

import io.github.olajed.jgb.dsl.generator.PolynomialGenerator;
import io.github.olajed.jgb.dsl.lexer.Lexer;
import io.github.olajed.jgb.dsl.parser.Parser;

public class Main {
    public static void main(String[] args) {
        try {
            var lexer = new Lexer();
            var tokens = lexer.scan("x + y + z");
            var ast = new Parser(tokens).parse();
            var generator = new PolynomialGenerator();
            var polys = generator.generate(ast);

            var r = generator.getRing();
            System.out.println(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}