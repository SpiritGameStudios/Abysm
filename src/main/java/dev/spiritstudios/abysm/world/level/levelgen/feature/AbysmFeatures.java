package dev.spiritstudios.abysm.world.level.levelgen.feature;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;

public final class AbysmFeatures {
	public static final Feature<StalagmiteFeature.Config> STALAGMITE = register(
		"stalagmite",
		new StalagmiteFeature()
	);

	public static final Feature<StateProviderFeatureConfig> SPRIGS = register(
		"sprigs",
		new SprigsFeature()
	);

	public static final Feature<UnderwaterVegetationFeature.Config> UNDERWATER_VEGETATION = register(
		"underwater_vegetation",
		new UnderwaterVegetationFeature(UnderwaterVegetationFeature.Config.CODEC)
	);

	public static final Feature<HangingLanternFeature.Config> HANGING_LANTERN = register(
		"hanging_lantern",
		new HangingLanternFeature(HangingLanternFeature.Config.CODEC)
	);

	public static final Feature<OrefurlFeature.Config> OREFURL = register(
		"orefuel",
		new OrefurlFeature(OrefurlFeature.Config.CODEC)
	);

	public static final Feature<UnderwaterVegetationPatchFeature.Config> UNDERWATER_VEGETATION_PATCH = register(
		"underwater_vegetation_patch",
		new UnderwaterVegetationPatchFeature(UnderwaterVegetationPatchFeature.Config.CODEC)
	);

	private static <T extends Feature<?>> T register(
		String id,
		T feature
	) {
		return Registry.register(
			BuiltInRegistries.FEATURE,
			Abysm.id(id),
			feature
		);
	}

	public static void init() {
		// I doubt anyone has datapacks relying on this but it cant hurt
		BuiltInRegistries.FEATURE.addAlias(
			Abysm.id("bloomshroom_vegetation"),
			Abysm.id("underwater_vegetation")
		);
	}
}
