package dev.spiritstudios.abysm.util;

import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class PressureFinder {
	/**
	 * Gets the pressure at the given position according to the biome
	 *
	 * @param world    the world
	 * @param blockPos the blockPos
	 * @return a float for the pressure, 0 being "no pressure" (most biomes) and 200 being "the player will die now"
	 * @see dev.spiritstudios.abysm.entity.depths.MysteriousBlobEntity#isHappy()
	 */
	public static float getPressure(Level world, BlockPos blockPos) {
		Holder<Biome> biome = world.getBiome(blockPos);
		if (biome == null) return 0;

		if (biome.is(AbysmBiomes.DEEP_SEA_RUINS)) return 10f;

		if (biome.is(AbysmBiomes.GLOWING_CAVES)) return 60f;

		if (biome.is(AbysmBiomes.THE_ENTWINED)) return 75f;

		if (biome.is(AbysmBiomes.INKDEPTH_REALM)) return 100f;

		return 0;
	}
}
