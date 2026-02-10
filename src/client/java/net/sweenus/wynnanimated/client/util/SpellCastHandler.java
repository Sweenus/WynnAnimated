package net.sweenus.wynnanimated.client.util;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.sweenus.wynnanimated.client.AnimationRegistry;

public class SpellCastHandler {

    public static void performSpellAnimation(String spellName) {
        if (!AnimationRegistry.isWynntilsLoaded()) return;
        AbstractClientPlayerEntity player = MinecraftClient.getInstance().player;

        switch (spellName) {
            case "Spin Attack":
                AnimationRegistry.playAnimation(player, AnimationRegistry.SPIN_ATTACK_ANIMATION, AnimationRegistry.SPIN_ATTACK_SPEED);
                break;
            case "Dash":
                AnimationRegistry.playAnimation(player, AnimationRegistry.DASH_ANIMATION, AnimationRegistry.DASH_SPEED);
                break;
            case "Multi Hit":
                AnimationRegistry.playAnimation(player, AnimationRegistry.MULTI_HIT_ANIMATION, AnimationRegistry.MULTI_HIT_SPEED);
                break;
            case "Smoke Bomb":
                AnimationRegistry.playAnimation(player, AnimationRegistry.SMOKE_BOMB_ANIMATION, AnimationRegistry.SMOKE_BOMB_SPEED);
                break;

            case "Bash":
                AnimationRegistry.playAnimation(player, AnimationRegistry.BASH_ANIMATION, AnimationRegistry.BASH_SPEED);
                break;
            case "Charge":
                AnimationRegistry.playAnimation(player, AnimationRegistry.CHARGE_ANIMATION, AnimationRegistry.CHARGE_SPEED);
                break;
            case "Uppercut":
                AnimationRegistry.playAnimation(player, AnimationRegistry.UPPERCUT_ANIMATION, AnimationRegistry.UPPERCUT_SPEED);
                break;
            case "War Scream":
                AnimationRegistry.playAnimation(player, AnimationRegistry.WAR_SCREAM_ANIMATION, AnimationRegistry.WAR_SCREAM_SPEED);
                break;

            case "Arrow Storm":
                AnimationRegistry.playAnimation(player, AnimationRegistry.ARROW_STORM_ANIMATION, AnimationRegistry.ARROW_STORM_SPEED);
                break;
            case "Escape":
                AnimationRegistry.playAnimation(player, AnimationRegistry.ESCAPE_ANIMATION, AnimationRegistry.ESCAPE_SPEED);
                break;
            case "Arrow Bomb":
                AnimationRegistry.playAnimation(player, AnimationRegistry.BOMB_ANIMATION, AnimationRegistry.BOMB_SPEED);
                break;
            case "Arrow Shield":
                AnimationRegistry.playAnimation(player, AnimationRegistry.ARROW_SHIELD_ANIMATION, AnimationRegistry.ARROW_SHIELD_SPEED);
                break;

            case "Heal":
                AnimationRegistry.playAnimation(player, AnimationRegistry.HEAL_ANIMATION, AnimationRegistry.HEAL_SPEED);
                break;
            case "Teleport":
                AnimationRegistry.playAnimation(player, AnimationRegistry.TELEPORT_ANIMATION, AnimationRegistry.TELEPORT_SPEED);
                break;
            case "Meteor":
                AnimationRegistry.playAnimation(player, AnimationRegistry.METEOR_ANIMATION, AnimationRegistry.METEOR_SPEED);
                break;
            case "Ice Snake":
                AnimationRegistry.playAnimation(player, AnimationRegistry.ICE_SNAKE_ANIMATION, AnimationRegistry.ICE_SNAKE_SPEED);
                break;

            case "Totem":
                AnimationRegistry.playAnimation(player, AnimationRegistry.TOTEM_ANIMATION, AnimationRegistry.TOTEM_SPEED);
                break;
            case "Haul":
                AnimationRegistry.playAnimation(player, AnimationRegistry.HAUL_ANIMATION, AnimationRegistry.HAUL_SPEED);
                break;
            case "Aura":
                AnimationRegistry.playAnimation(player, AnimationRegistry.AURA_ANIMATION, AnimationRegistry.AURA_SPEED);
                break;
            case "Uproot":
                AnimationRegistry.playAnimation(player, AnimationRegistry.UPROOT_ANIMATION, AnimationRegistry.UPROOT_SPEED);
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
                case "Archer/Hunter" -> AnimationRegistry.BASIC_ATTACK_BOW;
                case "Warrior/Knight" -> AnimationRegistry.BASIC_ATTACK_SPEAR;
                case "Mage/Dark Wizard" -> AnimationRegistry.BASIC_ATTACK_WAND;
                case "Assassin/Ninja" -> AnimationRegistry.BASIC_ATTACK_DAGGER;
                case "Shaman/Skyseer" -> AnimationRegistry.BASIC_ATTACK_RELIK;
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
        if (AnimationRegistry.debugMode) System.out.println("class is: " + (wynnClass != null ? wynnClass : "inferred from weapon"));

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