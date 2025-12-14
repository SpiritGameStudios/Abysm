package dev.spiritstudios.abysm.world.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

public class GracefulLookControl extends LookControl {
	private final int yawAdjustThreshold;
	private static final int ADDED_PITCH = 10;
	private static final int ADDED_YAW = 20;

	public GracefulLookControl(Mob entity, int yawAdjustThreshold) {
		super(entity);
		this.yawAdjustThreshold = yawAdjustThreshold;
	}

	@Override
	public void tick() {
		if (this.lookAtCooldown > 0) {
			this.lookAtCooldown--;
			this.getYRotD().ifPresent(yaw -> this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, yaw, this.yMaxRotSpeed));
			this.getXRotD().ifPresent(pitch -> this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), pitch, this.xMaxRotAngle)));
		} else {
			if (this.mob.getNavigation().isDone()) {
				this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), 0.0F, 5.0F));
			}

			this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, this.yMaxRotSpeed);
		}

//		float f = MathHelper.wrapDegrees(this.entity.headYaw - this.entity.bodyYaw);
//		if (f < -this.yawAdjustThreshold) {
//			this.entity.bodyYaw -= 4.0F;
//		} else if (f > this.yawAdjustThreshold) {
//			this.entity.bodyYaw += 4.0F;
//		}
	}

	@Override
	public float rotateTowards(float start, float end, float maxChange) {
		return Mth.rotLerp(1/20F, start, end);
	}
}
