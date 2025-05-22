package com.ola.dsl.ast;

import com.ola.number.Numeric;
import com.ola.utils.Pair;

import java.util.List;
import java.util.Map;

public class PolynomialNode extends AstNode {
    private final List<Pair<Numeric, Map<String, Integer>>> monomials;

    public PolynomialNode(List<Pair<Numeric, Map<String, Integer>>> monomials) {
        this.monomials = monomials;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitPolynomialNode(this);
    }

    public List<Pair<Numeric, Map<String, Integer>>> getMonomials() {
        return monomials;
    }
}
