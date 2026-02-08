package net.sweenus.wynnanimated.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.sweenus.wynnanimated.Wynnanimated;
import net.sweenus.wynnanimated.client.WynnanimatedClient;
import net.sweenus.wynnanimated.client.util.SpellCastHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerMixin {

    @Unique
    AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;

    @Unique
    private boolean wasHandSwinging = false;
    @Unique
    private int lastHandSwingTicks = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    private void wynnanimated$tick(CallbackInfo ci) {

        if (player.isSneaking())
            MinecraftClient.getInstance().execute(() -> {
                //Debug
                //SpellCastHandler.performSpellAnimation("Spin Attack");
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.TEST_ANIMATION, WynnanimatedClient.TEST_SPEED);
            });

        // Detect the start of a new swing rather than polling handSwingProgress
        // Only count swings where the player is actually pressing the attack key,
        // to filter out server-triggered swing animations from Wynncraft
        boolean attackKeyPressed = MinecraftClient.getInstance().options.attackKey.isPressed();
        boolean isNewSwing = attackKeyPressed
                && ((player.handSwinging && !wasHandSwinging)
                || (player.handSwinging && player.handSwingTicks < lastHandSwingTicks));
        wasHandSwinging = player.handSwinging;
        lastHandSwingTicks = player.handSwingTicks;

        if (isNewSwing) {
            if (SpellCastHandler.performAttackAnimation()) {
                // Suppress vanilla hand swing when custom animation is playing
                player.handSwinging = false;
            }
        } else if (!WynnanimatedClient.isPlayingAnyAnimation(player, WynnanimatedClient.basicAttackList)
                && player.isUsingItem()) {
            SpellCastHandler.performAttackAnimation();
        }


    }

}