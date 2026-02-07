package net.sweenus.wynnanimated.client.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public final class WynnCooldownCache {

    private static final Map<String, Integer> CACHE = new HashMap<>();

    private WynnCooldownCache() {}

    public static int get(ItemStack stack) {
        return CACHE.getOrDefault(makeKey(stack), -1);
    }

    public static void put(ItemStack stack, int cooldownTicks) {
        CACHE.put(makeKey(stack), cooldownTicks);
    }

    private static String makeKey(ItemStack stack) {
        String id = Registries.ITEM.getId(stack.getItem()).toString();
        int loreHash = computeLoreHash(stack);
        return id + "#" + loreHash;
    }


    private static int computeLoreHash(ItemStack stack) {
        LoreComponent lore = stack.get(DataComponentTypes.LORE);
        if (lore == null) return 0;

        int hash = 1;
        for (Text line : lore.lines()) {
            hash = 31 * hash + line.getString().hashCode();
        }
        return hash;
    }

}