package dev.spiritstudios.abysm.mixin.blue;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import dev.spiritstudios.abysm.data.AbysmDataAttachments;
import dev.spiritstudios.abysm.world.entity.effect.BlueEffect;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {
	@Shadow
	protected abstract double getEffectiveGravity();

	@Shadow
	protected abstract void travelInAir(Vec3 movementInput);

	@Shadow
	public abstract double getAttributeValue(Holder<Attribute> attribute);

	private LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "canGlide", at = @At("HEAD"), cancellable = true)
	private void cancelGlidingWhenBlue(CallbackInfoReturnable<Boolean> cir) {
		if (BlueEffect.hasBlueEffect((LivingEntity) (Object) this)) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "getFluidFallingAdjustedMovement", at = @At("HEAD"))
	private void useNormalGravityWhenBlue(double gravity, boolean falling, Vec3 motion, CallbackInfoReturnable<Vec3> cir, @Local(ordinal = 0, argsOnly = true) LocalDoubleRef gravityRef) {
		if (BlueEffect.hasBlueEffect((LivingEntity) (Object) this)) {
			gravityRef.set(this.getEffectiveGravity() * 16);
		}
	}

	@Inject(method = "travelInLava", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V", ordinal = 3, shift = At.Shift.AFTER))
	private void addBonusUnderlavaGravityWhenBlue(Vec3 vec3, double d, boolean bl, double e, CallbackInfo ci, @Local(ordinal = 1, argsOnly = true) double effectiveGravity) {
		if (BlueEffect.hasBlueEffect((LivingEntity) (Object) this)) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0, effectiveGravity * (-3.0 / 4.0), 0.0));
		}
	}

	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;travelInFluid(Lnet/minecraft/world/phys/Vec3;)V"), cancellable = true)
	private void alwaysTravelNormallyUnderwaterWhenBlue(Vec3 movementInput, CallbackInfo ci) {
		if (BlueEffect.hasBlueEffect((LivingEntity) (Object) this)) {
			// travel normally instead of using fluid physics
			this.travelInAir(movementInput);

			// manually apply some extra drag
			boolean rising = this.getDeltaMovement().y > 0.0;
			if (this.isInWater()) {
				float waterMovementEfficiency = (float) this.getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY);
				boolean onGround = this.onGround();
				float horizontalDrag = onGround
					? 0.98F - (1.0F - waterMovementEfficiency) * 0.4F
					: 0.99F - (1.0F - waterMovementEfficiency) * 0.04F;

				this.setDeltaMovement(this.getDeltaMovement().multiply(horizontalDrag, rising ? 0.8F : 0.99F, horizontalDrag));
			} else {
				this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, rising ? 0.7F : 0.95F, 0.7));
			}

			ci.cancel();
		}
	}

	@Inject(method = "onEffectsRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;removeAttributeModifiers(Lnet/minecraft/world/entity/ai/attributes/AttributeMap;)V"))
	private void possiblyRemoveBlue(Collection<MobEffectInstance> effects, CallbackInfo ci, @Local(ordinal = 0) MobEffectInstance instance) {
		if (instance.getEffect().value() instanceof BlueEffect) {
			if (!BlueEffect.hasBlueEffect((LivingEntity) (Object) this)) {
				this.setAttached(AbysmDataAttachments.BLUE, false);
			}
		}
	}
}
