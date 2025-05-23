package com.ola.dsl.ast;

import com.ola.dsl.tokens.TokenType;

public class FieldConfigurationNode extends AstNode {
    private final TokenType elementsType;
    private final int maybeModulo;

    public FieldConfigurationNode(TokenType elementsType) {
        this.elementsType = elementsType;
        this.maybeModulo = 0;
    }

    public FieldConfigurationNode(TokenType elementsType, int maybeModulo) {
        this.elementsType = elementsType;
        this.maybeModulo = maybeModulo;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitFieldConfigurationNode(this);
    }

    public TokenType getElementsType() {
        return elementsType;
    }

    public int getModulo() {
        return maybeModulo;
    }

    @Override
    public String toString() {
        var result = "FieldConfigurationNode[elementsType=%s".formatted(elementsType.name());
        if (elementsType == TokenType.GF) {
            result += ", Modulo =" + maybeModulo;
        }

        result += "]";
        return result;
    }
}
