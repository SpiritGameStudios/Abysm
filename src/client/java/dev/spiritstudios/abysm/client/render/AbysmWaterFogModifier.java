package dev.spiritstudios.abysm.client.render;

import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

public class AbysmWaterFogModifier {
	private static float targetUnderwaterVisibilityMultiplier = 1.0F;
	private static float lerpedUnderwaterVisibilityMultiplier = 1.0F;
	public static float lastWaterVisionMultiplier = 1.0F;
	private static long updateTime = -1L;

	public static float getLightness(ClientLevel world) {
		return 0.5F + 2.0F * Mth.clamp(Mth.cos(world.getTimeOfDay(1.0F) * Mth.TWO_PI), -0.25F, 0.25F);
	}

	public static Holder<Biome> getBiome(ClientLevel world, Camera camera) {
		return world.getBiome(camera.getBlockPosition());
	}

	public static int adjustWaterFogColor(int original, ClientLevel world, Camera camera) {
		float lightness = getLightness(world);
		Holder<Biome> biome = getBiome(world, camera);

		return getFogColor(original, lightness, biome);
	}

	public static int getFogColor(int original, float lightness, Holder<Biome> biome) {
		if (lightness < 0.999F && biome.is(AbysmBiomes.FLORAL_REEF)) {
			int nightWaterFogColor = 0x11082F;
			return ARGB.lerp(lightness, nightWaterFogColor, original);
		} else {
			return original;
		}
	}

	public static void updateUnderwaterVisibility(ClientLevel world, Camera camera, long time) {
		float lightness = getLightness(world);
		Holder<Biome> biome = getBiome(world, camera);

		float visibilityMultiplier = getUnderwaterVisibilityMultiplier(biome, lightness);

		if (updateTime < 0L) {
			targetUnderwaterVisibilityMultiplier = visibilityMultiplier;
			lerpedUnderwaterVisibilityMultiplier = visibilityMultiplier;
			lastWaterVisionMultiplier = visibilityMultiplier;
			updateTime = time;
			return;
		}

		float lerpFactor = Mth.clamp((float) (time - updateTime) / 5000.0F, 0.0F, 1.0F);
		float currentVisibility = Mth.lerp(lerpFactor, lerpedUnderwaterVisibilityMultiplier, targetUnderwaterVisibilityMultiplier);

		if (targetUnderwaterVisibilityMultiplier != visibilityMultiplier) {
			targetUnderwaterVisibilityMultiplier = visibilityMultiplier;
			lerpedUnderwaterVisibilityMultiplier = currentVisibility;
			updateTime = time;
		}

		lastWaterVisionMultiplier = currentVisibility;
	}

	public static float getUnderwaterVisibilityMultiplier(Holder<Biome> biome, float lightness) {
		if (biome.is(AbysmBiomes.FLORAL_REEF)) {
			return 0.3F + 0.7F * lightness;
		} else if (biome.is(AbysmBiomes.DEEP_SEA_RUINS)) {
			return 0.13F;
		} else {
			return 1.0F;
		}
	}

	public static void onNotApplicable() {
		updateTime = -1L;
		targetUnderwaterVisibilityMultiplier = 1.0F;
		lerpedUnderwaterVisibilityMultiplier = 1.0F;
		lastWaterVisionMultiplier = 1.0F;
	}
}
