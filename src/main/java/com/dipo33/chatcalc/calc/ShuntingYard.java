package com.dipo33.chatcalc.calc;

import com.dipo33.chatcalc.calc.element.FormulaFunction;
import com.dipo33.chatcalc.calc.element.FormulaNumber;
import com.dipo33.chatcalc.calc.element.FormulaOperator;
import com.dipo33.chatcalc.calc.element.IFormulaElement;
import com.dipo33.chatcalc.calc.exception.MissingOperandException;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ShuntingYard {

    public static NumberValue lastResult = null;

    public static Queue<IFormulaElement> shuntingYard(List<IFormulaElement> elements) {
        Queue<IFormulaElement> outputQueue = new LinkedList<>();
        Deque<IFormulaElement> operatorStack = new ArrayDeque<>();

        for (IFormulaElement token : elements) {
            switch (token.getType()) {
                case NUMBER:
                    outputQueue.add(token);
                    break;
                case OPERATOR:
                    FormulaOperator o1 = (FormulaOperator) token;
                    while (!operatorStack.isEmpty() && operatorStack.peek().getType() == IFormulaElement.Type.OPERATOR) {
                        FormulaOperator o2 = (FormulaOperator) operatorStack.peek();
                        if (o2.getPrecedence() > o1.getPrecedence() || (o2.getPrecedence() == o1.getPrecedence() && o1.isLeftAssociative())) {
                            outputQueue.add(operatorStack.pop());
                        } else {
                            break;
                        }
                    }

                    operatorStack.push(token);
                    break;
                case LEFT_BRACKET, FUNCTION:
                    operatorStack.push(token);
                    break;
                case RIGHT_BRACKET:
                    while (!operatorStack.isEmpty() && operatorStack.peek().getType() != IFormulaElement.Type.LEFT_BRACKET) {
                        outputQueue.add(operatorStack.pop());
                    }
                    if (operatorStack.isEmpty()) {
                        throw new RuntimeException("Missing a left bracket");
                    }
                    operatorStack.pop();

                    if (!operatorStack.isEmpty() && operatorStack.peek().getType() == IFormulaElement.Type.FUNCTION) {
                        outputQueue.add(operatorStack.pop());
                    }
                    break;
            }
        }

        while (!operatorStack.isEmpty()) {
            IFormulaElement element = operatorStack.pop();
            if (element.getType() == IFormulaElement.Type.LEFT_BRACKET) {
                throw new RuntimeException("Missing a right bracket");
            }
            outputQueue.add(element);
        }

        return outputQueue;
    }

    public static NumberValue evaluatePrefix(Queue<IFormulaElement> queue) throws MissingOperandException {
        Deque<NumberValue> stack = new ArrayDeque<>();
        while (!queue.isEmpty()) {
            IFormulaElement token = queue.remove();
            if (token.getType() == IFormulaElement.Type.NUMBER) {
                stack.push(((FormulaNumber) token).getValue());
            } else if (token.getType() == IFormulaElement.Type.OPERATOR) {
                FormulaOperator op = (FormulaOperator) token;
                if (stack.isEmpty()) {
                    throw new MissingOperandException();
                }
                NumberValue a = stack.pop();
                if (op.getValue() == FormulaOperator.OperatorType.NEGATION) { // todo: move to evaluate
                    stack.push(a.multiply(new RationalNumber(-1)));
                } else {
                    if (stack.isEmpty()) {
                        throw new MissingOperandException();
                    }
                    NumberValue b = stack.pop();
                    stack.push(op.evaluate(b, a));
                }
            } else if (token.getType() == IFormulaElement.Type.FUNCTION) {
                FormulaFunction fun = (FormulaFunction) token;
                stack.push(fun.evaluate(stack));
            } else {
                throw new RuntimeException("Unknown error, should not happen");
            }
        }

        lastResult = stack.pop();
        if (lastResult.isInteger()) {
            lastResult = new RationalNumber(lastResult.asInteger(), BigInteger.ONE);
        }

        return lastResult;
    }
}
