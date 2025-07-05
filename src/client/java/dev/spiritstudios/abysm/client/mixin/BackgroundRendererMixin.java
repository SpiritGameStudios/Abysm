package dev.spiritstudios.abysm.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
	@Shadow
	private static long lastWaterFogColorUpdateTime;
	@Unique private static float underwaterVisibilityMultiplier = 1.0F;
	@Unique private static float nextUnderwaterVisibilityMultiplier = 1.0F;

	@ModifyVariable(method = "getFogColor", at = @At("STORE"), ordinal = 1)
	private static int adjustWaterFogColor(int value, Camera camera, float tickProgress, ClientWorld world, int clampedViewDistance, float skyDarkness) {
		CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
		if (cameraSubmersionType == CameraSubmersionType.WATER) {
			float lightness = 0.5F + 2.0F * MathHelper.clamp(MathHelper.cos(world.getSkyAngle(1.0F) * MathHelper.TAU), -0.25F, 0.25F);
			RegistryEntry<Biome> biome = world.getBiome(BlockPos.ofFloored(camera.getPos()));

			// update underwater visiblity
			float visibilityMultiplier;
			if (biome.matchesKey(AbysmBiomes.FLORAL_REEF)) {
				visibilityMultiplier = 0.3F + 0.7F * lightness;
			} else if(biome.matchesKey(AbysmBiomes.DEEP_SEA_RUINS)) {
				visibilityMultiplier = 0.13F;
			}else {
				visibilityMultiplier = 1.0F;
			}

			if (lastWaterFogColorUpdateTime < 0L) {
				underwaterVisibilityMultiplier = visibilityMultiplier;
				nextUnderwaterVisibilityMultiplier = visibilityMultiplier;
			} else {
				underwaterVisibilityMultiplier = nextUnderwaterVisibilityMultiplier;
				nextUnderwaterVisibilityMultiplier = MathHelper.lerp(0.007F, underwaterVisibilityMultiplier, visibilityMultiplier);
			}

			// adjust fog color
			if (lightness < 0.999F && biome.matchesKey(AbysmBiomes.FLORAL_REEF)) {
				int nightWaterFogColor = 0x11082F;
				return ColorHelper.lerp(lightness, nightWaterFogColor, value);
			} else {
				return value;
			}
		} else {
			return value;
		}
	}

	@Inject(method = "getFogColor", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F", ordinal = 0))
	private static void reduceUnderwaterVisiblity(Camera camera, float tickProgress, ClientWorld world, int clampedViewDistance, float skyDarkness, CallbackInfoReturnable<Vector4f> cir, @Local(ordinal = 5) LocalFloatRef underwaterVisibilityRef) {
		CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
		if (cameraSubmersionType == CameraSubmersionType.WATER) {
			// adjust underwater visibility
			float visibilityMultiplier = MathHelper.lerp(tickProgress, underwaterVisibilityMultiplier, nextUnderwaterVisibilityMultiplier);

			if (visibilityMultiplier < 0.999F) {
				underwaterVisibilityRef.set(underwaterVisibilityRef.get() * visibilityMultiplier);
			}
		}
	}
}
