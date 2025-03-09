package com.github.tr4k41s.misc.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import com.github.tr4k41s.misc.hud.MaskTimerHUD;
import com.github.tr4k41s.misc.utils.SoundUtils;
import net.minecraft.util.Vec3;

import static com.github.tr4k41s.misc.Misc.mc;

public class MiscConfig extends Config{
    @HUD(name = "Mask Timers", category = "Mask Timers")
    public MaskTimerHUD maskTimerHUD = new MaskTimerHUD();

    @Switch(
            name = "Enable sound on mask/pet available",
            category = "Mask Timers"
    )
    public static boolean MaskTimerSoundToggle = false;

    @Button(
            name = "Test sound",
            text = "Clickity Click",
            category = "Mask Timers"
    )
    Runnable runnable = () -> {
        String[] sounds = {"note.pling","mob.blaze.hit","fire.ignite","random.break","Custom"};
        String sound = MaskTimerSoundValue == 4 ? MaskTimerSound : sounds[MaskTimerSoundValue];
        SoundUtils.playSound(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), sound, MaskTimerSoundVolume, MaskTimerSoundPitch);
    };

    @Dropdown(
            name = "Sound",
            category = "Mask Timers",
            options = {"note.pling","mob.blaze.hit","fire.ignite","random.break","Custom"}
    )
    public static int MaskTimerSoundValue = 1;

    @Text(
            name = "Custom Sound",
            placeholder = "sound",
            category = "Mask Timers",
            secure = false, multiline = false
    )
    public static String MaskTimerSound = "";

    @Slider(
            name = "Volume",
            min = 0f, max = 100f,
            category = "Mask Timers"
    )
    public static float MaskTimerSoundVolume = 50f;

    @Slider(
            name = "Pitch",
            min = 0f, max = 2f,
            category = "Mask Timers"
    )
    public static float MaskTimerSoundPitch = 1f;

    @Switch(
            name = "Remove Hypixel ranks from chat",
            category = "Chat Formatter"
    )
    public static boolean removeRanks = false;

    @Switch(
            name = "Shorten chat tab prefixes",
            category = "Chat Formatter"
    )
    public static boolean changePrefix = false;

    @Switch(
            name = "Remove Skyblock Levels from chat (keeps the prefix emblem)",
            category = "Chat Formatter"
    )
    public static boolean removeLVL = false;

    @Switch(
            name = "Remove chat spam",
            category = "Chat Formatter"
    )
    public static boolean removeText = false;

    public MiscConfig() {
        super(new Mod("misc", ModType.SKYBLOCK), "misc.json");
        initialize();
    }
}
