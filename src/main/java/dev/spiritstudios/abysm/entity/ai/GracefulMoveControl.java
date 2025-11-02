package dev.spiritstudios.abysm.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;

public class GracefulMoveControl extends SmoothSwimmingMoveControl {
	public GracefulMoveControl(Mob entity, int pitchChange, int yawChange, float speedInWater, float speedInAir, boolean buoyant) {
		super(entity, pitchChange, yawChange, speedInWater, speedInAir, buoyant);
	}

	@Override
	public float rotateTowards(float start, float end, float maxChange) {
		return Mth.rotLerp(1 / 20F, start, end);
	}
}
