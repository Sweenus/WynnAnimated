package net.sweenus.wynnanimated.client;

import com.zigythebird.playeranim.animation.PlayerAnimResources;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractFadeModifier;
import com.zigythebird.playeranimcore.animation.layered.modifier.SpeedModifier;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonConfiguration;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonMode;
import com.zigythebird.playeranimcore.easing.EasingType;
import com.zigythebird.playeranimcore.enums.PlayState;
import net.sweenus.wynnanimated.client.anim.CameraArmPitchModifier;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.sweenus.wynnanimated.client.config.ModConfig;
import net.sweenus.wynnanimated.client.util.CustomSoundListener;

import java.util.*;
import java.util.WeakHashMap;

public class AnimationRegistry {
    public static final String MOD_ID = WynnanimatedClient.MOD_ID;
    public static final String WYNNTILS_MOD_ID = WynnanimatedClient.WYNNTILS_MOD_ID;

    // Archer Class Spells
    public static final Identifier ARROW_STORM_ANIMATION = Identifier.of(MOD_ID, "arrow_storm");
    public static float ARROW_STORM_SPEED = 2.5f;
    public static final Identifier ESCAPE_ANIMATION = Identifier.of(MOD_ID, "escape");
    public static float ESCAPE_SPEED = 1.5f;
    public static final Identifier BOMB_ANIMATION = Identifier.of(MOD_ID, "arrow_bomb");
    public static float BOMB_SPEED = 2.0f;
    public static final Identifier ARROW_SHIELD_ANIMATION = Identifier.of(MOD_ID, "arrow_shield");
    public static float ARROW_SHIELD_SPEED = 1.6f;

    // Assassin Class Spells
    public static final Identifier SPIN_ATTACK_ANIMATION = Identifier.of(MOD_ID, "spin_attack");
    public static float SPIN_ATTACK_SPEED = 2.5f;
    public static final Identifier DASH_ANIMATION = Identifier.of(MOD_ID, "dash");
    public static float DASH_SPEED = 1.8f;
    public static final Identifier MULTI_HIT_ANIMATION = Identifier.of(MOD_ID, "multi_hit");
    public static float MULTI_HIT_SPEED = 2.1f;
    public static final Identifier SMOKE_BOMB_ANIMATION = Identifier.of(MOD_ID, "smoke_bomb");
    public static float SMOKE_BOMB_SPEED = 1.9f;

    // Warrior Class Spells
    public static final Identifier BASH_ANIMATION = Identifier.of(MOD_ID, "bash");
    public static float BASH_SPEED = 2.1f;
    public static final Identifier CHARGE_ANIMATION = Identifier.of(MOD_ID, "charge");
    public static float CHARGE_SPEED = 1.4f;
    public static final Identifier WAR_SCREAM_ANIMATION = Identifier.of(MOD_ID, "war_scream");
    public static float WAR_SCREAM_SPEED = 2.3f;
    public static final Identifier UPPERCUT_ANIMATION = Identifier.of(MOD_ID, "uppercut");
    public static float UPPERCUT_SPEED = 1.8f;

    // Mage Class Spells
    public static final Identifier HEAL_ANIMATION = Identifier.of(MOD_ID, "heal");
    public static float HEAL_SPEED = 1.5f;
    public static final Identifier TELEPORT_ANIMATION = Identifier.of(MOD_ID, "teleport");
    public static float TELEPORT_SPEED = 2.0f;
    public static final Identifier METEOR_ANIMATION = Identifier.of(MOD_ID, "meteor");
    public static float METEOR_SPEED = 1.8f;
    public static final Identifier ICE_SNAKE_ANIMATION = Identifier.of(MOD_ID, "ice_snake");
    public static float ICE_SNAKE_SPEED = 1.9f;

    // Shaman Class Spells
    public static final Identifier TOTEM_ANIMATION = Identifier.of(MOD_ID, "totem");
    public static float TOTEM_SPEED = 2.5f;
    public static final Identifier HAUL_ANIMATION = Identifier.of(MOD_ID, "haul");
    public static float HAUL_SPEED = 1.8f;
    public static final Identifier UPROOT_ANIMATION = Identifier.of(MOD_ID, "uproot");
    public static float UPROOT_SPEED = 2.0f;
    public static final Identifier AURA_ANIMATION = Identifier.of(MOD_ID, "aura");
    public static float AURA_SPEED = 1.0f;

