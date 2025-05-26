package com.ola.dsl.ast;

import com.ola.enums.OrderingType;

public class OrderingConfigurationNode extends AstNode {
    private final OrderingType orderingType;

    public OrderingConfigurationNode(OrderingType orderingType) {
        this.orderingType = orderingType;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitOrderingConfigurationNode(this);
    }

    public OrderingType getOrderingType() {
        return orderingType;
    }

    @Override
    public String toString() {
        return "OrderingConfigurationNode[orderingType=%s]".formatted(orderingType.name());
    }
}
