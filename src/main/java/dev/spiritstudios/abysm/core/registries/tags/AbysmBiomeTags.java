package dev.spiritstudios.abysm.core.registries.tags;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public final class AbysmBiomeTags {
	public static final TagKey<Biome> DEEP_SEA_RUINS_HAS_STRUCTURE = of("has_structure/deep_sea_ruins");

	public static final TagKey<Biome> SPAWNS_VARIANT_DEPTH_SNAPPER = of("spawns_variant/depth_snapper");


	private static TagKey<Biome> of(String id) {
		return TagKey.create(Registries.BIOME, Abysm.id(id));
	}
}
