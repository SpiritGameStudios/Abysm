package dev.spiritstudios.abysm.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class DensityBlobBlockEntity extends BlockEntity {

	public DensityBlobBlockEntity(BlockPos pos, BlockState state) {
		super(AbysmBlockEntityTypes.DENSITY_BLOB_BLOCK, pos, state);
	}
}
