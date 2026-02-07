package net.sweenus.wynnanimated.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.sweenus.wynnanimated.client.util.SpellCastHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.wynntils.features.combat.AutoAttackFeature")
public class AutoAttackFeatureMixin {

    @Inject(
            method = "onTick(Lcom/wynntils/mc/event/TickEvent;)V",
            at = @At("TAIL")
    )
    private void wynnanimated$onTick(
            @Coerce Object event,
            CallbackInfo ci
    ) {
        var mc = MinecraftClient.getInstance();
        var player = mc.player;
        if (player == null) return;

        // At this point:
        // - world is valid
        // - weapon is usable
        // - attack input was accepted
        // - attack was just sent
        MinecraftClient.getInstance().execute(SpellCastHandler::performAttackAnimation);
    }
}