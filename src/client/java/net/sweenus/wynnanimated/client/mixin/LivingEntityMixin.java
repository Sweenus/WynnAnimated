package net.sweenus.wynnanimated.client.mixin;

import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.sweenus.wynnanimated.client.WynnanimatedClient;
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
    private static final double ANIMATION_RANGE_SQ = 20.0 * 20.0; // 32 blocks

    @Unique
    private boolean wasUsingItem = false;

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
                && otherPlayer.squaredDistanceTo(mc.player) <= ANIMATION_RANGE_SQ) {
            String classType = WynnPlayerClassCache.getPlayerClass(otherPlayer.getGameProfile().getName());
            if (classType != null) {
                Identifier animId = WynnWeaponResolver.resolveAttackAnimationFromClass(classType);
                if (animId != null && !WynnanimatedClient.isPlayingCustomAnimation(otherPlayer, animId)) {
                    WynnanimatedClient.playAnimation(otherPlayer, animId, 1.0f);
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void wynnanimated$tickItemUse(CallbackInfo ci) {
        // Early exit for non-player entities (covers mobs, animals, etc.)
        if (!(livingEntity instanceof AbstractClientPlayerEntity otherPlayer)) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || livingEntity == mc.player) return;
        if (otherPlayer.squaredDistanceTo(mc.player) > ANIMATION_RANGE_SQ) return;

        // Detect transition: not using item -> using item (archer bow attack)
        boolean usingItem = otherPlayer.isUsingItem();
        boolean startedUsingItem = usingItem && !wasUsingItem;
        wasUsingItem = usingItem;

        if (startedUsingItem) {
            String classType = WynnPlayerClassCache.getPlayerClass(otherPlayer.getGameProfile().getName());
            if ("ARCHER".equals(classType)) {
                Identifier animId = WynnanimatedClient.BOW_SHOOT_VERTICAL_ANIMATION;
                if (!WynnanimatedClient.isPlayingCustomAnimation(otherPlayer, animId)) {
                    WynnanimatedClient.playAnimation(otherPlayer, animId, 1.0f);
                }
            }
        }
    }
}
