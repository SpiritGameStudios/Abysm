package dev.spiritstudios.abysm.mixin.blue;

import dev.spiritstudios.abysm.world.entity.effect.BlueEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public abstract void setSwimming(boolean swimming);

	@Inject(method = "updateSwimming", at = @At(value = "HEAD"), cancellable = true)
	private void blockSwimmingWhenBlue(CallbackInfo ci) {
		if ((Entity) (Object) this instanceof LivingEntity livingEntity) {
			if (BlueEffect.hasBlueEffect(livingEntity)) {
				this.setSwimming(false);
				ci.cancel();
			}
		}
	}

	@Inject(method = "getFluidJumpThreshold", at = @At("HEAD"), cancellable = true)
	private void blockSwimmingWhenBlue(CallbackInfoReturnable<Double> cir) {
		if ((Entity) (Object) this instanceof LivingEntity livingEntity) {
			if (BlueEffect.hasBlueEffect(livingEntity)) {
				// set this to a large value to make sure the entity is always "above" water
				cir.setReturnValue(65536.0);
			}
		}
	}
}
