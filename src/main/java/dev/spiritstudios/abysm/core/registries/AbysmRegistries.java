package dev.spiritstudios.abysm.core.registries;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import dev.spiritstudios.abysm.data.variant.ElectricOoglyBooglyVariant;
import dev.spiritstudios.abysm.data.variant.GupGupVariant;
import dev.spiritstudios.abysm.data.variant.SnapperEntityVariant;
import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.DensityBlob;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;

public final class AbysmRegistries {
	public static final Registry<MapCodec<? extends DensityBlob>> DENSITY_BLOB_TYPE = FabricRegistryBuilder.createSimple(AbysmRegistryKeys.DENSITY_BLOB_TYPE).buildAndRegister();

	public static void init() {
		DynamicRegistries.registerSynced(AbysmRegistryKeys.ENTITY_PATTERN, EntityPatternVariant.CODEC);
		DynamicRegistries.registerSynced(AbysmRegistryKeys.BLOOMRAY_ENTITY_VARIANT, BloomrayEntityVariant.CODEC);
		DynamicRegistries.registerSynced(AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT, ElectricOoglyBooglyVariant.CODEC);
		DynamicRegistries.registerSynced(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT, GupGupVariant.CODEC);
		DynamicRegistries.registerSynced(AbysmRegistryKeys.SNAPPER_ENTITY_VARIANT, SnapperEntityVariant.CODEC);

		DynamicRegistries.registerSynced(AbysmRegistryKeys.FISH_ENCHANTMENT, FishEnchantment.CODEC);
	}
}
