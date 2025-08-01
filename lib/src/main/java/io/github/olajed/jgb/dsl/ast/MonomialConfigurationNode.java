package io.github.olajed.jgb.dsl.ast;

import io.github.olajed.jgb.enums.MonomialType;

/**
 * AST node that configures how individual monomials are interpreted
 * and displayed within the algebraic DSL.
 *
 * @see MonomialType for the available policies
 */
public class MonomialConfigurationNode extends AstNode {
    private final MonomialType monomialType;

    /**
     * Creates a configuration node with the requested monomial policy.
     *
     * @param monomialType policy governing ordering / storage of monomials
     */
    public MonomialConfigurationNode(MonomialType monomialType) {
        this.monomialType = monomialType;
    }

    /**
     * Visitor hook.
     *
     * @param visitor visitor implementing domain‑specific behaviour
     * @param <T>     result type produced by the visitor
     * @return result from {@code visitor.visitMonomialConfigurationNode(this)}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitMonomialConfigurationNode(this);
    }

    /**
     * Returns the configured {@link MonomialType} used for representing monomials.
     *
     * @return the configured {@link MonomialType}
     */
    public MonomialType getMonomialType() {
        return monomialType;
    }

    /** Usable for diagnostics and unit‑test assertions. */
    @Override
    public String toString() {
        return "MonomialConfigurationNode[monomialType=%s]".formatted(monomialType.name());
    }
}
