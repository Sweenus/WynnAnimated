package net.sweenus.wynnanimated.client.util;

import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.sweenus.wynnanimated.client.WynnanimatedClient;

public class SpellCastHandler {

    // ****************************************************************************
// *                                                                      *
// *                       SPELL DEFINITIONS                              *
// *                      sourced from Wynntils                           *
// ****************************************************************************

// ****************************************************************************
// *  Archer Class Spells:                                                  *
// ****************************************************************************
/* ARROW_STORM(ClassType.ARCHER, 1, "Arrow Storm", 6, 0),
   ESCAPE(ClassType.ARCHER, 2, "Escape", 3, 0),
   BOMB(ClassType.ARCHER, 3, "Arrow Bomb", 8, 0),
   ARROW_SHIELD(ClassType.ARCHER, 4, "Arrow Shield", 8, 1), */

// ****************************************************************************
// *  Assassin Class Spells:                                               *
// ****************************************************************************
/* SPIN_ATTACK(ClassType.ASSASSIN, 1, "Spin Attack", 6, 0),
   DASH(ClassType.ASSASSIN, 2, "Dash", 2, 0),
   MULTI_HIT(ClassType.ASSASSIN, 3, "Multi Hit", 8, 0),
   SMOKE_BOMB(ClassType.ASSASSIN, 4, "Smoke Bomb", 8, 0), */

// ****************************************************************************
// *  Warrior Class Spells:                                                *
// ****************************************************************************
/* BASH(ClassType.WARRIOR, 1, "Bash", 6, 0),
   CHARGE(ClassType.WARRIOR, 2, "Charge", 4, 0),
   UPPERCUT(ClassType.WARRIOR, 3, "Uppercut", 9, 0),
   WAR_SCREAM(ClassType.WARRIOR, 4, "War Scream", 7, -1), */

// ****************************************************************************
// *  Mage Class Spells:                                                   *
// ****************************************************************************
/* HEAL(ClassType.MAGE, 1, "Heal", 8, -1),
   TELEPORT(ClassType.MAGE, 2, "Teleport", 4, 0),
   METEOR(ClassType.MAGE, 3, "Meteor", 8, 0),
   ICE_SNAKE(ClassType.MAGE, 4, "Ice Snake", 6, -1), */

// ****************************************************************************
// *  Shaman Class Spells:                                                 *
// ****************************************************************************
/* TOTEM(ClassType.SHAMAN, 1, "Totem", 4, 0),
   HAUL(ClassType.SHAMAN, 2, "Haul", 3, -1),
   AURA(ClassType.SHAMAN, 3, "Aura", 8, 0),
   UPROOT(ClassType.SHAMAN, 4, "Uproot", 6, 0), */

// ****************************************************************************
// *  Unspecified Spells:                                                  *
// ****************************************************************************
/* FIRST_SPELL(ClassType.NONE, 1, "1st Spell", 0, 0),
   SECOND_SPELL(ClassType.NONE, 2, "2nd Spell", 0, 0),
   THIRD_SPELL(ClassType.NONE, 3, "3rd Spell", 0, 0),
   FOURTH_SPELL(ClassType.NONE, 4, "4th Spell", 0, 0); */

// ****************************************************************************
// *                                                                      *
// *                       END OF SPELL DEFINITIONS                       *
// *                                                                      *
// ****************************************************************************


