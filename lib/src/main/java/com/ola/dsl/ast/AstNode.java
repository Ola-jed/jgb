package com.ola.dsl.ast;

public abstract class AstNode {
    public interface Visitor<T> {
        T visitOrderingConfigurationNode(OrderingConfigurationNode node);

        T visitFieldConfigurationNode(FieldConfigurationNode node);

        T visitVariablesConfigurationNode(VariablesConfigurationNode node);

        T visitMonomialConfigurationNode(MonomialConfigurationNode node);

        T visitPolynomialNode(PolynomialNode node);
    }

    public abstract <T> T accept(Visitor<T> visitor);
}