package dev.spiritstudios.abysm.client.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderPass;
import dev.spiritstudios.abysm.client.duck.ClientPlayerEntityDuckInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.registry.tag.FluidTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {
	@Shadow @Final private MinecraftClient client;

	@WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setUniform(Ljava/lang/String;[F)V", ordinal = 1))
	private void tweakLightmap(RenderPass instance, String s, float[] floats, Operation<Void> original, @Local(argsOnly = true) float tickProgress) {
		ClientPlayerEntity player = this.client.player;

		// these should not be null at this point
		Objects.requireNonNull(player);

		float brightenSkyFactor = 0.0F;

		if (player.isSubmergedIn(FluidTags.WATER)) {
			brightenSkyFactor = ((ClientPlayerEntityDuckInterface) player).abysm$getUnderwaterAmbientSkyLight(tickProgress);
		}

		floats[0] += brightenSkyFactor;
		original.call(instance, s, floats);
	}
}
