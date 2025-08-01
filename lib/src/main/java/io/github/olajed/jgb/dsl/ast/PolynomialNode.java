package io.github.olajed.jgb.dsl.ast;

import io.github.olajed.jgb.number.Numeric;
import io.github.olajed.jgb.utils.Pair;

import java.util.List;
import java.util.Map;

/**
 * AST node representing a symbolic polynomial in the DSL.
 * <p>
 * Each monomial is stored as a Pair :
 * the first component is the coefficient; the second maps variable names to their
 * non‑negative exponents. A missing variable implies exponent 0; an empty map therefore
 * denotes a constant term.
 * <p>
 * The structure is intentionally agnostic of ordering and sparsity,
 * delegating those concerns to surrounding {@code OrderingConfigurationNode} and
 * {@code MonomialConfigurationNode} settings.
 */
public class PolynomialNode extends AstNode {
    private final List<Pair<Numeric, Map<String, Integer>>> monomials;

    /**
     * Constructs a polynomial node from its monomial list.
     *
     * @param monomials list of {@code (coefficient, exponentMap)} pairs; the list may be
     *                  unsorted—ordering is handled elsewhere.
     *                  The constructor does <em>not</em> copy the list for speed;
     *                  pass an immutable list if isolation is required.
     */
    public PolynomialNode(List<Pair<Numeric, Map<String, Integer>>> monomials) {
        this.monomials = monomials;
    }

    /**
     * Visitor hook.
     *
     * @param visitor visitor implementing domain‑specific logic
     * @param <T>     return type produced by the visitor
     * @return result from {@code visitor.visitPolynomialNode(this)}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitPolynomialNode(this);
    }

    /**
     * Returns the underlying monomial list.
     *
     * @return list of coefficient/exponent‑map pairs (live view)
     */
    public List<Pair<Numeric, Map<String, Integer>>> getMonomials() {
        return monomials;
    }

    /**
     * Compact human‑readable rendering used for diagnostics.
     * Not intended for canonical output—respect the DSL printer for that.
     */
    @Override
    public String toString() {
        // See source for pretty‑printing logic.
        var sb = new StringBuilder("PolynomialNode[");
        for (var i = 0; i < monomials.size(); i++) {
            var monomial = monomials.get(i);
            var coefficient = monomial.first();
            var vars = monomial.second();
            var hasVars = !vars.isEmpty();
            var coefficientString = coefficient.toString().replaceAll("\\.0$", "");
            var showCoefficient = !coefficientString.equals("1") || !hasVars;

            if (showCoefficient) sb.append(coefficientString);
            if (hasVars) {
                if (showCoefficient) sb.append(" * ");
                int k = 0;
                for (var e : vars.entrySet()) {
                    if (k++ > 0) sb.append(" * ");
                    sb.append(e.getKey());
                    if (e.getValue() != 1) sb.append("^").append(e.getValue());
                }
            }
            if (i < monomials.size() - 1) sb.append(", ");
        }

        return sb.append(']').toString();
    }
}
