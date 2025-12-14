package dev.spiritstudios.abysm.world.level.levelgen.structure;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.level.levelgen.structure.pool.AbysmStructurePools;
import dev.spiritstudios.abysm.world.level.levelgen.structure.ruins.DeepSeaRuinsStructure;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

public class AbysmStructures {
	public static final ResourceKey<Structure> DEEP_SEA_RUINS = of("deep_sea_ruins");
	public static final ResourceKey<Structure> DEEP_SEA_RUINS_BASIC_RUIN = of("deep_sea_ruins/basic_ruin");

	public static void bootstrap(BootstrapContext<Structure> registerable) {
		HolderGetter<Biome> biomeLookup = registerable.lookup(Registries.BIOME);
		HolderGetter<StructureTemplatePool> poolLookup = registerable.lookup(Registries.TEMPLATE_POOL);

		HolderSet<Biome> isDeepOcean = biomeLookup.getOrThrow(BiomeTags.IS_DEEP_OCEAN);

		registerable.register(
			DEEP_SEA_RUINS,
			new DeepSeaRuinsStructure(
				new Structure.StructureSettings.Builder(isDeepOcean).generationStep(GenerationStep.Decoration.UNDERGROUND_STRUCTURES).build()
			)
		);

		registerable.register(
			DEEP_SEA_RUINS_BASIC_RUIN,
			new JigsawStructure(
				new Structure.StructureSettings.Builder(isDeepOcean).generationStep(GenerationStep.Decoration.UNDERGROUND_STRUCTURES).build(),
				poolLookup.getOrThrow(AbysmStructurePools.BASIC_RUIN),
				3,
				UniformHeight.of(VerticalAnchor.absolute(-25), VerticalAnchor.absolute(-15)),
				false
			)
		);
	}

	private static ResourceKey<Structure> of(String id) {
		return ResourceKey.create(Registries.STRUCTURE, Abysm.id(id));
	}
}
