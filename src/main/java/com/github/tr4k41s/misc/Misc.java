package com.github.tr4k41s.misc;


import com.github.tr4k41s.misc.config.MiscConfig;
import com.github.tr4k41s.misc.features.MaskTimers;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import static com.github.tr4k41s.misc.Misc.MOD_ID;

@Mod(modid = "misc", useMetadata=true)
public class Misc {
    public static final String MOD_ID = "misc";
    public static MiscConfig config;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        config = new MiscConfig();
        MinecraftForge.EVENT_BUS.register(new MaskTimers());
    }

    public static final Minecraft mc = Minecraft. getMinecraft();
}
