package net.sweenus.wynnanimated.client.anim;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractModifier;
import com.zigythebird.playeranimcore.bones.PlayerAnimBone;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.MathHelper;

public class CameraArmPitchModifier extends AbstractModifier {
    private static final float PITCH_MULTIPLIER = 1.0f;

    @Override
    public PlayerAnimBone get3DTransform(PlayerAnimBone bone) {
        PlayerAnimBone transformed = super.get3DTransform(bone);

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return transformed;

        if (!(getController() instanceof PlayerAnimationController controller)) return transformed;
        if (!(controller.getAvatar() instanceof AbstractClientPlayerEntity avatar)) return transformed;
        if (client.options.getPerspective() == Perspective.FIRST_PERSON && avatar != client.player) return transformed;

        String name = transformed.getName();
        if (!isArmBone(name)) return transformed;

        float pitchRad = avatar.getPitch() * MathHelper.RADIANS_PER_DEGREE;
        transformed.addRot(pitchRad * PITCH_MULTIPLIER, 0.0f, 0.0f);
        return transformed;
    }

    private static boolean isArmBone(String name) {
        return "right_arm".equals(name) || "left_arm".equals(name);
    }
}
