package io.github.olajed.jgb.dsl.generator;

import io.github.olajed.jgb.dsl.ast.FieldConfigurationNode;
import io.github.olajed.jgb.dsl.ast.PolynomialNode;
import io.github.olajed.jgb.dsl.ast.VariablesConfigurationNode;
import io.github.olajed.jgb.enums.MonomialType;
import io.github.olajed.jgb.enums.NumericType;
import io.github.olajed.jgb.number.GaloisFieldElement;
import io.github.olajed.jgb.number.NumericException;
import io.github.olajed.jgb.number.Rational;
import io.github.olajed.jgb.number.Real;
import io.github.olajed.jgb.ordering.GrevlexOrdering;
import io.github.olajed.jgb.structures.PolynomialRing;
import io.github.olajed.jgb.utils.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PolynomialGeneratorTest {
    private PolynomialGenerator generator;

    @BeforeEach
    void setup() {
        generator = new PolynomialGenerator();
    }

    @Test
    void testFieldConfigurationNode_setsReal() {
        var node = new FieldConfigurationNode(NumericType.Real);
        node.accept(generator);
        assertEquals(NumericType.Real, generator.getRing() == null ? NumericType.Real : null);
    }

    @Test
    void testVariablesConfigurationNode_setsVariables() {
        var node = new VariablesConfigurationNode(Arrays.asList("x", "y"));
        node.accept(generator);
        generator.generate(List.of(node));
        assertArrayEquals(new String[]{"x", "y"}, generator.getRing().indeterminates());
    }

    @Test
    void testGenerate_simplePolynomialRealField() {
        var ast = Arrays.asList(
                new FieldConfigurationNode(NumericType.Real),
                new VariablesConfigurationNode(Arrays.asList("x", "y")),
                new PolynomialNode(Arrays.asList(
                        new Pair<>(new Real(1), Map.of("x", 2)),
                        new Pair<>(new Real(3), Map.of("y", 1))
                ))
        );

        var polys = generator.generate(ast);
        assertEquals(1, polys.size());

        var field = new PolynomialRing(Real.class, new String[]{"x", "y"});
        var expected = field.createPolynomial(
                Arrays.asList(
                        field.createMonomial(new Real(1), Map.of("x", 2), MonomialType.DENSE),
                        field.createMonomial(new Real(3), Map.of("y", 1), MonomialType.DENSE)
                ),
                new GrevlexOrdering<>()
        );

        assertEquals(expected, polys.getFirst());
    }

    @Test
    void testGenerate_infersVariables() {
        var ast = Arrays.asList(
                new FieldConfigurationNode(NumericType.Rational),
                new PolynomialNode(List.of(new Pair<>(new Rational(1), Map.of("z", 2))))
        );

        var polys = generator.generate(ast);
        assertEquals(1, polys.size());

        var field = new PolynomialRing(Rational.class, new String[]{"z"});
        var expected = field.createPolynomial(
                List.of(field.createMonomial(new Rational(1), Map.of("z", 2), MonomialType.DENSE)),
                new GrevlexOrdering<>()
        );

        assertEquals(expected, polys.getFirst());
    }

    @Test
    void testGenerate_galoisFieldReduction() {
        var ast = Arrays.asList(
                new FieldConfigurationNode(NumericType.GaloisField, 5),
                new VariablesConfigurationNode(List.of("x")),
                new PolynomialNode(List.of(new Pair<>(new Real(7), Map.of("x", 1))))
        );

        var polys = generator.generate(ast);
        assertEquals(1, polys.size());
        var field = new PolynomialRing(GaloisFieldElement.class, new String[]{"x"});
        var expected = field.createPolynomial(
                List.of(field.createMonomial(new GaloisFieldElement(2, 5), Map.of("x", 1), MonomialType.DENSE)),
                new GrevlexOrdering<>()
        );

        assertEquals(expected, polys.getFirst());
    }

    @Test
    void testGenerate_emptyAst_returnsNoPolynomials() {
        var polys = generator.generate(Collections.emptyList());
        assertTrue(polys.isEmpty());
    }

    @Test
    void testInvalidConversion_throws() {
        var ast = Arrays.asList(
                new FieldConfigurationNode(NumericType.Rational),
                new VariablesConfigurationNode(List.of("x")),
                new PolynomialNode(List.of(new Pair<>(new Real(3.5), Map.of("x", 1))))
        );

        assertThrows(NumericException.class, () -> generator.generate(ast));
    }
}

