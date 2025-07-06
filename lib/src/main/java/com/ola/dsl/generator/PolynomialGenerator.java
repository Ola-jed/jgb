package com.ola.dsl.generator;

import com.ola.dsl.ast.*;
import com.ola.enums.MonomialType;
import com.ola.enums.NumericType;
import com.ola.number.*;
import com.ola.ordering.GrevlexOrdering;
import com.ola.ordering.GrlexOrdering;
import com.ola.ordering.LexOrdering;
import com.ola.ordering.MonomialOrdering;
import com.ola.structures.Monomial;
import com.ola.structures.Polynomial;
import com.ola.structures.PolynomialRing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * AST visitor that transforms a list of {@link AstNode} instances into
 * {@link Polynomial} objects defined over a configurable {@link PolynomialRing}.
 *
 * <p>Supports field selection ({@code Real}, {@code Complex}, {@code Rational}, {@code GaloisField}),
 * monomial representation (dense/sparse), variable declaration, and monomial orderings
 * (lexicographic, graded lexicographic, graded reverse lexicographic).</p>
 *
 * <p>Configuration nodes are visited first to initialize the polynomial ring.
 * If no variable list is explicitly declared, variables are inferred from the polynomials.</p>
 *
 * <h3>Usage</h3>
 * <pre>{@code
 * PolynomialGenerator generator = new PolynomialGenerator();
 * List<Polynomial<Numeric>> polys = generator.generate(ast);
 * }</pre>
 *
 * @see AstNode
 * @see Polynomial
 * @see PolynomialRing
 */
public class PolynomialGenerator implements AstNode.Visitor<Void> {
    private NumericType numericType = NumericType.Real;
    private int modulo;
    private String[] variables;
    private PolynomialRing ring;
    private List<Polynomial<Numeric>> polynomials;
    private MonomialType monomialType = MonomialType.DENSE;
    private MonomialOrdering<Numeric> ordering = new GrevlexOrdering<>();

    /**
     * Converts a list of AST nodes into a list of {@link Polynomial} instances.
     *
     * <p>The method processes the input in two passes:
     * <ol>
     *   <li>Visits all non-polynomial configuration nodes to set up the numeric field,
     *       variables, monomial type, and ordering.</li>
     *   <li>Builds the polynomial ring, inferring variables if none were declared.</li>
     *   <li>Visits all polynomial nodes to generate fully-typed {@link Polynomial} objects.</li>
     * </ol>
     *
     * @param ast the list of AST nodes representing polynomial expressions and configuration
     * @return list of {@link Polynomial} objects created from the AST
     */
    public List<Polynomial<Numeric>> generate(List<AstNode> ast) {
        variables = new String[0];
        polynomials = new ArrayList<>();

        // We try to define the configuration first, so we visit those nodes in priority
        for (var node : ast) {
            if (!(node instanceof PolynomialNode)) {
                node.accept(this);
            }
        }

        // We build the ring, if no variables were given we try to infer them from the nodes
        buildRing(ast);

        // Then, we visit the polynomials nodes
        for (var node : ast) {
            if (node instanceof PolynomialNode) {
                node.accept(this);
            }
        }

        return polynomials;
    }

    @Override
    public Void visitOrderingConfigurationNode(OrderingConfigurationNode node) {
        ordering = switch (node.getOrderingType()) {
            case LEX -> new LexOrdering<>();
            case GRLEX -> new GrlexOrdering<>();
            case null, default -> new GrevlexOrdering<>();
        };

        return null;
    }

    @Override
    public Void visitFieldConfigurationNode(FieldConfigurationNode node) {
        if (node.getElementsType() == NumericType.Complex) {
            numericType = NumericType.Complex;
        } else if (node.getElementsType() == NumericType.Real) {
            numericType = NumericType.Real;
        } else if (node.getElementsType() == NumericType.Rational) {
            numericType = NumericType.Rational;
        } else {
            numericType = NumericType.GaloisField;
            modulo = node.getModulo();
        }

        return null;
    }

    @Override
    public Void visitVariablesConfigurationNode(VariablesConfigurationNode node) {
        variables = node.getVariables().toArray(new String[0]);
        return null;
    }

    @Override
    public Void visitMonomialConfigurationNode(MonomialConfigurationNode node) {
        monomialType = node.getMonomialType();
        return null;
    }

    @Override
    public Void visitPolynomialNode(PolynomialNode node) {
        var monomials = new ArrayList<Monomial<Numeric>>();
        Numeric constantSum = null;
        for (var monomial : node.getMonomials()) {
            var coefficient = NumericUtils.tryAssign(monomial.first(), numericType, modulo);
            if (monomial.second().isEmpty()) {
                if (constantSum == null) {
                    constantSum = coefficient;
                } else {
                    constantSum = constantSum.add(coefficient);
                }
            } else {
                monomials.add(ring.createMonomial(coefficient, monomial.second(), monomialType));
            }
        }

        // If there was any constant term, add it at the end
        if (constantSum != null) {
            monomials.add(ring.createMonomial(constantSum, Map.of(), monomialType));
        }

        polynomials.add(ring.createPolynomial(monomials, ordering));
        return null;
    }


    private void buildRing(List<AstNode> nodes) {
        if (variables.length == 0) {
            inferIndeterminates(nodes);
        }

        if (numericType == NumericType.GaloisField) {
            ring = new PolynomialRing(GaloisFieldElement.class, variables);
        } else if (numericType == NumericType.Rational) {
            ring = new PolynomialRing(Rational.class, variables);
        } else if (numericType == NumericType.Real) {
            ring = new PolynomialRing(Real.class, variables);
        } else {
            ring = new PolynomialRing(Complex.class, variables);
        }
    }

    private void inferIndeterminates(List<AstNode> nodes) {
        var indeterminates = new HashSet<String>();
        for (var node : nodes) {
            if (node instanceof PolynomialNode polynomialNode) {
                for (var monomial : polynomialNode.getMonomials()) {
                    indeterminates.addAll(monomial.second().keySet());
                }
            }
        }

        this.variables = indeterminates.stream().sorted().toArray(String[]::new);
    }
}
