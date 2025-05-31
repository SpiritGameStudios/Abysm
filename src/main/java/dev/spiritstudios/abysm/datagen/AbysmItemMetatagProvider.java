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
			AbysmBlocks.SUNNY_SPRIGS,
			AbysmBlocks.MAUVE_SPRIGS,

			AbysmBlocks.ROSY_BLOOMSHROOM,
			AbysmBlocks.SUNNY_BLOOMSHROOM,
			AbysmBlocks.MAUVE_BLOOMSHROOM,

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

			AbysmBlocks.ANTENNAE_PLANT
		);

		putBlocks(builder, 0.85F,
			AbysmBlocks.ROSY_BLOOMSHROOM_STEM,
			AbysmBlocks.ROSY_BLOOMSHROOM_HYPHAE,
			AbysmBlocks.ROSY_BLOOMSHROOM_CAP,

			AbysmBlocks.SUNNY_BLOOMSHROOM_STEM,
			AbysmBlocks.SUNNY_BLOOMSHROOM_HYPHAE,
			AbysmBlocks.SUNNY_BLOOMSHROOM_CAP,

			AbysmBlocks.MAUVE_BLOOMSHROOM_STEM,
			AbysmBlocks.MAUVE_BLOOMSHROOM_HYPHAE,
			AbysmBlocks.MAUVE_BLOOMSHROOM_CAP
		);

		putBlocks(builder, 1.0F,
			AbysmBlocks.BLOOMING_SODALITE_CROWN,
			AbysmBlocks.BLOOMING_ANYOLITE_CROWN,
			AbysmBlocks.BLOOMING_MELILITE_CROWN
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
