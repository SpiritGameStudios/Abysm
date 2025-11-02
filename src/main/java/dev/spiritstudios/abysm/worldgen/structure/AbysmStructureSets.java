package dev.spiritstudios.abysm.worldgen.structure;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

public class AbysmStructureSets {
	public static ResourceKey<StructureSet> DEEP_SEA_RUINS = keyOf("deep_sea_ruins");

	@SuppressWarnings("deprecation")
	public static void bootstrap(BootstrapContext<StructureSet> registry) {
		HolderGetter<Structure> structureLookup = registry.lookup(Registries.STRUCTURE);
		HolderGetter<StructureSet> structureSetLookup = registry.lookup(Registries.STRUCTURE_SET);
		HolderGetter<Biome> biomeLookup = registry.lookup(Registries.BIOME);

		registry.register(
			DEEP_SEA_RUINS,
			new StructureSet(
				structureLookup.getOrThrow(AbysmStructures.DEEP_SEA_RUINS),
				new RandomSpreadStructurePlacement(
					Vec3i.ZERO,
					StructurePlacement.FrequencyReductionMethod.DEFAULT,
					1.0F,
					4, // chosen by fair dice roll
						   // guaranteed to be random
					structureSetLookup.get(BuiltinStructureSets.OCEAN_MONUMENTS)
						.map(monument -> new StructurePlacement.ExclusionZone(monument, 10)),
					28,
					8,
					RandomSpreadType.TRIANGULAR
				)
			)
		);
	}

	private static ResourceKey<StructureSet> keyOf(String id) {
		return ResourceKey.create(Registries.STRUCTURE_SET, Abysm.id(id));
	}
}
