package dev.spiritstudios.abysm.worldgen.tree;

import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public final class AbysmFoliagePlacerTypes {
	/**
	 * Note: all entries are automatically registered in {@link dev.spiritstudios.abysm.Abysm}
	 */

	public static final FoliagePlacerType<?> BLOOMSHROOM = new FoliagePlacerType<>(BloomshroomFoliagePlacer.CODEC);
}

