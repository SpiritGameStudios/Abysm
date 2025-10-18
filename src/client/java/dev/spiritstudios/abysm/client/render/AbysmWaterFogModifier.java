package dev.spiritstudios.abysm.client.render;

import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;

public class AbysmWaterFogModifier {
	private static float targetUnderwaterVisibilityMultiplier = 1.0F;
	private static float lerpedUnderwaterVisibilityMultiplier = 1.0F;
	public static float lastUnderwaterVisibilityMultiplier = 1.0F;
	private static long updateTime = -1L;

	public static float getLightness(ClientWorld world) {
		return 0.5F + 2.0F * MathHelper.clamp(MathHelper.cos(world.getSkyAngle(1.0F) * MathHelper.TAU), -0.25F, 0.25F);
	}

	public static RegistryEntry<Biome> getBiome(ClientWorld world, Camera camera) {
		return world.getBiome(camera.getBlockPos());
	}

	public static int adjustWaterFogColor(int original, ClientWorld world, Camera camera) {
		float lightness = getLightness(world);
		RegistryEntry<Biome> biome = getBiome(world, camera);

		return getFogColor(original, lightness, biome);
	}

	public static int getFogColor(int original, float lightness, RegistryEntry<Biome> biome) {
		if (lightness < 0.999F && biome.matchesKey(AbysmBiomes.FLORAL_REEF)) {
			int nightWaterFogColor = 0x11082F;
			return ColorHelper.lerp(lightness, nightWaterFogColor, original);
		} else {
			return original;
		}
	}

	public static void updateUnderwaterVisibility(ClientWorld world, Camera camera, long time) {
		float lightness = getLightness(world);
		RegistryEntry<Biome> biome = getBiome(world, camera);

		float visibilityMultiplier = getUnderwaterVisibilityMultiplier(biome, lightness);

		if (updateTime < 0L) {
			targetUnderwaterVisibilityMultiplier = visibilityMultiplier;
			lerpedUnderwaterVisibilityMultiplier = visibilityMultiplier;
			lastUnderwaterVisibilityMultiplier = visibilityMultiplier;
			updateTime = time;
			return;
		}

		float lerpFactor = MathHelper.clamp((float) (time - updateTime) / 5000.0F, 0.0F, 1.0F);
		float currentVisibility = MathHelper.lerp(lerpFactor, lerpedUnderwaterVisibilityMultiplier, targetUnderwaterVisibilityMultiplier);

		if (targetUnderwaterVisibilityMultiplier != visibilityMultiplier) {
			targetUnderwaterVisibilityMultiplier = visibilityMultiplier;
			lerpedUnderwaterVisibilityMultiplier = currentVisibility;
			updateTime = time;
		}

		lastUnderwaterVisibilityMultiplier = currentVisibility;
	}

	public static float getUnderwaterVisibilityMultiplier(RegistryEntry<Biome> biome, float lightness) {
		if (biome.matchesKey(AbysmBiomes.FLORAL_REEF)) {
			return 0.3F + 0.7F * lightness;
		} else if (biome.matchesKey(AbysmBiomes.DEEP_SEA_RUINS)) {
			return 0.13F;
		} else {
			return 1.0F;
		}
	}

	public static void onSkipped() {
		updateTime = -1L;
		targetUnderwaterVisibilityMultiplier = 1.0F;
		lerpedUnderwaterVisibilityMultiplier = 1.0F;
		lastUnderwaterVisibilityMultiplier = 1.0F;
	}
}
