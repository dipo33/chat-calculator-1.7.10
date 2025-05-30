package com.dipo33.chatcalc.calc.element;

import java.util.Objects;

public class FormulaBracket implements IFormulaElement {

    private final Type type;

    public FormulaBracket(boolean isLeft) {
        type = isLeft ? Type.LEFT_BRACKET : Type.RIGHT_BRACKET;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final FormulaBracket that)) {
            return false;
        }
        return this.type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
