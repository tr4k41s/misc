package com.github.tr4k41s.misc.hud;

import cc.polyfrost.oneconfig.hud.TextHud;
import cc.polyfrost.oneconfig.config.core.OneColor;
import com.github.tr4k41s.misc.features.MaskTimers;
import com.github.tr4k41s.misc.config.MiscConfig;

import java.util.List;

public class MaskTimerHUD extends TextHud {

    public MaskTimerHUD() {
        super(true, 20, 30, 1.0F, false, false, 0.0F, 0.0F, 0.0F, new OneColor(0, 0, 0), false, 0.0F, new OneColor(0, 0, 0));
    }

    @Override
    protected void getLines(List<String> lines, boolean example) {
        if (!MiscConfig.MaskTimers) return;

        if (example) {
            lines.add("§bBonzo's Mask: §f4.90s");
            lines.add("§bSpirit Mask: §f12.85s");
            lines.add("§bPhoenix Pet: §f32.45s");
            return;
        }

        formatText(lines, "Bonzo's Mask", MaskTimers.pop[0]);
        formatText(lines, "Spirit Mask", MaskTimers.pop[1]);
        formatText(lines, "Phoenix Pet", MaskTimers.pop[2]);
    }

    private void formatText(List<String> lines, String name, long ticks) {
        if (ticks > 0) {
            float seconds = ticks / 20.0f;
            lines.add(String.format("§b%s: §f%.2fs", name, seconds));
        }
    }
}
