package dev.spiritstudios.abysm.datagen;

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

import static dev.spiritstudios.abysm.block.AbysmBlocks.*;

public class AbysmItemMetatagProvider extends MetatagProvider<Item> {
	public AbysmItemMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(dataOutput, RegistryKeys.ITEM, registriesFuture, DataOutput.OutputType.DATA_PACK);
	}

	private void putBlocks(MetatagProvider<Item>.MetatagBuilder<Float> builder, float value, Block... blocks) {
		for (Block block : blocks) {
			putBlock(builder, block, value);
		}
	}

	private void putBlock(MetatagProvider<Item>.MetatagBuilder<Float> builder, Block block, float value) {
		builder.put(block.asItem(), value);
	}

	@Override
	protected void configure(Consumer<MetatagProvider<Item>.MetatagBuilder<?>> consumer, RegistryWrapper.WrapperLookup wrapperLookup) {
		MetatagProvider<Item>.MetatagBuilder<Float> compostingChance = create(ItemMetatags.COMPOSTING_CHANCE);

		putBlocks(compostingChance, 0.3F,
			ROSEBLOOM_PETALS,
			SUNBLOOM_PETALS,
			MALLOWBLOOM_PETALS,

			OOZETRICKLE_FILAMENTS,
			BRINE_BRACKEN
		);

		putBlocks(compostingChance, 0.5F,
			TALL_OOZETRICKLE_FILAMENTS
		);

		putBlocks(compostingChance, 0.65F,
			ROSY_SPRIGS,
			SUNNY_SPRIGS,
			MAUVE_SPRIGS,

			ROSY_BLOOMSHROOM,
			SUNNY_BLOOMSHROOM,
			MAUVE_BLOOMSHROOM,

			ROSEBLOOM_PETALEAVES,
			SUNBLOOM_PETALEAVES,
			MALLOWBLOOM_PETALEAVES,

			WHITE_SCABIOSA,
			ORANGE_SCABIOSA,
			MAGENTA_SCABIOSA,
			LIGHT_BLUE_SCABIOSA,
			YELLOW_SCABIOSA,
			LIME_SCABIOSA,
			PINK_SCABIOSA,
			GREY_SCABIOSA,
			LIGHT_GREY_SCABIOSA,
			CYAN_SCABIOSA,
			PURPLE_SCABIOSA,
			BLUE_SCABIOSA,
			BROWN_SCABIOSA,
			GREEN_SCABIOSA,
			RED_SCABIOSA,
			BLACK_SCABIOSA,

			ANTENNAE_PLANT,
			MONARE_VASE,
			BOOMSHROOM
		);

		putBlocks(compostingChance, 0.85F,
			ROSY_BLOOMSHROOM_STEM,
			ROSY_BLOOMSHROOM_HYPHAE,
			ROSY_BLOOMSHROOM_CAP,

			SUNNY_BLOOMSHROOM_STEM,
			SUNNY_BLOOMSHROOM_HYPHAE,
			SUNNY_BLOOMSHROOM_CAP,

			MAUVE_BLOOMSHROOM_STEM,
			MAUVE_BLOOMSHROOM_HYPHAE,
			MAUVE_BLOOMSHROOM_CAP,

			MONARE_VASE_PETAL,
			MONARE_VASE_BULB,
			MONARE_VASE_BLOCK
		);

		putBlocks(compostingChance, 1.0F,
			BLOOMING_SODALITE_CROWN,
			BLOOMING_ANYOLITE_CROWN,
			BLOOMING_MELILITE_CROWN
		);

		consumer.accept(compostingChance);
	}
}
