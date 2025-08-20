package dev.spiritstudios.abysm.mixin.blue;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import dev.spiritstudios.abysm.entity.effect.BlueEffect;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
	@Shadow
	protected abstract double getEffectiveGravity();

	@Shadow
	protected abstract void travelMidAir(Vec3d movementInput);

	@Shadow
	public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

	private LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "canGlide", at = @At("HEAD"), cancellable = true)
	private void cancelGlidingWhenBlue(CallbackInfoReturnable<Boolean> cir) {
		if (BlueEffect.hasBlueEffect((LivingEntity) (Object) this)) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "applyFluidMovingSpeed", at = @At("HEAD"))
	private void useNormalGravityWhenBlue(double gravity, boolean falling, Vec3d motion, CallbackInfoReturnable<Vec3d> cir, @Local(ordinal = 0, argsOnly = true) LocalDoubleRef gravityRef) {
		if (BlueEffect.hasBlueEffect((LivingEntity) (Object) this)) {
			gravityRef.set(this.getEffectiveGravity() * 16);
		}
	}

	@Inject(method = "travelInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 4, shift = At.Shift.AFTER))
	private void addBonusUnderlavaGravityWhenBlue(Vec3d movementInput, CallbackInfo ci, @Local(ordinal = 1) double effectiveGravity) {
		if (BlueEffect.hasBlueEffect((LivingEntity) (Object) this)) {
			this.setVelocity(this.getVelocity().add(0.0, effectiveGravity * (-3.0 / 4.0), 0.0));
		}
	}

	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;travelInFluid(Lnet/minecraft/util/math/Vec3d;)V"), cancellable = true)
	private void alwaysTravelNormallyUnderwaterWhenBlue(Vec3d movementInput, CallbackInfo ci) {
		if (BlueEffect.hasBlueEffect((LivingEntity) (Object) this)) {
			// travel normally instead of using fluid physics
			this.travelMidAir(movementInput);
			// manually apply some extra drag
			boolean rising = this.getVelocity().y > 0.0;
			if (this.isTouchingWater()) {
				float waterMovementEfficiency = (float) this.getAttributeValue(EntityAttributes.WATER_MOVEMENT_EFFICIENCY);
				boolean onGround = this.isOnGround();
				float horizontalDrag = onGround
					? 0.98F - (1.0F - waterMovementEfficiency) * 0.4F
					: 0.99F - (1.0F - waterMovementEfficiency) * 0.04F;

				this.setVelocity(this.getVelocity().multiply(horizontalDrag, rising ? 0.8F : 0.99F, horizontalDrag));
			} else {
				this.setVelocity(this.getVelocity().multiply(0.7, rising ? 0.7F : 0.95F, 0.7));
			}

			ci.cancel();
		}
	}
}
