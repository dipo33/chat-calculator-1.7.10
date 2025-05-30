package com.dipo33.chatcalc.calc.element;

public interface IFormulaElement {

    Type getType();

    enum Type {
        NUMBER,
        OPERATOR,
        LEFT_BRACKET,
        RIGHT_BRACKET
    }
}
