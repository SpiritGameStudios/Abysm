package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.registry.AbysmBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;

public class AbysmModelProvider extends FabricModelProvider {

	public AbysmModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator generator) {
		generator.registerSimpleCubeAll(AbysmBlocks.FLOROPUMICE);
	}

	@Override
	public void generateItemModels(ItemModelGenerator generator) {

	}
}
