package com.dipo33.chatcalc.calc;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface NumberValue {

    NumberValue add(NumberValue other);

    NumberValue subtract(NumberValue other);

    NumberValue multiply(NumberValue other);

    NumberValue divide(NumberValue other);

    NumberValue power(NumberValue other);

    boolean isInteger();

    BigInteger asInteger();

    String toString();

    String asFractionString();

    String asDecimalString(int maxDecimalPlaces);

    String asStackString();

    String asFluidString();

    String asTimeString();

    String asTickTimeString();

    NumberValue displayRound();

    BigDecimal asBigDecimal();
}
