package com.github.tr4k41s.misc.commands;

import com.github.tr4k41s.misc.utils.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import static com.github.tr4k41s.misc.Misc.mc;

public class SimulateMessage extends CommandBase{
    @Override
    public String getCommandName() {
        return "sim";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + this.getCommandName();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            final String message = String.join(" ", args).replaceAll("&", "§");
            ChatUtils.sendCleanMessage(new ChatComponentText(message));
        } catch (NumberFormatException e) {
            ChatUtils.sendModMessage(new ChatComponentText("missing message"));
            return;
        }
    }
}
