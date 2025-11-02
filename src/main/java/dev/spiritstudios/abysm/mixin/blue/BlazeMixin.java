package dev.spiritstudios.abysm.mixin.blue;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.entity.effect.BlueEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Blaze.class)
public abstract class BlazeMixin extends Monster {
	protected BlazeMixin(EntityType<? extends Monster> entityType, Level world) {
		super(entityType, world);
	}

	@WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Blaze;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
	private void doNotFloatIfBlue(Blaze instance, Vec3 velocity, Operation<Void> original) {
		if (BlueEffect.isBlue(this)) {
			if (this.getDeltaMovement().y < 0.0) {
				// skip
				return;
			}
		}

		original.call(instance, velocity);
	}

	@WrapOperation(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Blaze;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
	private void doNotFlyIfBlue(Blaze instance, Vec3 velocity, Operation<Void> original) {
		if (BlueEffect.isBlue(this)) {
			if (velocity.y > this.getDeltaMovement().y) {
				// skip
				return;
			}
		}

		original.call(instance, velocity);
	}
}
