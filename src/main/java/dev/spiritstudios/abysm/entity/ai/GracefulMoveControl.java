package dev.spiritstudios.abysm.entity.ai;

import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;

public class GracefulMoveControl extends AquaticMoveControl {
	public GracefulMoveControl(MobEntity entity, int pitchChange, int yawChange, float speedInWater, float speedInAir, boolean buoyant) {
		super(entity, pitchChange, yawChange, speedInWater, speedInAir, buoyant);
	}

	@Override
	public float changeAngle(float start, float end, float maxChange) {
		return MathHelper.lerpAngleDegrees(1 / 20F, start, end);
	}
}
