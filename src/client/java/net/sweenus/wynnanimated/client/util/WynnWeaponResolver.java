package net.sweenus.wynnanimated.client.util;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.sweenus.wynnanimated.client.AnimationRegistry;
import org.jetbrains.annotations.Nullable;

public final class WynnWeaponResolver {

    /**
     * Resolves attack animation from the player's held weapon item.
     * Works for the local player whose weapon items are accurate.
     * Other players' weapons appear as potions on Wynncraft, so use
     * {@link #resolveAttackAnimationFromClass} for them instead.
     */
    public static @Nullable Identifier resolveAttackAnimation(AbstractClientPlayerEntity player) {
        Item item = player.getMainHandStack().getItem();

        if (item == Items.BOW)          return AnimationRegistry.BOW_SHOOT_VERTICAL_ANIMATION;
        if (item == Items.IRON_SHOVEL)  return AnimationRegistry.SWING_ANIMATION;
        if (item == Items.WOODEN_SHOVEL)return AnimationRegistry.SPELL_CAST_ANIMATION;
        if (item == Items.SHEARS)       return AnimationRegistry.ROGUE_SLASH_ANIMATION;
        if (item == Items.STONE_SHOVEL) return AnimationRegistry.THROW_ANIMATION;

        return null;
    }

    /**
     * Resolves attack animation from a Wynncraft API class type string.
     * API returns: ARCHER, WARRIOR, MAGE, ASSASSIN, SHAMAN
     */
    public static @Nullable Identifier resolveAttackAnimationFromClass(String apiClassType) {
        return switch (apiClassType) {
            case "ARCHER"   -> AnimationRegistry.BOW_SHOOT_VERTICAL_ANIMATION;
            case "WARRIOR"  -> AnimationRegistry.SWING_ANIMATION;
            case "MAGE"     -> AnimationRegistry.SPELL_CAST_ANIMATION;
            case "ASSASSIN" -> AnimationRegistry.ROGUE_SLASH_ANIMATION;
            case "SHAMAN"   -> AnimationRegistry.THROW_ANIMATION;
            default -> null;
        };
    }
}
