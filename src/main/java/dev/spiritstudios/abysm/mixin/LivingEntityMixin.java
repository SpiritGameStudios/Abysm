package dev.spiritstudios.abysm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.duck.LivingEntityDuck;
import dev.spiritstudios.abysm.entity.effect.BlueEffect;
import dev.spiritstudios.abysm.networking.EntityUpdateBlueS2CPayload;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, LivingEntityDuck {
	private LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Unique
	private boolean abysm$isBlue = false;
	@Unique
	private boolean abysm$wasBlue = false;

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
