package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.entity.BigFloralFishEntity;
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
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_SMALL_SPARKLE = ofFloralFishSmall("sparkle");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_SMALL_COCO = ofFloralFishSmall("coco");

	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_BIG_TERRA = ofFloralFishBig("terra");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_BIG_LOOKOUT = ofFloralFishBig("lookout");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_BIG_TABBY = ofFloralFishBig("tabby");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_BIG_AXOLOTL = ofFloralFishBig("axolotl");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_BIG_STRINGY = ofFloralFishBig("stringy");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_BIG_STARRY = ofFloralFishBig("starry");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_BIG_SIRFISHY = ofFloralFishBig("sirfishy");

	public static void bootstrap(Registerable<EntityPatternVariant> registerable) {
		register(registerable, FLORAL_FISH_SMALL_COLORFUL, SmallFloralFishEntity.DEFAULT_PATTERN_VARIANT);
		register(registerable, FLORAL_FISH_SMALL_FLOWY, AbysmEntityTypes.SMALL_FLORAL_FISH, "Flowy");
		register(registerable, FLORAL_FISH_SMALL_SHRIMPSTER, AbysmEntityTypes.SMALL_FLORAL_FISH, "Shrimpster");
		register(registerable, FLORAL_FISH_SMALL_SPEEDY, AbysmEntityTypes.SMALL_FLORAL_FISH, "Speedy");
		register(registerable, FLORAL_FISH_SMALL_WISE, AbysmEntityTypes.SMALL_FLORAL_FISH, "Wise");
		register(registerable, FLORAL_FISH_SMALL_SPARKLE, AbysmEntityTypes.SMALL_FLORAL_FISH, "Sparkle");
		register(registerable, FLORAL_FISH_SMALL_COCO, AbysmEntityTypes.SMALL_FLORAL_FISH, "Coco");

		register(registerable, FLORAL_FISH_BIG_TERRA, BigFloralFishEntity.DEFAULT_PATTERN_VARIANT);
		register(registerable, FLORAL_FISH_BIG_LOOKOUT, AbysmEntityTypes.BIG_FLORAL_FISH, "Lookout");
		register(registerable, FLORAL_FISH_BIG_TABBY, AbysmEntityTypes.BIG_FLORAL_FISH, "Tabby");
		register(registerable, FLORAL_FISH_BIG_AXOLOTL, AbysmEntityTypes.BIG_FLORAL_FISH, "Axolotl");
		register(registerable, FLORAL_FISH_BIG_STRINGY, AbysmEntityTypes.BIG_FLORAL_FISH, "Stringy");
		register(registerable, FLORAL_FISH_BIG_STARRY, AbysmEntityTypes.BIG_FLORAL_FISH, "Starry");
		register(registerable, FLORAL_FISH_BIG_SIRFISHY, AbysmEntityTypes.BIG_FLORAL_FISH, "Coco");
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

	public static RegistryKey<EntityPatternVariant> ofFloralFishBig(String path) {
		return of("floral_fish_big/" + path);
	}

	public static RegistryKey<EntityPatternVariant> of(String path) {
		return RegistryKey.of(AbysmRegistries.ENTITY_PATTERN, Abysm.id(path));
	}
}
