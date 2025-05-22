package com.ola.dsl.parser;

import com.ola.dsl.ast.AstNode;
import com.ola.dsl.tokens.Token;

import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int current;
    // TODO : Implement this

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        current = 0;
    }

    public List<AstNode> parse() {
        return null;
    }
}
