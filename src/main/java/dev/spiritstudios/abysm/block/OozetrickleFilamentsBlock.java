package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.registry.tags.AbysmBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class OozetrickleFilamentsBlock extends UnderwaterPlantBlock implements Fertilizable {
	public static final MapCodec<OozetrickleFilamentsBlock> CODEC = createCodec(OozetrickleFilamentsBlock::new);
	private static final VoxelShape SHAPE = Block.createColumnShape(12.0, 0.0, 5.0);
	private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	@Override
	public MapCodec<OozetrickleFilamentsBlock> getCodec() {
		return CODEC;
	}

	public OozetrickleFilamentsBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isIn(AbysmBlockTags.OOZE_VEGETATION_PLANTABLE_ON);
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE.offset(state.getModelOffset(pos));
	}

	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
		BlockPos upPos = pos.up();
		BlockState upState = world.getBlockState(upPos);
		return upState.isAir() || upState.isOf(Blocks.WATER);
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		BlockState lowerState = AbysmBlocks.TALL_OOZETRICKLE_FILAMENTS.getDefaultState();
		BlockState upperState = lowerState.with(TallOozetrickleFilamentsBlock.HALF, DoubleBlockHalf.UPPER);

		placeBlock(lowerState, pos, world);
		placeBlock(upperState, pos.up(), world);
	}

	protected void placeBlock(BlockState state, BlockPos pos, ServerWorld world) {
		world.setBlockState(pos, state.with(WATERLOGGED, world.isWater(pos)), Block.NOTIFY_LISTENERS);
	}
}
