package com.dipo33.chatcalc.calc;

import com.dipo33.chatcalc.calc.element.FormulaBracket;
import com.dipo33.chatcalc.calc.element.FormulaNumber;
import com.dipo33.chatcalc.calc.element.FormulaOperator;
import com.dipo33.chatcalc.calc.element.IFormulaElement;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    public static List<IFormulaElement> parse(String formulaString) {
        formulaString = formulaString.replaceAll("\\s+", "");
        if (formulaString.isEmpty()) {
            throw new RuntimeException("No formula found");
        }

        List<IFormulaElement> elements = new ArrayList<>();

        int i = 0;
        while (i < formulaString.length()) {
            char c = formulaString.charAt(i);
            if (isNumber(c)) {
                int begin = i;
                while (i + 1 < formulaString.length() && isNumber(formulaString.charAt(i + 1))) {
                    ++i;
                }

                String number = formulaString.substring(begin, i + 1);
                elements.add(new FormulaNumber(RationalNumber.fromString(number)));
            } else {
                switch (c) {
                    case '+':
                        elements.add(new FormulaOperator(FormulaOperator.OperatorType.ADDITION));
                        break;
                    case '-':
                        if (i == 0 || formulaString.charAt(i - 1) == '(') {
                            elements.add(new FormulaOperator(FormulaOperator.OperatorType.NEGATION));
                        } else {
                            elements.add(new FormulaOperator(FormulaOperator.OperatorType.SUBTRACTION));
                        }
                        break;
                    case '*':
                        elements.add(new FormulaOperator(FormulaOperator.OperatorType.MULTIPLICATION));
                        break;
                    case '/':
                        elements.add(new FormulaOperator(FormulaOperator.OperatorType.DIVISION));
                        break;
                    case '%':
                        elements.add(new FormulaOperator(FormulaOperator.OperatorType.MODULO));
                        break;
                    case '^':
                        elements.add(new FormulaOperator(FormulaOperator.OperatorType.POWER));
                        break;
                    case '(':
                        elements.add(new FormulaBracket(true));
                        break;
                    case ')':
                        elements.add(new FormulaBracket(false));
                        break;
                    case 'x':
                        if (ShuntingYard.lastResult == null) {
                            throw new RuntimeException("You can't use 'x' before you do any computation");
                        }
                        elements.add(new FormulaNumber(ShuntingYard.lastResult));
                        break;
                    default:
                        throw new RuntimeException("Illegal character found in formula: " + c);
                }
            }

            ++i;
        }

        return elements;
    }

    private static boolean isNumber(char c) {
        return Character.isDigit(c) || c == '.';
    }
}
