package net.sweenus.wynnanimated.client;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.sweenus.wynnanimated.client.util.CustomSoundListener;

import java.util.*;
import java.util.WeakHashMap;

public class AnimationRegistry {
    public static final String MOD_ID = WynnanimatedClient.MOD_ID;
    public static final String WYNNTILS_MOD_ID = WynnanimatedClient.WYNNTILS_MOD_ID;

    // Archer Class Spells
    public static final Identifier ARROW_STORM_ANIMATION = Identifier.of(MOD_ID, "arrow_storm");
    public static final float ARROW_STORM_SPEED = 2.5f;
    public static final Identifier ESCAPE_ANIMATION = Identifier.of(MOD_ID, "escape");
    public static final float ESCAPE_SPEED = 1.5f;
    public static final Identifier BOMB_ANIMATION = Identifier.of(MOD_ID, "arrow_bomb");
    public static final float BOMB_SPEED = 2.0f;
    public static final Identifier ARROW_SHIELD_ANIMATION = Identifier.of(MOD_ID, "arrow_shield");
    public static final float ARROW_SHIELD_SPEED = 1.6f;

    // Assassin Class Spells
    public static final Identifier SPIN_ATTACK_ANIMATION = Identifier.of(MOD_ID, "spin_attack");
    public static final float SPIN_ATTACK_SPEED = 2.5f;
    public static final Identifier DASH_ANIMATION = Identifier.of(MOD_ID, "dash");
    public static final float DASH_SPEED = 1.8f;
    public static final Identifier MULTI_HIT_ANIMATION = Identifier.of(MOD_ID, "multi_hit");
    public static final float MULTI_HIT_SPEED = 2.1f;
    public static final Identifier SMOKE_BOMB_ANIMATION = Identifier.of(MOD_ID, "smoke_bomb");
    public static final float SMOKE_BOMB_SPEED = 1.9f;

    // Warrior Class Spells
    public static final Identifier BASH_ANIMATION = Identifier.of(MOD_ID, "bash");
    public static final float BASH_SPEED = 2.1f;
    public static final Identifier CHARGE_ANIMATION = Identifier.of(MOD_ID, "charge");
    public static final float CHARGE_SPEED = 1.4f;
    public static final Identifier WAR_SCREAM_ANIMATION = Identifier.of(MOD_ID, "war_scream");
    public static final float WAR_SCREAM_SPEED = 2.3f;
    public static final Identifier UPPERCUT_ANIMATION = Identifier.of(MOD_ID, "uppercut");
    public static final float UPPERCUT_SPEED = 1.8f;

    // Mage Class Spells
    public static final Identifier HEAL_ANIMATION = Identifier.of(MOD_ID, "heal");
    public static final float HEAL_SPEED = 1.5f;
    public static final Identifier TELEPORT_ANIMATION = Identifier.of(MOD_ID, "teleport");
    public static final float TELEPORT_SPEED = 2.0f;
    public static final Identifier METEOR_ANIMATION = Identifier.of(MOD_ID, "meteor");
    public static final float METEOR_SPEED = 1.8f;
    public static final Identifier ICE_SNAKE_ANIMATION = Identifier.of(MOD_ID, "ice_snake");
    public static final float ICE_SNAKE_SPEED = 1.9f;

    // Shaman Class Spells
    public static final Identifier TOTEM_ANIMATION = Identifier.of(MOD_ID, "totem");
    public static final float TOTEM_SPEED = 2.5f;
    public static final Identifier HAUL_ANIMATION = Identifier.of(MOD_ID, "haul");
    public static final float HAUL_SPEED = 1.8f;
    public static final Identifier UPROOT_ANIMATION = Identifier.of(MOD_ID, "uproot");
    public static final float UPROOT_SPEED = 2.0f;
    public static final Identifier AURA_ANIMATION = Identifier.of(MOD_ID, "aura");
    public static final float AURA_SPEED = 1.0f;

    // Basic Attacks
    public static final Identifier BASIC_ATTACK_BOW = Identifier.of(MOD_ID, "basic_attack_bow");
    public static final Identifier BASIC_ATTACK_SPEAR = Identifier.of(MOD_ID, "basic_attack_spear");
    public static final Identifier BASIC_ATTACK_WAND = Identifier.of(MOD_ID, "basic_attack_wand");
    public static final Identifier BASIC_ATTACK_DAGGER = Identifier.of(MOD_ID, "basic_attack_dagger");
    public static final Identifier BASIC_ATTACK_RELIK = Identifier.of(MOD_ID, "basic_attack_relik");

    // Pose
    public static final Identifier BOW_STANCE_READY_ANIMATION = Identifier.of(MOD_ID, "bow_stance_ready");
    public static final float BOW_STANCE_READY_SPEED = 1.0f;


    public static List<Identifier> basicAttackList = new ArrayList<>();

    public static boolean debugMode = true;
    public static boolean shouldShowArms = true; // Make this configurable later
    public static final FirstPersonConfiguration firstPersonConfiguration = new FirstPersonConfiguration();
    public static final CustomSoundListener soundListener = new CustomSoundListener();

