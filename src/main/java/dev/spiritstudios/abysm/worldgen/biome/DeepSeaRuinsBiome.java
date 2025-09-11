package dev.spiritstudios.abysm.worldgen.biome;

import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import dev.spiritstudios.abysm.worldgen.feature.AbysmPlacedFeatures;
import dev.spiritstudios.abysm.worldgen.noise.AbysmNoiseParameters;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.MusicType;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.OverworldBiomeCreator;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;

import static net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRule;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.block;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.condition;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.noiseThreshold;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.not;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.sequence;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.stoneDepth;

public final class DeepSeaRuinsBiome extends AbysmBiome {
	public DeepSeaRuinsBiome() {
		super(AbysmBiomes.DEEP_SEA_RUINS, 0.5F, true, 0.5F);
	}

	@Override
	public BiomeEffects.Builder createEffects() {
		return new BiomeEffects.Builder()
			.waterColor(0x1C7A56)
			.waterFogColor(0x06140F)
			.fogColor(0xC0D8FF)
			.skyColor(OverworldBiomeCreator.getSkyColor(temperature))
			.moodSound(BiomeMoodSound.CAVE)
			.music(MusicType.createIngameMusic(AbysmSoundEvents.MUSIC_OVERWORLD_DEEP_SEA_RUINS));
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
			.feature(GenerationStep.Feature.LOCAL_MODIFICATIONS, AbysmPlacedFeatures.SURFACE_SMOOTH_FLOROPUMICE_STALAGMITES)
			.feature(GenerationStep.Feature.UNDERGROUND_ORES, AbysmPlacedFeatures.ORE_CLAY_DREGLOAM)
			.feature(GenerationStep.Feature.UNDERGROUND_ORES, AbysmPlacedFeatures.ORE_GOLDEN_LAZULI_DREGLOAM)
			.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, AbysmPlacedFeatures.PATCH_HANGING_LANTERN)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, AbysmPlacedFeatures.OOZE_PATCH)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, AbysmPlacedFeatures.PATCH_SEAGRASS_CAVE)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, AbysmPlacedFeatures.PATCH_GOLDEN_LAZULI_OREFURL);

		return builder;
	}

	@Override
	public SpawnSettings.Builder createSpawnSettings() {
		SpawnSettings.Builder builder = new SpawnSettings.Builder()
			.spawn(
				SpawnGroup.UNDERGROUND_WATER_CREATURE,
				70,
				new SpawnSettings.SpawnEntry(AbysmEntityTypes.LECTORFIN, 5, 8)
			)
			.spawn(
				SpawnGroup.WATER_AMBIENT,
				40,
				new SpawnSettings.SpawnEntry(AbysmEntityTypes.GUP_GUP, 20, 30)
			)
			.spawn(
				SpawnGroup.WATER_AMBIENT,
				40,
				new SpawnSettings.SpawnEntry(AbysmEntityTypes.AROWANA_MAGICII, 5, 8)
			)
			.spawn(
				SpawnGroup.WATER_CREATURE,
				1,
				new SpawnSettings.SpawnEntry(AbysmEntityTypes.RETICULATED_FLIPRAY, 1, 2)
			);

		DefaultBiomeFeatures.addBatsAndMonsters(builder);
		return builder;
	}

	@Override
	public void addToGenerator() {
		// Deep Sea Ruins is not generated through typical biome gen, and is instead generated within the bounds of its structure

		MaterialRule DREGLOAM = block(AbysmBlocks.DREGLOAM.getDefaultState());
		MaterialRule MUD = block(Blocks.MUD.getDefaultState());

		MaterialRule rule = condition(
			not(stoneDepth(3, false, VerticalSurfaceType.FLOOR)), // do not apply near surface, in case the biome somehow leaks out of the cave too far
			sequence(
				condition(
					noiseThreshold(AbysmNoiseParameters.RUINS_SEDIMENT_TYPE, -0.85F, 0.45F),
					sequence(
						condition( // one layer of mud on bottom
							stoneDepth(0, false, VerticalSurfaceType.CEILING),
							MUD
						),
						condition(
							noiseThreshold(AbysmNoiseParameters.RUINS_SEDIMENT_TYPE, -0.65F, 0.3F),
							sequence(
								condition( // two layers of mud on bottom
									stoneDepth(1, false, VerticalSurfaceType.CEILING),
									MUD
								),
								condition(
									noiseThreshold(AbysmNoiseParameters.RUINS_SEDIMENT_TYPE, -0.3F, 0.05F),
									condition( // three layers of mud on bottom
										stoneDepth(2, false, VerticalSurfaceType.CEILING),
										MUD
									)
								)
							)
						)
					)
				),
				DREGLOAM // fill rest with dregloam
			)
		);

		addOverworldSurfaceRulesForBiome(
			rule
		);
	}
}
