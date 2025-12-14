package dev.spiritstudios.abysm.world.entity.pattern;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.world.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.core.registries.AbysmRegistryKeys;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class AbysmEntityPatternVariants {
	public static final ResourceKey<EntityPatternVariant> FLORAL_FISH_SMALL_COLORFUL = ofFloralFishSmall("colorful");
	public static final ResourceKey<EntityPatternVariant> FLORAL_FISH_SMALL_FLOWY = ofFloralFishSmall("flowy");
	public static final ResourceKey<EntityPatternVariant> FLORAL_FISH_SMALL_SHRIMPSTER = ofFloralFishSmall("shrimpster");
	public static final ResourceKey<EntityPatternVariant> FLORAL_FISH_SMALL_SPEEDY = ofFloralFishSmall("speedy");
	public static final ResourceKey<EntityPatternVariant> FLORAL_FISH_SMALL_WISE = ofFloralFishSmall("wise");
	public static final ResourceKey<EntityPatternVariant> FLORAL_FISH_SMALL_SPARKLE = ofFloralFishSmall("sparkle");
	public static final ResourceKey<EntityPatternVariant> FLORAL_FISH_SMALL_COCO = ofFloralFishSmall("coco");

	public static final ResourceKey<EntityPatternVariant> FLORAL_FISH_BIG_TERRA = ofFloralFishBig("terra");
	public static final ResourceKey<EntityPatternVariant> FLORAL_FISH_BIG_LOOKOUT = ofFloralFishBig("lookout");
	public static final ResourceKey<EntityPatternVariant> FLORAL_FISH_BIG_TABBY = ofFloralFishBig("tabby");
	public static final ResourceKey<EntityPatternVariant> FLORAL_FISH_BIG_AXOLOTL = ofFloralFishBig("axolotl");
	public static final ResourceKey<EntityPatternVariant> FLORAL_FISH_BIG_STRINGY = ofFloralFishBig("stringy");
	public static final ResourceKey<EntityPatternVariant> FLORAL_FISH_BIG_STARRY = ofFloralFishBig("starry");
	public static final ResourceKey<EntityPatternVariant> FLORAL_FISH_BIG_SIRFISHY = ofFloralFishBig("sirfishy");

	public static void bootstrap(BootstrapContext<EntityPatternVariant> registerable) {
		register(registerable, FLORAL_FISH_SMALL_COLORFUL, AbysmEntityTypes.SMALL_FLORAL_FISH);
		register(registerable, FLORAL_FISH_SMALL_FLOWY, AbysmEntityTypes.SMALL_FLORAL_FISH);
		register(registerable, FLORAL_FISH_SMALL_SHRIMPSTER, AbysmEntityTypes.SMALL_FLORAL_FISH);
		register(registerable, FLORAL_FISH_SMALL_SPEEDY, AbysmEntityTypes.SMALL_FLORAL_FISH);
		register(registerable, FLORAL_FISH_SMALL_WISE, AbysmEntityTypes.SMALL_FLORAL_FISH);
		register(registerable, FLORAL_FISH_SMALL_SPARKLE, AbysmEntityTypes.SMALL_FLORAL_FISH);
		register(registerable, FLORAL_FISH_SMALL_COCO, AbysmEntityTypes.SMALL_FLORAL_FISH);

		register(registerable, FLORAL_FISH_BIG_TERRA, AbysmEntityTypes.BIG_FLORAL_FISH);
		register(registerable, FLORAL_FISH_BIG_LOOKOUT, AbysmEntityTypes.BIG_FLORAL_FISH);
		register(registerable, FLORAL_FISH_BIG_TABBY, AbysmEntityTypes.BIG_FLORAL_FISH);
		register(registerable, FLORAL_FISH_BIG_AXOLOTL, AbysmEntityTypes.BIG_FLORAL_FISH);
		register(registerable, FLORAL_FISH_BIG_STRINGY, AbysmEntityTypes.BIG_FLORAL_FISH);
		register(registerable, FLORAL_FISH_BIG_STARRY, AbysmEntityTypes.BIG_FLORAL_FISH);
		register(registerable, FLORAL_FISH_BIG_SIRFISHY, AbysmEntityTypes.BIG_FLORAL_FISH);
	}

	private static void register(BootstrapContext<EntityPatternVariant> registry, ResourceKey<EntityPatternVariant> key, EntityType<?> entityType, Component name) {
		Identifier patternPath = Abysm.id("textures/entity/pattern/" + key.identifier().getPath() + ".png");
		EntityPatternVariant variant = new EntityPatternVariant(entityType, name, patternPath);
		register(registry, key, variant);
	}

	private static void register(BootstrapContext<EntityPatternVariant> registry, ResourceKey<EntityPatternVariant> key, EntityType<?> entityType) {
		register(registry, key, entityType, Component.translatable(key.registry().toLanguageKey() + "." + key.identifier().getPath().replace('/', '.')));
	}

	// I needed the boostrap method to all look the same (。﹏。*)
	private static void register(BootstrapContext<EntityPatternVariant> registry, ResourceKey<EntityPatternVariant> key, EntityPatternVariant variant) {
		registry.register(key, variant);
	}

	public static ResourceKey<EntityPatternVariant> ofFloralFishSmall(String path) {
		return of("floral_fish_small/" + path);
	}

	public static ResourceKey<EntityPatternVariant> ofFloralFishBig(String path) {
		return of("floral_fish_big/" + path);
	}

	public static ResourceKey<EntityPatternVariant> of(String path) {
		return ResourceKey.create(AbysmRegistryKeys.ENTITY_PATTERN, Abysm.id(path));
	}
}
