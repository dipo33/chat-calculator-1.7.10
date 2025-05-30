package com.dipo33.chatcalc.calc.element;

import com.dipo33.chatcalc.calc.RationalNumber;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final FormulaNumber that)) {
            return false;
        }
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
