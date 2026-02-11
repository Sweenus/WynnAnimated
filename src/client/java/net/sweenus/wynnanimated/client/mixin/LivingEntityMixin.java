package net.sweenus.wynnanimated.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.sweenus.wynnanimated.client.AnimationRegistry;
import net.sweenus.wynnanimated.client.WynnanimatedClient;
import net.sweenus.wynnanimated.client.config.ModConfig;
import net.sweenus.wynnanimated.client.util.AttackTracker;
import net.sweenus.wynnanimated.client.util.WynnPlayerClassCache;
import net.sweenus.wynnanimated.client.util.WynnWeaponResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique
    LivingEntity livingEntity = (LivingEntity) (Object) this;

    @Unique
    private static double getAnimationRangeSq() {
        int range = ModConfig.get().animationRange;
        return (double) range * range;
    }

    @Unique
    private static boolean getPlayerAnimations() {
        return ModConfig.get().animateOtherPlayers;
    }

    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;)V", at = @At("HEAD"), cancellable = true)
    private void wynnanimated$cancelSwing(Hand hand, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();

        // Suppress vanilla arm swing for local player when custom animation is active
        if (livingEntity == mc.player
                && livingEntity.age - AttackTracker.lastAnimationTick < AttackTracker.SWING_SUPPRESS_TICKS) {
            ci.cancel();
        }

        // Play attack animation for other players on main hand swing (requires privacy setting to be open: characterDataAccess)
        if (hand == Hand.MAIN_HAND
                && livingEntity instanceof AbstractClientPlayerEntity otherPlayer
                && livingEntity != mc.player
                && mc.player != null
                && getPlayerAnimations()
                && otherPlayer.squaredDistanceTo(mc.player) <= getAnimationRangeSq()) {
            String classType = WynnPlayerClassCache.getPlayerClass(otherPlayer.getGameProfile().getName());
            if (classType != null) {
                Identifier animId = WynnWeaponResolver.resolveAttackAnimationFromClass(classType);
                if (animId != null && !AnimationRegistry.isPlayingCustomAnimation(otherPlayer, animId)) {
                    AnimationRegistry.playAnimation(otherPlayer, animId, 1.5f);
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void wynnanimated$tickItemUse(CallbackInfo ci) {
        if (!(livingEntity instanceof AbstractClientPlayerEntity otherPlayer)) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || livingEntity == mc.player) return;
        if ( !getPlayerAnimations() || otherPlayer.squaredDistanceTo(mc.player) > getAnimationRangeSq()) return;

        // Detect bow sounds relative to other player position (don't know a better way to detect bow basic attacks)
        // Requires the other player to be on the same channel (does not work with player ghosts)
        if (AnimationRegistry.isSpecificSoundPlayingAtCoordinates(
                Identifier.of("minecraft", "entity.splash_potion.throw"),
                otherPlayer.getX(), otherPlayer.getY(), otherPlayer.getZ())) {
            //if (AnimationRegistry.debugMode)
                //System.out.println(WynnanimatedClient.LOG_ID + " Detected bow shoot sound from " + otherPlayer.getDisplayName() + "'s position");

            String classType = WynnPlayerClassCache.getPlayerClass(otherPlayer.getGameProfile().getName());
            if ("ARCHER".equals(classType)) {
                Identifier animId = AnimationRegistry.BASIC_ATTACK_BOW;
                if (!AnimationRegistry.isPlayingCustomAnimation(otherPlayer, animId)) {
                    AnimationRegistry.playAnimation(otherPlayer, animId, 1.8f);
                }
            }
        }
    }
}
