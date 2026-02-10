package net.sweenus.wynnanimated.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.sweenus.wynnanimated.client.AnimationRegistry;
import net.sweenus.wynnanimated.client.util.SpellCastHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@Mixin(targets = "com.wynntils.models.spells.SpellModel")
public class SpellModelMixin {

    @Inject(
            method = "onSpellCast(Lcom/wynntils/models/spells/event/SpellEvent$Cast;)V",
            at = @At("TAIL"),
            remap = false
    )
    private void wynnanimated$onSpellCast(
            @Coerce Object event,
            CallbackInfo ci
    ) {
        try {
            Method getSpellType = event.getClass().getMethod("getSpellType");
            Object spellType = getSpellType.invoke(event);

            Method getName = spellType.getClass().getMethod("getName");
            String spellName = (String) getName.invoke(spellType);

            if (AnimationRegistry.debugMode) System.out.println("Spell cast: " + spellName);
            MinecraftClient.getInstance().execute(() -> {
                SpellCastHandler.performSpellAnimation(spellName);
            });

        } catch (ReflectiveOperationException ignored) {}
    }
}
