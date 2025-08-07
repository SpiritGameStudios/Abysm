package dev.spiritstudios.abysm.worldgen.structure;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.worldgen.structure.pool.AbysmStructurePools;
import dev.spiritstudios.abysm.worldgen.structure.ruins.DeepSeaRuinsStructure;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;

public class AbysmStructures {
	public static final RegistryKey<Structure> DEEP_SEA_RUINS = of("deep_sea_ruins");
	public static final RegistryKey<Structure> DEEP_SEA_RUINS_BASIC_RUIN = of("deep_sea_ruins/basic_ruin");

	public static void bootstrap(Registerable<Structure> registerable) {
		RegistryEntryLookup<Biome> biomeLookup = registerable.getRegistryLookup(RegistryKeys.BIOME);
		RegistryEntryLookup<StructurePool> poolLookup = registerable.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);

		RegistryEntryList<Biome> isDeepOcean = biomeLookup.getOrThrow(BiomeTags.IS_DEEP_OCEAN);

		registerable.register(
			DEEP_SEA_RUINS,
			new DeepSeaRuinsStructure(
				new Structure.Config.Builder(isDeepOcean).step(GenerationStep.Feature.UNDERGROUND_STRUCTURES).build()
			)
		);

		registerable.register(
			DEEP_SEA_RUINS_BASIC_RUIN,
			new JigsawStructure(
				new Structure.Config.Builder(isDeepOcean).step(GenerationStep.Feature.UNDERGROUND_STRUCTURES).build(),
				poolLookup.getOrThrow(AbysmStructurePools.BASIC_RUIN),
				3,
				UniformHeightProvider.create(YOffset.fixed(-25), YOffset.fixed(-15)),
				false
			)
		);
	}

	private static RegistryKey<Structure> of(String id) {
		return RegistryKey.of(RegistryKeys.STRUCTURE, Abysm.id(id));
	}
}
