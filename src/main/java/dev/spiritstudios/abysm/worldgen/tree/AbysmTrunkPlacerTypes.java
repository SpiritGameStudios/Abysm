package dev.spiritstudios.abysm.worldgen.tree;

import net.minecraft.world.gen.trunk.TrunkPlacerType;

public final class AbysmTrunkPlacerTypes {
	/**
	 * Note: all entries are automatically registered in {@link dev.spiritstudios.abysm.Abysm}
	 */

	public static final TrunkPlacerType<?> BLOOMSHROOM = new TrunkPlacerType<>(BloomshroomTrunkPlacer.CODEC);
}

