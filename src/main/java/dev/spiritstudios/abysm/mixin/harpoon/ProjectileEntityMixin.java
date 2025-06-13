package dev.spiritstudios.abysm.mixin.harpoon;

import dev.spiritstudios.abysm.entity.harpoon.HarpoonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends Entity {

	@Shadow
	private boolean leftOwner;

	public ProjectileEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "updateRotation()V", at = @At("HEAD"), cancellable = true)
	private void snapToIt(CallbackInfo ci) {
		if ((ProjectileEntity) (Object) this instanceof HarpoonEntity harpoon) {
			Vec3d vec3d = this.getVelocity();
			double d = vec3d.horizontalLength();
			boolean snap = !this.leftOwner || harpoon.getTicksAlive() < 5;
			boolean returning = harpoon.isReturning();
			this.setPitch(HarpoonEntity.updateHarpoonRotation(this.lastPitch, (float)(MathHelper.atan2(vec3d.y, d) * 180.0F / (float)Math.PI), snap, returning));
			//noinspection SuspiciousNameCombination
			this.setYaw(HarpoonEntity.updateHarpoonRotation(this.lastYaw, (float)(MathHelper.atan2(vec3d.x, vec3d.z) * 180.0F / (float)Math.PI), snap, returning));
			ci.cancel();
		}
	}
}
