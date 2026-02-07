package net.sweenus.wynnanimated.client.util;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public final class WynnCooldownObserver {

    public static void tick(AbstractClientPlayerEntity player) {
        ItemStack stack = player.getMainHandStack();
        if (stack.isEmpty()) return;

        if (WynnCooldownCache.get(stack) >= 0) return;

        var mgr = player.getItemCooldownManager();

        if (!mgr.isCoolingDown(stack)) return;

        float progress = mgr.getCooldownProgress(stack, 0.0f);
        if (progress <= 0.01f || progress >= 0.99f) return;

        // Approximate cooldown length
        int estimatedTicks =
                MathHelper.clamp(Math.round(20f / (1f - progress)), 5, 40);

        WynnCooldownCache.put(stack, estimatedTicks);
    }
}