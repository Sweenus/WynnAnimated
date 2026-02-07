package net.sweenus.wynnanimated.client.util;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;

public class HelperMethods {


    public static boolean isMainHandOnCooldown(AbstractClientPlayerEntity player) {
        ItemStack stack = player.getMainHandStack();
        if (stack.isEmpty()) return false;

        return player.getItemCooldownManager().isCoolingDown(stack);
    }

}
