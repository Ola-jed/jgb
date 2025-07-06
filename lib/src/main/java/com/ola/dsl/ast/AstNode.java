package com.ola.dsl.ast;

/**
 * Abstract base class representing a node in the algebraic DSL AST.
 * <p>
 * Subclasses define specific DSL constructs such as polynomial expressions,
 * variable declarations, and configuration blocks.
 */
public abstract class AstNode {

    /**
     * Visitor interface for traversing and processing {@link AstNode} instances.
     * <p>
     * Implements the Visitor design pattern to allow type-specific logic
     * without requiring instanceof checks or casting.
     *
     * @param <T> Return type of the visitor methods.
     */
    public interface Visitor<T> {
        /**
         * Visit a node configuring the monomial ordering.
         * @param node The ordering configuration node.
         * @return Visitor-defined result.
         */
        T visitOrderingConfigurationNode(OrderingConfigurationNode node);

        /**
         * Visit a node configuring the algebraic field (e.g., finite fields).
         * @param node The field configuration node.
         * @return Visitor-defined result.
         */
        T visitFieldConfigurationNode(FieldConfigurationNode node);

        /**
         * Visit a node defining variable names and scope.
         * @param node The variables configuration node.
         * @return Visitor-defined result.
         */
        T visitVariablesConfigurationNode(VariablesConfigurationNode node);

        /**
         * Visit a node configuring monomial formatting or representation.
         * @param node The monomial configuration node.
         * @return Visitor-defined result.
         */
        T visitMonomialConfigurationNode(MonomialConfigurationNode node);

        /**
         * Visit a node representing a polynomial expression.
         * @param node The polynomial node.
         * @return Visitor-defined result.
         */
        T visitPolynomialNode(PolynomialNode node);
    }

    /**
     * Accept a visitor that processes this AST node.
     *
     * @param visitor Visitor to accept.
     * @param <T>     The return type of the visitor's operation.
     * @return Result of the visitor's visit method.
     */
    public abstract <T> T accept(Visitor<T> visitor);
}
