package dev.spiritstudios.abysm.worldgen.biome;

import com.terraformersmc.biolith.api.surface.SurfaceGeneration;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

import static net.minecraft.world.gen.surfacebuilder.MaterialRules.*;

public abstract class AbysmBiome {
	protected final RegistryKey<Biome> key;
	protected final float temperature;

	protected final boolean precipitation;
	protected final float downfall;

	protected final Biome.TemperatureModifier temperatureModifier;

	public AbysmBiome(RegistryKey<Biome> key, float temperature, boolean precipitation, float downfall, Biome.TemperatureModifier temperatureModifier) {
		this.key = key;

		this.temperature = temperature;
		this.precipitation = precipitation;
		this.downfall = downfall;

		this.temperatureModifier = temperatureModifier;
	}

	public AbysmBiome(RegistryKey<Biome> key, float temperature, boolean precipitation, float downfall) {
		this(key, temperature, precipitation, downfall, Biome.TemperatureModifier.NONE);
	}

	public void bootstrap(Registerable<Biome> registerable, RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup) {
		registerable.register(
			key,
			new Biome.Builder()
				.precipitation(precipitation)
				.temperature(temperature)
				.downfall(downfall)
				.effects(createEffects().build())
				.spawnSettings(createSpawnSettings().build())
				.generationSettings(createGenerationSettings(featureLookup, carverLookup).build())
				.temperatureModifier(temperatureModifier)
				.build()
		);
	}

	public abstract BiomeEffects.Builder createEffects();

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

		createGenerationSettings(builder);

		return builder;
	}

	public GenerationSettings.Builder createGenerationSettings(GenerationSettings.LookupBackedBuilder builder) {
		return builder;
	}

	public abstract SpawnSettings.Builder createSpawnSettings();

	public abstract void addToGenerator();

	public void addOverworldSurfaceRulesForBiome(MaterialRules.MaterialRule... rules) {
		addOverworldSurfaceRules(onlyInThisBiome(rules));
	}

	public void addOverworldSurfaceRules(MaterialRule... rules) {
		SurfaceGeneration.addOverworldSurfaceRules(
			Identifier.ofVanilla("rules/overworld"),
			rules
		);
	}

	public MaterialRules.MaterialRule onlyInThisBiome(MaterialRules.MaterialRule... rules) {
		return condition(
			biome(this.key),
			rules.length == 1 ? rules[0] : sequence(rules)
		);
	}

	// This is private in OverworldBiomeCreator for some reason?
	protected static void addBasicFeatures(GenerationSettings.LookupBackedBuilder generationSettings) {
		DefaultBiomeFeatures.addLandCarvers(generationSettings);
		DefaultBiomeFeatures.addAmethystGeodes(generationSettings);
		DefaultBiomeFeatures.addDungeons(generationSettings);
		DefaultBiomeFeatures.addMineables(generationSettings);
		DefaultBiomeFeatures.addSprings(generationSettings);
		DefaultBiomeFeatures.addFrozenTopLayer(generationSettings);
	}
}
