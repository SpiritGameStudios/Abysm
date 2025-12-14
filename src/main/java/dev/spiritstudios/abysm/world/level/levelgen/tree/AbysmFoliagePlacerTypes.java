package dev.spiritstudios.abysm.world.level.levelgen.tree;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public final class AbysmFoliagePlacerTypes {
	public static final FoliagePlacerType<BloomshroomFoliagePlacer> BLOOMSHROOM = register(
		"bloomshroom",
		BloomshroomFoliagePlacer.CODEC
	);

	private static <T extends FoliagePlacer> FoliagePlacerType<T> register(String id, MapCodec<T> codec) {
		return Registry.register(
			BuiltInRegistries.FOLIAGE_PLACER_TYPE,
			Abysm.id(id),
			new FoliagePlacerType<>(codec)
		);
	}

	public static void init() {
		// NO-OP
	}
}

