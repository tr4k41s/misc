package com.github.tr4k41s.misc.utils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.tr4k41s.misc.Misc.mc;

public class ScoreboardUtils {

    /**
     * @author Gabagooooooooooool
     * @version 2.0
     * @brief Condition Utilities
     */

    public static boolean inGame() {
        return (mc.thePlayer != null && mc.theWorld != null);
    }

    public static boolean inSkyblock() {
        if (!inGame()) return false;
        ScoreObjective scoreboardObj = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
        return scoreboardObj != null && (Utils.removeFormatting(scoreboardObj.getDisplayName()).contains("SKYBLOCK") || Utils.removeFormatting(scoreboardObj.getDisplayName()).contains("SKIBLOCK"));
    }

    public static boolean inCatacombs() {
        if (!inGame()) return false;
        return scoreboardContains("Catacombs");
    }

    /**
     * @author RoseGold
     * @brief Scoreboard Utilities
     */

    public static boolean scoreboardContains(String string) {
        for (String line : getScoreboard()) {
            if (Utils.removeFormatting(cleanSB(line)).contains(string)) {
                return true;
            }
        }
        return false;
    }

    public static String cleanSB(String scoreboard) {
        char[] nvString = Utils.removeFormatting(scoreboard).toCharArray();
        StringBuilder cleaned = new StringBuilder();

        for (char c : nvString) {
            if ((int) c > 20 && (int) c < 127) {
                cleaned.append(c);
            }
        }

        return cleaned.toString();
    }

    @SuppressWarnings({"ExecutionException", "IllegalArgumentException"})
    public static List<String> getScoreboard() {
        List<String> lines = new ArrayList<>();
        if (mc.theWorld == null) return lines;
        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        if (scoreboard == null) return lines;

        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) return lines;

        Collection<Score> scores = scoreboard.getSortedScores(objective);
        List<Score> list = scores.stream()
                .filter(input -> input != null && input.getPlayerName() != null && !input.getPlayerName()
                        .startsWith("#"))
                .collect(Collectors.toList());

        if (list.size() > 15) {
            scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
        } else {
            scores = list;
        }

        for (Score score : scores) {
            ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
            lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
        }

        return lines;
    }
}
