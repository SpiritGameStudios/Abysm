package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.spectre.api.core.registry.metatag.MetatagBuilder;
import dev.spiritstudios.spectre.api.core.registry.metatag.MetatagsProvider;
import dev.spiritstudios.spectre.api.core.registry.metatag.SpectreMetatags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.ANTENNAE_PLANT;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.BLACK_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.BLOOMING_ANYOLITE_CROWN;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.BLOOMING_MELILITE_CROWN;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.BLOOMING_SODALITE_CROWN;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.BLUE_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.BROWN_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.CYAN_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.GREEN_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.GREY_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.LIGHT_BLUE_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.LIGHT_GREY_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.LIME_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.MAGENTA_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.MALLOWBLOOM_PETALEAVES;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.MALLOWBLOOM_PETALS;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.MAUVE_BLOOMSHROOM;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.MAUVE_BLOOMSHROOM_CAP;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.MAUVE_BLOOMSHROOM_HYPHAE;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.MAUVE_BLOOMSHROOM_STEM;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.MAUVE_SPRIGS;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.OOZETRICKLE_FILAMENTS;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.ORANGE_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.PINK_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.PURPLE_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.RED_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.ROSEBLOOM_PETALEAVES;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.ROSEBLOOM_PETALS;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.ROSY_BLOOMSHROOM;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.ROSY_BLOOMSHROOM_CAP;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.ROSY_BLOOMSHROOM_HYPHAE;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.ROSY_BLOOMSHROOM_STEM;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.ROSY_SPRIGS;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.SUNBLOOM_PETALEAVES;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.SUNBLOOM_PETALS;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.SUNNY_BLOOMSHROOM;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.SUNNY_BLOOMSHROOM_CAP;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.SUNNY_BLOOMSHROOM_HYPHAE;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.SUNNY_BLOOMSHROOM_STEM;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.SUNNY_SPRIGS;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.TALL_OOZETRICKLE_FILAMENTS;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.WHITE_SCABIOSA;
import static dev.spiritstudios.abysm.world.level.block.AbysmBlocks.YELLOW_SCABIOSA;

public class AbysmItemMetatagProvider extends MetatagsProvider.ItemMetatagProvider {
	public AbysmItemMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(
			dataOutput,
			lookupProvider
		);
	}

	private void putBlocks(MetatagBuilder<Item, Float> builder, float value, Block... blocks) {
		for (Block block : blocks) {
			putBlock(builder, block, value);
		}
	}

	private void putBlock(MetatagBuilder<Item, Float> builder, Block block, float value) {
		builder.put(block.asItem(), value);
	}

	@Override
	protected void addMetatags(HolderLookup.Provider provider) {
		var compostingChance = builder(SpectreMetatags.COMPOSTING_CHANCE);
		putBlocks(
			compostingChance,
			0.3F,
			ROSEBLOOM_PETALS,
			SUNBLOOM_PETALS,
			MALLOWBLOOM_PETALS,

			OOZETRICKLE_FILAMENTS
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

			ANTENNAE_PLANT
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
			MAUVE_BLOOMSHROOM_CAP
		);

		putBlocks(compostingChance, 1.0F,
			BLOOMING_SODALITE_CROWN,
			BLOOMING_ANYOLITE_CROWN,
			BLOOMING_MELILITE_CROWN
		);
	}

	@Override
	public @NotNull String getName() {
		return "Item Metatags";
	}
}
