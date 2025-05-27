package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class EntityPatternVariantRegistry {
	public static final RegistryKey<Registry<EntityPatternVariant>> ENTITY_PATTERN_VARIANT_KEY = RegistryKey.ofRegistry(Abysm.id("entity_pattern"));

	public static void init() {
		DynamicRegistries.registerSynced(ENTITY_PATTERN_VARIANT_KEY, EntityPatternVariant.CODEC);
	}

}
