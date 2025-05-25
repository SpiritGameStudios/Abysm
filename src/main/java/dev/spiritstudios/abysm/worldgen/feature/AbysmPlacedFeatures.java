package dev.spiritstudios.abysm.worldgen.feature;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;

public class AbysmPlacedFeatures {
	public static void bootstrap(Registerable<PlacedFeature> registerable) {

	}

	public static RegistryKey<PlacedFeature> ofKey(String id) {
		return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Abysm.id(id));
	}
}
