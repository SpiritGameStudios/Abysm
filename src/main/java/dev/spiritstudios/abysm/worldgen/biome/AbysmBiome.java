package dev.spiritstudios.abysm.worldgen.biome;

import com.terraformersmc.biolith.api.surface.SurfaceGeneration;
import net.minecraft.block.Blocks;
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

import static net.minecraft.world.gen.surfacebuilder.MaterialRules.biome;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.block;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.condition;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.not;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.sequence;
import static net.minecraft.world.gen.surfacebuilder.MaterialRules.water;

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
			AbysmBiomes.FLORAL_REEF,
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

	public abstract GenerationSettings.Builder createGenerationSettings(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup);

	public abstract SpawnSettings.Builder createSpawnSettings();

	public abstract void addToGenerator();

	public void addSurfaceRules(MaterialRules.MaterialRule... rules) {
		SurfaceGeneration.addOverworldSurfaceRules(
			Identifier.ofVanilla("rules/overworld"),
			condition(
				biome(key),
				rules.length == 1 ? rules[0] : sequence(rules)
			)
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
