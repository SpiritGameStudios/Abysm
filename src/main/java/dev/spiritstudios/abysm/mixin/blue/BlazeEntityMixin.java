package dev.spiritstudios.abysm.mixin.blue;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.entity.effect.BlueEffect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlazeEntity.class)
public abstract class BlazeEntityMixin extends HostileEntity {
	protected BlazeEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@WrapOperation(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/BlazeEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
	private void doNotFloatIfBlue(BlazeEntity instance, Vec3d vec3d, Operation<Void> original) {
		if (BlueEffect.isBlue(this)) {
			if (this.getVelocity().y < 0.0) {
				// skip
				return;
			}
		}

		original.call(instance, vec3d);
	}

	@WrapOperation(method = "mobTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/BlazeEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
	private void doNotFlyIfBlue(BlazeEntity instance, Vec3d vec3d, Operation<Void> original) {
		if (BlueEffect.isBlue(this)) {
			if (vec3d.y > this.getVelocity().y) {
				// skip
				return;
			}
		}

		original.call(instance, vec3d);
	}
}
