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

		this.addDrop(AbysmBlocks.POLISHED_FLOROPUMICE);
		this.addDrop(AbysmBlocks.CHISLED_FLOROPUMICE);
		this.addDrop(AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE);
		this.addDrop(AbysmBlocks.CHISELED_SMOOTH_FLOROPUMICE);
		this.addDrop(AbysmBlocks.SMOOTH_FLOROPUMICE_PILLAR);

		this.addDrop(AbysmBlocks.PURPLE_SCABIOSA);
	}

	private void dropSelf(Block... blocks) {
		for(Block block : blocks) {
			this.addDrop(block);
		}
	}

	private void dropWhenSilkTouch(Block... blocks) {
		for(Block block : blocks) {
			dropWhenSilkTouch(block);
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
