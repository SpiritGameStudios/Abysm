package dev.spiritstudios.abysm.world.level.levelgen.structure;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.level.levelgen.structure.ruins.DeepSeaRuinsStructure;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class AbysmStructureTypes {
	public static final StructureType<DeepSeaRuinsStructure> DEEP_SEA_RUINS = register(
		"deep_sea_ruins",
		DeepSeaRuinsStructure.CODEC
	);

	private static <T extends Structure> StructureType<T> register(String id, MapCodec<T> codec) {
		return Registry.register(
			BuiltInRegistries.STRUCTURE_TYPE,
			Abysm.id(id),
			() -> codec
		);
	}

	public static void init() {
		// NO-OP
	}
}
