package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.worldgen.tree.BloomshroomFoliagePlacer;
import dev.spiritstudios.abysm.worldgen.tree.BloomshroomTrunkPlacer;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

public final class AbysmFoliagePlacerTypes {
	public static final FoliagePlacerType<?> BLOOMSHROOM = new FoliagePlacerType<>(BloomshroomFoliagePlacer.CODEC);
}

