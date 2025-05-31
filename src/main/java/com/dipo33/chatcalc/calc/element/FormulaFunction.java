package com.dipo33.chatcalc.calc.element;

import com.dipo33.chatcalc.calc.NumberValue;
import com.dipo33.chatcalc.calc.RationalNumber;

import java.util.Deque;

public class FormulaFunction implements IFormulaElement {

    private final FunctionType type;

    public FormulaFunction(final FunctionType type) {
        this.type = type;
    }

    public static FormulaFunction from(final String fun) {
        return switch (fun.toLowerCase()) {
            case "abs" -> new FormulaFunction(FunctionType.ABSOLUTE_VALUE);
            case "sgn" -> new FormulaFunction(FunctionType.SIGN);
            case "sqrt" -> new FormulaFunction(FunctionType.SQUARE_ROOT);
            case "root" -> new FormulaFunction(FunctionType.ROOT);
            case "floor" -> new FormulaFunction(FunctionType.FLOOR);
            case "ceil" -> new FormulaFunction(FunctionType.CEIL);
            case "round" -> new FormulaFunction(FunctionType.ROUND);
            case "fact" -> new FormulaFunction(FunctionType.FACTORIAL);
            default -> throw new IllegalArgumentException("Unknown function: " + fun);
        };
    }

    public FunctionType getValue() {
        return type;
    }

    public NumberValue evaluate(Deque<NumberValue> stack) {
        return switch (getValue()) {
            case ABSOLUTE_VALUE -> stack.pop().abs();
            case SIGN -> stack.pop().signum();
            case SQUARE_ROOT -> stack.pop().sqrt();
            case ROOT -> {
                NumberValue root = stack.pop();
                NumberValue a = stack.pop();
                yield a.power(root.power(new RationalNumber(-1)));
            }
            case FLOOR -> stack.pop().floor();
            case CEIL -> stack.pop().ceil();
            case ROUND -> {
                NumberValue precision = stack.pop();
                NumberValue a = stack.pop();
                yield a.round(precision);
            }
            case FACTORIAL -> stack.pop().fact();
        };
    }

    @Override
    public Type getType() {
        return Type.FUNCTION;
    }

    public enum FunctionType {
        ABSOLUTE_VALUE,
        SIGN,
        SQUARE_ROOT,
        ROOT,
        FLOOR,
        CEIL,
        ROUND,
        FACTORIAL
    }
}
