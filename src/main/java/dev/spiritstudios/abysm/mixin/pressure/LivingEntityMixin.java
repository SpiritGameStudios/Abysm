package dev.spiritstudios.abysm.mixin.pressure;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.entity.depths.MysteriousBlobEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Shadow
	public abstract float getScale();

	@WrapOperation(method = "getDimensions", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityDimensions;scaled(F)Lnet/minecraft/entity/EntityDimensions;"))
	private EntityDimensions scale(EntityDimensions instance, float ratio, Operation<EntityDimensions> original) {
		instance = original.call(instance, ratio);
		if (!((LivingEntity) (Object) this instanceof MysteriousBlobEntity xyzzy)) {
			return instance;
		}
		return instance.scaled(
			abysm$clampScale(xyzzy.getScaleXZ()),
			abysm$clampScale(xyzzy.getScaleY())
		);
	}

	@Unique
	float abysm$clampScale(float scale) {
		return MathHelper.clamp(scale, 0.1f, 10f);
	}
}
