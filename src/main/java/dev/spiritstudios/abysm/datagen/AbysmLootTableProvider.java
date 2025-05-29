package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.block.AbysmBlockFamilies;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class AbysmLootTableProvider extends FabricBlockLootTableProvider {
	public AbysmLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		addLootForFamilies(
			AbysmBlockFamilies.FLOROPUMICE,
			AbysmBlockFamilies.FLOROPUMICE_BRICKS,
			AbysmBlockFamilies.FLOROPUMICE_TILES,
			AbysmBlockFamilies.SMOOTH_FLOROPUMICE,
			AbysmBlockFamilies.SMOOTH_FLOROPUMICE_BRICKS,
			AbysmBlockFamilies.CUT_SMOOTH_FLOROPUMICE
		);

		dropSelf(
			AbysmBlocks.POLISHED_FLOROPUMICE,
			AbysmBlocks.CHISLED_FLOROPUMICE,
			AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE,
			AbysmBlocks.CHISELED_SMOOTH_FLOROPUMICE,
			AbysmBlocks.SMOOTH_FLOROPUMICE_PILLAR,

			AbysmBlocks.ROSY_SPRIGS,
			AbysmBlocks.ROSY_BLOOMSHROOM,
			AbysmBlocks.ROSY_BLOOMSHROOM_STEM,
			AbysmBlocks.ROSY_BLOOMSHROOM_CAP,
			AbysmBlocks.BLOOMSHROOM_GOOP,

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

		this.addPottedPlantDrops(AbysmBlocks.POTTED_ROSY_SPRIGS);
		this.addPottedPlantDrops(AbysmBlocks.POTTED_ROSY_BLOOMSHROOM);

		this.addDrop(AbysmBlocks.ROSEBLOOMED_FLOROPUMICE, block -> this.drops(block, AbysmBlocks.FLOROPUMICE));
	}

	private void dropSelf(Block... blocks) {
		for(Block block : blocks) {
			this.addDrop(block);
		}
	}

	private void dropWhenSilkTouch(Block... blocks) {
		for(Block block : blocks) {
			this.addDropWithSilkTouch(block);
		}
	}

	private void addLootForFamilies(BlockFamily... families) {
		for(BlockFamily family : families) {
			addLootForFamily(family);
		}
	}

	private void addLootForFamily(BlockFamily family) {
		this.dropSelf(family.getBaseBlock());
		for(BlockFamily.Variant variant : BlockFamily.Variant.values()) {
			if(variant != BlockFamily.Variant.DOOR && variant != BlockFamily.Variant.SLAB) {
				Block block = family.getVariant(variant);
				if(block != null) {
					this.dropSelf(block);
				}
			}
		}
		Block door = family.getVariant(BlockFamily.Variant.DOOR);
		if(door != null) {
			this.addDrop(door, this.doorDrops(door));
		}
		Block slab = family.getVariant(BlockFamily.Variant.SLAB);
		if(slab != null) {
			this.addDrop(slab, this.slabDrops(slab));
		}
	}
}
