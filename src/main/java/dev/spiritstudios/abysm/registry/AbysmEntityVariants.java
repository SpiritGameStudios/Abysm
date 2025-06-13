package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.variant.AbstractEntityVariant;
import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbysmEntityVariants {

	public static final RegistryKey<BloomrayEntityVariant> ROSY_BLOOMRAY = ofBloomray("rosy");
	public static final RegistryKey<BloomrayEntityVariant> SUNNY_BLOOMRAY = ofBloomray("sunny");
	public static final RegistryKey<BloomrayEntityVariant> MAUVE_BLOOMRAY = ofBloomray("mauve");

	public static void bloomrayBootstrap(Registerable<BloomrayEntityVariant> registerable) {
		register(registerable, ROSY_BLOOMRAY, BloomrayEntityVariant.DEFAULT);
		registerBloomrayVariant(registerable, SUNNY_BLOOMRAY, BloomrayEntityVariant.HideableCrownType.ANYOLITE_CROWN);
		registerBloomrayVariant(registerable, MAUVE_BLOOMRAY, BloomrayEntityVariant.HideableCrownType.MELILITE_CROWN);
	}

	private static RegistryKey<BloomrayEntityVariant> ofBloomray(String path) {
		return of(AbysmRegistries.BLOOMRAY_ENTITY_VARIANT, path);
	}

	private static <T extends AbstractEntityVariant> RegistryKey<T> of(RegistryKey<Registry<T>> key, String path) {
		return RegistryKey.of(key, Abysm.id(path));
	}

	private static void registerBloomrayVariant(Registerable<BloomrayEntityVariant> registry, RegistryKey<BloomrayEntityVariant> key, BloomrayEntityVariant.HideableCrownType crownType) {
		Identifier variantPath = Abysm.id("textures/entity/" + key.getValue().getPath() + "_bloomray.png");
		Text name = Text.translatable("abysm.entity.bloomray." + key.getValue().getPath());
		BloomrayEntityVariant variant = new BloomrayEntityVariant(name, variantPath, crownType);
		register(registry, key, variant);
	}

	private static <T extends AbstractEntityVariant> void register(Registerable<T> registerable, RegistryKey<T> key, T variant) {
		registerable.register(key, variant);
	}

}
