package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.registry.AbysmBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class AbysmLootTableProvider extends FabricBlockLootTableProvider {
	public AbysmLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		this.addDrop(AbysmBlocks.FLOROPUMICE);
		this.addDrop(AbysmBlocks.POLISHED_FLOROPUMICE);
		this.addDrop(AbysmBlocks.FLOROPUMICE_BRICKS);
		this.addDrop(AbysmBlocks.FLOROPUMICE_TILES);
		this.addDrop(AbysmBlocks.CHISLED_FLOROPUMICE);
		this.addDrop(AbysmBlocks.SMOOTH_FLOROPUMICE);
		this.addDrop(AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE);
		this.addDrop(AbysmBlocks.SMOOTH_FLOROPUMICE_BRICKS);
		this.addDrop(AbysmBlocks.CUT_SMOOTH_FLOROPUMICE);
		this.addDrop(AbysmBlocks.CHISELED_SMOOTH_FLOROPUMICE);
		this.addDrop(AbysmBlocks.SMOOTH_FLOROPUMICE_PILLAR);

		this.addDrop(AbysmBlocks.PURPLE_SCABIOSA);
	}
}
