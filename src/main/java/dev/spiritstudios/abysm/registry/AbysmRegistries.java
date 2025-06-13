package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class AbysmRegistries {
	public static final RegistryKey<Registry<EntityPatternVariant>> ENTITY_PATTERN = ofRegistry("entity_pattern");
	public static final RegistryKey<Registry<BloomrayEntityVariant>> BLOOMRAY_ENTITY_VARIANT = ofRegistry("bloomray_variant");

	private static <T> RegistryKey<Registry<T>> ofRegistry(String path) {
		return RegistryKey.ofRegistry(Abysm.id(path));
	}

	public static void init() {
		// TODO: Reloadable DynReg
		DynamicRegistries.registerSynced(ENTITY_PATTERN, EntityPatternVariant.CODEC);
		DynamicRegistries.registerSynced(BLOOMRAY_ENTITY_VARIANT, BloomrayEntityVariant.CODEC);
	}
}
