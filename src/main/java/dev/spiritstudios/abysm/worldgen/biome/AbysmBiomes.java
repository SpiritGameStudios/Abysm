package dev.spiritstudios.abysm.worldgen.biome;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.worldgen.biome.custom.DeepSeaRuinsBiome;
import dev.spiritstudios.abysm.worldgen.biome.custom.FloralReefBiome;
import dev.spiritstudios.abysm.worldgen.biome.custom.GlowingCavesBiome;
import dev.spiritstudios.abysm.worldgen.biome.custom.InkdepthRealmBiome;
import dev.spiritstudios.abysm.worldgen.biome.custom.PearlescentSeaBiome;
import dev.spiritstudios.abysm.worldgen.biome.custom.TheEntwinedBiome;
import java.util.List;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class AbysmBiomes {
	public static final ResourceKey<Biome> FLORAL_REEF = ofKey("floral_reef");
	public static final ResourceKey<Biome> DEEP_SEA_RUINS = ofKey("deep_sea_ruins");
	public static final ResourceKey<Biome> THE_ENTWINED = ofKey("the_entwined");
	public static final ResourceKey<Biome> PEARLESCENT_SEA = ofKey("pearlescent_sea");
	public static final ResourceKey<Biome> INKDEPTH_REALM = ofKey("inkdepth_realm");
	public static final ResourceKey<Biome> GLOWING_CAVES = ofKey("glowing_caves");

	public static final List<AbysmBiome> BIOMES = List.of(
		new FloralReefBiome(),
		new DeepSeaRuinsBiome()//,
		//new TheEntwinedBiome(),
		//new PearlescentSeaBiome(),
		//new InkdepthRealmBiome(),
		//new GlowingCavesBiome
	);

	public static void bootstrap(BootstrapContext<Biome> registerable) {
		HolderGetter<PlacedFeature> featureLookup = registerable.lookup(Registries.PLACED_FEATURE);
		HolderGetter<ConfiguredWorldCarver<?>> carverLookup = registerable.lookup(Registries.CONFIGURED_CARVER);

		for (AbysmBiome biome : BIOMES) biome.bootstrap(registerable, featureLookup, carverLookup);
	}

	private static ResourceKey<Biome> ofKey(String path) {
		return ResourceKey.create(Registries.BIOME, Abysm.id(path));
	}

	public static void addAllToGenerator() {
		BIOMES.forEach(AbysmBiome::addToGenerator);
	}
}
