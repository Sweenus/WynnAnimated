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

    @Inject(method = "tick", at = @At("TAIL"))
    private void wynnanimated$tick(CallbackInfo ci) {

        if (player.isSneaking())
            MinecraftClient.getInstance().execute(() -> {
                //Debug
                //SpellCastHandler.performSpellAnimation("Spin Attack");
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.TEST_ANIMATION, WynnanimatedClient.TEST_SPEED);
            });

        if (!WynnanimatedClient.isPlayingAnyAnimation(player, WynnanimatedClient.basicAttackList)) {
            if (player.handSwinging && player.handSwingProgress < 0.25f) {
                SpellCastHandler.performAttackAnimation();
            } else if (player.isUsingItem()){
                SpellCastHandler.performAttackAnimation();
            }
        }


    }

}