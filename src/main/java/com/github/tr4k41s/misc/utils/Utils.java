package com.github.tr4k41s.misc.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.scoreboard.ScoreObjective;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.tr4k41s.misc.Misc.mc;

public class Utils {
    public static List<String> getItemLore(ItemStack itemStack) {
        if (itemStack == null)
            return null;
        int NBT_INTEGER = 3;
        int NBT_STRING = 8;
        int NBT_LIST = 9;
        int NBT_COMPOUND = 10;
        if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("display", 10)) {
            NBTTagCompound display = itemStack.getTagCompound().getCompoundTag("display");
            if (display.hasKey("Lore", 9)) {
                NBTTagList lore = display.getTagList("Lore", 8);
                List<String> loreAsList = new ArrayList<>();
                for (int lineNumber = 0; lineNumber < lore.tagCount(); lineNumber++)
                    loreAsList.add(lore.getStringTagAt(lineNumber));
                return Collections.unmodifiableList(loreAsList);
            }
        }
        return Collections.emptyList();
    }

    public static String removeFormatting(String input) {
        return input.replaceAll("[§|&][0-9a-fk-or]", "");
    }
}
