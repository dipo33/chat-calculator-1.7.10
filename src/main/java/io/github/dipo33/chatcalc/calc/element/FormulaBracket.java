package io.github.dipo33.chatcalc.calc.element;

public class FormulaBracket implements IFormulaElement {

    private final Type type;

    public FormulaBracket(boolean isLeft) {
        type = isLeft ? Type.LEFT_BRACKET : Type.RIGHT_BRACKET;
    }

    @Override
    public Type getType() {
        return type;
    }
}
