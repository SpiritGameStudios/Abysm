package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.registry.AbysmBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class TagProviders {
	public static void addAll(FabricDataGenerator.Pack pack) {
		pack.addProvider(TagProviders.BlockTagProvider::new);
	}

	private static class  BlockTagProvider extends FabricTagProvider.BlockTagProvider {

		public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
				.add(AbysmBlocks.FLOROPUMICE);
		}
	}
}
