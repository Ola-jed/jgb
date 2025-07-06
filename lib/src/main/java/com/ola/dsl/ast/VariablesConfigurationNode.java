package com.ola.dsl.ast;

import java.util.List;

/**
 * AST node declaring the variable names used in polynomial expressions.
 * <p>
 * This declaration is global within a DSL block.
 * No assumptions are made about uniqueness or sorting—these are enforced at DSL parsing time.
 */
public class VariablesConfigurationNode extends AstNode {
    private final List<String> variables;

    /**
     * Constructs a node specifying the set of symbolic variables in use.
     *
     * @param variables ordered list of variable names, e.g., {@code List.of("x", "y", "z")}
     */
    public VariablesConfigurationNode(List<String> variables) {
        this.variables = variables;
    }

    /**
     * Accepts a visitor implementing the {@link Visitor} interface.
     *
     * @param visitor visitor instance
     * @param <T>     return type of the visitor’s operation
     * @return result from {@code visitor.visitVariablesConfigurationNode(this)}
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitVariablesConfigurationNode(this);
    }

    /**
     * Returns the list of declared variable names.
     *
     * @return list of identifiers in declared order
     */
    public List<String> getVariables() {
        return variables;
    }

    /**
     * String representation used in diagnostics and logs.
     *
     * @return readable description of declared variables
     */
    @Override
    public String toString() {
        return "VariablesConfigurationNode[variables=%s]".formatted(String.join(", ", variables));
    }
}
