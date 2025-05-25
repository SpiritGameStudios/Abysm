package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.worldgen.tree.BloomshroomTrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

public final class AbysmTrunkPlacerTypes {
	public static final TrunkPlacerType<?> BLOOMSHROOM = new TrunkPlacerType<>(BloomshroomTrunkPlacer.CODEC);
}

