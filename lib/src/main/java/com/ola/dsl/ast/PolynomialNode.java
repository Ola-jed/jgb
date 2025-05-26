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

    @Override
    public String toString() {
        var sb = new StringBuilder("PolynomialNode[");
        for (var i = 0; i < monomials.size(); i++) {
            var monomial = monomials.get(i);
            var coefficient = monomial.first();
            var variables = monomial.second();
            var monomialString = new StringBuilder();

            // Handle coefficient formatting
            boolean hasVariables = !variables.isEmpty();
            String coefficientString = coefficient.toString();
            if (coefficientString.endsWith(".0")) {
                coefficientString = coefficientString.substring(0, coefficientString.length() - 2);
            }

            var showCoefficient = !coefficientString.equals("1") || !hasVariables;
            if (showCoefficient) {
                monomialString.append(coefficientString);
            }

            if (hasVariables) {
                if (showCoefficient) {
                    monomialString.append(" * ");
                }

                var count = 0;
                for (var entry : variables.entrySet()) {
                    if (count++ > 0) {
                        monomialString.append(" * ");
                    }

                    monomialString.append(entry.getKey());
                    if (entry.getValue() != 1) {
                        monomialString.append("^").append(entry.getValue());
                    }
                }
            }

            sb.append(monomialString);
            if (i < monomials.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
