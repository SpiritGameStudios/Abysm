package dev.spiritstudios.abysm.worldgen.biome.custom;

import com.terraformersmc.biolith.api.biome.BiomePlacement;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiome;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import dev.spiritstudios.abysm.worldgen.feature.AbysmPlacedFeatures;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.sounds.Musics;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;

import static net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
import static net.minecraft.world.level.levelgen.SurfaceRules.ON_CEILING;
import static net.minecraft.world.level.levelgen.SurfaceRules.ON_FLOOR;
import static net.minecraft.world.level.levelgen.SurfaceRules.UNDER_FLOOR;
import static net.minecraft.world.level.levelgen.SurfaceRules.DEEP_UNDER_FLOOR;
import static net.minecraft.world.level.levelgen.SurfaceRules.state;
import static net.minecraft.world.level.levelgen.SurfaceRules.ifTrue;
import static net.minecraft.world.level.levelgen.SurfaceRules.sequence;
import static net.minecraft.world.level.levelgen.SurfaceRules.abovePreliminarySurface;

public final class FloralReefBiome extends AbysmBiome {

	public FloralReefBiome() {
		super(AbysmBiomes.FLORAL_REEF, 0.5F, true, 0.5F);
	}

	@Override
	public BiomeSpecialEffects.Builder createEffects() {
		return new BiomeSpecialEffects.Builder()
			.waterColor(0x0093C4)
			.waterFogColor(0x08304C)
			.fogColor(0xC0D8FF)
			.skyColor(OverworldBiomes.calculateSkyColor(this.temperature))
			.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
			.backgroundMusic(Musics.createGameMusic(AbysmSoundEvents.MUSIC_OVERWORLD_FLORAL_REEF));
	}

	@Override
	public void createGenerationSettings(BiomeGenerationSettings.Builder builder) {
		builder
			.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, AbysmPlacedFeatures.FLOROPUMICE_STALAGMITES)
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AbysmPlacedFeatures.MIXED_BLOOMED_PATCH)
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AbysmPlacedFeatures.PATCH_SPRIGS)
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_WARM)
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AbysmPlacedFeatures.PATCH_PETALS_UNDERWATER)
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AbysmPlacedFeatures.TREES_BLOOMSHROOM)
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AbysmPlacedFeatures.PATCH_PETALS_SURFACE);
	}

	@Override
	public MobSpawnSettings.Builder createSpawnSettings() {
		MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder()
			.addSpawn(
				MobCategory.WATER_AMBIENT,
				50,
				new MobSpawnSettings.SpawnerData(AbysmEntityTypes.BIG_FLORAL_FISH, 8, 16)
			)
			.addSpawn(
				MobCategory.WATER_AMBIENT,
				50,
				new MobSpawnSettings.SpawnerData(AbysmEntityTypes.SMALL_FLORAL_FISH, 8, 16)
			)
			.addSpawn(
				MobCategory.WATER_AMBIENT,
				50,
				new MobSpawnSettings.SpawnerData(AbysmEntityTypes.PADDLEFISH, 4, 8)
			)
			.addSpawn(
				MobCategory.WATER_CREATURE,
				1,
				new MobSpawnSettings.SpawnerData(AbysmEntityTypes.BLOOMRAY, 1, 2)
			)
			.addSpawn(
				MobCategory.WATER_AMBIENT,
				50,
				new MobSpawnSettings.SpawnerData(AbysmEntityTypes.GUP_GUP, 20, 30)
			)
			.addSpawn(
				MobCategory.WATER_AMBIENT,
				50,
				new MobSpawnSettings.SpawnerData(AbysmEntityTypes.AROWANA_MAGICII, 5, 8)
			)
			.addSpawn(
				MobCategory.WATER_AMBIENT,
				50,
				new MobSpawnSettings.SpawnerData(AbysmEntityTypes.SYNTHETHIC_ORNIOTHOPE, 5, 8)
			);

		BiomeDefaultFeatures.commonSpawns(builder);
		return builder;
	}

	@Override
	public void addToGenerator() {
		// TODO: Replace this with a NoiseHypercube for more control
		BiomePlacement.replaceOverworld(
			Biomes.WARM_OCEAN, key,
			0.5F
		);

		RuleSource SAND = state(Blocks.SAND.defaultBlockState());
		RuleSource SANDSTONE = state(Blocks.SANDSTONE.defaultBlockState());

		RuleSource SDC_SANDSTONE_SAND = sequence(
			ifTrue(ON_CEILING, SANDSTONE),
			SAND
		);

		RuleSource surfaceRule = sequence(
			ifTrue(
				ON_FLOOR,
				ifTrue(
					SurfaceRules.waterBlockCheck(-1, 0),
					SDC_SANDSTONE_SAND // one layer of sand at surface and shallow water
				)
			),
			// unlike warm ocean, apply thick sand and sandstone no matter the depth
			ifTrue(
				UNDER_FLOOR,
				SDC_SANDSTONE_SAND // place a few layers of sand (+ sandstone at bottom)
			),
			ifTrue(DEEP_UNDER_FLOOR, SANDSTONE) // place thick sandstone at shallow water
		);

		/*
		// surface rule to replicate warm ocean behaviour, for reference
		// this comment can be removed if no longer useful

		MaterialRule STONE = block(Blocks.STONE.getDefaultState());
		MaterialRule GRAVEL = block(Blocks.GRAVEL.getDefaultState());

		MaterialRule SDC_STONE_GRAVEL = sequence(
			condition(STONE_DEPTH_CEILING, STONE),
			GRAVEL
		);

		MaterialRule surfaceRule = sequence(
			condition(
				STONE_DEPTH_FLOOR,
				condition(
					MaterialRules.water(-1, 0),
					SDC_SANDSTONE_SAND // one layer of sand at surface and shallow water
				)
			),
			condition(
				MaterialRules.waterWithStoneDepth(-6, -1),
				sequence(
					condition(
						STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH,
						SDC_SANDSTONE_SAND // place a few layers of sand (+ sandstone at bottom)
					),
					condition(STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH_RANGE_6, SANDSTONE) // place thick sandstone at shallow water
				)
			),
			condition(STONE_DEPTH_FLOOR,
				sequence(
					SDC_SANDSTONE_SAND, // one layer of sand
					SDC_STONE_GRAVEL // one layer of gravel... theoretically? probably entirely blocked by the sand layer
				)
			)
		);
		*/

		addOverworldSurfaceRulesForBiome(
			ifTrue(abovePreliminarySurface(), surfaceRule)
		);
	}
}
