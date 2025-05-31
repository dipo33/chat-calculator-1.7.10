package com.dipo33.chatcalc.calc.element;

public interface IFormulaElement {

    Type getType();

    enum Type {
        NUMBER,
        OPERATOR,
        FUNCTION,
        LEFT_BRACKET,
        RIGHT_BRACKET
    }
}
