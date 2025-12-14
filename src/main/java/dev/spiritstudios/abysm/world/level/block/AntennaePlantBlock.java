package dev.spiritstudios.abysm.world.level.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AntennaePlantBlock extends UnderwaterPlantBlock {
	public static final MapCodec<AntennaePlantBlock> CODEC = simpleCodec(AntennaePlantBlock::new);
	private static final VoxelShape SHAPE = Block.column(10.0, 0.0, 10.0);

	@Override
	public MapCodec<AntennaePlantBlock> codec() {
		return CODEC;
	}

	public AntennaePlantBlock(Properties settings) {
		super(settings);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE.move(state.getOffset(pos));
	}
}
