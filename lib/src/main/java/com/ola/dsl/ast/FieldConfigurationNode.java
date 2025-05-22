package com.ola.dsl.ast;

import com.ola.number.Numeric;

public class FieldConfigurationNode extends AstNode {
    private final Class<? extends Numeric> elementsType;
    private final int maybeModulo;

    public FieldConfigurationNode(Class<? extends Numeric> elementsType) {
        this.elementsType = elementsType;
        this.maybeModulo = 0;
    }

    public FieldConfigurationNode(Class<? extends Numeric> elementsType, int maybeModulo) {
        this.elementsType = elementsType;
        this.maybeModulo = maybeModulo;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitFieldConfigurationNode(this);
    }

    public Class<? extends Numeric> getElementsType() {
        return elementsType;
    }

    public int getMaybeModulo() {
        return maybeModulo;
    }
}
