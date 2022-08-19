package io.github.dipo33.chatcalc.command;

import io.github.dipo33.chatcalc.calc.Parser;
import io.github.dipo33.chatcalc.calc.RationalNumber;
import io.github.dipo33.chatcalc.calc.ShuntingYard;
import io.github.dipo33.chatcalc.calc.element.IFormulaElement;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.EmptyStackException;
import java.util.List;

public class CalcCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "c";
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
            sender.addChatMessage(new ChatComponentText("§lFormula: §f" + formula));

            List<IFormulaElement> elements = Parser.parse(formula);
            RationalNumber result = ShuntingYard.evaluatePrefix(ShuntingYard.shuntingYard(elements));
            if (!result.isInteger())
                sender.addChatMessage(new ChatComponentText("§lFraction: §f" + result.asFractionString()));
            sender.addChatMessage(new ChatComponentText("§lDecimal: §f" + result.asDecimalString(50)));

            if (result.isInteger()) {
                sender.addChatMessage(new ChatComponentText("§lStacks: §f" + result.asStackString()));
                sender.addChatMessage(new ChatComponentText("§lFluid: §f" + result.asFluidString()));
            }
        } catch (EmptyStackException e) {
            sender.addChatMessage(new ChatComponentText("§c§lError: §fInvalid formula"));
        } catch (Exception e) {
            sender.addChatMessage(new ChatComponentText("§c§lError: §f" + (e.getMessage() == null ? e.getClass().toString() : e.getMessage())));
        }
    }
}
