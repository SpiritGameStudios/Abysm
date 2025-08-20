package dev.spiritstudios.abysm.mixin.blue;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.entity.effect.BlueEffect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BatEntity.class)
public abstract class BatEntityMixin extends AmbientEntity {
	@Shadow
	public abstract boolean isRoosting();

	@Shadow
	public abstract void setRoosting(boolean roosting);

	protected BatEntityMixin(EntityType<? extends AmbientEntity> entityType, World world) {
		super(entityType, world);
	}

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/BatEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 1))
	private void fallIfBlue(BatEntity instance, Vec3d vec3d, Operation<Void> original) {
		if (BlueEffect.isBlue(this)) {
			if (this.getVelocity().y < 0.0) {
				// skip
				return;
			}
		}

		original.call(instance, vec3d);
	}

	@Inject(method = "mobTick", at = @At("HEAD"))
	private void stopRoostingIfBlue(ServerWorld world, CallbackInfo ci) {
		if (BlueEffect.isBlue(this) && !this.isOnGround()) {
			if (this.isRoosting()) {
				this.setRoosting(false);
				if (!this.isSilent()) {
					world.syncWorldEvent(null, WorldEvents.BAT_TAKES_OFF, this.getBlockPos(), 0);
				}
			}
		}
	}

	@WrapOperation(method = "mobTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;"))
	private Vec3d doNotFlyIfBlue(Vec3d instance, double x, double y, double z, Operation<Vec3d> original) {
		Vec3d out = original.call(instance, x, y, z);

		if (BlueEffect.isBlue(this)) {
			out = new Vec3d(out.x, Math.min(out.y, instance.y), out.z);
		}

		return out;
	}
}
