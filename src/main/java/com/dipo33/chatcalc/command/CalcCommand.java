package com.dipo33.chatcalc.command;

import com.dipo33.chatcalc.calc.NumberValue;
import com.dipo33.chatcalc.calc.Parser;
import com.dipo33.chatcalc.calc.ShuntingYard;
import com.dipo33.chatcalc.calc.element.IFormulaElement;
import com.dipo33.chatcalc.calc.exception.MissingOperandException;

import java.math.BigInteger;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CalcCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "calc";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "poop";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            String formula = String.join("", args);
            sender.addChatMessage(new ChatComponentText("§e§l---------------------------------------------"));
            sender.addChatMessage(new ChatComponentText("§b§lFormula: §f" + formula));

            List<IFormulaElement> elements = Parser.parse(formula);
            NumberValue result = ShuntingYard.evaluatePrefix(ShuntingYard.shuntingYard(elements)).displayRound();
            if (!result.isInteger()) {
                sender.addChatMessage(new ChatComponentText("§a§lFraction: §f" + result.asFractionString()));
            }
            sender.addChatMessage(new ChatComponentText("§a§lDecimal: §f" + result.asDecimalString(44)));

            if (result.isInteger() && result.asInteger().compareTo(BigInteger.ZERO) > 0) {
                sender.addChatMessage(new ChatComponentText("§a§lStacks: §f" + result.asStackString()));
                sender.addChatMessage(new ChatComponentText("§a§lFluid: §f" + result.asFluidString()));
                if (result.asInteger().compareTo(BigInteger.valueOf(630720000000L)) <= 0) {
                    sender.addChatMessage(new ChatComponentText("§a§lTime: §f" + result.asTimeString()));
                    sender.addChatMessage(new ChatComponentText("§a§lTick Time: §f" + result.asTickTimeString()));
                }
            }
        } catch (MissingOperandException e) {
            sender.addChatMessage(new ChatComponentText("§c§lError: §fMissing operand"));
        } catch (Exception e) {
            sender.addChatMessage(new ChatComponentText("§c§lError: §f" + (e.getMessage() == null ? e.getClass().toString() : e.getMessage())));
        }
    }
}
