# Chat Calculator Mod

**Chat Calculator** is a Minecraft Forge mod for version `1.7.10` that allows players to perform advanced mathematical calculations directly through the in-game chat. It supports rational numbers, operator precedence, parentheses, and various custom output formats such as item stacks, fluid amounts, and in-game time representation.

---

## 🧠 Features

* Parse and evaluate complex math formulas using the command `/calc`
* Supports:
  * Addition, Subtraction, Multiplication, Division, Power, Negation
  * Parentheses for grouping
  * Rational numbers with fractional precision
  * Re-use of last result using `x`
* Displays result in multiple formats:
  * Decimal
  * Fraction
  * Item stacks (`64`)
  * Fluid amounts (`144 mB`)
  * Real-world time (`years, days, hours...`)
  * Minecraft tick time

---

## 🚀 Getting Started

### Requirements

* **Minecraft**: 1.7.10
* **Forge**: Compatible Forge Mod Loader (FML) for 1.7.10
* **Java**: Java 17+

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/dipo33/chat-calculator-1.7.10.git
   cd chat-calculator-1.7.10
   ```

2. Build the mod:

   ```bash
   ./gradlew build
   ```

3. Place the generated `.jar` file from `build/libs` into your Minecraft `mods/` folder.

---

## 🧮 Usage

Use the command `/calc <expression>` in chat to evaluate a mathematical formula.

### Examples

```
/calc 5 + 4 * (3 - 1)
/calc x ^ 2 + 7    # Uses the result from the previous calculation as "x"
/calc 1000000 / 64
```

### Output

```
---------------------------------------------
Formula: 5+4*(3-1)
Fraction: 13/1
Decimal: 13
Stacks: 0x64 + 13
Fluid: 0x144mB + 13mB
Time: 13 seconds
Tick Time: 0 ticks
```

---

## 🧪 Testing

Run unit tests using:

```bash
./gradlew test
```

Test class: `MathTests.java`

---

## 📄 License

This project is licensed under the MIT License – see the [LICENSE](./LICENSE) file for details.

---

## 🙌 Credits

Created by [dipo33](https://github.com/dipo33).
Based on the Forge 1.7.10 modding API.
