package com.dipo33.chatcalc.calc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;

public class RationalNumber implements NumberValue {

    private final BigInteger numerator;
    private final BigInteger denominator;

    public RationalNumber(int number) {
        this.numerator = BigInteger.valueOf(number);
        this.denominator = BigInteger.ONE;
    }

    public RationalNumber(BigInteger numerator, BigInteger denominator) {
        BigInteger simplifier = gcd(numerator, denominator);
        if (denominator.compareTo(BigInteger.ZERO) < 0) {
            simplifier = simplifier.negate();
        }
        this.numerator = numerator.divide(simplifier);
        this.denominator = denominator.divide(simplifier);
    }

    public RationalNumber(int numerator, int denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public static RationalNumber fromString(String number) {
        if (!validate(number)) {
            throw new NumberFormatException("Invalid number format");
        }
        return new RationalNumber(parseNumerator(number), parseDenominator(number));
    }

    public RationalNumber add(RationalNumber other) {
        BigInteger new_denominator = lcm(denominator, other.denominator);
        BigInteger new_numerator = new_denominator.divide(denominator).multiply(numerator).add(
            new_denominator.divide(other.denominator).multiply(other.numerator)
        );

        return new RationalNumber(new_numerator, new_denominator);
    }

    @Override
    public NumberValue add(final NumberValue other) {
        if (other instanceof final RationalNumber that) {
            return this.add(that);
        }

        return this.asIrrational().add(other);
    }

    public RationalNumber subtract(RationalNumber other) {
        BigInteger new_denominator = lcm(denominator, other.denominator);
        BigInteger new_numerator = new_denominator.divide(denominator).multiply(numerator).subtract(
            new_denominator.divide(other.denominator).multiply(other.numerator)
        );

        return new RationalNumber(new_numerator, new_denominator);
    }

    @Override
    public NumberValue subtract(final NumberValue other) {
        if (other instanceof final RationalNumber that) {
            return this.subtract(that);
        }

        return this.asIrrational().subtract(other);
    }

    public RationalNumber multiply(RationalNumber other) {
        return new RationalNumber(numerator.multiply(other.numerator), denominator.multiply(other.denominator));
    }

    @Override
    public NumberValue multiply(final NumberValue other) {
        if (other instanceof final RationalNumber that) {
            return this.multiply(that);
        }

        return this.asIrrational().multiply(other);
    }

    public RationalNumber divide(RationalNumber other) {
        return multiply(new RationalNumber(other.denominator, other.numerator));
    }

    @Override
    public NumberValue divide(final NumberValue other) {
        if (other instanceof final RationalNumber that) {
            return this.divide(that);
        }

        return this.asIrrational().divide(other);
    }

    public RationalNumber modulo(RationalNumber other) {
        var repeats = this.divide(other).floor();
        return this.subtract(repeats.multiply(other));
    }

    @Override
    public NumberValue modulo(final NumberValue other) {
        if (other instanceof final RationalNumber that) {
            return this.modulo(that);
        }

        return this.asIrrational().modulo(other);
    }

    public NumberValue power(RationalNumber other) {
        if (!other.isInteger()) {
            return asIrrational().power(other);
        }

        int pow = other.asInteger().intValueExact();

        if (pow < 0) {
            return new RationalNumber(denominator, numerator).power(other.multiply(new RationalNumber(-1, 1)));
        }
        if (pow > 512) {
            throw new ArithmeticException("Exponent can't be higher than 512");
        }

        return new RationalNumber(numerator.pow(pow), denominator.pow(pow));
    }

    @Override
    public NumberValue power(final NumberValue other) {
        if (other instanceof final RationalNumber that) {
            return this.power(that);
        }

        return this.asIrrational().power(other);
    }

    @Override
    public NumberValue abs() {
        return new RationalNumber(numerator.abs(), denominator.abs());
    }

    @Override
    public RationalNumber floor() {
        return new RationalNumber(
            this.asBigDecimal().setScale(0, RoundingMode.FLOOR).toBigInteger(),
            BigInteger.ONE
        );
    }

    @Override
    public BigDecimal asBigDecimal() {
        return new BigDecimal(numerator).divide(new BigDecimal(denominator), 100, RoundingMode.HALF_UP);
    }

    public IrrationalNumber asIrrational() {
        return new IrrationalNumber(asBigDecimal());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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

    @Override
    public boolean isInteger() {
        return denominator.equals(BigInteger.ONE);
    }

    @Override
    public BigInteger asInteger() {
        return numerator.divide(denominator);
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    @Override
    public String asFractionString() {
        return toString();
    }

    @Override
    public String asDecimalString(int maxDecimalPlaces) {
        BigDecimal decimalNumerator = new BigDecimal(numerator.toString());
        BigDecimal decimalDenominator = new BigDecimal(denominator.toString());

        String res = decimalNumerator.divide(decimalDenominator, maxDecimalPlaces, RoundingMode.DOWN).toPlainString();
        res = res.contains(".") ? res.replaceAll("0*$", "").replaceAll("\\.$", "") : res;
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

    @Override
    public String asStackString() {
        BigInteger value = asInteger();
        BigInteger stacks = value.divide(BigInteger.valueOf(64));
        BigInteger leftover = value.mod(BigInteger.valueOf(64));

        return addDelimitingCommas(stacks.toString()) + "x64 + " + leftover;
    }

    @Override
    public String asFluidString() {
        BigInteger value = asInteger();
        BigInteger stacks = value.divide(BigInteger.valueOf(144));
        BigInteger leftover = value.mod(BigInteger.valueOf(144));

        return addDelimitingCommas(stacks.toString()) + "x144mB + " + leftover + "mB";
    }

    @Override
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
        if (years.compareTo(BigInteger.ZERO) > 0) {
            result.append(years).append(" years, ");
        }
        if (days.compareTo(BigInteger.ZERO) > 0) {
            result.append(days).append(" days, ");
        }
        if (hours.compareTo(BigInteger.ZERO) > 0) {
            result.append(hours).append(" hours, ");
        }
        if (minutes.compareTo(BigInteger.ZERO) > 0) {
            result.append(minutes).append(" minutes, ");
        }
        if (seconds.compareTo(BigInteger.ZERO) > 0) {
            result.append(seconds).append(" seconds");
        }

        if (result.charAt(result.length() - 1) == ' ') {
            return result.substring(0, result.length() - 2);
        }

        return result.toString();
    }

    @Override
    public String asTickTimeString() {
        BigInteger value = asInteger();

        BigInteger ticks = value.mod(BigInteger.valueOf(20));
        value = value.divide(BigInteger.valueOf(20));

        if (value.equals(BigInteger.ZERO)) {
            return ticks + " ticks";
        }

        String rest = new RationalNumber(value, BigInteger.ONE).asTimeString();
        if (ticks.equals(BigInteger.ZERO)) {
            return rest;
        }
        return rest + ", " + ticks + " ticks";
    }

    @Override
    public NumberValue displayRound() {
        return this;
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
        if (!num.contains(".")) {
            return BigInteger.ONE;
        }

        int decimalLength = num.length() - num.indexOf('.') - 1;
        return BigInteger.TEN.pow(decimalLength);
    }

    private static boolean validate(final String number) {
        return number.matches("^(-)?\\d+(\\.\\d+)?$");
    }
}
