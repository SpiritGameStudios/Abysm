package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.worldgen.feature.SprigsFeature;
import dev.spiritstudios.abysm.worldgen.feature.StalagmiteFeature;
import dev.spiritstudios.abysm.worldgen.feature.StateProviderFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public final class AbysmFeatures {
	public static final Feature<StalagmiteFeature.Config> STALAGMITE = new StalagmiteFeature();
	public static final Feature<StateProviderFeatureConfig> SPRIGS = new SprigsFeature();
}
