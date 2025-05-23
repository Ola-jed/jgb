package com.ola.dsl.ast;

import com.ola.dsl.tokens.TokenType;

public class MonomialConfigurationNode extends AstNode {
    private final TokenType monomialType;

    public MonomialConfigurationNode(TokenType monomialType) {
        this.monomialType = monomialType;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitMonomialConfigurationNode(this);
    }

    public TokenType getMonomialType() {
        return monomialType;
    }

    @Override
    public String toString() {
        return "MonomialConfigurationNode[monomialType=%s]".formatted(monomialType.name());
    }
}
