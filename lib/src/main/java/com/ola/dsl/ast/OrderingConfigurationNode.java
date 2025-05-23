package com.ola.dsl.ast;

import com.ola.dsl.tokens.TokenType;

public class OrderingConfigurationNode extends AstNode {
    private final TokenType tokenType;

    public OrderingConfigurationNode(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitOrderingConfigurationNode(this);
    }

    public TokenType getOrderingType() {
        return tokenType;
    }

    @Override
    public String toString() {
        return "OrderingConfigurationNode[orderingType=%s]".formatted(tokenType.name());
    }
}
