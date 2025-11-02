package dev.spiritstudios.abysm.worldgen.feature;

import net.minecraft.world.level.levelgen.feature.Feature;

public final class AbysmFeatures {
	/**
	 * Note: all entries are automatically registered in {@link dev.spiritstudios.abysm.Abysm}
	 */

	public static final Feature<StalagmiteFeature.Config> STALAGMITE = new StalagmiteFeature();
	public static final Feature<StateProviderFeatureConfig> SPRIGS = new SprigsFeature();
	public static final Feature<UnderwaterVegetationFeature.Config> BLOOMSHROOM_VEGETATION = new UnderwaterVegetationFeature(UnderwaterVegetationFeature.Config.CODEC);
	public static final Feature<HangingLanternFeature.Config> HANGING_LANTERN = new HangingLanternFeature(HangingLanternFeature.Config.CODEC);
	public static final Feature<OrefurlFeature.Config> OREFURL = new OrefurlFeature(OrefurlFeature.Config.CODEC);
	public static final Feature<UnderwaterVegetationPatchFeature.Config> UNDERWATER_VEGETATION_PATCH = new UnderwaterVegetationPatchFeature(UnderwaterVegetationPatchFeature.Config.CODEC);
}
