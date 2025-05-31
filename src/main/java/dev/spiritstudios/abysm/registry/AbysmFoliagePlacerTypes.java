package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.worldgen.tree.BloomshroomFoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public final class AbysmFoliagePlacerTypes {
	public static final FoliagePlacerType<?> BLOOMSHROOM = new FoliagePlacerType<>(BloomshroomFoliagePlacer.CODEC);
}

