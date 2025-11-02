package dev.spiritstudios.abysm.mixin.manowar;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.spiritstudios.abysm.entity.floralreef.ManOWarEntity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@ModifyReturnValue(method = "getScale", at = @At("RETURN"))
	private float scaleBabiesByCheating(float original) {
		return (LivingEntity) (Object) this instanceof ManOWarEntity manOWar && manOWar.isBaby() ? original * 0.5F : original;
	}
}
