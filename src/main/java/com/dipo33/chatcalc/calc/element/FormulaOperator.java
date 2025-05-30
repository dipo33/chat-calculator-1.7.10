package com.dipo33.chatcalc.calc.element;

import com.dipo33.chatcalc.calc.RationalNumber;

public class FormulaOperator implements IFormulaElement {

    private final OperatorType type;

    public FormulaOperator(OperatorType type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return Type.OPERATOR;
    }

    public OperatorType getValue() {
        return type;
    }

    public RationalNumber evaluate(RationalNumber a, RationalNumber b) {
        switch (getValue()) {
            case ADDITION:
                return a.add(b);
            case SUBTRACTION:
                return a.subtract(b);
            case MULTIPLICATION:
                return a.multiply(b);
            case DIVISION:
                return a.divide(b);
            case POWER:
                return a.power(b);
            default:
                throw new EnumConstantNotPresentException(OperatorType.class, getValue().name());
        }
    }

    public int getPrecedence() {
        switch (getValue()) {
            case ADDITION:
            case SUBTRACTION:
                return 6;
            case MULTIPLICATION:
            case DIVISION:
                return 7;
            case POWER:
                return 8;
            case NEGATION:
                return 10;
            default:
                throw new EnumConstantNotPresentException(OperatorType.class, getValue().name());
        }
    }

    public boolean isLeftAssociative() {
        switch (getValue()) {
            case ADDITION:
            case SUBTRACTION:
            case MULTIPLICATION:
            case DIVISION:
            case NEGATION:
                return true;
            case POWER:
                return false;
            default:
                throw new EnumConstantNotPresentException(OperatorType.class, getValue().name());
        }
    }

    public enum OperatorType {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION,
        POWER,
        NEGATION
    }
}
