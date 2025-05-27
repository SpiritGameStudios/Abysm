package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.entity.SmallFloralFishEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class AbysmEntityPatternVariants {
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_SMALL_COLORFUL = ofFloralFishSmall("colorful");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_SMALL_FLOWY = ofFloralFishSmall("flowy");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_SMALL_SHRIMPSTER = ofFloralFishSmall("shrimpster");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_SMALL_SPEEDY = ofFloralFishSmall("speedy");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_SMALL_WISE = ofFloralFishSmall("wise");

	public static void bootstrap(Registerable<EntityPatternVariant> registerable) {
		register(registerable, FLORAL_FISH_SMALL_COLORFUL, SmallFloralFishEntity.DEFAULT_PATTERN_VARIANT);
		register(registerable, FLORAL_FISH_SMALL_FLOWY, AbysmEntityTypes.SMALL_FLORAL_FISH, "Flowy");
		register(registerable, FLORAL_FISH_SMALL_SHRIMPSTER, AbysmEntityTypes.SMALL_FLORAL_FISH, "Shrimpster");
		register(registerable, FLORAL_FISH_SMALL_SPEEDY, AbysmEntityTypes.SMALL_FLORAL_FISH, "Speedy");
		register(registerable, FLORAL_FISH_SMALL_WISE, AbysmEntityTypes.SMALL_FLORAL_FISH, "Wise");
	}

	// Unsure if names are going to need to be capitalized, or if I'll switch to translations,
	// so that param in the register method may get removed
	private static void register(Registerable<EntityPatternVariant> registry, RegistryKey<EntityPatternVariant> key, EntityType<?> entityType, String name) {
		Identifier patternPath = Abysm.id("textures/entity/pattern/" + key.getValue().getPath() + ".png");
		EntityPatternVariant variant = new EntityPatternVariant(entityType, name, patternPath);
		register(registry, key, variant);
	}

	// I needed the boostrap method to all look the same (。﹏。*)
	private static void register(Registerable<EntityPatternVariant> registry, RegistryKey<EntityPatternVariant> key, EntityPatternVariant variant) {
		registry.register(key, variant);
	}

	public static RegistryKey<EntityPatternVariant> ofFloralFishSmall(String path) {
		return of("floral_fish_small/" + path);
	}

	public static RegistryKey<EntityPatternVariant> of(String path) {
		return RegistryKey.of(EntityPatternVariantRegistry.ENTITY_PATTERN_VARIANT_KEY, Abysm.id(path));
	}

}
