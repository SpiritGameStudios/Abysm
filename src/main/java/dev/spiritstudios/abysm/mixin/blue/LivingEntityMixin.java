package dev.spiritstudios.abysm.mixin.blue;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import dev.spiritstudios.abysm.duck.LivingEntityDuck;
import dev.spiritstudios.abysm.entity.effect.BlueEffect;
import dev.spiritstudios.abysm.networking.EntityUpdateBlueS2CPayload;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, LivingEntityDuck {
	@Shadow
	protected abstract double getEffectiveGravity();

	@Shadow
	protected abstract void travelMidAir(Vec3d movementInput);

	@Shadow
	public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

	@Unique
	private boolean abysm$isBlue = false;
	@Unique
	private boolean abysm$wasBlue = false;

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

	@Override
	public void abysm$setBlue(boolean isBlue) {
		this.abysm$isBlue = isBlue;
	}

	@Override
	public boolean abysm$isBlue() {
		return this.abysm$isBlue;
	}

	@Override
	public boolean abysm$checkWasBlue() {
		boolean wasBlue = this.abysm$wasBlue;
		this.abysm$wasBlue = this.abysm$isBlue;
		return wasBlue;
	}

	@Unique
	private void abysm$tryUpdateIsBlue(boolean isBlue) {
		if (this.abysm$isBlue != isBlue) {
			this.abysm$setBlue(isBlue);
			if (!this.getEntityWorld().isClient) {
				new EntityUpdateBlueS2CPayload(this.getId(), isBlue).send(this);
			}
		}
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
	private void checkBlue(NbtCompound nbt, CallbackInfo ci) {
		this.abysm$tryUpdateIsBlue(BlueEffect.hasBlueEffect((LivingEntity) (Object) this));
	}

	@Inject(method = "onStatusEffectsRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;onRemoved(Lnet/minecraft/entity/attribute/AttributeContainer;)V"))
	private void possiblyRemoveBlue(Collection<StatusEffectInstance> effects, CallbackInfo ci, @Local(ordinal = 0) StatusEffectInstance instance) {
		if (instance.getEffectType().value() instanceof BlueEffect) {
			if (!BlueEffect.hasBlueEffect((LivingEntity) (Object) this)) {
				this.abysm$tryUpdateIsBlue(false);
			}
		}
	}

	@Inject(method = "onStatusEffectApplied", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;onApplied(Lnet/minecraft/entity/attribute/AttributeContainer;I)V"))
	private void setBlue(StatusEffectInstance effect, Entity source, CallbackInfo ci) {
		if (effect.getEffectType().value() instanceof BlueEffect) {
			this.abysm$tryUpdateIsBlue(true);
		}
	}
}
