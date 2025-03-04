package com.github.tr4k41s.misc.features;

import com.github.tr4k41s.misc.config.MiscConfig;
import com.github.tr4k41s.misc.events.PacketEvent;
import com.github.tr4k41s.misc.utils.ChatUtils;
import com.github.tr4k41s.misc.utils.ScoreboardUtils;
import com.github.tr4k41s.misc.utils.Utils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.github.tr4k41s.misc.Misc.mc;

public class MaskTimers {
    private static int bonzoCD;
    private static final boolean[] popped = {false, false, false}; // notif boolean to not run twice
    public static final int[] pop = {0,0,0}; // time in ticks

        @SubscribeEvent
        public void onWorldChange(WorldEvent.Load event) {
            for (int i = 0; i < pop.length; i++) {
                pop[i] = 0;
                popped[i] = false;
            }
        }

        @SubscribeEvent
        public void onChat(ClientChatReceivedEvent event) {
            if (!ScoreboardUtils.inCatacombs() || !MiscConfig.MaskTimers) return;
            EntityPlayerSP player;
            ItemStack head;
            String message = event.message.getUnformattedText();
            /*if (message.contains("xd")) {
                pop[1] = 50;
                pop[2] = 100;
                pop[0] = 150;
                popped[1] = true;
                popped[2] = true;
                popped[0] = true;
            }*/ //debug
            switch (message) {
                case "Your Bonzo's Mask saved your life!":
                case "Your ⚚ Bonzo's Mask saved your life!":
                    player = mc.thePlayer;
                    head = player.getCurrentArmor(3);
                    if (head != null && head.getItem() == Items.skull) {
                        for (String line : Utils.getItemLore(head)) {
                            String stripped = StringUtils.stripControlCodes(line);
                            if (stripped.startsWith("Cooldown: ")) {
                                bonzoCD = Integer.parseInt(stripped.replaceAll("\\D", "")) * 20;
                                break;
                            }
                        }
                        if (bonzoCD > 0) {
                            pop[0] = bonzoCD;
                            popped[0] = true;
                        }
                    }
                    break;
                case "Second Wind Activated! Your Spirit Mask saved your life!":
                    pop[1] = 600;
                    popped[1] = true;
                    break;
                case "Your Phoenix Pet saved you from certain death!":
                    pop[2] = 1200;
                    popped[2] = true;
                    break;
            }
        }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent event) {
        if (event.packet instanceof net.minecraft.network.play.server.S32PacketConfirmTransaction) {
            String[] names = {"Bonzo's Mask", "Spirit Mask", "Phoenix Pet"};
            for (int i = 0; i < pop.length; i++) {
                if (!popped[i] || pop[i]-- > 1) continue;
                popped[i] = false;
                ChatUtils.sendModMessage("§b" + names[i] + "§f is now available!");
            }
        }
    }
}


