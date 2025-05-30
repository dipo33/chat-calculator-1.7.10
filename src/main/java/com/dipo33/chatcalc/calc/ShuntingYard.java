package com.dipo33.chatcalc.calc;

import com.dipo33.chatcalc.calc.element.FormulaNumber;
import com.dipo33.chatcalc.calc.element.FormulaOperator;
import com.dipo33.chatcalc.calc.element.IFormulaElement;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class ShuntingYard {

    public static RationalNumber lastResult = null;

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
                    while (!operatorStack.isEmpty() && operatorStack.lastElement().getType() != IFormulaElement.Type.LEFT_BRACKET) {
                        outputQueue.add(operatorStack.pop());
                    }
                    if (operatorStack.isEmpty())
                        throw new RuntimeException("Missing a left bracket");
                    operatorStack.pop();
            }
        }

        while (!operatorStack.empty()) {
            IFormulaElement element = operatorStack.pop();
            if (element.getType() == IFormulaElement.Type.LEFT_BRACKET)
                throw new RuntimeException("Missing a right bracket");
            outputQueue.add(element);
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
                if (((FormulaOperator) token).getValue() == FormulaOperator.OperatorType.NEGATION) {
                    stack.push(a.multiply(new RationalNumber(-1)));
                } else {
                    RationalNumber b = stack.pop();
                    stack.push(op.evaluate(b, a));
                }
            } else {
                throw new RuntimeException("Unknown error, should not happen");
            }
        }

        lastResult = stack.pop();
        return lastResult;
    }
}