    public static void performSpellAnimation(String spellName) {
        if (!WynnanimatedClient.isWynntilsLoaded()) return;
        AbstractClientPlayerEntity player = MinecraftClient.getInstance().player;

        switch (spellName) {
            case "Spin Attack":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.SPIN_ANIMATION, WynnanimatedClient.SPIN_SPEED);
                break;
            case "Dash":
                // No animation
                break;
            case "Multi Hit":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.RANGED_SLASH_ANIMATION, WynnanimatedClient.RANGED_SLASH_SPEED);
                break;
            case "Smoke Bomb":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.THROW_ANIMATION, WynnanimatedClient.THROW_SPEED);
                break;

            case "Bash":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.SLAM_ANIMATION, WynnanimatedClient.SLAM_SPEED);
                break;
            case "Charge":
                // No animation
                break;
            case "Uppercut":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.UP_SLASH_ANIMATION, WynnanimatedClient.UP_SLASH_SPEED);
                break;
            case "War Scream":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.BATTLECRY_ANIMATION, WynnanimatedClient.BATTLECRY_SPEED);
                break;

            case "Arrow Storm":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.RAPIDFIRE_HORIZONTAL_ANIMATION, WynnanimatedClient.RAPIDFIRE_HORIZONTAL_SLOW_SPEED);
                break;
            case "Escape":
                // No animation
                break;
            case "Arrow Bomb":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.RAPIDFIRE_HORIZONTAL_ANIMATION, WynnanimatedClient.RAPIDFIRE_HORIZONTAL_SLOW_SPEED);
                break;
            case "Arrow Shield":
                // No animation
                break;
            default:
                System.out.println("Unknown animation type: " + spellName);
        }

    }

    public static boolean performAttackAnimation() {
        if (!WynnanimatedClient.isWynntilsLoaded()) return false;

        AbstractClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return false;

        ItemStack stack = player.getMainHandStack();
        if (stack.isEmpty()) return false;

        String wynnClass = WynntilsCompat.getPlayerClass();
        if (wynnClass == null || HelperMethods.isMainHandOnCooldown(player)) return false;

        float attackSpeed = computeAttackSpeedMultiplier(stack);

        System.out.println("Performing attack animation for " + wynnClass);
        switch (wynnClass) {
            case "Archer/Hunter" -> WynnanimatedClient.playAnimation(
                    player,
                    WynnanimatedClient.RAPIDFIRE_HORIZONTAL_ANIMATION,
                    speed(WynnanimatedClient.RAPIDFIRE_HORIZONTAL_SPEED, attackSpeed)
            );
            case "Warrior/Knight" -> WynnanimatedClient.playAnimation(
                    player,
                    WynnanimatedClient.SWING_ANIMATION,
                    speed(WynnanimatedClient.SWING_SPEED, attackSpeed)
            );
            case "Mage/Dark Wizard" -> WynnanimatedClient.playAnimation(
                    player,
                    WynnanimatedClient.SLASH_LEFT_ANIMATION,
                    speed(WynnanimatedClient.SLASH_LEFT_SPEED, attackSpeed)
            );
            case "Assasin/Ninja" -> WynnanimatedClient.playAnimation(
                    player,
                    WynnanimatedClient.SLASH_RIGHT_ANIMATION,
                    speed(WynnanimatedClient.SLASH_RIGHT_SPEED, attackSpeed)
            );
            case "Shaman/Skyseer" -> {
                WynnanimatedClient.playAnimation(
                    player,
                    WynnanimatedClient.THROW_ANIMATION,
                    speed(WynnanimatedClient.THROW_SHAMAN_SPEED, attackSpeed)
            );
            }
            default -> {
                return false;
            }
        }
        return true;
    }


    private static float speed(float baseSpeed, float multiplier) {
        return (baseSpeed * multiplier);
    }

    private static boolean useCooldownObserver = true; // false = use lore resolver, true = use cooldown observer
    private static float computeAttackSpeedMultiplier(ItemStack stack) {
        int cooldownTicks;

        if (useCooldownObserver) {
            // Use observer-based method
            cooldownTicks = WynnCooldownCache.get(stack);
            System.out.println("Using cooldown observer: " + cooldownTicks + " ticks");
        } else {
            // Use lore-based method (with fallback to observer)
            cooldownTicks = WynnAttackSpeedResolver.resolveCooldownFromLore(stack);

            if (cooldownTicks < 0) {
                cooldownTicks = WynnCooldownCache.get(stack);
                System.out.println("Lore resolver failed, falling back to cache: " + cooldownTicks + " ticks");
            } else {
                System.out.println("Using lore resolver: " + cooldownTicks + " ticks");
            }
        }

        if (cooldownTicks < 0) {
            cooldownTicks = 15; // safe fallback
            System.out.println("Both methods failed, using fallback: " + cooldownTicks + " ticks");
        }

        float vanilla = 25f;

        return MathHelper.clamp(
                vanilla / cooldownTicks,
                0.6f,
                1.6f
        );
    }



}
