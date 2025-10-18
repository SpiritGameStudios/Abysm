package dev.spiritstudios.abysm.client.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.client.render.AbysmWaterFogModifier;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.fog.WaterFogModifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFogModifier.class)
public abstract class WaterFogModifierMixin {

	@Inject(method = "getFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getWaterFogColor()I"))
	private void updateUnderwaterVisibility(ClientWorld world, Camera camera, int viewDistance, float skyDarkness, CallbackInfoReturnable<Integer> cir, @Local(ordinal = 0) long measuringTime) {
		AbysmWaterFogModifier.updateUnderwaterVisibility(world, camera, measuringTime);
	}

	@WrapOperation(method = "getFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getWaterFogColor()I"))
	private int adjustFogColor(Biome instance, Operation<Integer> original, ClientWorld world, Camera camera, int viewDistance, float skyDarkness, @Local(ordinal = 0) long measuringTime) {
		int value = original.call(instance);
		return AbysmWaterFogModifier.adjustWaterFogColor(value, world, camera);
	}

	@Inject(method = "onSkipped", at = @At("RETURN"))
	private void onSkipped(CallbackInfo ci) {
		AbysmWaterFogModifier.onSkipped();
	}
}
