package com.ola.dsl.ast;

import com.ola.number.Numeric;
import com.ola.ordering.MonomialOrdering;

public class OrderingConfigurationNode extends AstNode {
    private final MonomialOrdering<? extends Numeric> orderingType;

    public OrderingConfigurationNode(MonomialOrdering<? extends Numeric> orderingType) {
        this.orderingType = orderingType;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitOrderingConfigurationNode(this);
    }

    public MonomialOrdering<? extends Numeric> getOrderingType() {
        return orderingType;
    }
}
