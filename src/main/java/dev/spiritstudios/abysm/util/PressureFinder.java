package dev.spiritstudios.abysm.util;

import dev.spiritstudios.abysm.core.registries.AbysmMetatags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class PressureFinder {
	/**
	 * Gets the pressure at the given position according to the biome
	 *
	 * @param level the level
	 * @param pos   the blockPos
	 * @return a float for the pressure, 0 being "no pressure" (most biomes) and 1 being "the player will die now"
	 * @see dev.spiritstudios.abysm.world.entity.depths.MysteriousBlobEntity#isHappy()
	 */
	public static float getPressure(Level level, BlockPos pos) {
		return level.getBiome(pos).getData(AbysmMetatags.PRESSURE).orElse(0F);
	}
}
