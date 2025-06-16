package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.datagen.AbysmItemGroupProvider;
import dev.spiritstudios.abysm.datagen.AbysmItemMetatagProvider;
import dev.spiritstudios.abysm.datagen.AbysmLootTableProvider;
import dev.spiritstudios.abysm.datagen.AbysmRecipeProvider;
import dev.spiritstudios.abysm.datagen.AbysmTagProviders;
import dev.spiritstudios.abysm.datagen.AutomaticDynamicRegistryProvider;
import dev.spiritstudios.abysm.registry.AbysmEntityPatternVariants;
import dev.spiritstudios.abysm.registry.AbysmEntityVariants;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
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
		pack.addProvider(AbysmSoundsProvider::new);
		pack.addProvider(AbysmClientBlockMetatagProvider::new);

		pack.addProvider(AbysmItemGroupProvider::new);

		pack.addProvider(AutomaticDynamicRegistryProvider.factory(RegistryKeys.BIOME, Abysm.MODID));
		pack.addProvider(AutomaticDynamicRegistryProvider.factory(RegistryKeys.CONFIGURED_FEATURE, Abysm.MODID));
		pack.addProvider(AutomaticDynamicRegistryProvider.factory(RegistryKeys.PLACED_FEATURE, Abysm.MODID));

		pack.addProvider(AutomaticDynamicRegistryProvider.factory(AbysmRegistries.ENTITY_PATTERN, Abysm.MODID));
		pack.addProvider(AutomaticDynamicRegistryProvider.factory(AbysmRegistries.BLOOMRAY_ENTITY_VARIANT, Abysm.MODID));
		pack.addProvider(AutomaticDynamicRegistryProvider.factory(AbysmRegistries.ELECTRIC_OOGLY_BOOGLY_VARIANT, Abysm.MODID));

		pack.addProvider(AbysmLootTableProvider::new);
		pack.addProvider(AbysmRecipeProvider::new);

		pack.addProvider(AbysmItemMetatagProvider::new);

		AbysmTagProviders.addAll(pack);
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
			.addRegistry(RegistryKeys.BIOME, AbysmBiomes::bootstrap)
			.addRegistry(RegistryKeys.CONFIGURED_FEATURE, AbysmConfiguredFeatures::bootstrap)
			.addRegistry(RegistryKeys.PLACED_FEATURE, AbysmPlacedFeatures::bootstrap)
			.addRegistry(AbysmRegistries.ENTITY_PATTERN, AbysmEntityPatternVariants::bootstrap)
			.addRegistry(AbysmRegistries.BLOOMRAY_ENTITY_VARIANT, AbysmEntityVariants::bloomrayBootstrap)
			.addRegistry(AbysmRegistries.ELECTRIC_OOGLY_BOOGLY_VARIANT, AbysmEntityVariants::ooglyBooglyBootstrap);
	}
}
