package net.sweenus.wynnanimated.client.util;

import net.minecraft.item.ItemStack;
import net.sweenus.wynnanimated.client.AnimationRegistry;
import net.sweenus.wynnanimated.client.WynnanimatedClient;

import java.util.Locale;
import java.util.Map;

public final class WynnAttackSpeedResolver {

    private static final Map<String, Integer> SPEED_TO_TICKS = Map.of(
            "SUPER FAST", 6,
            "VERY FAST", 8,
            "FAST", 10,
            "NORMAL", 15,
            "SLOW", 20,
            "VERY SLOW", 30
    );

    public static int resolveCooldownFromLore(ItemStack stack) {
        var lore = stack.get(net.minecraft.component.DataComponentTypes.LORE);
        if (lore == null) return -1;

        for (var line : lore.lines()) {
            String raw = line.getString().toUpperCase(Locale.ROOT);

            for (var entry : SPEED_TO_TICKS.entrySet()) {
                if (raw.contains(entry.getKey())) {
                    if (AnimationRegistry.debugMode) System.out.println(WynnanimatedClient.LOG_ID + " Weapon speed is " +entry.getKey() + " " + entry.getValue());
                    return entry.getValue();
                }
            }
        }
        return -1;
    }
}