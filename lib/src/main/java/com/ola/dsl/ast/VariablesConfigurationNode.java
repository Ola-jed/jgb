package com.ola.dsl.ast;

public class VariablesConfigurationNode extends AstNode {
    private final String[] variables;

    public VariablesConfigurationNode(String[] variables) {
        this.variables = variables;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitVariablesConfigurationNode(this);
    }

    public String[] getVariables() {
        return variables;
    }
}
