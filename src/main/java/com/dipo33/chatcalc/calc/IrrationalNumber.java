package com.dipo33.chatcalc.calc;

import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class IrrationalNumber implements NumberValue {

    private final BigDecimal value;

    public IrrationalNumber(final BigDecimal value) {
        this.value = value;
    }

    @Override
    public NumberValue add(final NumberValue other) {
        return new IrrationalNumber(value.add(other.asBigDecimal()));
    }

    @Override
    public NumberValue subtract(final NumberValue other) {
        return new IrrationalNumber(value.subtract(other.asBigDecimal()));
    }

    @Override
    public NumberValue multiply(final NumberValue other) {
        return new IrrationalNumber(value.multiply(other.asBigDecimal()));
    }

    @Override
    public NumberValue divide(final NumberValue other) {
        return new IrrationalNumber(value.divide(other.asBigDecimal(), 100, RoundingMode.HALF_UP));
    }

    @Override
    public NumberValue modulo(final NumberValue other) {
        var repeats = this.divide(other).floor();
        return this.subtract(repeats.multiply(other));
    }

    @Override
    public NumberValue power(final NumberValue other) {
        return new IrrationalNumber(BigDecimalMath.pow(value, other.asBigDecimal(), new MathContext(100, RoundingMode.HALF_UP)));
    }

    @Override
    public NumberValue abs() {
        return new IrrationalNumber(value.abs());
    }

    @Override
    public NumberValue signum() {
        var comparison = value.compareTo(BigDecimal.ZERO);
        return new RationalNumber(BigInteger.valueOf(comparison), BigInteger.ONE);
    }

    @Override
    public NumberValue sqrt() {
        return power(new RationalNumber(1, 2));
    }

    @Override
    public RationalNumber floor() {
        return new RationalNumber(
            value.setScale(0, RoundingMode.FLOOR).toBigInteger(),
            BigInteger.ONE
        );
    }

    @Override
    public NumberValue ceil() {
        return new RationalNumber(
            value.setScale(0, RoundingMode.CEILING).toBigInteger(),
            BigInteger.ONE
        );
    }

    @Override
    public NumberValue round(final NumberValue precision) {
        if (!precision.isInteger()) {
            throw new ArithmeticException("Rounding with non-integer precision is not supported");
        }
        return new RationalNumber(
            value.setScale(precision.asInteger().intValueExact(), RoundingMode.HALF_UP).toBigInteger(),
            BigInteger.ONE
        );
    }

    @Override
    public NumberValue fact() {
        if (!isInteger()) {
            throw new ArithmeticException("Factorial can only be calculated for integers");
        }

        return asRational().fact();
    }

    @Override
    public boolean isInteger() {
        return this.value.stripTrailingZeros().scale() <= 0;
    }

    @Override
    public BigInteger asInteger() {
        return value.toBigInteger();
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }

    @Override
    public String asFractionString() {
        return "IRRATIONAL";
    }

    @Override
    public String asDecimalString(final int maxDecimalPlaces) {
        return value.round(new MathContext(maxDecimalPlaces, RoundingMode.DOWN)).toPlainString();
    }

    @Override
    public String asStackString() {
        return asRational().asStackString();
    }

    @Override
    public String asFluidString() {
        return asRational().asFluidString();
    }

    @Override
    public String asTimeString() {
        return asRational().asTimeString();
    }

    @Override
    public String asTickTimeString() {
        return asRational().asTickTimeString();
    }

    @Override
    public NumberValue displayRound() {
        return new IrrationalNumber(value.setScale(44, RoundingMode.DOWN).stripTrailingZeros());
    }

    @Override
    public BigDecimal asBigDecimal() {
        return value;
    }

    public RationalNumber asRational() {
        return new RationalNumber(asInteger(), BigInteger.ONE);
    }
}
