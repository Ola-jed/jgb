package com.ola.dsl.ast;

import com.ola.enums.NumericType;

/**
 * AST node representing the configuration of a base field
 * in the algebraic DSL.
 * <p>
 * Supports various numeric types, including finite fields (e.g., GF(p)),
 * by specifying the element type and, optionally, a modulus.
 */
public class FieldConfigurationNode extends AstNode {
    private final NumericType elementsType;
    private final int maybeModulo;

    /**
     * Constructs a field configuration node for non-modular numeric types.
     *
     * @param elementsType The numeric type (e.g., {@code Rational}, {@code Real}).
     */
    public FieldConfigurationNode(NumericType elementsType) {
        this.elementsType = elementsType;
        this.maybeModulo = 0;
    }

    /**
     * Constructs a field configuration node with an explicit modulus,
     * typically for Galois fields.
     *
     * @param elementsType The numeric type (e.g., {@code GaloisField}).
     * @param maybeModulo  The modulus for modular arithmetic, e.g. p in GF(p).
     */
    public FieldConfigurationNode(NumericType elementsType, int maybeModulo) {
        this.elementsType = elementsType;
        this.maybeModulo = maybeModulo;
    }

    /**
     * Accepts a visitor implementing the {@link Visitor} interface.
     *
     * @param visitor Visitor instance.
     * @param <T>     Return type of the visitor.
     * @return Result from {@code visitor.visitFieldConfigurationNode(this)}.
     */
    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitFieldConfigurationNode(this);
    }

    /**
     * Returns the numeric type of field elements.
     *
     * @return Element type (e.g., {@code Rational}, {@code GaloisField}).
     */
    public NumericType getElementsType() {
        return elementsType;
    }

    /**
     * Returns the modulus for modular arithmetic, if applicable.
     *
     * @return Modulus if defined (nonzero), otherwise 0.
     */
    public int getModulo() {
        return maybeModulo;
    }

    /**
     * String representation for debugging and diagnostics.
     *
     * @return Human-readable string describing the field configuration.
     */
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
