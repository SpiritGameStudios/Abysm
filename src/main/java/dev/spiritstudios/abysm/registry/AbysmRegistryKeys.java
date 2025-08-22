package dev.spiritstudios.abysm.registry;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import dev.spiritstudios.abysm.data.variant.ElectricOoglyBooglyVariant;
import dev.spiritstudios.abysm.data.variant.GupGupEntityVariant;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensityBlob;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class AbysmRegistryKeys {
	public static final RegistryKey<Registry<EcosystemType<?>>> ECOSYSTEM_TYPE = ofKey("ecosystem_type");
	public static final RegistryKey<Registry<MapCodec<? extends DensityBlob>>> DENSITY_BLOB_TYPE = ofKey("density_blob_type");

	public static final RegistryKey<Registry<EntityPatternVariant>> ENTITY_PATTERN = ofKey("entity_pattern");
	public static final RegistryKey<Registry<BloomrayEntityVariant>> BLOOMRAY_ENTITY_VARIANT = ofKey("bloomray_variant");
	public static final RegistryKey<Registry<ElectricOoglyBooglyVariant>> ELECTRIC_OOGLY_BOOGLY_VARIANT = ofKey("electric_oogly_boogly_variant");
	public static final RegistryKey<Registry<GupGupEntityVariant>> GUP_GUP_ENTITY_VARIANT = ofKey("gup_gup_variant");
	public static final RegistryKey<Registry<FishEnchantment>> FISH_ENCHANTMENT = ofKey("fish_enchantment");

	private static <T> RegistryKey<Registry<T>> ofKey(String path) {
		return RegistryKey.ofRegistry(Abysm.id(path));
	}
}