    public static void registerAnimations() {
        // Register ability animations
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ARROW_STORM_ANIMATION,       10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ESCAPE_ANIMATION,            10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BOMB_ANIMATION,              10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ARROW_SHIELD_ANIMATION,      10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SPIN_ATTACK_ANIMATION,       10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(DASH_ANIMATION,              10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(MULTI_HIT_ANIMATION,         10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SMOKE_BOMB_ANIMATION,        10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BASH_ANIMATION,              10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(CHARGE_ANIMATION,            10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(UPPERCUT_ANIMATION,          10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(WAR_SCREAM_ANIMATION,        10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(HEAL_ANIMATION,              10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(TELEPORT_ANIMATION,          10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(METEOR_ANIMATION,            10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ICE_SNAKE_ANIMATION,         10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(TOTEM_ANIMATION,             10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(HAUL_ANIMATION,              10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(UPROOT_ANIMATION,            10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(AURA_ANIMATION,              10, (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        // Register basic attack animations
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BASIC_ATTACK_RELIK,          9, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BASIC_ATTACK_BOW,            9, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BASIC_ATTACK_SPEAR,          9, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BASIC_ATTACK_DAGGER,         9, (AbstractClientPlayerEntity -> new ModifierLayer<>()));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BASIC_ATTACK_WAND,           9, (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        // Register pose animations
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BOW_STANCE_READY_ANIMATION,  8, (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        if (debugMode) System.out.println("Registered WynnAnimated animations");
    }

    private static final WeakHashMap<AbstractClientPlayerEntity, Map<Identifier, SpeedModifier>> playerSpeedModifiers = new WeakHashMap<>();
    public static int lastAnimationPlayedTick = Integer.MIN_VALUE;

    public static void playAnimation(AbstractClientPlayerEntity player, Identifier selectedAnimation, float speedValue) {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(selectedAnimation);
        if (animation != null) {
            Map<Identifier, SpeedModifier> speedMods = playerSpeedModifiers.computeIfAbsent(player, k -> new HashMap<>());

            SpeedModifier speedMod = speedMods.get(selectedAnimation);

            if (speedMod == null) {
                speedMod = new SpeedModifier(speedValue);
                animation.addModifier(speedMod, 0);
                speedMods.put(selectedAnimation, speedMod);
            } else {
                speedMod.speed = speedValue;
                if (debugMode) System.out.println("Updated existing animation speed modifier to " + speedMod.speed);
            }

            // Only update the body-yaw tracking tick for the local player
            if (player == MinecraftClient.getInstance().player) {
                lastAnimationPlayedTick = player.age;
            }

            animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(2, Ease.INOUTSINE),
                    new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(selectedAnimation))
                            .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                            .setFirstPersonConfiguration(firstPersonConfiguration), true);
        } else {
            if (debugMode) System.out.println("Failed to locate animation");
        }
    }

    public static void updateAnimationSpeed(AbstractClientPlayerEntity player, Identifier animationId, float speedValue) {
        Map<Identifier, SpeedModifier> speedMods = playerSpeedModifiers.get(player);
        if (speedMods != null) {
            SpeedModifier speedMod = speedMods.get(animationId);
            if (speedMod != null) {
                speedMod.speed = speedValue;
            }
        }
    }

    public static void stopAnimation(AbstractClientPlayerEntity player, Identifier selectedAnimation) {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(selectedAnimation);
        if (animation == null) return;

        var layer = ((KeyframeAnimationPlayer)animation.getAnimation());
        if (layer != null && layer.getData().getName().equals(selectedAnimation))
            layer.stop();
    }

    public static void fadeOutAnimation(AbstractClientPlayerEntity player, Identifier selectedAnimation, int fadeTicks) {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(selectedAnimation);
        if (animation != null && animation.isActive()) {
            animation.replaceAnimationWithFade(
                    AbstractFadeModifier.standardFadeIn(fadeTicks, Ease.INOUTSINE), null, true);
        }
    }

    public static boolean isPlayingCustomAnimation(AbstractClientPlayerEntity player, Identifier animation) {
        var anim = PlayerAnimationAccess.getPlayerAssociatedData(player).get(animation);
        return anim != null && anim.isActive();
    }

    public static void createLists() {
        basicAttackList.add(BASIC_ATTACK_SPEAR);
        basicAttackList.add(BASIC_ATTACK_BOW);
        basicAttackList.add(BASIC_ATTACK_DAGGER);
        basicAttackList.add(BASIC_ATTACK_WAND);
        basicAttackList.add(BASIC_ATTACK_RELIK);
    }

    public static boolean isPlayingAnyAnimation(AbstractClientPlayerEntity player, List<Identifier> list) {
        for (Identifier identifier : list) {
            var anim = PlayerAnimationAccess.getPlayerAssociatedData(player).get(identifier);
            if (anim != null && anim.isActive()) return true;
        }
        return false;
    }

    public static boolean isSpecificSoundPlayingAtCoordinates(Identifier soundId, double x, double y, double z) {
        return soundListener.isSpecificSoundPlayingAtCoordinates(soundId, x, y, z);
    }

    public static void setFirstPersonConfiguration() {
        if (shouldShowArms)
            firstPersonConfiguration
                    .setShowRightArm(true)
                    .setShowLeftArm(true)
                    .setShowLeftItem(true)
                    .setShowRightItem(true)
                    .setShowArmor(false);
    }

    public static boolean isWynntilsLoaded() {
        return FabricLoader.getInstance().isModLoaded(WYNNTILS_MOD_ID);
    }
}