package net.sweenus.wynnanimated.client.anim;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractModifier;
import com.zigythebird.playeranimcore.bones.PlayerAnimBone;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.Set;

public class CameraArmPitchModifier extends AbstractModifier {
    private static final float PITCH_MULTIPLIER = 1.0f;
    private static final float PITCH_DOWN_START_DEGREES = 10.0f;
    private static final float MAX_PITCH_DOWN_DEGREES = 90.0f;
    private static final float MAX_FORWARD_OFFSET = 9.0f;
    private static final Set<String> IGNORE_ANIMATIONS = Set.of(
            "basic_attack_spear",
            "bash"
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
        if (current != null && IGNORE_ANIMATIONS.contains(current.getNameOrId())) {
            return transformed;
        }

        String name = transformed.getName();
        if (!isArmBone(name)) return transformed;

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
        return transformed;
    }

    private static boolean isArmBone(String name) {
        return "right_arm".equals(name) || "left_arm".equals(name);
    }
}
