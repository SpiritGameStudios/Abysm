package dev.spiritstudios.abysm.registry.tags;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public class AbysmBiomeTags {
	public static final TagKey<Biome> DEEP_SEA_RUINS_HAS_STRUCTURE = of("has_structure/deep_sea_ruins");

	private static TagKey<Biome> of(String id) {
		return TagKey.of(RegistryKeys.BIOME, Abysm.id(id));
	}
}
