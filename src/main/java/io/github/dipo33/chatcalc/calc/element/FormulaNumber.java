package io.github.dipo33.chatcalc.calc.element;

import io.github.dipo33.chatcalc.calc.RationalNumber;

public class FormulaNumber implements IFormulaElement {

    private final RationalNumber number;

    public FormulaNumber(RationalNumber number) {
        this.number = number;
    }

    @Override
    public Type getType() {
        return Type.NUMBER;
    }

    public RationalNumber getValue() {
        return number;
    }
}
