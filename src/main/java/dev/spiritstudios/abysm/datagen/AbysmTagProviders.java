package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.world.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class AbysmTagProviders {
	public static void addAll(FabricDataGenerator.Pack pack) {
		pack.addProvider(AbysmTagProviders.BlockTagProvider::new);
		pack.addProvider(AbysmTagProviders.BiomeTagProvider::new);
	}

	private static class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
		public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
				.add(AbysmBlocks.FLOROPUMICE);
		}
	}

	private static class BiomeTagProvider extends FabricTagProvider<Biome> {
		public BiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, RegistryKeys.BIOME, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			getOrCreateTagBuilder(BiomeTags.IS_OVERWORLD)
				.add(AbysmBiomes.FLORAL_REEF);
		}
	}
}
