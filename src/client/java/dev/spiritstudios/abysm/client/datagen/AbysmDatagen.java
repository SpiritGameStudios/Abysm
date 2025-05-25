package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.datagen.AbysmItemGroupGenerator;
import dev.spiritstudios.abysm.datagen.AbysmLootTableProvider;
import dev.spiritstudios.abysm.datagen.AbysmTagProviders;
import dev.spiritstudios.abysm.datagen.AutomaticDynamicRegistryProvider;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import dev.spiritstudios.abysm.worldgen.feature.AbysmConfiguredFeatures;
import dev.spiritstudios.abysm.worldgen.feature.AbysmPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class AbysmDatagen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		FabricDataGenerator.Pack pack = dataGenerator.createPack();

		pack.addProvider(AbysmModelProvider::new);
		pack.addProvider(ClientBlockMetatagProvider::new);

		// note: disabled because the generated file doesn't seem to actually work
		pack.addProvider(AbysmItemGroupGenerator::new);

		pack.addProvider(AutomaticDynamicRegistryProvider.factory(RegistryKeys.BIOME, Abysm.MODID));
		pack.addProvider(AutomaticDynamicRegistryProvider.factory(RegistryKeys.CONFIGURED_FEATURE, Abysm.MODID));
		pack.addProvider(AutomaticDynamicRegistryProvider.factory(RegistryKeys.PLACED_FEATURE, Abysm.MODID));

		pack.addProvider(AbysmLootTableProvider::new);

		AbysmTagProviders.addAll(pack);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder
			.addRegistry(RegistryKeys.BIOME, AbysmBiomes::bootstrap)
			.addRegistry(RegistryKeys.CONFIGURED_FEATURE, AbysmConfiguredFeatures::bootstrap)
			.addRegistry(RegistryKeys.PLACED_FEATURE, AbysmPlacedFeatures::bootstrap);
	}
}
