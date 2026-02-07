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
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WynnanimatedClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("WynnAnimated");
    public static final String MOD_ID = "wynnanimated";
    public static final String WYNNTILS_MOD_ID = "wynntils";

    public static final Identifier SPIN_ANIMATION = Identifier.of(MOD_ID, "spin");
    public static final SpeedModifier SPIN_SPEED = new SpeedModifier(2.5f);
    public static final Identifier SPIN_LONG_ANIMATION = Identifier.of(MOD_ID, "spin_long");
    public static final SpeedModifier SPIN_LONG_SPEED = new SpeedModifier(2.5f);
    public static final Identifier THROW_ANIMATION = Identifier.of(MOD_ID, "throw");
    public static final SpeedModifier THROW_SPEED = new SpeedModifier(1.9f);
    public static final Identifier RANGED_SLASH_ANIMATION = Identifier.of(MOD_ID, "ranged_slash");
    public static final SpeedModifier RANGED_SLASH_SPEED = new SpeedModifier(2.1f);
    public static final Identifier SLAM_ANIMATION = Identifier.of(MOD_ID, "two_handed_slam");
    public static final SpeedModifier SLAM_SPEED = new SpeedModifier(2.1f);
    public static final Identifier UPWARD_SLASH_ANIMATION = Identifier.of(MOD_ID, "upward_slash");
    public static final SpeedModifier UPWARD_SLASH_SPEED = new SpeedModifier(2.1f);
    public static final Identifier GROUND_CLEAVE_ANIMATION = Identifier.of(MOD_ID, "ground_cleave");
    public static final SpeedModifier GROUND_CLEAVE_SPEED = new SpeedModifier(2.1f);


    public static final Identifier RAPIDFIRE_HORIZONTAL_ANIMATION = Identifier.of(MOD_ID, "rapidfire_horizontal");
    public static final SpeedModifier RAPIDFIRE_HORIZONTAL_SPEED = new SpeedModifier(2.1f);
    public static final SpeedModifier RAPIDFIRE_HORIZONTAL_SLOW_SPEED = new SpeedModifier(0.9f);
    public static final Identifier SLASH_RIGHT_ANIMATION = Identifier.of(MOD_ID, "slash_right");
    public static final SpeedModifier SLASH_RIGHT_SPEED = new SpeedModifier(2.1f);
    public static final Identifier SLASH_LEFT_ANIMATION = Identifier.of(MOD_ID, "slash_left");
    public static final SpeedModifier SLASH_LEFT_SPEED = new SpeedModifier(2.1f);

    public static boolean shouldShowArms = true; // Make this configurable later
    public static final FirstPersonConfiguration firstPersonConfiguration = new FirstPersonConfiguration();

    @Override
    public void onInitializeClient() {
        registerAnimations();
        setFirstPersonConfiguration();
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

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(UPWARD_SLASH_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(GROUND_CLEAVE_ANIMATION, 10,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(RAPIDFIRE_HORIZONTAL_ANIMATION, 9,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SLASH_RIGHT_ANIMATION, 9,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(SLASH_LEFT_ANIMATION, 9,
                (AbstractClientPlayerEntity -> new ModifierLayer<>()));

        System.out.println("Registered WynnAnimated animations");
    }

    public static void playAnimation(AbstractClientPlayerEntity player, Identifier selectedAnimation, SpeedModifier animationSpeed) {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(selectedAnimation);
        if (animation != null) {
            animation.addModifier(animationSpeed, 0);
            animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(2, Ease.INOUTSINE),
                    new KeyframeAnimationPlayer((KeyframeAnimation) PlayerAnimationRegistry.getAnimation(selectedAnimation))
                            .setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                            .setFirstPersonConfiguration(firstPersonConfiguration), true);
        } else {
            System.out.println("Failed to locate animation");
        }
    }

    public static void stopAnimation(AbstractClientPlayerEntity player, Identifier selectedAnimation) {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(selectedAnimation);
        if (animation == null) return;

        var layer = ((KeyframeAnimationPlayer)animation.getAnimation());
        if (layer != null && layer.getData().getName().equals(selectedAnimation))
            layer.stop();
    }

}