package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.specter.api.block.BlockMetatags;
import dev.spiritstudios.specter.api.registry.metatag.datagen.MetatagProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static dev.spiritstudios.abysm.block.AbysmBlocks.DREGLOAM;
import static dev.spiritstudios.abysm.block.AbysmBlocks.OOZING_DREGLOAM;

public class AbysmBlockMetatagProvider extends MetatagProvider<Block> {
	public AbysmBlockMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(dataOutput, RegistryKeys.BLOCK, registriesFuture, DataOutput.OutputType.DATA_PACK);
	}

	@Override
	protected void configure(Consumer<MetatagBuilder<Block, ?>> consumer, RegistryWrapper.WrapperLookup wrapperLookup) {
		MetatagBuilder<Block, BlockState> builder = create(BlockMetatags.FLATTENABLE);

		builder.put(OOZING_DREGLOAM, DREGLOAM.getDefaultState());

		consumer.accept(builder);
	}

	@Override
	public String getName() {
		return "Datapack " + super.getName();
	}
}
