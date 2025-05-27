package dev.spiritstudios.abysm.worldgen.biome;

import com.terraformersmc.biolith.api.biome.BiomePlacement;
import dev.spiritstudios.abysm.worldgen.feature.AbysmPlacedFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.OverworldBiomeCreator;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.OceanPlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

import static net.minecraft.world.gen.surfacebuilder.MaterialRules.block;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.condition;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.not;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.water;

public final class FloralReefBiome extends AbysmBiome {
	public static final MaterialRules.MaterialCondition CONDITION = MaterialRules.biome(AbysmBiomes.FLORAL_REEF);

	public FloralReefBiome() {
		super(AbysmBiomes.FLORAL_REEF, 0.5F, true, 0.5F);
	}

	@Override
	public BiomeEffects.Builder createEffects() {
		// TODO: Temporary colors
		return new BiomeEffects.Builder()
			.waterColor(0x0093C4)
			.waterFogColor(0x08304C)
			.fogColor(0xC0D8FF)
			.skyColor(OverworldBiomeCreator.getSkyColor(temperature));
	}

	@Override
	public GenerationSettings.Builder createGenerationSettings(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
		GenerationSettings.LookupBackedBuilder builder = new GenerationSettings.LookupBackedBuilder(featureLookup, carverLookup);

		addBasicFeatures(builder);

		DefaultBiomeFeatures.addDefaultOres(builder);
		DefaultBiomeFeatures.addDefaultDisks(builder);
		DefaultBiomeFeatures.addWaterBiomeOakTrees(builder);
		DefaultBiomeFeatures.addDefaultFlowers(builder);
		DefaultBiomeFeatures.addDefaultGrass(builder);
		DefaultBiomeFeatures.addDefaultMushrooms(builder);
		DefaultBiomeFeatures.addDefaultVegetation(builder, true);

		builder
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, AbysmPlacedFeatures.PATCH_SPRIGS)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, AbysmPlacedFeatures.FLOROPUMICE_STALAGMITES)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, AbysmPlacedFeatures.TREES_BLOOMSHROOM)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEA_PICKLE);

		return builder;
	}

	@Override
	public SpawnSettings.Builder createSpawnSettings() {
		return new SpawnSettings.Builder();
	}

	@Override
	public void addToGenerator() {
		// TODO: Replace this with a NoiseHypercube for more control
		BiomePlacement.replaceOverworld(
			BiomeKeys.WARM_OCEAN, key,
			0.5F
		);

		addSurfaceRules(condition(
			not(water(-1, 0)),
			condition(
				MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH_RANGE_6,
				block(Blocks.SAND.getDefaultState())
			)
		));
	}
}
