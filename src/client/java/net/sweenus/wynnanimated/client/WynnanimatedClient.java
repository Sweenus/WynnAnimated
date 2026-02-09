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
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.sweenus.wynnanimated.client.util.WynnCooldownObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class WynnanimatedClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("WynnAnimated");
    public static final String MOD_ID = "wynnanimated";
    public static final String WYNNTILS_MOD_ID = "wynntils";

    public static final Identifier SPIN_ANIMATION = Identifier.of(MOD_ID, "spin");
    public static final float SPIN_SPEED = 2.5f;
    public static final Identifier SPIN_LONG_ANIMATION = Identifier.of(MOD_ID, "spin_long");
    public static final float SPIN_LONG_SPEED = 2.5f;
    public static final Identifier THROW_ANIMATION = Identifier.of(MOD_ID, "throw");
    public static final float THROW_SPEED = 1.9f;
    public static final Identifier RANGED_SLASH_ANIMATION = Identifier.of(MOD_ID, "ranged_slash");
    public static final float RANGED_SLASH_SPEED = 2.1f;
    public static final Identifier SLAM_ANIMATION = Identifier.of(MOD_ID, "two_handed_slam");
    public static final float SLAM_SPEED = 2.1f;
    public static final Identifier UP_SLASH_ANIMATION = Identifier.of(MOD_ID, "up_slash");
    public static final float UP_SLASH_SPEED = 1.4f;
    public static final Identifier GROUND_CLEAVE_ANIMATION = Identifier.of(MOD_ID, "ground_cleave");
    public static final float GROUND_CLEAVE_SPEED = 2.1f;
    public static final Identifier BATTLECRY_ANIMATION = Identifier.of(MOD_ID, "battlecry");
    public static final float BATTLECRY_SPEED = 1.5f;
    public static final Identifier UPPERCUT_ANIMATION = Identifier.of(MOD_ID, "uppercut");
    public static final float UPPERCUT_SPEED = 1.8f;
    public static final Identifier SPELL_CAST_ANIMATION = Identifier.of(MOD_ID, "spell_cast");
    public static final float SPELL_CAST_SPEED = 1.0f;
    public static final Identifier SPELL_CAST_ALT_ANIMATION = Identifier.of(MOD_ID, "spell_cast_alt");
    public static final float SPELL_CAST_ALT_SPEED = 1.6f;
    public static final Identifier SPELL_ICE_SNAKE_ANIMATION = Identifier.of(MOD_ID, "spell_ice_snake");
    public static final float SPELL_ICE_SNAKE_SPEED = 1.1f;
    public static final Identifier SPELL_HEAL_ANIMATION = Identifier.of(MOD_ID, "spell_heal");
    public static final float SPELL_HEAL_SPEED = 1.2f;
    public static final Identifier SPELL_AURA_ANIMATION = Identifier.of(MOD_ID, "spell_aura");
    public static final float SPELL_AURA_SPEED = 1.0f;


    public static final Identifier BOW_SHOOT_ANIMATION = Identifier.of(MOD_ID, "bow_shoot_horizontal");
    public static final Identifier BOW_RAPIDFIRE_VERTICAL_ANIMATION = Identifier.of(MOD_ID, "bow_rapidfire_vertical");
    public static final float BOW_RAPIDFIRE_VERTICAL_SPEED = 2.2f;
    public static final float BOW_SHOOT_HORIZONTAL_SPEED = 1.2f;
    public static final Identifier BOW_SHOOT_VERTICAL_ANIMATION = Identifier.of(MOD_ID, "bow_shoot_vertical");
    public static final Identifier SLASH_RIGHT_ANIMATION = Identifier.of(MOD_ID, "slash_right");
    public static final float SLASH_RIGHT_SPEED = 1.0f;
    public static final Identifier SLASH_LEFT_ANIMATION = Identifier.of(MOD_ID, "slash_left");
    public static final float SLASH_LEFT_SPEED = 1.0f;
    public static final Identifier SWING_ANIMATION = Identifier.of(MOD_ID, "two_handed_swing_alt");
    public static final float SWING_SPEED = 1.0f;
    public static final float THROW_SHAMAN_SPEED = 1.2f;
    public static final Identifier ROGUE_SLASH_ANIMATION = Identifier.of(MOD_ID, "rogue_slash");


    public static final Identifier TEST_ANIMATION = Identifier.of(MOD_ID, "two_handed_swing");
    public static final float TEST_SPEED = 1.2f;

    public static List<Identifier> basicAttackList = new ArrayList<>();

    public static boolean shouldShowArms = true; // Make this configurable later
    public static final FirstPersonConfiguration firstPersonConfiguration = new FirstPersonConfiguration();

    @Override
    public void onInitializeClient() {
        registerAnimations();
        setFirstPersonConfiguration();
        createLists();

        // Track cooldowns for basic attack animation speed
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player instanceof AbstractClientPlayerEntity player) {
                WynnCooldownObserver.tick(player);
            }
        });
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

    public static void registerAnimations() {

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SPIN_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SPIN_LONG_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(THROW_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(RANGED_SLASH_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SLAM_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(UP_SLASH_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(GROUND_CLEAVE_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BOW_SHOOT_ANIMATION, 9,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BOW_RAPIDFIRE_VERTICAL_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BOW_SHOOT_VERTICAL_ANIMATION, 9,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SLASH_RIGHT_ANIMATION, 9,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SLASH_LEFT_ANIMATION, 9,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(BATTLECRY_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SWING_ANIMATION, 9,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(TEST_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ROGUE_SLASH_ANIMATION, 9,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SPELL_CAST_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SPELL_CAST_ALT_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SPELL_ICE_SNAKE_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SPELL_HEAL_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SPELL_AURA_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(UPPERCUT_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        System.out.println("Registered WynnAnimated animations");
    }

    private static final Map<Identifier, SpeedModifier> speedModifiers = new HashMap<>();

    public static void playAnimation(AbstractClientPlayerEntity player, Identifier selectedAnimation, float speedValue) {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(selectedAnimation);
        if (animation != null) {
            SpeedModifier speedMod = speedModifiers.get(selectedAnimation);

            if (speedMod == null) {
                // First time - create and add the modifier
                speedMod = new SpeedModifier(speedValue);
                animation.addModifier(speedMod, 0);
                speedModifiers.put(selectedAnimation, speedMod);
            } else {
                // Update the existing modifier's speed
                speedMod.speed = speedValue;
                System.out.println("Updated existing animation speed modifier to " + speedMod.speed);
            }

            animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(2, Ease.INOUTSINE),
                    new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(selectedAnimation))
                            .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                            .setFirstPersonConfiguration(firstPersonConfiguration), true);
        } else {
            System.out.println("Failed to locate animation");
        }
    }

    public static void updateAnimationSpeed(Identifier animationId, float speedValue) {
        SpeedModifier speedMod = speedModifiers.get(animationId);
        if (speedMod != null) {
            speedMod.speed = speedValue;
        }
    }

    public static void stopAnimation(AbstractClientPlayerEntity player, Identifier selectedAnimation) {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(selectedAnimation);
        if (animation == null) return;

        var layer = ((KeyframeAnimationPlayer)animation.getAnimation());
        if (layer != null && layer.getData().getName().equals(selectedAnimation))
            layer.stop();
    }

    public static boolean isPlayingCustomAnimation(AbstractClientPlayerEntity player, Identifier animation) {
        var anim = PlayerAnimationAccess.getPlayerAssociatedData(player).get(animation);
        return anim != null && anim.isActive();
    }

    public static void createLists() {
        basicAttackList.add(SWING_ANIMATION);
        basicAttackList.add(BOW_SHOOT_ANIMATION);
        basicAttackList.add(SLASH_LEFT_ANIMATION);
        basicAttackList.add(SLASH_RIGHT_ANIMATION);
        basicAttackList.add(THROW_ANIMATION);
    }

    public static boolean isPlayingAnyAnimation(AbstractClientPlayerEntity player, List<Identifier> list) {
        for (Identifier identifier : list) {
            var anim = PlayerAnimationAccess.getPlayerAssociatedData(player).get(identifier);
            if (anim != null && anim.isActive()) return true;
        }
        return false;
    }


}