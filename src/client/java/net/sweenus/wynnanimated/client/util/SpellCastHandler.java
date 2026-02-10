package net.sweenus.wynnanimated.client.util;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.sweenus.wynnanimated.client.AnimationRegistry;

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
        if (!AnimationRegistry.isWynntilsLoaded()) return;
        AbstractClientPlayerEntity player = MinecraftClient.getInstance().player;

        switch (spellName) {
            case "Spin Attack":
                AnimationRegistry.playAnimation(player, AnimationRegistry.SPIN_ANIMATION, AnimationRegistry.SPIN_SPEED);
                break;
            case "Dash":
                // No animation
                break;
            case "Multi Hit":
                AnimationRegistry.playAnimation(player, AnimationRegistry.RANGED_SLASH_ANIMATION, AnimationRegistry.RANGED_SLASH_SPEED);
                break;
            case "Smoke Bomb":
                AnimationRegistry.playAnimation(player, AnimationRegistry.THROW_ANIMATION, AnimationRegistry.THROW_SPEED);
                break;

            case "Bash":
                AnimationRegistry.playAnimation(player, AnimationRegistry.SLAM_ANIMATION, AnimationRegistry.SLAM_SPEED);
                break;
            case "Charge":
                AnimationRegistry.playAnimation(player, AnimationRegistry.SPELL_CAST_ANIMATION, AnimationRegistry.SPELL_CAST_SPEED);
                break;
            case "Uppercut":
                AnimationRegistry.playAnimation(player, AnimationRegistry.UPPERCUT_ANIMATION, AnimationRegistry.UPPERCUT_SPEED);
                break;
            case "War Scream":
                AnimationRegistry.playAnimation(player, AnimationRegistry.BATTLECRY_ANIMATION, AnimationRegistry.BATTLECRY_SPEED);
                break;

            case "Arrow Storm":
                AnimationRegistry.playAnimation(player, AnimationRegistry.BOW_RAPIDFIRE_VERTICAL_ANIMATION, AnimationRegistry.BOW_RAPIDFIRE_VERTICAL_SPEED);
                break;
            case "Escape":
                // No animation
                break;
            case "Arrow Bomb":
                AnimationRegistry.playAnimation(player, AnimationRegistry.BOW_SHOOT_VERTICAL_ANIMATION, AnimationRegistry.BOW_SHOOT_HORIZONTAL_SPEED);
                break;
            case "Arrow Shield":
                AnimationRegistry.playAnimation(player, AnimationRegistry.SPELL_CAST_ANIMATION, AnimationRegistry.SPELL_CAST_ALT_SPEED);
                break;

            case "Heal":
                AnimationRegistry.playAnimation(player, AnimationRegistry.SPELL_HEAL_ANIMATION, AnimationRegistry.SPELL_HEAL_SPEED);
                break;
            case "Teleport":
                AnimationRegistry.playAnimation(player, AnimationRegistry.SPELL_CAST_ANIMATION, AnimationRegistry.SPELL_CAST_SPEED);
                break;
            case "Meteor":
                AnimationRegistry.playAnimation(player, AnimationRegistry.SPELL_CAST_ALT_ANIMATION, AnimationRegistry.SPELL_CAST_ALT_SPEED);
                break;
            case "Ice Snake":
                AnimationRegistry.playAnimation(player, AnimationRegistry.SPELL_ICE_SNAKE_ANIMATION, AnimationRegistry.SPELL_ICE_SNAKE_SPEED);
                break;

            case "Totem":
                AnimationRegistry.playAnimation(player, AnimationRegistry.SPELL_ICE_SNAKE_ANIMATION, AnimationRegistry.SPELL_ICE_SNAKE_SPEED);
                break;
            case "Haul":
                AnimationRegistry.playAnimation(player, AnimationRegistry.SPELL_CAST_ANIMATION, AnimationRegistry.SPELL_CAST_SPEED);
                break;
            case "Aura":
                AnimationRegistry.playAnimation(player, AnimationRegistry.SPELL_AURA_ANIMATION, AnimationRegistry.SPELL_AURA_SPEED);
                break;
            case "Uproot":
                AnimationRegistry.playAnimation(player, AnimationRegistry.BATTLECRY_ANIMATION, AnimationRegistry.BATTLECRY_SPEED);
                break;
            default:
                if (AnimationRegistry.debugMode) System.out.println("Unknown animation type: " + spellName);
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
                case "Archer/Hunter" -> AnimationRegistry.BOW_SHOOT_VERTICAL_ANIMATION;
                case "Warrior/Knight" -> AnimationRegistry.SWING_ANIMATION;
                case "Mage/Dark Wizard" -> AnimationRegistry.SPELL_CAST_ANIMATION;
                case "Assassin/Ninja" -> AnimationRegistry.ROGUE_SLASH_ANIMATION;
                case "Shaman/Skyseer" -> AnimationRegistry.THROW_ANIMATION;
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
        if (AnimationRegistry.debugMode) System.out.println("class is: " + (wynnClass != null ? wynnClass : "inferred +from weapon"));


        // Don't restart if this animation is already playing (prevents stutter on fast cooldown weapons)
        if (AnimationRegistry.isPlayingCustomAnimation(player, animId)) return true;

        // Get animation's full duration (stopTick, not endTick, to include the return-to-rest phase)
        KeyframeAnimation anim = (KeyframeAnimation) PlayerAnimationRegistry.getAnimation(animId);
        int animDuration = (anim != null) ? anim.getLength() : 0;

        AttackTracker.lastAnimationTick = player.age;

        if (animDuration > 0 && animDuration > cooldownTicks) {
            // Animation is longer than cooldown - uniform speedup to fit
            float speed = Math.min((float) animDuration / cooldownTicks, MAX_ANIMATION_SPEED);
            AnimationRegistry.playAnimation(player, animId, speed);
            activeAttackAnimId = null;
            if (AnimationRegistry.debugMode) System.out.println("Performing attack animation for " + wynnClass
                    + " | duration: " + animDuration + ", cooldown: " + cooldownTicks + ", speed: " + speed);
        } else if (animDuration > 0 && animDuration < cooldownTicks) {
            // Animation is shorter than cooldown - normal speed then slow tail
            AnimationRegistry.playAnimation(player, animId, 1.0f);
            activeAttackAnimId = animId;
            animStartTick = player.age;
            normalPhaseTicks = (int) (animDuration * NORMAL_SPEED_PORTION);
            int tailAnimTicks = animDuration - normalPhaseTicks;
            int tailGameTicks = cooldownTicks - normalPhaseTicks;
            slowPhaseSpeed = (float) tailAnimTicks / tailGameTicks;
            totalPacingTicks = cooldownTicks;
            if (AnimationRegistry.debugMode) System.out.println("Performing attack animation for " + wynnClass
                    + " | duration: " + animDuration + ", cooldown: " + cooldownTicks
                    + ", normalPhase: " + normalPhaseTicks + " ticks, slowPhase speed: " + slowPhaseSpeed);
        } else {
            // Animation matches cooldown exactly (or duration unknown) - play at normal speed
            AnimationRegistry.playAnimation(player, animId, 1.0f);
            activeAttackAnimId = null;
            if (AnimationRegistry.debugMode) System.out.println("Performing attack animation for " + wynnClass
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
        AnimationRegistry.updateAnimationSpeed(player, activeAttackAnimId, speed);
    }

    private static boolean useCooldownObserver = true; // false = use lore resolver, true = use cooldown observer
    private static int resolveCooldownTicks(ItemStack stack) {
        int cooldownTicks;

        if (useCooldownObserver) {
            cooldownTicks = WynnCooldownCache.get(stack);
            if (AnimationRegistry.debugMode) System.out.println("Using cooldown observer: " + cooldownTicks + " ticks");
        } else {
            cooldownTicks = WynnAttackSpeedResolver.resolveCooldownFromLore(stack);

            if (cooldownTicks < 0) {
                cooldownTicks = WynnCooldownCache.get(stack);
                if (AnimationRegistry.debugMode) System.out.println("Lore resolver failed, falling back to cache: " + cooldownTicks + " ticks");
            } else {
                if (AnimationRegistry.debugMode) System.out.println("Using lore resolver: " + cooldownTicks + " ticks");
            }
        }

        if (cooldownTicks < 0) {
            cooldownTicks = 15; // safe fallback
            if (AnimationRegistry.debugMode) System.out.println("Both methods failed, using fallback: " + cooldownTicks + " ticks");
        }

        return cooldownTicks;
    }



}
