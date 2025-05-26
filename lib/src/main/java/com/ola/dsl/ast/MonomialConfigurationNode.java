package com.ola.dsl.ast;


import com.ola.enums.MonomialType;

public class MonomialConfigurationNode extends AstNode {
    private final MonomialType monomialType;

    public MonomialConfigurationNode(MonomialType monomialType) {
        this.monomialType = monomialType;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitMonomialConfigurationNode(this);
    }

    public MonomialType getMonomialType() {
        return monomialType;
    }

    @Override
    public String toString() {
        return "MonomialConfigurationNode[monomialType=%s]".formatted(monomialType.name());
    }
}
