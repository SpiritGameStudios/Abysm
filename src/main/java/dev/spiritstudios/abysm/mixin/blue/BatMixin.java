package dev.spiritstudios.abysm.mixin.blue;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.world.entity.effect.BlueEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bat.class)
public abstract class BatMixin extends AmbientCreature {
	@Shadow
	public abstract boolean isResting();

	@Shadow
	public abstract void setResting(boolean resting);

	protected BatMixin(EntityType<? extends AmbientCreature> entityType, Level world) {
		super(entityType, world);
	}

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ambient/Bat;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 1))
	private void fallIfBlue(Bat instance, Vec3 vec3d, Operation<Void> original) {
		if (BlueEffect.isBlue(this)) {
			if (this.getDeltaMovement().y < 0.0) {
				// skip
				return;
			}
		}

		original.call(instance, vec3d);
	}

	@Inject(method = "customServerAiStep", at = @At("HEAD"))
	private void stopRestingIfBlue(ServerLevel level, CallbackInfo ci) {
		if (BlueEffect.isBlue(this) && !this.onGround()) {
			if (this.isResting()) {
				this.setResting(false);
				if (!this.isSilent()) {
					level.levelEvent(null, LevelEvent.SOUND_BAT_LIFTOFF, this.blockPosition(), 0);
				}
			}
		}
	}

	@WrapOperation(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"))
	private Vec3 doNotFlyIfBlue(Vec3 instance, double x, double y, double z, Operation<Vec3> original) {
		Vec3 out = original.call(instance, x, y, z);

		if (BlueEffect.isBlue(this)) {
			out = new Vec3(out.x, Math.min(out.y, instance.y), out.z);
		}

		return out;
	}
}
