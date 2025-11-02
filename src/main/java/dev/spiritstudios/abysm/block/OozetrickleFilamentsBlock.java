package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.registry.tags.AbysmBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OozetrickleFilamentsBlock extends UnderwaterPlantBlock implements BonemealableBlock {
	public static final MapCodec<OozetrickleFilamentsBlock> CODEC = simpleCodec(OozetrickleFilamentsBlock::new);
	private static final VoxelShape SHAPE = Block.column(12.0, 0.0, 5.0);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	@Override
	public MapCodec<OozetrickleFilamentsBlock> codec() {
		return CODEC;
	}

	public OozetrickleFilamentsBlock(Properties settings) {
		super(settings);
	}

	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		return floor.is(AbysmBlockTags.OOZE_VEGETATION_PLANTABLE_ON);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE.move(state.getOffset(pos));
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		BlockPos upPos = pos.above();
		BlockState upState = world.getBlockState(upPos);
		return upState.isAir() || upState.is(Blocks.WATER);
	}

	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		BlockState lowerState = AbysmBlocks.TALL_OOZETRICKLE_FILAMENTS.defaultBlockState();
		BlockState upperState = lowerState.setValue(TallOozetrickleFilamentsBlock.HALF, DoubleBlockHalf.UPPER);

		placeBlock(lowerState, pos, world);
		placeBlock(upperState, pos.above(), world);
	}

	protected void placeBlock(BlockState state, BlockPos pos, ServerLevel world) {
		world.setBlock(pos, state.setValue(WATERLOGGED, world.isWaterAt(pos)), Block.UPDATE_CLIENTS);
	}
}
