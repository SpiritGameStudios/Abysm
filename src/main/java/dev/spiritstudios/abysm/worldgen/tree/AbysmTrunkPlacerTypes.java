package dev.spiritstudios.abysm.worldgen.tree;

import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

public final class AbysmTrunkPlacerTypes {
	/**
	 * Note: all entries are automatically registered in {@link dev.spiritstudios.abysm.Abysm}
	 */

	public static final TrunkPlacerType<?> BLOOMSHROOM = new TrunkPlacerType<>(BloomshroomTrunkPlacer.CODEC);
}

