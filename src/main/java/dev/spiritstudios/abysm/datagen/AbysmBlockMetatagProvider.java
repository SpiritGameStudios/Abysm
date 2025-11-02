package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.specter.api.block.BlockMetatags;
import dev.spiritstudios.specter.api.registry.metatag.datagen.MetatagProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static dev.spiritstudios.abysm.block.AbysmBlocks.DREGLOAM;
import static dev.spiritstudios.abysm.block.AbysmBlocks.OOZING_DREGLOAM;

public class AbysmBlockMetatagProvider extends MetatagProvider<Block> {
	public AbysmBlockMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(dataOutput, Registries.BLOCK, registriesFuture, PackOutput.Target.DATA_PACK);
	}

	@Override
	protected void configure(Consumer<MetatagProvider<Block>.MetatagBuilder<?>> consumer, HolderLookup.Provider wrapperLookup) {
		MetatagBuilder<BlockState> builder = create(BlockMetatags.FLATTENABLE);

		builder.put(OOZING_DREGLOAM, DREGLOAM.defaultBlockState());

		consumer.accept(builder);
	}
}
