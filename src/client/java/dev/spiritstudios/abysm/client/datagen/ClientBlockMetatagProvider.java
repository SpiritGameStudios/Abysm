package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.specter.api.registry.metatag.datagen.MetatagProvider;
import dev.spiritstudios.specter.api.render.client.BlockRenderLayer;
import dev.spiritstudios.specter.api.render.client.RenderMetatags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ClientBlockMetatagProvider extends MetatagProvider<Block> {
	protected ClientBlockMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(dataOutput, RegistryKeys.BLOCK, registriesFuture, DataOutput.OutputType.RESOURCE_PACK);
	}

	@Override
	protected void configure(Consumer<MetatagBuilder<Block, ?>> consumer, RegistryWrapper.WrapperLookup wrapperLookup) {
		MetatagBuilder<Block, BlockRenderLayer> renderLayer = create(RenderMetatags.RENDER_LAYER);

		renderLayer.put(AbysmBlocks.PURPLE_SCABIOSA, BlockRenderLayer.CUTOUT);

		consumer.accept(renderLayer);
	}
}
