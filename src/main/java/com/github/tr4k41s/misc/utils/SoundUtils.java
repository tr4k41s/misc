package com.github.tr4k41s.misc.utils;

import net.minecraft.util.Vec3;

import static com.github.tr4k41s.misc.Misc.mc;

public class SoundUtils {
    public static void playSound(Vec3 pos, String sound, float volume, float pitch) {
        if (mc.theWorld != null) {
            mc.theWorld.playSound((pos != null) ? pos.xCoord : mc.thePlayer.posX, (pos != null) ? pos.yCoord : mc.thePlayer.posY, (pos != null) ? pos.zCoord : mc.thePlayer.posZ, sound, volume, pitch, false);
        }
    }
}
