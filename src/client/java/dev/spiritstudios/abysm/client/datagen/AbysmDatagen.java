package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.datagen.AbysmEntityPatternVariantProvider;
import dev.spiritstudios.abysm.datagen.AbysmItemGroupProvider;
import dev.spiritstudios.abysm.datagen.AbysmItemMetatagProvider;
import dev.spiritstudios.abysm.datagen.AbysmLootTableProvider;
import dev.spiritstudios.abysm.datagen.AbysmRecipeProvider;
import dev.spiritstudios.abysm.datagen.AbysmTagProviders;
import dev.spiritstudios.abysm.datagen.AutomaticDynamicRegistryProvider;
import dev.spiritstudios.abysm.registry.AbysmEntityPatternVariants;
import dev.spiritstudios.abysm.registry.EntityPatternVariantRegistry;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import dev.spiritstudios.abysm.worldgen.feature.AbysmConfiguredFeatures;
import dev.spiritstudios.abysm.worldgen.feature.AbysmPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.JsonKeySortOrderCallback;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class AbysmDatagen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		FabricDataGenerator.Pack pack = dataGenerator.createPack();

		pack.addProvider(AbysmModelProvider::new);
		pack.addProvider(ClientBlockMetatagProvider::new);

		pack.addProvider(AbysmItemGroupProvider::new);

		pack.addProvider(AbysmEntityPatternVariantProvider::new);

		pack.addProvider(AutomaticDynamicRegistryProvider.factory(RegistryKeys.BIOME, Abysm.MODID));
		pack.addProvider(AutomaticDynamicRegistryProvider.factory(RegistryKeys.CONFIGURED_FEATURE, Abysm.MODID));
		pack.addProvider(AutomaticDynamicRegistryProvider.factory(RegistryKeys.PLACED_FEATURE, Abysm.MODID));

		pack.addProvider(AbysmLootTableProvider::new);
		pack.addProvider(AbysmRecipeProvider::new);

		pack.addProvider(AbysmItemMetatagProvider::new);

		AbysmTagProviders.addAll(pack);
	}

	@Override
	public void addJsonKeySortOrders(JsonKeySortOrderCallback callback) {
		callback.add("entity_type", 2);
		callback.add("name", 3);
		callback.add("pattern", 4);
		callback.add("base", 5);
		callback.add("colorable", 6);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder
			.addRegistry(RegistryKeys.BIOME, AbysmBiomes::bootstrap)
			.addRegistry(RegistryKeys.CONFIGURED_FEATURE, AbysmConfiguredFeatures::bootstrap)
			.addRegistry(RegistryKeys.PLACED_FEATURE, AbysmPlacedFeatures::bootstrap)
			.addRegistry(EntityPatternVariantRegistry.ENTITY_PATTERN_VARIANT_KEY, AbysmEntityPatternVariants::bootstrap);
	}
}
