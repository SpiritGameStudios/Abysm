package dev.spiritstudios.abysm.client.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.client.render.AbysmWaterFogModifier;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.environment.WaterFogEnvironment;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFogEnvironment.class)
public abstract class WaterFogEnvironmentMixin {

	@Inject(method = "getBaseColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getWaterFogColor()I"))
	private void updateUnderwaterVisibility(ClientLevel world, Camera camera, int viewDistance, float skyDarkness, CallbackInfoReturnable<Integer> cir, @Local(ordinal = 0) long measuringTime) {
		AbysmWaterFogModifier.updateUnderwaterVisibility(world, camera, measuringTime);
	}

	@WrapOperation(method = "getBaseColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getWaterFogColor()I"))
	private int adjustFogColor(Biome instance, Operation<Integer> original, ClientLevel world, Camera camera, int viewDistance, float skyDarkness, @Local(ordinal = 0) long measuringTime) {
		int value = original.call(instance);
		return AbysmWaterFogModifier.adjustWaterFogColor(value, world, camera);
	}

	@Inject(method = "onNotApplicable", at = @At("RETURN"))
	private void onNotApplicable(CallbackInfo ci) {
		AbysmWaterFogModifier.onNotApplicable();
	}
}
