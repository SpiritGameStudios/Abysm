package dev.spiritstudios.abysm.client.mixin.render;

import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WaterFogEnvironment.class)
public abstract class WaterFogEnvironmentMixin {
//	@Inject(method = "getBaseColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getWaterFogColor()I"))
//	private void updateUnderwaterVisibility(ClientLevel world, Camera camera, int viewDistance, float skyDarkness, CallbackInfoReturnable<Integer> cir, @Local(ordinal = 0) long measuringTime) {
//		AbysmWaterFogModifier.updateUnderwaterVisibility(world, camera, measuringTime);
//	}
//
//	@WrapOperation(method = "getBaseColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/attribute/EnvironmentAttributeProbe;getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;F)Ljava/lang/Object;"))
//	private <T> T adjustFogColor(EnvironmentAttributeProbe probe, EnvironmentAttribute<Integer> environmentAttribute, float f, Operation<Integer> original) {
//		int value = original.call(probe, environmentAttribute, f);
//		return AbysmWaterFogModifier.adjustWaterFogColor(value, world, camera);
//	}
//
//	@Inject(method = "onNotApplicable", at = @At("RETURN"))
//	private void onNotApplicable(CallbackInfo ci) {
//		AbysmWaterFogModifier.onNotApplicable();
//	}
}
