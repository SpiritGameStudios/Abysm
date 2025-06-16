package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class AntennaePlantBlock extends UnderwaterPlantBlock {
	public static final MapCodec<AntennaePlantBlock> CODEC = createCodec(AntennaePlantBlock::new);
	private static final VoxelShape SHAPE = Block.createColumnShape(10.0, 0.0, 10.0);

	@Override
	public MapCodec<AntennaePlantBlock> getCodec() {
		return CODEC;
	}

	public AntennaePlantBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE.offset(state.getModelOffset(pos));
	}
}
