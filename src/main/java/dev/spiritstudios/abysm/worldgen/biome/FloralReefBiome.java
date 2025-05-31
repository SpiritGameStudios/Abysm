package dev.spiritstudios.abysm.worldgen.biome;

import com.terraformersmc.biolith.api.biome.BiomePlacement;
import dev.spiritstudios.abysm.registry.AbysmEntityTypes;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import dev.spiritstudios.abysm.worldgen.feature.AbysmPlacedFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.sound.MusicType;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.OceanPlacedFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

import static net.minecraft.world.gen.surfacebuilder.MaterialRules.*;

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
			.skyColor(OverworldBiomeCreator.getSkyColor(temperature))
			.music(MusicType.createIngameMusic(AbysmSoundEvents.MUSIC_OVERWORLD_FLORAL_REEF));
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
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEAGRASS_WARM)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, AbysmPlacedFeatures.TREES_BLOOMSHROOM);

		return builder;
	}

	@Override
	public SpawnSettings.Builder createSpawnSettings() {
		SpawnSettings.Builder builder = new SpawnSettings.Builder()
			.spawn(
				SpawnGroup.WATER_AMBIENT,
				50,
				new SpawnSettings.SpawnEntry(AbysmEntityTypes.BIG_FLORAL_FISH, 8, 8)
			)
			.spawn(
				SpawnGroup.WATER_AMBIENT,
				50,
				new SpawnSettings.SpawnEntry(AbysmEntityTypes.SMALL_FLORAL_FISH, 8, 8)
			);

		DefaultBiomeFeatures.addBatsAndMonsters(builder);
		return builder;
	}

	@Override
	public void addToGenerator() {
		// TODO: Replace this with a NoiseHypercube for more control
		BiomePlacement.replaceOverworld(
			BiomeKeys.WARM_OCEAN, key,
			0.5F
		);

		addSurfaceRules(
			condition(surface(),
				sequence(
					sequence(condition(
						STONE_DEPTH_CEILING,
						block(Blocks.SANDSTONE.getDefaultState())
					), block(Blocks.SAND.getDefaultState())),
					sequence(condition(
						STONE_DEPTH_CEILING,
						block(Blocks.STONE.getDefaultState())
					), block(Blocks.GRAVEL.getDefaultState()))
				)
			)
		);
	}
}
