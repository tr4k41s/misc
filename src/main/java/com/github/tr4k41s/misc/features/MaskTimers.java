package com.github.tr4k41s.misc.features;

import com.github.tr4k41s.misc.config.MiscConfig;
import com.github.tr4k41s.misc.events.PacketEvent;
import com.github.tr4k41s.misc.hud.MaskTimerHUD;
import com.github.tr4k41s.misc.utils.ChatUtils;
import com.github.tr4k41s.misc.utils.ScoreboardUtils;
import com.github.tr4k41s.misc.utils.SoundUtils;
import com.github.tr4k41s.misc.utils.Utils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.github.tr4k41s.misc.Misc.mc;

public class MaskTimers {
    private static int bonzoCD;
    private static final boolean[] popped = {false, false, false}; // notif boolean to not run twice
    public static final int[] pop = {0,0,0}; // time in ticks
    MaskTimerHUD maskTimerHUD = new MaskTimerHUD();
    boolean isToggled = maskTimerHUD.isEnabled();

        @SubscribeEvent
        public void onWorldChange(WorldEvent.Load event) {
            for (int i = 0; i < pop.length; i++) {
                pop[i] = 0;
                popped[i] = false;
            }
        }

        @SubscribeEvent
        public void onChat(ClientChatReceivedEvent event) {
            if (!ScoreboardUtils.inCatacombs() || !isToggled) return;
            ItemStack head;
            String message = event.message.getUnformattedText();
            switch (message) {
                case "Your Bonzo's Mask saved your life!":
                case "Your ⚚ Bonzo's Mask saved your life!":
                    head = mc.thePlayer.getCurrentArmor(3);
                    if (head == null || head.getItem() != Items.skull) return;
                    for (String line : Utils.getItemLore(head)) {
                        String stripped = StringUtils.stripControlCodes(line);
                        if (!stripped.startsWith("Cooldown: ")) continue;
                        pop[0] = Integer.parseInt(stripped.replaceAll("\\D", "")) * 20;;
                        popped[0] = true;
                        break;
                    }
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
    public void onPacketReceived(PacketEvent.ReceiveEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null || !(event.packet instanceof S32PacketConfirmTransaction)) return;
        String[] names = {"Bonzo's Mask", "Spirit Mask", "Phoenix Pet"};
        String[] sounds = {"note.pling","mob.blaze.hit","fire.ignite","random.break","Custom"};
        String sound = MiscConfig.MaskTimerSoundValue == 4 ? MiscConfig.MaskTimerSound : sounds[MiscConfig.MaskTimerSoundValue];
        for (int i = 0; i < pop.length; i++) {
            if (!popped[i] || pop[i]-- > 1) continue;
            popped[i] = false;
            ChatUtils.sendModMessage("§b" + names[i] + "§f is now available!");
            if (MiscConfig.MaskTimerSoundToggle) { SoundUtils.playSound(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), sound, MiscConfig.MaskTimerSoundVolume, MiscConfig.MaskTimerSoundPitch); }
        }
    }
}


