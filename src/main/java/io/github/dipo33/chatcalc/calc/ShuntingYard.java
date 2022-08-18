package io.github.dipo33.chatcalc.calc;

import io.github.dipo33.chatcalc.calc.element.FormulaNumber;
import io.github.dipo33.chatcalc.calc.element.FormulaOperator;
import io.github.dipo33.chatcalc.calc.element.IFormulaElement;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class ShuntingYard {

    public static Queue<IFormulaElement> shuntingYard(List<IFormulaElement> elements) {
        Queue<IFormulaElement> outputQueue = new LinkedList<>();
        Stack<IFormulaElement> operatorStack = new Stack<>();


        for (IFormulaElement token : elements) {
            switch (token.getType()) {
                case NUMBER:
                    outputQueue.add(token);
                    break;
                case OPERATOR:
                    FormulaOperator o1 = (FormulaOperator) token;
                    while (!operatorStack.empty() && operatorStack.lastElement().getType() == IFormulaElement.Type.OPERATOR) {
                        FormulaOperator o2 = (FormulaOperator) operatorStack.lastElement();
                        if (o2.getPrecedence() > o1.getPrecedence() || (o2.getPrecedence() == o1.getPrecedence() && o1.isLeftAssociative())) {
                            outputQueue.add(operatorStack.pop());
                        } else {
                            break;
                        }
                    }

                    operatorStack.push(token);
                    break;
                case LEFT_BRACKET:
                    operatorStack.add(token);
                    break;
                case RIGHT_BRACKET:
                    // TODO: check if there is bracket
                    while (operatorStack.lastElement().getType() != IFormulaElement.Type.LEFT_BRACKET) {
                        outputQueue.add(operatorStack.pop());
                    }
                    operatorStack.pop();
            }
        }

        while (!operatorStack.empty()) {
            outputQueue.add(operatorStack.pop());
        }

        return outputQueue;
    }

    public static RationalNumber evaluatePrefix(Queue<IFormulaElement> queue) {
        Stack<RationalNumber> stack = new Stack<>();
        while (!queue.isEmpty()) {
            IFormulaElement token = queue.remove();
            if (token.getType() == IFormulaElement.Type.NUMBER) {
                stack.push(((FormulaNumber) token).getValue());
            } else if (token.getType() == IFormulaElement.Type.OPERATOR) {
                FormulaOperator op = (FormulaOperator) token;
                RationalNumber a = stack.pop();
                RationalNumber b = stack.pop();
                stack.push(op.evaluate(b, a));
            } else {
                // TODO: SHOULD NOT HAPPEN
            }
        }

        assert stack.size() == 1;
        return stack.pop();
    }
}
