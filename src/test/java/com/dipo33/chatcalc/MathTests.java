package com.dipo33.chatcalc;

import com.dipo33.chatcalc.calc.RationalNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MathTests {

    @Test
    @DisplayName("GCD Test")
    public void testGCD() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = RationalNumber.class.getDeclaredMethod("gcd", BigInteger.class, BigInteger.class);
        method.setAccessible(true);
        BigInteger res = (BigInteger) method.invoke(null, BigInteger.valueOf(50), BigInteger.valueOf(40));

        assertEquals(0, BigInteger.valueOf(10).compareTo(res));
    }

    @Test
    @DisplayName("GCD Negative Test")
    public void testNegativeGCD() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = RationalNumber.class.getDeclaredMethod("gcd", BigInteger.class, BigInteger.class);
        method.setAccessible(true);
        BigInteger res = (BigInteger) method.invoke(null, BigInteger.valueOf(-50), BigInteger.valueOf(35));

        assertEquals(0, BigInteger.valueOf(5).compareTo(res));
    }

    @Test
    @DisplayName("GCD Negative Reversed Test")
    public void testNegativeReversedGCD() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = RationalNumber.class.getDeclaredMethod("gcd", BigInteger.class, BigInteger.class);
        method.setAccessible(true);
        BigInteger res = (BigInteger) method.invoke(null, BigInteger.valueOf(500), BigInteger.valueOf(-35));

        assertEquals(0, BigInteger.valueOf(5).compareTo(res));
    }

    @Test
    @DisplayName("Creation of positive Fraction with positive fields")
    public void createPositiveFraction() {
        RationalNumber number = new RationalNumber(70, 45);
        assertEquals(BigInteger.valueOf(14), number.getNumerator());
        assertEquals(BigInteger.valueOf(9), number.getDenominator());
    }

    @Test
    @DisplayName("Creation of positive Fraction with negative fields")
    public void createPositiveFractionWithNegativeFields() {
        RationalNumber number = new RationalNumber(-5, -8);
        assertEquals(BigInteger.valueOf(5), number.getNumerator());
        assertEquals(BigInteger.valueOf(8), number.getDenominator());
    }

    @Test
    @DisplayName("Creation of positive Fraction with negative numerator")
    public void createPositiveFractionWithNegativeNumerator() {
        RationalNumber number = new RationalNumber(-5, 8);
        assertEquals(BigInteger.valueOf(-5), number.getNumerator());
        assertEquals(BigInteger.valueOf(8), number.getDenominator());
    }

    @Test
    @DisplayName("Creation of positive Fraction with negative denominator")
    public void createPositiveFractionWithNegativeDenominator() {
        RationalNumber number = new RationalNumber(5, -8);
        assertEquals(BigInteger.valueOf(-5), number.getNumerator());
        assertEquals(BigInteger.valueOf(8), number.getDenominator());
    }

    @Test
    @DisplayName("Creation of Fraction from positive decimal String")
    public void createFractionFromPositiveDecimalString() {
        RationalNumber number = new RationalNumber("8.25");
        assertEquals(BigInteger.valueOf(33), number.getNumerator());
        assertEquals(BigInteger.valueOf(4), number.getDenominator());
    }

    @Test
    @DisplayName("Creation of Fraction from positive decimal String 2")
    public void createFractionFromPositiveDecimalString2() {
        RationalNumber number = new RationalNumber("17.138");
        assertEquals(BigInteger.valueOf(8569), number.getNumerator());
        assertEquals(BigInteger.valueOf(500), number.getDenominator());
    }

    @Test
    @DisplayName("Creation of Fraction from negative decimal String")
    public void createFractionFromNegativeDecimalString() {
        RationalNumber number = new RationalNumber("-3.33");
        assertEquals(BigInteger.valueOf(-333), number.getNumerator());
        assertEquals(BigInteger.valueOf(100), number.getDenominator());
    }

    @Test
    @DisplayName("Multiplication of positive Fractions")
    public void multiplyPositiveFractions() {
        RationalNumber number = new RationalNumber(8, 4).multiply(new RationalNumber(7, 2));
        assertEquals(BigInteger.valueOf(7), number.getNumerator());
        assertEquals(BigInteger.valueOf(1), number.getDenominator());
    }

    @Test
    @DisplayName("Division of positive Fractions")
    public void dividePositiveFractions() {
        RationalNumber number = new RationalNumber(9, 1).divide(new RationalNumber(4, 1));
        assertEquals(BigInteger.valueOf(9), number.getNumerator());
        assertEquals(BigInteger.valueOf(4), number.getDenominator());
    }

    @Test
    @DisplayName("Multiplication of negative Fractions")
    public void multiplyNegativeFractions() {
        RationalNumber number = new RationalNumber(-2, 1).multiply(new RationalNumber(1, -2));
        assertEquals(BigInteger.valueOf(1), number.getNumerator());
        assertEquals(BigInteger.valueOf(1), number.getDenominator());
    }

    @Test
    @DisplayName("Multiplication of negative and positive Fractions")
    public void multiplyNegativeAndPositiveFractions() {
        RationalNumber number = new RationalNumber(2, 1).multiply(new RationalNumber(1, -2));
        assertEquals(BigInteger.valueOf(-1), number.getNumerator());
        assertEquals(BigInteger.valueOf(1), number.getDenominator());
    }

    @Test
    @DisplayName("Addition of positive Fractions")
    public void sumNegativeFractions() {
        RationalNumber number = new RationalNumber(1, 3).add(new RationalNumber(1, 4));
        assertEquals(BigInteger.valueOf(7), number.getNumerator());
        assertEquals(BigInteger.valueOf(12), number.getDenominator());
    }

    @Test
    @DisplayName("Subtraction of positive Fractions")
    public void subtractNegativeFractions() {
        RationalNumber number = new RationalNumber(1, 3).subtract(new RationalNumber(1, 2));
        assertEquals(BigInteger.valueOf(-1), number.getNumerator());
        assertEquals(BigInteger.valueOf(6), number.getDenominator());
    }

    @Test
    @DisplayName("Power to Fraction")
    public void powerToFraction() {
        assertThrows(RuntimeException.class, () -> new RationalNumber(2, 1).power(new RationalNumber(1, 2)));
    }

    @Test
    @DisplayName("Power to Long")
    public void powerToLong() {
        assertThrows(ArithmeticException.class, () -> new RationalNumber(1, 1).power(new RationalNumber(new BigInteger("8000000000"), BigInteger.ONE)));
    }

    @Test
    @DisplayName("Power to Positive Exponent")
    public void powerToPositiveExponent() {
        RationalNumber number = new RationalNumber(2, 1).power(new RationalNumber(8, 1));
        assertEquals(BigInteger.valueOf(256), number.getNumerator());
        assertEquals(BigInteger.valueOf(1), number.getDenominator());
    }
}
