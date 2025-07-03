package dev.spiritstudios.abysm.util;

import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class PressureFinder {

	/**
	 * Gets the pressure at the given position according to the biome
	 * @param world the world
	 * @param blockPos the blockPos
	 * @return a float for the pressure, 0 being "no pressure" (most biomes) and 200 being "the player will die now"
	 */
	public static float getPressure(World world, BlockPos blockPos) {
		RegistryEntry<Biome> biome = world.getBiome(blockPos);
		if (biome == null) {
			return 0;
		}
		if (biome.matchesKey(AbysmBiomes.DEEP_SEA_RUINS)) {
			return 10f; // subject to change
		}
		return 0;
	}
}
