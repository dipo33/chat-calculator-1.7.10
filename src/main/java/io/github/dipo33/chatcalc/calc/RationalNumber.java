package io.github.dipo33.chatcalc.calc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
        return denominator.equals(BigInteger.ONE);
    }

    public BigInteger asInteger() {
        return numerator.divide(denominator);
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    public String asFractionString() {
        return toString();
    }

    public String asDecimalString(int maxDecimalPlaces) {
        BigDecimal decimalNumerator = new BigDecimal(numerator.toString());
        BigDecimal decimalDenominator = new BigDecimal(denominator.toString());

        String res = decimalNumerator.divide(decimalDenominator, maxDecimalPlaces, RoundingMode.DOWN).toPlainString();
        res = res.contains(".") ? res.replaceAll("0*$","").replaceAll("\\.$","") : res;
        if (res.contains(".")) {
            String intPart = res.substring(0, res.indexOf("."));
            res = addDelimitingCommas(intPart) + res.substring(res.indexOf("."));
        } else {
            res = addDelimitingCommas(res);
        }

        return res;
    }

    private String addDelimitingCommas(String str) {
        StringBuilder delimited = new StringBuilder();
        for (int i = str.length() - 1; i >= 0; i -= 3) {
            delimited.insert(0, ",");
            delimited.insert(0, str.substring(Math.max(0, i - 2), i + 1));
        }

        return delimited.substring(0, delimited.length() - 1);
    }

    public String asStackString() {
        BigInteger value = asInteger();
        BigInteger stacks = value.divide(BigInteger.valueOf(64));
        BigInteger leftover = value.mod(BigInteger.valueOf(64));

        return addDelimitingCommas(stacks.toString()) + "x64 + " + leftover;
    }

    public String asFluidString() {
        BigInteger value = asInteger();
        BigInteger stacks = value.divide(BigInteger.valueOf(144));
        BigInteger leftover = value.mod(BigInteger.valueOf(144));

        return addDelimitingCommas(stacks.toString()) + "x144mB + " + leftover + "mB";
    }

    public String asTimeString() {
        BigInteger value = asInteger();

        BigInteger seconds = value.mod(BigInteger.valueOf(60));
        value = value.divide(BigInteger.valueOf(60));

        BigInteger minutes = value.mod(BigInteger.valueOf(60));
        value = value.divide(BigInteger.valueOf(60));

        BigInteger hours = value.mod(BigInteger.valueOf(24));
        value = value.divide(BigInteger.valueOf(24));

        BigInteger days = value.mod(BigInteger.valueOf(365));
        BigInteger years = value.divide(BigInteger.valueOf(365));

        StringBuilder result = new StringBuilder();
        if (years.compareTo(BigInteger.ZERO) > 0)
            result.append(years).append(" years, ");
        if (days.compareTo(BigInteger.ZERO) > 0)
            result.append(days).append(" days, ");
        if (hours.compareTo(BigInteger.ZERO) > 0)
            result.append(hours).append(" hours, ");
        if (minutes.compareTo(BigInteger.ZERO) > 0)
            result.append(minutes).append(" minutes, ");
        if (seconds.compareTo(BigInteger.ZERO) > 0)
            result.append(seconds).append(" seconds");

        if (result.charAt(result.length() -1) == ' ')
            return result.substring(0, result.length() - 2);

        return result.toString();
    }

    public String asTickTimeString() {
        BigInteger value = asInteger();

        BigInteger ticks = value.mod(BigInteger.valueOf(20));
        value = value.divide(BigInteger.valueOf(20));

        if (value.equals(BigInteger.ZERO))
            return ticks + " ticks";

        String rest = new RationalNumber(value, BigInteger.ONE).asTimeString();
        if (ticks.equals(BigInteger.ZERO))
            return rest;
        return rest + ", " + ticks + " ticks";
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
