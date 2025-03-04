package com.github.tr4k41s.misc.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import com.github.tr4k41s.misc.hud.MaskTimerHUD;

public class MiscConfig extends Config{
    @Switch(name = "Mask Timers")
    public static boolean MaskTimers = false;

    @HUD(name = "Mask Timers", category = "HUD")
    public MaskTimerHUD maskTimerHUD = new MaskTimerHUD();

    public MiscConfig() {
        super(new Mod("misc", ModType.SKYBLOCK), "misc.json");
        initialize();
    }
}
