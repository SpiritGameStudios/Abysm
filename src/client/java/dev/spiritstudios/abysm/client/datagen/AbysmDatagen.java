package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.datagen.*;
import dev.spiritstudios.abysm.entity.AbysmDamageTypes;
import dev.spiritstudios.abysm.entity.pattern.AbysmEntityPatternVariants;
import dev.spiritstudios.abysm.entity.ruins.AbysmFishEnchantments;
import dev.spiritstudios.abysm.entity.variant.AbysmEntityVariants;
import dev.spiritstudios.abysm.registry.AbysmEnchantments;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import dev.spiritstudios.abysm.worldgen.densityfunction.AbysmDensityFunctions;
import dev.spiritstudios.abysm.worldgen.feature.AbysmConfiguredFeatures;
import dev.spiritstudios.abysm.worldgen.feature.AbysmPlacedFeatures;
import dev.spiritstudios.abysm.worldgen.noise.AbysmNoiseParameters;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructureSets;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.JsonKeySortOrderCallback;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class AbysmDatagen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		FabricDataGenerator.Pack pack = dataGenerator.createPack();

		// region assets
		pack.addProvider(AbysmModelProvider::new);
		pack.addProvider(AbysmSoundsProvider::new);
		pack.addProvider(AbysmClientBlockMetatagProvider::new);
		// endregion

		// region data
		// worldgen registries
		addProvider(pack, RegistryKeys.NOISE_PARAMETERS);
		addProvider(pack, RegistryKeys.DENSITY_FUNCTION);
		addProvider(pack, RegistryKeys.BIOME);
		addProvider(pack, RegistryKeys.CONFIGURED_FEATURE);
		addProvider(pack, RegistryKeys.PLACED_FEATURE);
		addProvider(pack, RegistryKeys.STRUCTURE_SET);
		addProvider(pack, RegistryKeys.STRUCTURE);

		// misc registries
		addProvider(pack, RegistryKeys.DAMAGE_TYPE);
		addProvider(pack, RegistryKeys.ENCHANTMENT);

		// abysm registries
		addProvider(pack, AbysmRegistries.ENTITY_PATTERN);

		addProvider(pack, AbysmRegistries.BLOOMRAY_ENTITY_VARIANT);
		addProvider(pack, AbysmRegistries.ELECTRIC_OOGLY_BOOGLY_VARIANT);

		addProvider(pack, AbysmRegistries.FISH_ENCHANTMENT);

		// loot tables
		pack.addProvider(AbysmBlockLootTableProvider::new);
		pack.addProvider(AbysmFishingLootTableProvider::new);

		// misc
		pack.addProvider(AbysmRecipeProvider::new);
		AbysmTagProviders.addAll(pack);

		pack.addProvider(AbysmItemGroupProvider::new);
		pack.addProvider(AbysmItemMetatagProvider::new);
		// endregion
	}

	private <T> AutomaticDynamicRegistryProvider<T> addProvider(FabricDataGenerator.Pack pack, RegistryKey<Registry<T>> registryKey) {
		return pack.addProvider(AutomaticDynamicRegistryProvider.factory(registryKey, Abysm.MODID));
	}

	// I'd(Kat) love to be able to specify the json order of individual data-gen'd things,
	// e.g. entity pattern variants & entity (texture) variants separately,
	// but this applies to all data-gen'd things from this class so I have to be careful :(
	@Override
	public void addJsonKeySortOrders(JsonKeySortOrderCallback callback) {
		// Entity Pattern Variants
		callback.add("entity_type", 2);
		callback.add("name", 3);
		callback.add("pattern", 4);
		callback.add("base", 5);
		callback.add("colorable", 6);

		// Abstract Entity (Texture) Variants
		callback.add("texture", 4); // name should be first, so this comes after name

		// Bloomray Entity (Texture) Variants
		callback.add("crown", 5);

		// Electric Oogly Boogly Entity (Texture) Variants
		callback.add("electricity_color", 5);
		callback.add("deadly", 6);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder
			// worldgen
			.addRegistry(RegistryKeys.NOISE_PARAMETERS, AbysmNoiseParameters::bootstrap)
			.addRegistry(RegistryKeys.DENSITY_FUNCTION, AbysmDensityFunctions::bootstrap)
			.addRegistry(RegistryKeys.BIOME, AbysmBiomes::bootstrap)
			.addRegistry(RegistryKeys.CONFIGURED_FEATURE, AbysmConfiguredFeatures::bootstrap)
			.addRegistry(RegistryKeys.PLACED_FEATURE, AbysmPlacedFeatures::bootstrap)
			.addRegistry(RegistryKeys.STRUCTURE_SET, AbysmStructureSets::bootstrap)
			.addRegistry(RegistryKeys.STRUCTURE, AbysmStructures::bootstrap)

			// misc
			.addRegistry(RegistryKeys.DAMAGE_TYPE, AbysmDamageTypes::bootstrap)
			.addRegistry(RegistryKeys.ENCHANTMENT, AbysmEnchantments::bootstrap)

			// abysm
			.addRegistry(AbysmRegistries.ENTITY_PATTERN, AbysmEntityPatternVariants::bootstrap)

			.addRegistry(AbysmRegistries.BLOOMRAY_ENTITY_VARIANT, AbysmEntityVariants::bloomrayBootstrap)
			.addRegistry(AbysmRegistries.ELECTRIC_OOGLY_BOOGLY_VARIANT, AbysmEntityVariants::ooglyBooglyBootstrap)

			.addRegistry(AbysmRegistries.FISH_ENCHANTMENT, AbysmFishEnchantments::bootstrap);
	}
}
