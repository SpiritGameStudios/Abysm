package dev.spiritstudios.abysm.entity.ai;

import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;

public class GracefulLookControl extends LookControl {
	private final int yawAdjustThreshold;
	private static final int ADDED_PITCH = 10;
	private static final int ADDED_YAW = 20;

	public GracefulLookControl(MobEntity entity, int yawAdjustThreshold) {
		super(entity);
		this.yawAdjustThreshold = yawAdjustThreshold;
	}

	@Override
	public void tick() {
		if (this.lookAtTimer > 0) {
			this.lookAtTimer--;
			this.getTargetYaw().ifPresent(yaw -> this.entity.headYaw = this.changeAngle(this.entity.headYaw, yaw, this.maxYawChange));
			this.getTargetPitch().ifPresent(pitch -> this.entity.setPitch(this.changeAngle(this.entity.getPitch(), pitch, this.maxPitchChange)));
		} else {
			if (this.entity.getNavigation().isIdle()) {
				this.entity.setPitch(this.changeAngle(this.entity.getPitch(), 0.0F, 5.0F));
			}

			this.entity.headYaw = this.changeAngle(this.entity.headYaw, this.entity.bodyYaw, this.maxYawChange);
		}

//		float f = MathHelper.wrapDegrees(this.entity.headYaw - this.entity.bodyYaw);
//		if (f < -this.yawAdjustThreshold) {
//			this.entity.bodyYaw -= 4.0F;
//		} else if (f > this.yawAdjustThreshold) {
//			this.entity.bodyYaw += 4.0F;
//		}
	}

	@Override
	public float changeAngle(float start, float end, float maxChange) {
		return MathHelper.lerpAngleDegrees(1/20F, start, end);
	}
}
