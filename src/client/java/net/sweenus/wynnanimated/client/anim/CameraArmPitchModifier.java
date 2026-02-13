package net.sweenus.wynnanimated.client.anim;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractModifier;
import com.zigythebird.playeranimcore.bones.PlayerAnimBone;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.sweenus.wynnanimated.client.config.ModConfig;

import java.util.Set;

public class CameraArmPitchModifier extends AbstractModifier {
    private static final float PITCH_MULTIPLIER = 1.4f;
    private static final float PITCH_DOWN_START_DEGREES = 10.0f;
    private static final float MAX_PITCH_DOWN_DEGREES = 90.0f;
    private static final float MAX_FORWARD_OFFSET = 9.0f;
    private static final float ARCHER_RIGHT_SHIFT_TO_FORWARD_FACTOR = 0.25f;
    private static final Set<String> IGNORE_ANIMATIONS = Set.of(
            "basic_attack_spear",
            "bash"
    );
    private static final Set<String> RIGHT_SHIFT_ARCHER_ANIMATIONS = Set.of(
            "arrow_storm",
            "escape",
            "arrow_bomb",
            "arrow_shield",
            "basic_attack_bow",
            "bow_stance_ready"
    );
    private static final Set<String> ARCHER_ANIMATIONS = Set.of(
            "arrow_storm",
            "escape",
            "arrow_bomb",
            "arrow_shield",
            "basic_attack_bow",
            "bow_stance_ready"
    );
    private static final Set<String> ASSASSIN_ANIMATIONS = Set.of(
            "spin_attack",
            "dash",
            "multi_hit",
            "smoke_bomb",
            "basic_attack_dagger"
    );
    private static final Set<String> WARRIOR_ANIMATIONS = Set.of(
            "bash",
            "charge",
            "war_scream",
            "uppercut",
            "basic_attack_spear"
    );
    private static final Set<String> MAGE_ANIMATIONS = Set.of(
            "heal",
            "teleport",
            "meteor",
            "ice_snake",
            "basic_attack_wand"
    );
    private static final Set<String> SHAMAN_ANIMATIONS = Set.of(
            "totem",
            "haul",
            "uproot",
            "aura",
            "basic_attack_relik"
    );

    @Override
    public PlayerAnimBone get3DTransform(PlayerAnimBone bone) {
        PlayerAnimBone transformed = super.get3DTransform(bone);

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return transformed;

        if (!(getController() instanceof PlayerAnimationController controller)) return transformed;
        if (!(controller.getAvatar() instanceof AbstractClientPlayerEntity avatar)) return transformed;
        boolean isFirstPerson = client.options.getPerspective() == Perspective.FIRST_PERSON;
        if (isFirstPerson && avatar != client.player) return transformed;

        Animation current = controller.getCurrentAnimationInstance();
        String currentName = current != null ? current.getNameOrId() : null;
        boolean skipPitchOffset = currentName != null && IGNORE_ANIMATIONS.contains(currentName);

        String name = transformed.getName();
        if (!isArmBone(name)) return transformed;

        if (!skipPitchOffset) {
            float pitchDeg = avatar.getPitch();
            float pitchRad = pitchDeg * MathHelper.RADIANS_PER_DEGREE;
            transformed.addRot(pitchRad * PITCH_MULTIPLIER, 0.0f, 0.0f);

            if (isFirstPerson) {
                float clamped = MathHelper.clamp(pitchDeg, PITCH_DOWN_START_DEGREES, MAX_PITCH_DOWN_DEGREES);
                if (clamped > PITCH_DOWN_START_DEGREES) {
                    float t = (clamped - PITCH_DOWN_START_DEGREES) / (MAX_PITCH_DOWN_DEGREES - PITCH_DOWN_START_DEGREES);
                    float forwardOffset = MAX_FORWARD_OFFSET * t;
                    // Move arms toward the camera in first person when looking down.
                    transformed.addPos(0.0f, 0.0f, forwardOffset);
                }
            }
        }

        if (isFirstPerson && currentName != null) {
            if (RIGHT_SHIFT_ARCHER_ANIMATIONS.contains(currentName)) {
                float rightShift = ModConfig.get().firstPersonArcherRightShift;
                if (rightShift != 0.0f) {
                    transformed.addPos(-rightShift, 0.0f, 0.0f);
                    // Keep the archer offset anchored by moving slightly toward the player
                    // based on the same right-shift value.
                    transformed.addPos(0.0f, 0.0f, rightShift * ARCHER_RIGHT_SHIFT_TO_FORWARD_FACTOR);
                }
            }

            float downShift = getClassDownShift(currentName);
            if (downShift != 0.0f) {
                transformed.addPos(0.0f, -downShift, 0.0f);
            }
        }
        return transformed;
    }

    private static float getClassDownShift(String animationName) {
        ModConfig cfg = ModConfig.get();
        if (ARCHER_ANIMATIONS.contains(animationName)) return cfg.firstPersonArcherDownShift;
        if (ASSASSIN_ANIMATIONS.contains(animationName)) return cfg.firstPersonAssassinDownShift;
        if (WARRIOR_ANIMATIONS.contains(animationName)) return cfg.firstPersonWarriorDownShift;
        if (MAGE_ANIMATIONS.contains(animationName)) return cfg.firstPersonMageDownShift;
        if (SHAMAN_ANIMATIONS.contains(animationName)) return cfg.firstPersonShamanDownShift;
        return 0.0f;
    }

    private static boolean isArmBone(String name) {
        return "right_arm".equals(name) || "left_arm".equals(name);
    }
}
