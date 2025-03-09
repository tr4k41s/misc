package com.github.tr4k41s.misc.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;

import java.nio.charset.StandardCharsets;

public class ChatUtils {
    public static void sendModMessage(IChatComponent message) {
        assert Minecraft.getMinecraft().thePlayer != null;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§8[§bm.sc§8] " + message));
    }

    public static void sendCleanMessage(IChatComponent message) {
        assert Minecraft.getMinecraft().thePlayer != null;
        Minecraft.getMinecraft().thePlayer.addChatMessage(message);
        MinecraftForge.EVENT_BUS.post(new ClientChatReceivedEvent((byte) 1, message));
    }
}
