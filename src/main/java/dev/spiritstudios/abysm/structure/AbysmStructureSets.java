package dev.spiritstudios.abysm.structure;

import dev.spiritstudios.abysm.worldgen.structure.AbysmStructureKeys;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureSetKeys;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.structure.Structure;

public class AbysmStructureSets {

	public static void bootstrap(Registerable<StructureSet> structureSetRegisterable) {
		RegistryEntryLookup<Structure> structureLookup = structureSetRegisterable.getRegistryLookup(RegistryKeys.STRUCTURE);
		RegistryEntryLookup<StructureSet> structureSetLookup = structureSetRegisterable.getRegistryLookup(RegistryKeys.STRUCTURE_SET);
		RegistryEntryLookup<Biome> biomeLookup = structureSetRegisterable.getRegistryLookup(RegistryKeys.BIOME);

		structureSetRegisterable.register(
			AbysmStructureSetKeys.DEEP_SEA_RUINS,
			new StructureSet(
				structureLookup.getOrThrow(AbysmStructureKeys.DEEP_SEA_RUINS),
				new RandomSpreadStructurePlacement(
					Vec3i.ZERO,
					StructurePlacement.FrequencyReductionMethod.DEFAULT,
					1.0F,
					55147411,
					structureSetLookup.getOptional(StructureSetKeys.OCEAN_MONUMENTS).map(monument -> new StructurePlacement.ExclusionZone(monument, 4)),
					28,
					8,
					SpreadType.TRIANGULAR
				)
			)
		);
	}
}
