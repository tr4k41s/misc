package com.github.tr4k41s.misc.features.chat;

import com.github.tr4k41s.misc.config.MiscConfig;
import com.github.tr4k41s.misc.utils.ChatUtils;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveRanks {
    private static final Map<Pattern, String> rankReplace = new LinkedHashMap<>();
    private static final Map<Pattern, String> prefixReplace = new LinkedHashMap<>();
    private static final List<Pattern> removeLevel = new ArrayList<>();
    private static final List<Pattern> removeText = new ArrayList<>();
    private boolean altered = false;
    private final boolean mainToggle = (MiscConfig.removeRanks || MiscConfig.changePrefix || MiscConfig.removeLVL);

    static {
        rankReplace.put(Pattern.compile("(?:§.)*§.\\[VIP((?:§.)*\\+)?(?:§.)*]\\s"), "§r§a");
        rankReplace.put(Pattern.compile("(?:§.)*§.\\[MVP((?:§.)*\\+)?(?:§.)*]\\s"), "§r§b");
        rankReplace.put(Pattern.compile("(?:§.)*§.\\[MVP(?:§.)*\\+\\+(?:§.)*]\\s"), "§r§6");
        rankReplace.put(Pattern.compile("(?:§.)*§.\\[(?:§.)*YOUTUBE(?:§.)*]\\s"), "§r§c");
        prefixReplace.put(Pattern.compile("^(?:§.)*§.Party\\s(?:§.)*>"),"§r§9>");
        prefixReplace.put(Pattern.compile("^(?:§.)*§.Co-op\\s>"),"§r§b>");
        prefixReplace.put(Pattern.compile("^(?:§.)*§.Guild\\s>"),"§r§2>");
        prefixReplace.put(Pattern.compile("^(?:§.)*§.Friend\\s>"),"§r§a>");
        removeLevel.add(Pattern.compile("(?:§.)*§.\\[(?:§.)*§.\\d{1,3}(?:§.)*§.]\\s"));
        removeText.add(Pattern.compile("has obtained (.+)?"));
        removeText.add(Pattern.compile("\\[(Tank|Mage|Archer|Healer|Berserk)]"));
        removeText.add(Pattern.compile("RIGHT CLICK on (a|the) .+ door to open it\\.", Pattern.CASE_INSENSITIVE));
        removeText.add(Pattern.compile("(Also )?granted you", Pattern.CASE_INSENSITIVE));
        removeText.add(Pattern.compile("RARE DROP!( .+)?"));
        removeText.add(Pattern.compile("(A )?Blessing of .+( was picked up)?!", Pattern.CASE_INSENSITIVE));
        removeText.add(Pattern.compile("profile( ID)?:", Pattern.CASE_INSENSITIVE));
        removeText.add(Pattern.compile("They will be restored when"));
        removeText.add(Pattern.compile("is disabled here!", Pattern.CASE_INSENSITIVE));
        removeText.add(Pattern.compile("Dragon's Breath on you!"));
        removeText.add(Pattern.compile("Crypt Wither Skull exploded,", Pattern.CASE_INSENSITIVE));
        removeText.add(Pattern.compile("struck you for"));
        removeText.add(Pattern.compile("Queuing\\.\\.\\."));
        removeText.add(Pattern.compile("using this class!", Pattern.CASE_INSENSITIVE));
        removeText.add(Pattern.compile("found x10 (Spider|Gold|Diamond|Ice) Essence!"));
        removeText.add(Pattern.compile("Everyone gains an extra essence!"));
        removeText.add(Pattern.compile("has already been (used\\.|searched!)"));
        removeText.add(Pattern.compile("is ready to use!"));
        removeText.add(Pattern.compile("hear the sound of something opening\\.\\.\\."));
        removeText.add(Pattern.compile("Milestone .:"));
        removeText.add(Pattern.compile("Your .+ hit .+ for [\\d,.]+ damage\\."));
        removeText.add(Pattern.compile(".+ is ready to use! Press DROP to activate it!"));
        removeText.add(Pattern.compile("This ability is on cooldown for \\d+s\\."));
        removeText.add(Pattern.compile("Used .+!"));
        removeText.add(Pattern.compile(".+ is now available!"));
        removeText.add(Pattern.compile("[\\d,]+ health and shielded them for [\\d,]+!"));
        removeText.add(Pattern.compile("You picked up a .+ Orb"));
        removeText.add(Pattern.compile("\\[NPC] Mort:"));
        removeText.add(Pattern.compile("reduced the damage you took by [\\d,.]+!"));
        removeText.add(Pattern.compile("on cooldown for \\d+ more seconds\\."));
        removeText.add(Pattern.compile("A shiver runs down your spine\\.\\.\\."));
        removeText.add(Pattern.compile("You already have this class selected!"));
        removeText.add(Pattern.compile("You (sold|bought back) .+ x\\d for [\\d,.]+ Coin(s)?!", Pattern.CASE_INSENSITIVE));
        removeText.add(Pattern.compile("(hit|healed) you for [\\d,.]+ (true )?(damage|health)[.!]"));
        removeText.add(Pattern.compile("That item cannot be sold!"));
        removeText.add(Pattern.compile("(BLOOD DOOR has been )?opened( a WITHER door)?!"));
        removeText.add(Pattern.compile("Refreshing\\.\\.\\."));
        removeText.add(Pattern.compile("picked up your .+ Orb!"));
        removeText.add(Pattern.compile("formed a tether with"));
        removeText.add(Pattern.compile("You are reviving .+!"));
        removeText.add(Pattern.compile("You cannot use abilities in this room!"));
        removeText.add(Pattern.compile(".+ was revived by .+!"));
        removeText.add(Pattern.compile("You have teleported to .+!"));
        removeText.add(Pattern.compile("Mute silenced you!"));
        removeText.add(Pattern.compile(".+ is enraged!.+"));
        removeText.add(Pattern.compile("Not enough mana!"));
        removeText.add(Pattern.compile("Sending to server .+\\.\\.\\."));
        removeText.add(Pattern.compile("\\[SKULL] Wither Skull:"));
        removeText.add(Pattern.compile("is now ready!"));
        removeText.add(Pattern.compile("Warping\\.\\.\\."));
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        if (MiscConfig.removeText && shouldRemove(event.message.getUnformattedText())) {
            event.setCanceled(true);
            return;
        }

        if (!mainToggle) return;
        altered = false;
        final IChatComponent alteredComponent = processComponent(event.message, MiscConfig.removeRanks, MiscConfig.changePrefix, MiscConfig.removeLVL);

        if (altered) {
            event.setCanceled(true);
            ChatUtils.sendCleanMessage(alteredComponent);
        }
    }


    private IChatComponent processComponent(IChatComponent component, boolean removeRanks, boolean changePrefix, boolean removeLVL) {
        if (!mainToggle) return component;
        List<IChatComponent> newSiblings = new ArrayList<>();

        for (IChatComponent sibling : component.getSiblings()) {
            String siblingText = sibling.getUnformattedText().trim();
            boolean isSkipped = MiscConfig.removeLVL && (siblingText.equals("[") || siblingText.matches("^\\d+$") || siblingText.equals("]"));

            if (isSkipped) continue;

            newSiblings.add(sibling);
        }

        component.getSiblings().clear();
        component.getSiblings().addAll(newSiblings);


        IChatComponent textCopy = component.createCopy();
        textCopy.getSiblings().clear();
        final String originalText = textCopy.getFormattedText();
        String alteredText = originalText;

        alteredText = replaceText(alteredText, rankReplace, removeRanks);
        alteredText = replaceText(alteredText, prefixReplace, changePrefix);
        alteredText = removeLevel(alteredText, removeLVL);

        IChatComponent result = alteredText.equals(originalText) ? component.createCopy() : new ChatComponentText(alteredText);

        if (!alteredText.equals(originalText)) {
            result.setChatStyle(component.getChatStyle().createShallowCopy());
            altered = true;
        }

        List<IChatComponent> processedSiblings = new ArrayList<>();
        for (IChatComponent sibling : newSiblings) {
            processedSiblings.add(processComponent(sibling, removeRanks, changePrefix, removeLVL));
        }

        result.getSiblings().clear();
        result.getSiblings().addAll(processedSiblings);

        return result;
    }

    private String replaceText(String text, Map<Pattern, String> replacements, boolean condition) {
        if (condition) {
            for (Map.Entry<Pattern, String> entry : replacements.entrySet()) {
                Matcher matcher = entry.getKey().matcher(text);
                if (matcher.find()) {
                    text = matcher.replaceAll(entry.getValue());
                }
            }
        }
        return text;
    }

    private String removeLevel(String text, boolean condition) {
        if (condition) {
            for (Pattern pattern : removeLevel) {
                Matcher matcher = pattern.matcher(text);
                if (matcher.find()) {
                    text = matcher.replaceAll("");
                }
            }
        }
        return text;
    }

    private boolean shouldRemove(String unformattedText) {
        for (Pattern pattern : removeText) {
            if (pattern.matcher(unformattedText).find()) {
                return true;
            }
        }
        return false;
    }
}
