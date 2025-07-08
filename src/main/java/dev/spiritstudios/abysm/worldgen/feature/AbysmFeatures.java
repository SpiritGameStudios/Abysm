package dev.spiritstudios.abysm.worldgen.feature;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NetherForestVegetationFeatureConfig;

public final class AbysmFeatures {
	/**
	 * Note: all entries are automatically registered in {@link dev.spiritstudios.abysm.Abysm}
	 */

	public static final Feature<StalagmiteFeature.Config> STALAGMITE = new StalagmiteFeature();
	public static final Feature<StateProviderFeatureConfig> SPRIGS = new SprigsFeature();
	public static final Feature<NetherForestVegetationFeatureConfig> BLOOMSHROOM_VEGETATION = new BloomshroomVegetationFeature(NetherForestVegetationFeatureConfig.VEGETATION_CODEC);
	public static final Feature<HangingLanternFeature.Config> HANGING_LANTERN = new HangingLanternFeature(HangingLanternFeature.Config.CODEC);
}
