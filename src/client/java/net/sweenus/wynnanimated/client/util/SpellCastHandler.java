package net.sweenus.wynnanimated.client.util;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
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
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.SPELL_CAST_ANIMATION, WynnanimatedClient.SPELL_CAST_SPEED);
                break;
            case "Uppercut":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.UPPERCUT_ANIMATION, WynnanimatedClient.UPPERCUT_SPEED);
                break;
            case "War Scream":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.BATTLECRY_ANIMATION, WynnanimatedClient.BATTLECRY_SPEED);
                break;

            case "Arrow Storm":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.BOW_RAPIDFIRE_VERTICAL_ANIMATION, WynnanimatedClient.BOW_RAPIDFIRE_VERTICAL_SPEED);
                break;
            case "Escape":
                // No animation
                break;
            case "Arrow Bomb":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.BOW_SHOOT_VERTICAL_ANIMATION, WynnanimatedClient.BOW_SHOOT_HORIZONTAL_SPEED);
                break;
            case "Arrow Shield":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.SPELL_CAST_ANIMATION, WynnanimatedClient.SPELL_CAST_ALT_SPEED);
                break;

            case "Heal":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.SPELL_HEAL_ANIMATION, WynnanimatedClient.SPELL_HEAL_SPEED);
                break;
            case "Teleport":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.SPELL_CAST_ANIMATION, WynnanimatedClient.SPELL_CAST_SPEED);
                break;
            case "Meteor":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.SPELL_CAST_ALT_ANIMATION, WynnanimatedClient.SPELL_CAST_ALT_SPEED);
                break;
            case "Ice Snake":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.SPELL_ICE_SNAKE_ANIMATION, WynnanimatedClient.SPELL_ICE_SNAKE_SPEED);
                break;

            case "Totem":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.SPELL_ICE_SNAKE_ANIMATION, WynnanimatedClient.SPELL_ICE_SNAKE_SPEED);
                break;
            case "Haul":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.SPELL_CAST_ANIMATION, WynnanimatedClient.SPELL_CAST_SPEED);
                break;
            case "Aura":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.SPELL_AURA_ANIMATION, WynnanimatedClient.SPELL_AURA_SPEED);
                break;
            case "Uproot":
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.BATTLECRY_ANIMATION, WynnanimatedClient.BATTLECRY_SPEED);
                break;
            default:
                if (WynnanimatedClient.debugMode) System.out.println("Unknown animation type: " + spellName);
        }

    }

    // Attack animation pacing state
    private static Identifier activeAttackAnimId = null;
    private static int animStartTick = -1;
    private static int normalPhaseTicks = 0;
    private static float slowPhaseSpeed = 1.0f;
    private static int totalPacingTicks = 0;
    private static final float NORMAL_SPEED_PORTION = 0.8f;
    private static final int EARLY_COOLDOWN_THRESHOLD = 4;
    private static final float MAX_ANIMATION_SPEED = 6.0f;

    public static boolean performAttackAnimation() {
        AbstractClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return false;

        ItemStack stack = player.getMainHandStack();
        if (stack.isEmpty()) return false;

        // Try Wynntils class detection first, fall back to weapon-based resolution
        String wynnClass = WynntilsCompat.getPlayerClass();

        Identifier animId;
        if (wynnClass != null) {
            animId = switch (wynnClass) {
                case "Archer/Hunter" -> WynnanimatedClient.BOW_SHOOT_VERTICAL_ANIMATION;
                case "Warrior/Knight" -> WynnanimatedClient.SWING_ANIMATION;
                case "Mage/Dark Wizard" -> WynnanimatedClient.SPELL_CAST_ANIMATION;
                case "Assassin/Ninja" -> WynnanimatedClient.ROGUE_SLASH_ANIMATION;
                case "Shaman/Skyseer" -> WynnanimatedClient.THROW_ANIMATION;
                default -> null;
                };
            } else {
            // Wynntils not loaded or class unknown - resolve from held weapon
                    animId = WynnWeaponResolver.resolveAttackAnimation(player);
            }
        if (animId == null) return false;

        int cooldownTicks = resolveCooldownTicks(stack);

        // Only play if we're in the first few ticks of the cooldown (or no cooldown active)
        if (player.getItemCooldownManager().isCoolingDown(stack)) {
            float progress = player.getItemCooldownManager().getCooldownProgress(stack, 0);
            int elapsedTicks = Math.round((1.0f - progress) * cooldownTicks);
            if (elapsedTicks > EARLY_COOLDOWN_THRESHOLD) return false;
        }
        if (WynnanimatedClient.debugMode) System.out.println("class is: " + (wynnClass != null ? wynnClass : "inferred +from weapon"));


        // Don't restart if this animation is already playing (prevents stutter on fast cooldown weapons)
        if (WynnanimatedClient.isPlayingCustomAnimation(player, animId)) return true;

        // Get animation's full duration (stopTick, not endTick, to include the return-to-rest phase)
        KeyframeAnimation anim = (KeyframeAnimation) PlayerAnimationRegistry.getAnimation(animId);
        int animDuration = (anim != null) ? anim.getLength() : 0;

        AttackTracker.lastAnimationTick = player.age;

        if (animDuration > 0 && animDuration > cooldownTicks) {
            // Animation is longer than cooldown - uniform speedup to fit
            float speed = Math.min((float) animDuration / cooldownTicks, MAX_ANIMATION_SPEED);
            WynnanimatedClient.playAnimation(player, animId, speed);
            activeAttackAnimId = null;
            if (WynnanimatedClient.debugMode) System.out.println("Performing attack animation for " + wynnClass
                    + " | duration: " + animDuration + ", cooldown: " + cooldownTicks + ", speed: " + speed);
        } else if (animDuration > 0 && animDuration < cooldownTicks) {
            // Animation is shorter than cooldown - normal speed then slow tail
            WynnanimatedClient.playAnimation(player, animId, 1.0f);
            activeAttackAnimId = animId;
            animStartTick = player.age;
            normalPhaseTicks = (int) (animDuration * NORMAL_SPEED_PORTION);
            int tailAnimTicks = animDuration - normalPhaseTicks;
            int tailGameTicks = cooldownTicks - normalPhaseTicks;
            slowPhaseSpeed = (float) tailAnimTicks / tailGameTicks;
            totalPacingTicks = cooldownTicks;
            if (WynnanimatedClient.debugMode) System.out.println("Performing attack animation for " + wynnClass
                    + " | duration: " + animDuration + ", cooldown: " + cooldownTicks
                    + ", normalPhase: " + normalPhaseTicks + " ticks, slowPhase speed: " + slowPhaseSpeed);
        } else {
            // Animation matches cooldown exactly (or duration unknown) - play at normal speed
            WynnanimatedClient.playAnimation(player, animId, 1.0f);
            activeAttackAnimId = null;
            if (WynnanimatedClient.debugMode) System.out.println("Performing attack animation for " + wynnClass
                    + " | duration: " + animDuration + ", cooldown: " + cooldownTicks + ", speed: 1.0");
        }

        return true;
    }

    public static void tickAttackAnimationSpeed(AbstractClientPlayerEntity player) {
        if (activeAttackAnimId == null) return;

        int elapsed = player.age - animStartTick;

        if (elapsed >= totalPacingTicks) {
            activeAttackAnimId = null;
            return;
        }

        float speed = (elapsed <= normalPhaseTicks) ? 1.0f : slowPhaseSpeed;
        WynnanimatedClient.updateAnimationSpeed(player, activeAttackAnimId, speed);
    }

    private static boolean useCooldownObserver = true; // false = use lore resolver, true = use cooldown observer
    private static int resolveCooldownTicks(ItemStack stack) {
        int cooldownTicks;

        if (useCooldownObserver) {
            cooldownTicks = WynnCooldownCache.get(stack);
            if (WynnanimatedClient.debugMode) System.out.println("Using cooldown observer: " + cooldownTicks + " ticks");
        } else {
            cooldownTicks = WynnAttackSpeedResolver.resolveCooldownFromLore(stack);

            if (cooldownTicks < 0) {
                cooldownTicks = WynnCooldownCache.get(stack);
                if (WynnanimatedClient.debugMode) System.out.println("Lore resolver failed, falling back to cache: " + cooldownTicks + " ticks");
            } else {
                if (WynnanimatedClient.debugMode) System.out.println("Using lore resolver: " + cooldownTicks + " ticks");
            }
        }

        if (cooldownTicks < 0) {
            cooldownTicks = 15; // safe fallback
            if (WynnanimatedClient.debugMode) System.out.println("Both methods failed, using fallback: " + cooldownTicks + " ticks");
        }

        return cooldownTicks;
    }



}
