package com.dipo33.chatcalc.calc.element;

import com.dipo33.chatcalc.calc.NumberValue;

import java.util.Deque;

public class FormulaFunction implements IFormulaElement {

    private final FunctionType type;

    public FormulaFunction(final FunctionType type) {
        this.type = type;
    }

    public static FormulaFunction from(final String fun) {
        return switch (fun.toLowerCase()) {
            case "abs" -> new FormulaFunction(FunctionType.ABSOLUTE_VALUE);
            default -> throw new IllegalArgumentException("Unknown function: " + fun);
        };
    }

    public FunctionType getValue() {
        return type;
    }

    public NumberValue evaluate(Deque<NumberValue> stack) {
        return switch (getValue()) {
            case ABSOLUTE_VALUE -> stack.pop().abs();
        };
    }

    @Override
    public Type getType() {
        return Type.FUNCTION;
    }

    public enum FunctionType {
        ABSOLUTE_VALUE
    }
}
