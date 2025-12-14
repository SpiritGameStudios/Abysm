package dev.spiritstudios.abysm.world.level.levelgen.tree;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

public final class AbysmTrunkPlacerTypes {
	public static final TrunkPlacerType<BloomshroomTrunkPlacer> BLOOMSHROOM = register(
		"bloomshroom",
		BloomshroomTrunkPlacer.CODEC
	);

	private static <T extends TrunkPlacer> TrunkPlacerType<T> register(String id, MapCodec<T> codec) {
		return Registry.register(
			BuiltInRegistries.TRUNK_PLACER_TYPE,
			Abysm.id(id),
			new TrunkPlacerType<>(codec)
		);
	}

	public static void init() {
		// NO-OP
	}
}

