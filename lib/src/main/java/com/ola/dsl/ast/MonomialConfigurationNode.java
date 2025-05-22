package com.ola.dsl.ast;

import com.ola.structures.Monomial;

public class MonomialConfigurationNode extends AstNode {
    private final Class<? extends Monomial<?>> monomialType;

    public MonomialConfigurationNode(Class<? extends Monomial<?>> monomialType) {
        this.monomialType = monomialType;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitMonomialConfigurationNode(this);
    }

    public Class<? extends Monomial<?>> getMonomialType() {
        return monomialType;
    }
}
