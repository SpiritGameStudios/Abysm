package dev.spiritstudios.abysm.worldgen.biome;

import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import dev.spiritstudios.abysm.worldgen.feature.AbysmPlacedFeatures;
import dev.spiritstudios.abysm.worldgen.noise.AbysmNoiseParameters;
import net.minecraft.block.Blocks;
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

import static net.minecraft.world.gen.surfacebuilder.MaterialRules.*;

public final class DeepSeaRuinsBiome extends AbysmBiome {
	public DeepSeaRuinsBiome() {
		super(AbysmBiomes.DEEP_SEA_RUINS, 0.5F, true, 0.5F);
	}

	@Override
	public BiomeEffects.Builder createEffects() {
		// TODO: Temporary colors
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
			.feature(GenerationStep.Feature.UNDERGROUND_DECORATION, AbysmPlacedFeatures.PATCH_HANGING_LANTERN)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, AbysmPlacedFeatures.PATCH_SEAGRASS_CAVE)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, AbysmPlacedFeatures.PATCH_GOLDEN_LAZULI_OREFURL);

		return builder;
	}

	@Override
	public SpawnSettings.Builder createSpawnSettings() {
		SpawnSettings.Builder builder = new SpawnSettings.Builder();

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
