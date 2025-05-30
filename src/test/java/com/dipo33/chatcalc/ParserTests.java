package com.dipo33.chatcalc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dipo33.chatcalc.calc.Parser;
import com.dipo33.chatcalc.calc.RationalNumber;
import com.dipo33.chatcalc.calc.element.FormulaBracket;
import com.dipo33.chatcalc.calc.element.FormulaNumber;
import com.dipo33.chatcalc.calc.element.FormulaOperator;
import com.dipo33.chatcalc.calc.element.IFormulaElement;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTests {
    @Test
    @DisplayName("Parse rational number")
    public void parseRationalNumber() {
        RationalNumber expected = new RationalNumber(7);
        RationalNumber actual = RationalNumber.fromString("7");
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Parse rational number with dot")
    public void parseRationalNumberWithDot() {
        RationalNumber expected = new RationalNumber(741, 100);
        RationalNumber actual = RationalNumber.fromString("7.41");
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Parse rational number with two dots")
    public void parseRationalNumberWithTwoDots() {
        assertThrows(NumberFormatException.class, () -> RationalNumber.fromString("7.41.4"));
    }

    @Test
    @DisplayName("Parse rational number with nothing after dot")
    public void parseRationalNumberWithNothingAfterDot() {
        assertThrows(NumberFormatException.class, () -> RationalNumber.fromString("7."));
    }

    @Test
    @DisplayName("Parse rational number with nothing before dot")
    public void parseRationalNumberWithNothingBeforeDot() {
        assertThrows(NumberFormatException.class, () -> RationalNumber.fromString(".4"));
    }

    @Test
    @DisplayName("Parse just number")
    public void parseJustNumber() {
        List<IFormulaElement> expected = Arrays.asList(
            new FormulaNumber(new RationalNumber(7))
        );
        List<IFormulaElement> elements = Parser.parse("7");
        assertEquals(expected, elements);
    }

    @Test
    @DisplayName("Parse just operator")
    public void parseJustOperator() {
        List<IFormulaElement> expected = Arrays.asList(
            new FormulaOperator(FormulaOperator.OperatorType.ADDITION)
        );
        List<IFormulaElement> elements = Parser.parse("+");
        assertEquals(expected, elements);
    }

    @Test
    @DisplayName("Parse simple formula")
    public void parseSimpleFormula() {
        List<IFormulaElement> expected = Arrays.asList(
            new FormulaNumber(new RationalNumber(7)),
            new FormulaOperator(FormulaOperator.OperatorType.ADDITION),
            new FormulaNumber(new RationalNumber(4))
        );
        List<IFormulaElement> elements = Parser.parse("7+4");
        assertEquals(expected, elements);
    }

    @Test
    @DisplayName("Parse complex formula")
    public void parseComplexFormula() {
        List<IFormulaElement> expected = Arrays.asList(
            new FormulaOperator(FormulaOperator.OperatorType.NEGATION),
            new FormulaNumber(new RationalNumber(7)),
            new FormulaOperator(FormulaOperator.OperatorType.POWER),
            new FormulaNumber(new RationalNumber(4)),
            new FormulaOperator(FormulaOperator.OperatorType.MULTIPLICATION),
            new FormulaBracket(true),
            new FormulaNumber(new RationalNumber(1)),
            new FormulaOperator(FormulaOperator.OperatorType.SUBTRACTION),
            new FormulaNumber(new RationalNumber(2)),
            new FormulaBracket(false)
        );
        List<IFormulaElement> elements = Parser.parse("-7^4*(1-2)");
        assertEquals(expected, elements);
    }
}