    // Basic Attacks
    public static final Identifier BASIC_ATTACK_BOW = Identifier.of(MOD_ID, "basic_attack_bow");
    public static final Identifier BASIC_ATTACK_SPEAR = Identifier.of(MOD_ID, "basic_attack_spear");
    public static final Identifier BASIC_ATTACK_WAND = Identifier.of(MOD_ID, "basic_attack_wand");
    public static final Identifier BASIC_ATTACK_DAGGER = Identifier.of(MOD_ID, "basic_attack_dagger");
    public static final Identifier BASIC_ATTACK_RELIK = Identifier.of(MOD_ID, "basic_attack_relik");

    // Pose
    public static final Identifier BOW_STANCE_READY_ANIMATION = Identifier.of(MOD_ID, "bow_stance_ready");
    public static float BOW_STANCE_READY_SPEED = 1.0f;


    public static List<Identifier> basicAttackList = new ArrayList<>();

    public static boolean debugMode = true;
    public static boolean shouldShowArms = true;
    public static final FirstPersonConfiguration firstPersonConfiguration = new FirstPersonConfiguration();
    public static final CustomSoundListener soundListener = new CustomSoundListener();

    public static void registerAnimations() {
        // Register ability animations
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ARROW_STORM_ANIMATION,       10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ESCAPE_ANIMATION,            10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BOMB_ANIMATION,              10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ARROW_SHIELD_ANIMATION,      10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SPIN_ATTACK_ANIMATION,       10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(DASH_ANIMATION,              10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(MULTI_HIT_ANIMATION,         10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SMOKE_BOMB_ANIMATION,        10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BASH_ANIMATION,              10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(CHARGE_ANIMATION,            10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(UPPERCUT_ANIMATION,          10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(WAR_SCREAM_ANIMATION,        10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(HEAL_ANIMATION,              10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(TELEPORT_ANIMATION,          10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(METEOR_ANIMATION,            10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ICE_SNAKE_ANIMATION,         10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(TOTEM_ANIMATION,             10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(HAUL_ANIMATION,              10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(UPROOT_ANIMATION,            10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(AURA_ANIMATION,              10, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));

        // Register basic attack animations
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BASIC_ATTACK_RELIK,          9, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BASIC_ATTACK_BOW,            9, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BASIC_ATTACK_SPEAR,          9, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BASIC_ATTACK_DAGGER,         9, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BASIC_ATTACK_WAND,           9, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));

        // Register pose animations
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BOW_STANCE_READY_ANIMATION,  8, (player) -> new PlayerAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));

        if (debugMode) System.out.println(WynnanimatedClient.LOG_ID + " Registered WynnAnimated animations");
    }

    private static final WeakHashMap<AbstractClientPlayerEntity, Map<Identifier, SpeedModifier>> playerSpeedModifiers = new WeakHashMap<>();
    public static int lastAnimationPlayedTick = Integer.MIN_VALUE;

    public static void playAnimation(AbstractClientPlayerEntity player, Identifier selectedAnimation, float speedValue) {
        var controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(player, selectedAnimation);
        if (controller != null) {
            Map<Identifier, SpeedModifier> speedMods = playerSpeedModifiers.computeIfAbsent(player, k -> new HashMap<>());

            SpeedModifier speedMod = speedMods.get(selectedAnimation);

            if (speedMod == null) {
                speedMod = new SpeedModifier(speedValue);
                controller.addModifier(speedMod, 0);
                speedMods.put(selectedAnimation, speedMod);
            } else {
                speedMod.speed = speedValue;
                if (debugMode) System.out.println(WynnanimatedClient.LOG_ID + " Updated existing animation speed modifier to " + speedMod.speed);
            }

            ensureCameraPitchModifier(controller);

            // Only update the body-yaw tracking tick for the local player
            if (player == MinecraftClient.getInstance().player) {
                lastAnimationPlayedTick = player.age;
            }

            Animation anim = PlayerAnimResources.getAnimation(selectedAnimation);
            if (anim == null) {
                if (debugMode) System.out.println(WynnanimatedClient.LOG_ID + " Failed to locate animation");
                return;
            }
            controller.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
            controller.setFirstPersonConfiguration(firstPersonConfiguration);
            controller.replaceAnimationWithFade(
                    AbstractFadeModifier.standardFadeIn(2, EasingType.EASE_IN_OUT_SINE),
                    anim,
                    true
            );
        } else {
            if (debugMode) System.out.println(WynnanimatedClient.LOG_ID + " Failed to locate animation");
        }
    }

    private static void ensureCameraPitchModifier(PlayerAnimationController controller) {
        int count = controller.getModifierCount();
        for (int i = 0; i < count; i++) {
            if (controller.getModifier(i) instanceof CameraArmPitchModifier) {
                return;
            }
        }
        controller.addModifierLast(new CameraArmPitchModifier());
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
        var controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(player, selectedAnimation);
        if (controller == null) return;
        controller.stop();
    }

    public static void fadeOutAnimation(AbstractClientPlayerEntity player, Identifier selectedAnimation, int fadeTicks) {
        var controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(player, selectedAnimation);
        if (controller != null && controller.isActive()) {
            controller.replaceAnimationWithFade(
                    AbstractFadeModifier.standardFadeIn(fadeTicks, EasingType.EASE_IN_OUT_SINE),
                    (Animation) null,
                    true
            );
        }
    }

    public static boolean isPlayingCustomAnimation(AbstractClientPlayerEntity player, Identifier animation) {
        var controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(player, animation);
        return controller != null && controller.isActive();
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
            var controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(player, identifier);
            if (controller != null && controller.isActive()) return true;
        }
        return false;
    }

    public static boolean isSpecificSoundPlayingAtCoordinates(Identifier soundId, double x, double y, double z) {
        return soundListener.isSpecificSoundPlayingAtCoordinates(soundId, x, y, z);
    }

    public static void setFirstPersonConfiguration() {
        firstPersonConfiguration
                .setShowRightArm(shouldShowArms)
                .setShowLeftArm(shouldShowArms)
                .setShowLeftItem(shouldShowArms)
                .setShowRightItem(shouldShowArms)
                .setShowArmor(false);
    }

    public static boolean isWynntilsLoaded() {
        return FabricLoader.getInstance().isModLoaded(WYNNTILS_MOD_ID);
    }

    public static void applyConfig() {
        ModConfig cfg = ModConfig.get();

        debugMode = cfg.debugMode;
        shouldShowArms = cfg.showArms;

        // Archer
        ARROW_STORM_SPEED = cfg.arrowStormSpeed;
        ESCAPE_SPEED = cfg.escapeSpeed;
        BOMB_SPEED = cfg.bombSpeed;
        ARROW_SHIELD_SPEED = cfg.arrowShieldSpeed;
        BOW_STANCE_READY_SPEED = cfg.bowStanceReadySpeed;

        // Assassin
        SPIN_ATTACK_SPEED = cfg.spinAttackSpeed;
        DASH_SPEED = cfg.dashSpeed;
        MULTI_HIT_SPEED = cfg.multiHitSpeed;
        SMOKE_BOMB_SPEED = cfg.smokeBombSpeed;

        // Warrior
        BASH_SPEED = cfg.bashSpeed;
        CHARGE_SPEED = cfg.chargeSpeed;
        WAR_SCREAM_SPEED = cfg.warScreamSpeed;
        UPPERCUT_SPEED = cfg.uppercutSpeed;

        // Mage
        HEAL_SPEED = cfg.healSpeed;
        TELEPORT_SPEED = cfg.teleportSpeed;
        METEOR_SPEED = cfg.meteorSpeed;
        ICE_SNAKE_SPEED = cfg.iceSnakeSpeed;

        // Shaman
        TOTEM_SPEED = cfg.totemSpeed;
        HAUL_SPEED = cfg.haulSpeed;
        UPROOT_SPEED = cfg.uprootSpeed;
        AURA_SPEED = cfg.auraSpeed;

        setFirstPersonConfiguration();
    }
}
