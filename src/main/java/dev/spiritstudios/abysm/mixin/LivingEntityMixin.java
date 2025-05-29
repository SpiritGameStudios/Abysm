package dev.spiritstudios.abysm.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.registry.AbysmEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    // Warning: I don't think this injector chains
    @ModifyConstant(method = "travelInFluid", constant = @Constant(floatValue = 0.9F))
    private float applySpecialFluidMovingSpeed(float constant) {
        if (this.isSprinting()) return (float) getAttributeValue(AbysmEntityAttributes.SWIMMING_SPEED);
        return constant;
    }

    @ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder addSwimmingSpeedToDefault(DefaultAttributeContainer.Builder original) {
        return original.add(AbysmEntityAttributes.SWIMMING_SPEED, 0.9F);
    }

	/*
    @WrapOperation(method = "travelInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
    private Vec3d pleaseIncreaseVerticalMovementSpeed(Vec3d instance, double x, double y, double z, Operation<Vec3d> original) {
        return original.call(instance, x, y * (1 + getAttributeValue(AbysmEntityAttributes.SWIMMING_SPEED) - AbysmEntityAttributes.SWIMMING_SPEED.value().getDefaultValue()), z);
    }
	*/
}
