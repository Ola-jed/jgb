package com.ola.enums;

import com.ola.dsl.tokens.TokenType;

public enum MonomialType {
    DENSE,
    SPARSE;

    public static MonomialType fromTokenType(TokenType tokenType) {
        return switch (tokenType) {
            case TokenType.DENSE -> DENSE;
            case TokenType.SPARSE -> SPARSE;
            default -> null;
        };
    }
}
