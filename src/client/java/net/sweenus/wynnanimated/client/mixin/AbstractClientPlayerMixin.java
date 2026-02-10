package net.sweenus.wynnanimated.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.sweenus.wynnanimated.client.WynnanimatedClient;
import net.sweenus.wynnanimated.client.util.AttackTracker;
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

    // Archer combat ready stance
    @Unique
    private boolean wasPlayingBowAttack = false;
    @Unique
    private int lastBowAttackTick = Integer.MIN_VALUE;
    @Unique
    private static final int STANCE_TIMEOUT_TICKS = 40; // ~2 seconds

    @Inject(method = "tick", at = @At("TAIL"))
    private void wynnanimated$tick(CallbackInfo ci) {
        if (MinecraftClient.getInstance().player != (Object) this) return;

        if (player.isSneaking())
            MinecraftClient.getInstance().execute(() -> {
                //Debug
                //SpellCastHandler.performSpellAnimation("Spin Attack");
                //WynnanimatedClient.playAnimation(player, WynnanimatedClient.TEST_ANIMATION, WynnanimatedClient.TEST_SPEED);
            });

        // Detect the start of a new swing
        boolean isNewSwing = AttackTracker.attackInitiated
                && ((player.handSwinging && !wasHandSwinging)
                || (player.handSwinging && player.handSwingTicks < lastHandSwingTicks));
        wasHandSwinging = player.handSwinging;
        lastHandSwingTicks = player.handSwingTicks;

        if (isNewSwing) {
            AttackTracker.attackInitiated = false;
            if (SpellCastHandler.performAttackAnimation()) {
                // Suppress vanilla hand swing when custom animation is playing
                player.handSwinging = false;
            }
        } else if (!WynnanimatedClient.isPlayingAnyAnimation(player, WynnanimatedClient.basicAttackList)
                && player.isUsingItem()
                && player.getItemCooldownManager().isCoolingDown(player.getMainHandStack())) {
            SpellCastHandler.performAttackAnimation();
        }

        // Update attack animation speed pacing (normal speed -> slow tail)
        SpellCastHandler.tickAttackAnimationSpeed(player);

        // Smoothly align body toward camera direction during animations so first-person arms track correctly
        if (player.age - WynnanimatedClient.lastAnimationPlayedTick < AttackTracker.SWING_SUPPRESS_TICKS) {
            float diff = MathHelper.wrapDegrees(player.getYaw() - player.bodyYaw);
            player.bodyYaw += diff * 0.9f;
        }

        // --- Archer combat ready stance ---
        // Stance layer is priority 8, bow attack is priority 9.
        // The attack layer fully hides the stance while active, so we start the stance
        // *during* the attack — when the attack ends, the stance shows through instantly
        // with no flash to the default pose.
        boolean playingBowAttack = WynnanimatedClient.isPlayingCustomAnimation(player, WynnanimatedClient.BOW_SHOOT_VERTICAL_ANIMATION);

        // Bow attack just started → ensure stance is playing underneath (hidden by higher-priority attack layer)
        if (playingBowAttack && !wasPlayingBowAttack) {
            if (!WynnanimatedClient.isPlayingCustomAnimation(player, WynnanimatedClient.BOW_STANCE_READY_ANIMATION)) {
                WynnanimatedClient.playAnimation(player, WynnanimatedClient.BOW_STANCE_READY_ANIMATION, WynnanimatedClient.BOW_STANCE_READY_SPEED);
            }
        }

        if (playingBowAttack) {
            lastBowAttackTick = player.age;
        }
        wasPlayingBowAttack = playingBowAttack;

        // Idle timeout → fade out stance and return to normal
        if (WynnanimatedClient.isPlayingCustomAnimation(player, WynnanimatedClient.BOW_STANCE_READY_ANIMATION)
                && player.age - lastBowAttackTick > STANCE_TIMEOUT_TICKS) {
            WynnanimatedClient.fadeOutAnimation(player, WynnanimatedClient.BOW_STANCE_READY_ANIMATION, 5);
        }
    }

}
