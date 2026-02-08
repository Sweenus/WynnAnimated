package net.sweenus.wynnanimated.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.sweenus.wynnanimated.client.util.AttackTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique
    LivingEntity livingEntity = (LivingEntity) (Object) this;

    @Inject(method = "swingHand(Lnet/minecraft/util/Hand;)V", at = @At("HEAD"), cancellable = true)
    private void wynnanimated$cancelSwing(Hand hand, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (livingEntity == mc.player
                && livingEntity.age - AttackTracker.lastAnimationTick < AttackTracker.SWING_SUPPRESS_TICKS) {
            ci.cancel();
        }
    }
}
