package dev.spiritstudios.abysm.world.level.levelgen.biome;

import com.terraformersmc.biolith.api.surface.SurfaceGeneration;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import static net.minecraft.world.level.levelgen.SurfaceRules.*;

public abstract class AbysmBiome {
	protected final ResourceKey<Biome> key;
	protected final float temperature;

	protected final boolean precipitation;
	protected final float downfall;

	protected final Biome.TemperatureModifier temperatureModifier;

	public AbysmBiome(ResourceKey<Biome> key, float temperature, boolean precipitation, float downfall, Biome.TemperatureModifier temperatureModifier) {
		this.key = key;

		this.temperature = temperature;
		this.precipitation = precipitation;
		this.downfall = downfall;

		this.temperatureModifier = temperatureModifier;
	}

	public AbysmBiome(ResourceKey<Biome> key, float temperature, boolean precipitation, float downfall) {
		this(key, temperature, precipitation, downfall, Biome.TemperatureModifier.NONE);
	}

	public void bootstrap(BootstrapContext<Biome> registerable, HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
		registerable.register(
			this.key,
			new Biome.BiomeBuilder()
				.hasPrecipitation(this.precipitation)
				.temperature(this.temperature)
				.downfall(this.downfall)
				.specialEffects(createEffects().build())
				.mobSpawnSettings(createSpawnSettings().build())
				.generationSettings(createGenerationSettings(featureLookup, carverLookup).build())
				.temperatureAdjustment(this.temperatureModifier)
				.putAttributes(createAttributes())
				.build()
		);
	}

	public abstract EnvironmentAttributeMap.Builder createAttributes();

	public abstract BiomeSpecialEffects.Builder createEffects();

	public BiomeGenerationSettings.PlainBuilder createGenerationSettings(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
		BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);

		addBasicFeatures(builder);

		BiomeDefaultFeatures.addDefaultOres(builder);
		BiomeDefaultFeatures.addDefaultSoftDisks(builder);
		BiomeDefaultFeatures.addWaterTrees(builder);
		BiomeDefaultFeatures.addDefaultFlowers(builder);
		BiomeDefaultFeatures.addDefaultGrass(builder);
		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder, true);

		createGenerationSettings(builder);

		return builder;
	}

	public void createGenerationSettings(BiomeGenerationSettings.Builder builder) {
	}

	public abstract MobSpawnSettings.Builder createSpawnSettings();

	public abstract void addToGenerator();

	public void addOverworldSurfaceRulesForBiome(SurfaceRules.RuleSource... rules) {
		addOverworldSurfaceRules(onlyInThisBiome(rules));
	}

	public void addOverworldSurfaceRules(RuleSource... rules) {
		SurfaceGeneration.addOverworldSurfaceRules(
			Identifier.withDefaultNamespace("rules/overworld"),
			rules
		);
	}

	public SurfaceRules.RuleSource onlyInThisBiome(SurfaceRules.RuleSource... rules) {
		return ifTrue(
			isBiome(this.key),
			rules.length == 1 ? rules[0] : sequence(rules)
		);
	}

	// This is private in OverworldBiomeCreator for some reason?
	protected static void addBasicFeatures(BiomeGenerationSettings.Builder generationSettings) {
		BiomeDefaultFeatures.addDefaultCarversAndLakes(generationSettings);
		BiomeDefaultFeatures.addDefaultCrystalFormations(generationSettings);
		BiomeDefaultFeatures.addDefaultMonsterRoom(generationSettings);
		BiomeDefaultFeatures.addDefaultUndergroundVariety(generationSettings);
		BiomeDefaultFeatures.addDefaultSprings(generationSettings);
		BiomeDefaultFeatures.addSurfaceFreezing(generationSettings);
	}
}
