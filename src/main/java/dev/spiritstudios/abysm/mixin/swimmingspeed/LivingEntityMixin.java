package dev.spiritstudios.abysm.mixin.swimmingspeed;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.spiritstudios.abysm.world.entity.attribute.AbysmEntityAttributes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract double getAttributeValue(Holder<Attribute> attribute);

	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@ModifyExpressionValue(method = "travelInWater", at = @At(value = "CONSTANT", args = "floatValue=0.9"))
	private float applySpecialFluidMovingSpeed(float constant) {
		if (this.isSprinting()) return (float) getAttributeValue(AbysmEntityAttributes.SWIMMING_SPEED);
		return constant;
	}

	@ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
	private static AttributeSupplier.Builder addSwimmingSpeedToDefault(AttributeSupplier.Builder original) {
		return original.add(AbysmEntityAttributes.SWIMMING_SPEED, 0.9F);
	}
}
