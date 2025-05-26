package com.ola.enums;

import com.ola.dsl.tokens.TokenType;

public enum NumericType {
    GaloisField,
    Rational,
    Real,
    Complex;

    public static NumericType fromTokenType(TokenType tokenType) {
        return switch (tokenType) {
            case TokenType.R -> Real;
            case TokenType.C -> Complex;
            case TokenType.Q -> Rational;
            case TokenType.GF -> GaloisField;
            default -> null;
        };
    }
}
