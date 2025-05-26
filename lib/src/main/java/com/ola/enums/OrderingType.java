package com.ola.enums;

import com.ola.dsl.tokens.TokenType;

public enum OrderingType {
    LEX,
    GRLEX,
    GREVLEX;

    public static OrderingType fromTokenType(TokenType tokenType) {
        return switch (tokenType) {
            case TokenType.LEX -> LEX;
            case TokenType.GRLEX -> GRLEX;
            case TokenType.GREVLEX -> GREVLEX;
            default -> null;
        };
    }
}
