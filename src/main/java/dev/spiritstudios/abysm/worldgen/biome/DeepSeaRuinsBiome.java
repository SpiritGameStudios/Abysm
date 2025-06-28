package dev.spiritstudios.abysm.worldgen.biome;

import com.terraformersmc.biolith.api.biome.BiomePlacement;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.sound.MusicType;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.OverworldBiomeCreator;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;

public final class DeepSeaRuinsBiome extends AbysmBiome {
	public DeepSeaRuinsBiome() {
		super(AbysmBiomes.DEEP_SEA_RUINS, 0.5F, true, 0.5F);
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

		DefaultBiomeFeatures.addMineables(builder);

		DefaultBiomeFeatures.addDefaultOres(builder);
		DefaultBiomeFeatures.addDefaultDisks(builder);
		DefaultBiomeFeatures.addWaterBiomeOakTrees(builder);
		DefaultBiomeFeatures.addDefaultFlowers(builder);
		DefaultBiomeFeatures.addDefaultGrass(builder);
		DefaultBiomeFeatures.addDefaultMushrooms(builder);
		DefaultBiomeFeatures.addDefaultVegetation(builder, true);

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
		var defaultParameter = MultiNoiseUtil.ParameterRange.of(-1.0F, 1.0F);

		BiomePlacement.addOverworld(
			key,
			MultiNoiseUtil.createNoiseHypercube(
				defaultParameter,
				defaultParameter,
				MultiNoiseUtil.ParameterRange.of(-1.05F, -0.455F),
				defaultParameter,
				MultiNoiseUtil.ParameterRange.of(0.2F, 0.8F),
				defaultParameter,
				0.0F
			)
		);
	}
}
