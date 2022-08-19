package io.github.dipo33.chatcalc.calc;

import java.math.BigInteger;
import java.util.Objects;

public class RationalNumber {

    private final BigInteger numerator;
    private final BigInteger denominator;

    public RationalNumber(String number) {
        this(parseNumerator(number), parseDenominator(number));
    }

    public RationalNumber(int number) {
        this.numerator = BigInteger.valueOf(number);
        this.denominator = BigInteger.ONE;
    }

    public RationalNumber(BigInteger numerator, BigInteger denominator) {
        BigInteger simplifier = gcd(numerator, denominator);
        if (denominator.compareTo(BigInteger.ZERO) < 0) simplifier = simplifier.negate();
        this.numerator = numerator.divide(simplifier);
        this.denominator = denominator.divide(simplifier);
    }

    public RationalNumber(int numerator, int denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public RationalNumber add(RationalNumber other) {
        BigInteger new_denominator = lcm(denominator, other.denominator);
        BigInteger new_numerator = new_denominator.divide(denominator).multiply(numerator).add(
            new_denominator.divide(other.denominator).multiply(other.numerator)
        );

        return new RationalNumber(new_numerator, new_denominator);
    }

    public RationalNumber subtract(RationalNumber other) {
        BigInteger new_denominator = lcm(denominator, other.denominator);
        BigInteger new_numerator = new_denominator.divide(denominator).multiply(numerator).subtract(
            new_denominator.divide(other.denominator).multiply(other.numerator)
        );

        return new RationalNumber(new_numerator, new_denominator);
    }

    public RationalNumber multiply(RationalNumber other) {
        return new RationalNumber(numerator.multiply(other.numerator), denominator.multiply(other.denominator));
    }

    public RationalNumber divide(RationalNumber other) {
        return multiply(new RationalNumber(other.denominator, other.numerator));
    }

    public RationalNumber power(RationalNumber other) {
        if (!other.isInteger()) throw new ArithmeticException("Exponent can't be a decimal number");

        int pow = other.asInteger().intValueExact();
        if (pow > 512)
            throw new ArithmeticException("Exponent can't be higher than 512");

        return new RationalNumber(numerator.pow(pow), denominator.pow(pow));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RationalNumber that = (RationalNumber) o;
        return numerator.equals(that.numerator) && denominator.equals(that.denominator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    public boolean isInteger() {
        return numerator.mod(denominator).equals(BigInteger.ZERO);
    }

    public BigInteger asInteger() {
        return numerator.divide(denominator);
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    private static BigInteger lcm(BigInteger a, BigInteger b) {
        return a.abs().multiply(b.abs()).divide(gcd(a, b));
    }

    private static BigInteger gcd(BigInteger a, BigInteger b) {
        return recursiveGcd(a.abs(), b.abs());
    }

    private static BigInteger recursiveGcd(BigInteger a, BigInteger b) {
        return b.equals(BigInteger.ZERO) ? a : recursiveGcd(b, a.mod(b));
    }

    private static BigInteger parseNumerator(String num) {
        return new BigInteger(num.replaceAll("\\.", ""));
    }

    private static BigInteger parseDenominator(String num) {
        if (!num.contains(".")) return BigInteger.ONE;

        int decimalLength = num.length() - num.indexOf('.') - 1;
        return BigInteger.TEN.pow(decimalLength);
    }
}
