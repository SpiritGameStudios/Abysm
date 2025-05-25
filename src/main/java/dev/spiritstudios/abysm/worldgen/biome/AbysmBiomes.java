package dev.spiritstudios.abysm.worldgen.biome;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.List;

public final class AbysmBiomes {
	public static final RegistryKey<Biome> FLORAL_REEF = of("floral_reef");

	public static final List<AbysmBiome> BIOMES = List.of(
		new FloralReefBiome()
	);

	public static void bootstrap(Registerable<Biome> registerable) {
		RegistryEntryLookup<PlacedFeature> featureLookup = registerable.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
		RegistryEntryLookup<ConfiguredCarver<?>> carverLookup = registerable.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);

		for (AbysmBiome biome : BIOMES) biome.bootstrap(registerable, featureLookup, carverLookup);
	}

	private static RegistryKey<Biome> of(String path) {
		return RegistryKey.of(RegistryKeys.BIOME, Abysm.id(path));
	}

	public static void addAllToGenerator() {
		for (AbysmBiome biome : BIOMES) biome.addToGenerator();
	}
}
