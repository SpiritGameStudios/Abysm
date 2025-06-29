package dev.spiritstudios.abysm.worldgen.structure;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.structure.Structure;

public class AbysmStructures {

	public static void bootstrap(Registerable<Structure> structureRegisterable) {
		RegistryEntryLookup<Biome> biomeLookup = structureRegisterable.getRegistryLookup(RegistryKeys.BIOME);
		RegistryEntryLookup<StructurePool> poolLookup = structureRegisterable.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);

		structureRegisterable.register(
			AbysmStructureKeys.DEEP_SEA_RUINS,
			new DeepSeaRuinsStructure(
				new Structure.Config.Builder(biomeLookup.getOrThrow(BiomeTags.IS_DEEP_OCEAN)).step(GenerationStep.Feature.UNDERGROUND_STRUCTURES).build()
			)
		);

	}
}
