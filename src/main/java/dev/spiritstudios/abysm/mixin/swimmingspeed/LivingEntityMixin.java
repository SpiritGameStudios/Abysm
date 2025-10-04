package dev.spiritstudios.abysm.mixin.swimmingspeed;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.spiritstudios.abysm.entity.attribute.AbysmEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyExpressionValue(method = "travelInFluid", at = @At(value = "CONSTANT", args = "floatValue=0.9"))
	private float applySpecialFluidMovingSpeed(float constant) {
		if (this.isSprinting()) return (float) getAttributeValue(AbysmEntityAttributes.SWIMMING_SPEED);
		return constant;
	}

	@ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
	private static DefaultAttributeContainer.Builder addSwimmingSpeedToDefault(DefaultAttributeContainer.Builder original) {
		return original.add(AbysmEntityAttributes.SWIMMING_SPEED, 0.9F);
	}
}
