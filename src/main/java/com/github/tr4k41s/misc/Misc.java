package com.github.tr4k41s.misc;


import com.github.tr4k41s.misc.commands.SimulateMessage;
import com.github.tr4k41s.misc.config.MiscConfig;
import com.github.tr4k41s.misc.features.MaskTimers;
import com.github.tr4k41s.misc.features.chat.RemoveRanks;
import com.github.tr4k41s.misc.hud.HelmetDisplay;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "misc", useMetadata=true)
public class Misc {
    public static final String MOD_ID = "misc";
    public static MiscConfig config;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        config = new MiscConfig();
        MinecraftForge.EVENT_BUS.register(new MaskTimers());
        MinecraftForge.EVENT_BUS.register(new HelmetDisplay());
        MinecraftForge.EVENT_BUS.register(new RemoveRanks());
        ClientCommandHandler.instance.registerCommand(new SimulateMessage());
    }

    public static final Minecraft mc = Minecraft. getMinecraft();
}
