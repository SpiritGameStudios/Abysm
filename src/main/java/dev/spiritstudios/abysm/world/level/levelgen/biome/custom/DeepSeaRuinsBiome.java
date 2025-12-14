package dev.spiritstudios.abysm.world.level.levelgen.biome.custom;

import dev.spiritstudios.abysm.core.registries.AbysmSoundEvents;
import dev.spiritstudios.abysm.world.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import dev.spiritstudios.abysm.world.level.levelgen.biome.AbysmBiome;
import dev.spiritstudios.abysm.world.level.levelgen.biome.AbysmBiomes;
import dev.spiritstudios.abysm.world.level.levelgen.feature.AbysmPlacedFeatures;
import dev.spiritstudios.abysm.world.level.levelgen.noise.AbysmNoiseParameters;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.attribute.AmbientSounds;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

import static net.minecraft.world.level.levelgen.SurfaceRules.ifTrue;
import static net.minecraft.world.level.levelgen.SurfaceRules.noiseCondition;
import static net.minecraft.world.level.levelgen.SurfaceRules.not;
import static net.minecraft.world.level.levelgen.SurfaceRules.sequence;
import static net.minecraft.world.level.levelgen.SurfaceRules.state;
import static net.minecraft.world.level.levelgen.SurfaceRules.stoneDepthCheck;

public final class DeepSeaRuinsBiome extends AbysmBiome {

	public DeepSeaRuinsBiome() {
		super(AbysmBiomes.DEEP_SEA_RUINS, 0.5F, true, 0.5F);
	}

	@Override
	public EnvironmentAttributeMap.Builder createAttributes() {
		return EnvironmentAttributeMap.builder()
			.set(EnvironmentAttributes.WATER_FOG_COLOR, 0x06140F)
			.set(EnvironmentAttributes.FOG_COLOR, 0xC0D8FF)
			.set(EnvironmentAttributes.SKY_COLOR, OverworldBiomes.calculateSkyColor(this.temperature))
			.set(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS)
			.set(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(AbysmSoundEvents.MUSIC_OVERWORLD_DEEP_SEA_RUINS));
	}

	@Override
	public BiomeSpecialEffects.Builder createEffects() {
		return new BiomeSpecialEffects.Builder()
			.waterColor(0x1C7A56);
	}

	@Override
	public void createGenerationSettings(BiomeGenerationSettings.Builder builder) {
		builder
			.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, AbysmPlacedFeatures.SURFACE_SMOOTH_FLOROPUMICE_STALAGMITES)
			.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AbysmPlacedFeatures.ORE_CLAY_DREGLOAM)
			.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, AbysmPlacedFeatures.ORE_GOLDEN_LAZULI_DREGLOAM)
			.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, AbysmPlacedFeatures.PATCH_HANGING_LANTERN)
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AbysmPlacedFeatures.OOZE_PATCH)
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AbysmPlacedFeatures.PATCH_SEAGRASS_CAVE)
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AbysmPlacedFeatures.PATCH_GOLDEN_LAZULI_OREFURL);
	}

	@Override
	public MobSpawnSettings.Builder createSpawnSettings() {
		MobSpawnSettings.Builder builder = new MobSpawnSettings.Builder()
			.addSpawn(
				MobCategory.UNDERGROUND_WATER_CREATURE,
				70,
				new MobSpawnSettings.SpawnerData(AbysmEntityTypes.LECTORFIN, 5, 8)
			)
			.addSpawn(
				MobCategory.WATER_AMBIENT,
				40,
				new MobSpawnSettings.SpawnerData(AbysmEntityTypes.GUP_GUP, 20, 30)
			)
			.addSpawn(
				MobCategory.WATER_AMBIENT,
				40,
				new MobSpawnSettings.SpawnerData(AbysmEntityTypes.AROWANA_MAGICII, 5, 8)
			).addSpawn(
				MobCategory.WATER_CREATURE,
				1,
				new MobSpawnSettings.SpawnerData(AbysmEntityTypes.RETICULATED_FLIPRAY, 1, 2)
			);

		BiomeDefaultFeatures.commonSpawns(builder);
		return builder;
	}

	@Override
	public void addToGenerator() {
		// Deep Sea Ruins is not generated through typical biome gen, and is instead generated within the bounds of its structure

		RuleSource DREGLOAM = state(AbysmBlocks.DREGLOAM.defaultBlockState());
		RuleSource MUD = state(Blocks.MUD.defaultBlockState());

		RuleSource rule = ifTrue(
			not(stoneDepthCheck(3, false, CaveSurface.FLOOR)), // do not apply near surface, in case the biome somehow leaks out of the cave too far
			sequence(
				ifTrue(
					noiseCondition(AbysmNoiseParameters.RUINS_SEDIMENT_TYPE, -0.85F, 0.45F),
					sequence(
						ifTrue( // one layer of mud on bottom
							stoneDepthCheck(0, false, CaveSurface.CEILING),
							MUD
						),
						ifTrue(
							noiseCondition(AbysmNoiseParameters.RUINS_SEDIMENT_TYPE, -0.65F, 0.3F),
							sequence(
								ifTrue( // two layers of mud on bottom
									stoneDepthCheck(1, false, CaveSurface.CEILING),
									MUD
								),
								ifTrue(
									noiseCondition(AbysmNoiseParameters.RUINS_SEDIMENT_TYPE, -0.3F, 0.05F),
									ifTrue( // three layers of mud on bottom
										stoneDepthCheck(2, false, CaveSurface.CEILING),
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
