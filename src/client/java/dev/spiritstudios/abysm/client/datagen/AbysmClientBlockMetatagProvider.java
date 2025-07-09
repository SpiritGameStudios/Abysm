package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.block.AbysmBlocks;
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

public class AbysmClientBlockMetatagProvider extends MetatagProvider<Block> {
	protected AbysmClientBlockMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(dataOutput, RegistryKeys.BLOCK, registriesFuture, DataOutput.OutputType.RESOURCE_PACK);
	}

	@Override
	protected void configure(Consumer<MetatagBuilder<Block, ?>> consumer, RegistryWrapper.WrapperLookup wrapperLookup) {
		MetatagBuilder<Block, BlockRenderLayer> builder = create(RenderMetatags.RENDER_LAYER);

		addBlocksToLayer(builder, BlockRenderLayer.CUTOUT,
			AbysmBlocks.ROSY_SPRIGS,
			AbysmBlocks.POTTED_ROSY_SPRIGS,
			AbysmBlocks.SUNNY_SPRIGS,
			AbysmBlocks.POTTED_SUNNY_SPRIGS,
			AbysmBlocks.MAUVE_SPRIGS,
			AbysmBlocks.POTTED_MAUVE_SPRIGS,

			AbysmBlocks.ROSEBLOOM_PETALS,
			AbysmBlocks.SUNBLOOM_PETALS,
			AbysmBlocks.MALLOWBLOOM_PETALS,

			AbysmBlocks.ROSY_BLOOMSHROOM,
			AbysmBlocks.POTTED_ROSY_BLOOMSHROOM,
			AbysmBlocks.SUNNY_BLOOMSHROOM,
			AbysmBlocks.POTTED_SUNNY_BLOOMSHROOM,
			AbysmBlocks.MAUVE_BLOOMSHROOM,
			AbysmBlocks.POTTED_MAUVE_BLOOMSHROOM,

			AbysmBlocks.BLOOMING_SODALITE_CROWN,
			AbysmBlocks.BLOOMING_ANYOLITE_CROWN,
			AbysmBlocks.BLOOMING_MELILITE_CROWN,

			AbysmBlocks.WHITE_SCABIOSA,
			AbysmBlocks.ORANGE_SCABIOSA,
			AbysmBlocks.MAGENTA_SCABIOSA,
			AbysmBlocks.LIGHT_BLUE_SCABIOSA,
			AbysmBlocks.YELLOW_SCABIOSA,
			AbysmBlocks.LIME_SCABIOSA,
			AbysmBlocks.PINK_SCABIOSA,
			AbysmBlocks.GREY_SCABIOSA,
			AbysmBlocks.LIGHT_GREY_SCABIOSA,
			AbysmBlocks.CYAN_SCABIOSA,
			AbysmBlocks.PURPLE_SCABIOSA,
			AbysmBlocks.BLUE_SCABIOSA,
			AbysmBlocks.BROWN_SCABIOSA,
			AbysmBlocks.GREEN_SCABIOSA,
			AbysmBlocks.RED_SCABIOSA,
			AbysmBlocks.BLACK_SCABIOSA,

			AbysmBlocks.ANTENNAE_PLANT,
			AbysmBlocks.POTTED_ANTENNAE_PLANT,

			AbysmBlocks.GOLDEN_LAZULI_OREFURL,
			AbysmBlocks.GOLDEN_LAZULI_OREFURL_PLANT
		);

		addBlocksToLayer(builder, BlockRenderLayer.CUTOUT_MIPPED,
			AbysmBlocks.ROSEBLOOM_PETALEAVES,
			AbysmBlocks.SUNBLOOM_PETALEAVES,
			AbysmBlocks.MALLOWBLOOM_PETALEAVES
		);

		addBlocksToLayer(builder, BlockRenderLayer.TRANSLUCENT,
			AbysmBlocks.BLOOMSHROOM_GOOP
		);

		consumer.accept(builder);
	}

	private void addBlocksToLayer(MetatagBuilder<Block, BlockRenderLayer> builder, BlockRenderLayer renderLayer, Block... blocks) {
		for(Block block : blocks) {
			builder.put(block, renderLayer);
		}
	}
}
