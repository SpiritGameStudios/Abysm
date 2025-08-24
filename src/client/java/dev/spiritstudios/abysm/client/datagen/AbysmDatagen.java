package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.datagen.AbysmBlockLootTableProvider;
import dev.spiritstudios.abysm.datagen.AbysmBlockMetatagProvider;
import dev.spiritstudios.abysm.datagen.AbysmEntityLootTableProvider;
import dev.spiritstudios.abysm.datagen.AbysmFishingLootTableProvider;
import dev.spiritstudios.abysm.datagen.AbysmItemMetatagProvider;
import dev.spiritstudios.abysm.datagen.AbysmRecipeProvider;
import dev.spiritstudios.abysm.datagen.AbysmTagProviders;
import dev.spiritstudios.abysm.entity.AbysmDamageTypes;
import dev.spiritstudios.abysm.entity.pattern.AbysmEntityPatternVariants;
import dev.spiritstudios.abysm.entity.ruins.AbysmFishEnchantments;
import dev.spiritstudios.abysm.entity.variant.AbysmEntityVariants;
import dev.spiritstudios.abysm.item.AbysmItemGroups;
import dev.spiritstudios.abysm.registry.AbysmEnchantments;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import dev.spiritstudios.abysm.worldgen.densityfunction.AbysmDensityFunctions;
import dev.spiritstudios.abysm.worldgen.feature.AbysmConfiguredFeatures;
import dev.spiritstudios.abysm.worldgen.feature.AbysmPlacedFeatures;
import dev.spiritstudios.abysm.worldgen.noise.AbysmNoiseParameters;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructureSets;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructures;
import dev.spiritstudios.abysm.worldgen.structure.pool.AbysmStructurePools;
import dev.spiritstudios.abysm.worldgen.structure.processor.AbysmStructureProcessorLists;
import dev.spiritstudios.specter.api.item.SpecterItemRegistryKeys;
import dev.spiritstudios.specter.api.registry.AutomaticDynamicRegistryProvider;
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
		// endregion

		// region data
		// worldgen registries
		addProvider(pack, RegistryKeys.NOISE_PARAMETERS);
		addProvider(pack, RegistryKeys.DENSITY_FUNCTION);
		addProvider(pack, RegistryKeys.BIOME);
		addProvider(pack, RegistryKeys.CONFIGURED_FEATURE);
		addProvider(pack, RegistryKeys.PLACED_FEATURE);
		addProvider(pack, RegistryKeys.STRUCTURE_SET);
		addProvider(pack, RegistryKeys.PROCESSOR_LIST);
		addProvider(pack, RegistryKeys.STRUCTURE);
		addProvider(pack, RegistryKeys.TEMPLATE_POOL);

		// misc registries
		addProvider(pack, RegistryKeys.DAMAGE_TYPE);
		addProvider(pack, RegistryKeys.ENCHANTMENT);

		// abysm registries
		addProvider(pack, AbysmRegistryKeys.ENTITY_PATTERN);

		addProvider(pack, AbysmRegistryKeys.BLOOMRAY_ENTITY_VARIANT);
		addProvider(pack, AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT);
		addProvider(pack, AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT);
		addProvider(pack, AbysmRegistryKeys.SNAPPER_ENTITY_VARIANT);

		addProvider(pack, AbysmRegistryKeys.FISH_ENCHANTMENT);

		// loot tables
		pack.addProvider(AbysmBlockLootTableProvider::new);
		pack.addProvider(AbysmFishingLootTableProvider::new);
		pack.addProvider(AbysmEntityLootTableProvider::new);

		// misc
		pack.addProvider(AbysmRecipeProvider::new);
		AbysmTagProviders.addAll(pack);

		pack.addProvider(AbysmBlockMetatagProvider::new);
		pack.addProvider(AbysmItemMetatagProvider::new);
		addProvider(pack, SpecterItemRegistryKeys.ITEM_GROUP);
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
			.addRegistry(RegistryKeys.PROCESSOR_LIST, AbysmStructureProcessorLists::bootstrap)
			.addRegistry(RegistryKeys.STRUCTURE, AbysmStructures::bootstrap)
			.addRegistry(RegistryKeys.TEMPLATE_POOL, AbysmStructurePools::bootstrap)

			// misc
			.addRegistry(RegistryKeys.DAMAGE_TYPE, AbysmDamageTypes::bootstrap)
			.addRegistry(RegistryKeys.ENCHANTMENT, AbysmEnchantments::bootstrap)
			.addRegistry(SpecterItemRegistryKeys.ITEM_GROUP, AbysmItemGroups::bootstrap)

			// abysm
			.addRegistry(AbysmRegistryKeys.ENTITY_PATTERN, AbysmEntityPatternVariants::bootstrap)

			.addRegistry(AbysmRegistryKeys.BLOOMRAY_ENTITY_VARIANT, AbysmEntityVariants::bloomrayBootstrap)
			.addRegistry(AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT, AbysmEntityVariants::ooglyBooglyBootstrap)
			.addRegistry(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT, AbysmEntityVariants::gupGupBootstrap)
			.addRegistry(AbysmRegistryKeys.SNAPPER_ENTITY_VARIANT, AbysmEntityVariants::snapperBootstrap)

			.addRegistry(AbysmRegistryKeys.FISH_ENCHANTMENT, AbysmFishEnchantments::bootstrap);
	}
}
