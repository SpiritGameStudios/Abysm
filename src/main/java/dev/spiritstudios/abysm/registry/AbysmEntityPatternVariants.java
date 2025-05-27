package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;

public class AbysmEntityPatternVariants {
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_SMALL_COLORFUL = of("floral_fish_small_colorful");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_SMALL_FLOWY = of("floral_fish_small_flowy");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_SMALL_SHRIMPSTER = of("floral_fish_small_shrimpster");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_SMALL_SPEEDY = of("floral_fish_small_speedy");
	public static final RegistryKey<EntityPatternVariant> FLORAL_FISH_SMALL_WISE = of("floral_fish_small_wise");

	public static void bootstrap(Registerable<EntityPatternVariant> registerable) {
		registerable.register(FLORAL_FISH_SMALL_COLORFUL, new EntityPatternVariant(
				AbysmEntityTypes.SMALL_FLORAL_FISH, "Colorful", Abysm.id("textures/entity/pattern/floral_fish_small/colorful.png")
		));

		registerable.register(FLORAL_FISH_SMALL_FLOWY, new EntityPatternVariant(
			AbysmEntityTypes.SMALL_FLORAL_FISH, "Flowy", Abysm.id("textures/entity/pattern/floral_fish_small/flowy.png")
		));

		registerable.register(FLORAL_FISH_SMALL_SHRIMPSTER, new EntityPatternVariant(
			AbysmEntityTypes.SMALL_FLORAL_FISH, "Shrimpster", Abysm.id("textures/entity/pattern/floral_fish_small/shrimpster.png")
		));

		registerable.register(FLORAL_FISH_SMALL_SPEEDY, new EntityPatternVariant(
			AbysmEntityTypes.SMALL_FLORAL_FISH, "Speedy", Abysm.id("textures/entity/pattern/floral_fish_small/speedy.png")
		));

		registerable.register(FLORAL_FISH_SMALL_WISE, new EntityPatternVariant(
			AbysmEntityTypes.SMALL_FLORAL_FISH, "Wise", Abysm.id("textures/entity/pattern/floral_fish_small/wise.png")
		));
	}

	public static RegistryKey<EntityPatternVariant> of(String path) {
		return RegistryKey.of(EntityPatternVariantRegistry.ENTITY_PATTERN_VARIANT_KEY, Abysm.id(path));
	}

}
