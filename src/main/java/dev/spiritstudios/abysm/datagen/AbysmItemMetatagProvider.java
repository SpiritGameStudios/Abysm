package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.specter.api.item.ItemMetatags;
import dev.spiritstudios.specter.api.registry.metatag.datagen.MetatagProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AbysmItemMetatagProvider extends MetatagProvider<Item> {
	public AbysmItemMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(dataOutput, RegistryKeys.ITEM, registriesFuture, DataOutput.OutputType.DATA_PACK);
	}

	@Override
	protected void configure(Consumer<MetatagBuilder<Item, ?>> consumer, RegistryWrapper.WrapperLookup wrapperLookup) {
		MetatagBuilder<Item, Float> builder = create(ItemMetatags.COMPOSTING_CHANCE);

		putBlocks(builder, 0.65F,
			AbysmBlocks.ROSY_SPRIGS,
			AbysmBlocks.ROSY_BLOOMSHROOM,

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
			AbysmBlocks.BLACK_SCABIOSA
		);

		putBlocks(builder, 0.85F,
			AbysmBlocks.ROSY_BLOOMSHROOM_STEM,
			AbysmBlocks.ROSY_BLOOMSHROOM_HYPHAE,
			AbysmBlocks.ROSY_BLOOMSHROOM_CAP
		);

		consumer.accept(builder);
	}

	private void putBlocks(MetatagBuilder<Item, Float> builder, float value, Block... blocks) {
		for(Block block : blocks) {
			putBlock(builder, block, value);
		}
	}

	private void putBlock(MetatagBuilder<Item, Float> builder, Block block, float value) {
		builder.put(block.asItem(), value);
	}
}
