package com.ola.dsl.ast;

import com.ola.enums.NumericType;

public class FieldConfigurationNode extends AstNode {
    private final NumericType elementsType;
    private final int maybeModulo;

    public FieldConfigurationNode(NumericType elementsType) {
        this.elementsType = elementsType;
        this.maybeModulo = 0;
    }

    public FieldConfigurationNode(NumericType elementsType, int maybeModulo) {
        this.elementsType = elementsType;
        this.maybeModulo = maybeModulo;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitFieldConfigurationNode(this);
    }

    public NumericType getElementsType() {
        return elementsType;
    }

    public int getModulo() {
        return maybeModulo;
    }

    @Override
    public String toString() {
        var result = "FieldConfigurationNode[elementsType=%s".formatted(elementsType.name());
        if (elementsType == NumericType.GaloisField) {
            result += ", Modulo=" + maybeModulo;
        }

        result += "]";
        return result;
    }
}
