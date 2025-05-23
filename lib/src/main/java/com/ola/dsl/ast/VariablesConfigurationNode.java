package com.ola.dsl.ast;

import java.util.List;

public class VariablesConfigurationNode extends AstNode {
    private final List<String> variables;

    public VariablesConfigurationNode(List<String> variables) {
        this.variables = variables;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitVariablesConfigurationNode(this);
    }

    public List<String> getVariables() {
        return variables;
    }

    @Override
    public String toString() {
        return "VariablesConfigurationNode[variables=%s]".formatted(String.join(", ", variables));
    }
}
