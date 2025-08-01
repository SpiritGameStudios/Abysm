package dev.spiritstudios.abysm.block.entity;

import dev.spiritstudios.abysm.block.AbysmBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public class AbysmBlockEntityTypes {
	public static final BlockEntityType<DensityBlobBlockEntity> DENSITY_BLOB_BLOCK = create(DensityBlobBlockEntity::new, AbysmBlocks.DENSITY_BLOB_BLOCK);

	private static <T extends BlockEntity> BlockEntityType<T> create(FabricBlockEntityTypeBuilder.Factory<T> factory, net.minecraft.block.Block... blocks) {
		return FabricBlockEntityTypeBuilder.create(factory, blocks).build();
	}
}
