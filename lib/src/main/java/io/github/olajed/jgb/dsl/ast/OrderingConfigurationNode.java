package io.github.olajed.jgb.dsl.ast;

import io.github.olajed.jgb.enums.OrderingType;

/**
 * AST node specifying the monomial ordering strategy
 * for polynomial systems in the DSL.
 *
 * @see OrderingType for supported orderings (e.g., Lex, GradedLex, RevLex)
 */
public class OrderingConfigurationNode extends AstNode {
    private final OrderingType orderingType;

    /**
     * Constructs an ordering configuration node with the given ordering strategy.
     *
     * @param orderingType the monomial ordering type to be used
     */
    public OrderingConfigurationNode(OrderingType orderingType) {
        this.orderingType = orderingType;
    }

    /**
     * Accepts a visitor that operates on this AST node.
     *
     * @param visitor the visitor
     * @param <T>     return type of the visitor's operation
     * @return result from {@code visitor.visitOrderingConfigurationNode(this)}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitOrderingConfigurationNode(this);
    }

    /**
     * Returns the configured monomial ordering strategy.
     *
     * @return the ordering type
     */
    public OrderingType getOrderingType() {
        return orderingType;
    }

    /**
     * String representation for diagnostics and testing.
     *
     * @return human-readable description of this configuration
     */
    @Override
    public String toString() {
        return "OrderingConfigurationNode[orderingType=%s]".formatted(orderingType.name());
    }
}
