package dev.spiritstudios.abysm.worldgen.biome.custom;

import com.terraformersmc.biolith.api.biome.BiomePlacement;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiome;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import dev.spiritstudios.abysm.worldgen.feature.AbysmPlacedFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.MusicType;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.OverworldBiomeCreator;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.OceanPlacedFeatures;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

import static net.minecraft.world.gen.surfacebuilder.MaterialRules.MaterialRule;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.STONE_DEPTH_CEILING;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.STONE_DEPTH_FLOOR;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH_RANGE_6;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.block;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.condition;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.sequence;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.surface;

public final class FloralReefBiome extends AbysmBiome {

	public FloralReefBiome() {
		super(AbysmBiomes.FLORAL_REEF, 0.5F, true, 0.5F);
	}

	@Override
	public BiomeEffects.Builder createEffects() {
		return new BiomeEffects.Builder()
			.waterColor(0x0093C4)
			.waterFogColor(0x08304C)
			.fogColor(0xC0D8FF)
			.skyColor(OverworldBiomeCreator.getSkyColor(this.temperature))
			.moodSound(BiomeMoodSound.CAVE)
			.music(MusicType.createIngameMusic(AbysmSoundEvents.MUSIC_OVERWORLD_FLORAL_REEF));
	}

	@Override
	public void createGenerationSettings(GenerationSettings.LookupBackedBuilder builder) {
		builder
			.feature(GenerationStep.Feature.LOCAL_MODIFICATIONS, AbysmPlacedFeatures.FLOROPUMICE_STALAGMITES)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, AbysmPlacedFeatures.MIXED_BLOOMED_PATCH)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, AbysmPlacedFeatures.PATCH_SPRIGS)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, OceanPlacedFeatures.SEAGRASS_WARM)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, AbysmPlacedFeatures.PATCH_PETALS_UNDERWATER)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, AbysmPlacedFeatures.TREES_BLOOMSHROOM)
			.feature(GenerationStep.Feature.VEGETAL_DECORATION, AbysmPlacedFeatures.PATCH_PETALS_SURFACE);
	}

	@Override
	public SpawnSettings.Builder createSpawnSettings() {
		SpawnSettings.Builder builder = new SpawnSettings.Builder()
			.spawn(
				SpawnGroup.WATER_AMBIENT,
				50,
				new SpawnSettings.SpawnEntry(AbysmEntityTypes.BIG_FLORAL_FISH, 8, 16)
			)
			.spawn(
				SpawnGroup.WATER_AMBIENT,
				50,
				new SpawnSettings.SpawnEntry(AbysmEntityTypes.SMALL_FLORAL_FISH, 8, 16)
			)
			.spawn(
				SpawnGroup.WATER_AMBIENT,
				50,
				new SpawnSettings.SpawnEntry(AbysmEntityTypes.PADDLEFISH, 4, 8)
			)
			.spawn(
				SpawnGroup.WATER_CREATURE,
				1,
				new SpawnSettings.SpawnEntry(AbysmEntityTypes.BLOOMRAY, 1, 2)
			)
			.spawn(
				SpawnGroup.WATER_AMBIENT,
				50,
				new SpawnSettings.SpawnEntry(AbysmEntityTypes.GUP_GUP, 20, 30)
			)
			.spawn(
				SpawnGroup.WATER_AMBIENT,
				50,
				new SpawnSettings.SpawnEntry(AbysmEntityTypes.AROWANA_MAGICII, 5, 8)
			)
			.spawn(
				SpawnGroup.WATER_AMBIENT,
				50,
				new SpawnSettings.SpawnEntry(AbysmEntityTypes.SYNTHETHIC_ORNIOTHOPE, 5, 8)
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

		MaterialRule SAND = block(Blocks.SAND.getDefaultState());
		MaterialRule SANDSTONE = block(Blocks.SANDSTONE.getDefaultState());

		MaterialRule SDC_SANDSTONE_SAND = sequence(
			condition(STONE_DEPTH_CEILING, SANDSTONE),
			SAND
		);

		MaterialRule surfaceRule = sequence(
			condition(
				STONE_DEPTH_FLOOR,
				condition(
					MaterialRules.water(-1, 0),
					SDC_SANDSTONE_SAND // one layer of sand at surface and shallow water
				)
			),
			// unlike warm ocean, apply thick sand and sandstone no matter the depth
			condition(
				STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH,
				SDC_SANDSTONE_SAND // place a few layers of sand (+ sandstone at bottom)
			),
			condition(STONE_DEPTH_FLOOR_WITH_SURFACE_DEPTH_RANGE_6, SANDSTONE) // place thick sandstone at shallow water
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
			condition(surface(), surfaceRule)
		);
	}
}
