package com.dipo33.chatcalc.calc.element;

import com.dipo33.chatcalc.calc.NumberValue;

import java.util.Objects;

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

    public NumberValue evaluate(NumberValue a, NumberValue b) {
        return switch (getValue()) {
            case ADDITION -> a.add(b);
            case SUBTRACTION -> a.subtract(b);
            case MULTIPLICATION -> a.multiply(b);
            case DIVISION -> a.divide(b);
            case MODULO -> a.modulo(b);
            case POWER -> a.power(b);
            case NEGATION -> throw new IllegalStateException("Negation should be handled by the parser");
        };
    }

    public int getPrecedence() {
        return switch (getValue()) {
            case ADDITION, SUBTRACTION -> 6;
            case MULTIPLICATION, DIVISION, MODULO -> 7;
            case POWER -> 8;
            case NEGATION -> 10;
        };
    }

    public boolean isLeftAssociative() {
        return switch (getValue()) {
            case ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, MODULO, NEGATION -> true;
            case POWER -> false;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final FormulaOperator that)) {
            return false;
        }
        return this.type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    public enum OperatorType {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION,
        POWER,
        MODULO,
        NEGATION
    }
}
