package net.sweenus.wynnanimated.client.util;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.sweenus.wynnanimated.client.WynnanimatedClient;

public final class WynnCooldownObserver {

    private static boolean wasCoolingDown = false;
    private static int cooldownStartTick = -1;
    private static ItemStack cooldownStack = ItemStack.EMPTY;

    public static void tick(AbstractClientPlayerEntity player) {
        ItemStack stack = player.getMainHandStack();
        if (stack.isEmpty()) {
            wasCoolingDown = false;
            return;
        }

        boolean isCoolingDown = player.getItemCooldownManager().isCoolingDown(stack);

        if (isCoolingDown && !wasCoolingDown) {
            // Cooldown just started - record start tick and weapon
            cooldownStartTick = player.age;
            cooldownStack = stack.copy();
        } else if (!isCoolingDown && wasCoolingDown && cooldownStartTick >= 0) {
            // Cooldown just ended - measure and cache the actual duration
            int duration = player.age - cooldownStartTick;
            if (duration > 0) {
                WynnCooldownCache.put(cooldownStack, duration);
                if (WynnanimatedClient.debugMode) System.out.println("WynnCooldownObserver: measured cooldown = " + duration + " ticks");
            }
            cooldownStartTick = -1;
        }

        wasCoolingDown = isCoolingDown;
    }
}