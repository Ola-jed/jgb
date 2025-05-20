package com.ola.dsl.tokens;

public record Token(TokenType type, Object value, int line) {
}