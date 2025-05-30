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
        return switch (getValue()) {
            case ADDITION -> a.add(b);
            case SUBTRACTION -> a.subtract(b);
            case MULTIPLICATION -> a.multiply(b);
            case DIVISION -> a.divide(b);
            case POWER -> a.power(b);
            default -> throw new EnumConstantNotPresentException(OperatorType.class, getValue().name());
        };
    }

    public int getPrecedence() {
        return switch (getValue()) {
            case ADDITION, SUBTRACTION -> 6;
            case MULTIPLICATION, DIVISION -> 7;
            case POWER -> 8;
            case NEGATION -> 10;
        };
    }

    public boolean isLeftAssociative() {
        return switch (getValue()) {
            case ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, NEGATION -> true;
            case POWER -> false;
        };
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
