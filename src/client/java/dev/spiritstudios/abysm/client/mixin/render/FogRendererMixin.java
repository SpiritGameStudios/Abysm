package dev.spiritstudios.abysm.client.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.client.render.AbysmWaterFogModifier;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {

	@WrapOperation(method = "getFogColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getUnderwaterVisibility()F"))
	private float adjustUnderwaterVisibility(
		ClientPlayerEntity instance, Operation<Float> original
	) {
		float value = original.call(instance);
		float visibilityMultiplier = AbysmWaterFogModifier.lastUnderwaterVisibilityMultiplier;

		return value * visibilityMultiplier;
	}
}
