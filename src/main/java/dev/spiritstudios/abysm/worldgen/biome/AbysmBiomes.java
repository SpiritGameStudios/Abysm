package dev.spiritstudios.abysm.worldgen.biome;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.worldgen.biome.custom.DeepSeaRuinsBiome;
import dev.spiritstudios.abysm.worldgen.biome.custom.FloralReefBiome;
import dev.spiritstudios.abysm.worldgen.biome.custom.GlowingCavesBiome;
import dev.spiritstudios.abysm.worldgen.biome.custom.InkdepthRealmBiome;
import dev.spiritstudios.abysm.worldgen.biome.custom.PearlescentSeaBiome;
import dev.spiritstudios.abysm.worldgen.biome.custom.TheEntwinedBiome;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.List;

public final class AbysmBiomes {
	public static final RegistryKey<Biome> FLORAL_REEF = ofKey("floral_reef");
	public static final RegistryKey<Biome> DEEP_SEA_RUINS = ofKey("deep_sea_ruins");
	public static final RegistryKey<Biome> THE_ENTWINED = ofKey("the_entwined");
	public static final RegistryKey<Biome> PEARLESCENT_SEA = ofKey("pearlescent_sea");
	public static final RegistryKey<Biome> INKDEPTH_REALM = ofKey("inkdepth_realm");
	public static final RegistryKey<Biome> GLOWING_CAVES = ofKey("glowing_caves");

	public static final List<AbysmBiome> BIOMES = List.of(
		new FloralReefBiome(),
		new DeepSeaRuinsBiome()//,
		//new TheEntwinedBiome(),
		//new PearlescentSeaBiome(),
		//new InkdepthRealmBiome(),
		//new GlowingCavesBiome
	);

	public static void bootstrap(Registerable<Biome> registerable) {
		RegistryEntryLookup<PlacedFeature> featureLookup = registerable.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
		RegistryEntryLookup<ConfiguredCarver<?>> carverLookup = registerable.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);

		for (AbysmBiome biome : BIOMES) biome.bootstrap(registerable, featureLookup, carverLookup);
	}

	private static RegistryKey<Biome> ofKey(String path) {
		return RegistryKey.of(RegistryKeys.BIOME, Abysm.id(path));
	}

	public static void addAllToGenerator() {
		for (AbysmBiome biome : BIOMES) biome.addToGenerator();
	}
}
