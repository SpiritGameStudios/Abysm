package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.registry.tags.AbysmBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class TallOozetrickleFilamentsBlock extends TallPlantBlock implements Waterloggable {
	public static final MapCodec<TallOozetrickleFilamentsBlock> CODEC = createCodec(TallOozetrickleFilamentsBlock::new);
	private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	private static final VoxelShape SHAPE = Block.createColumnShape(12.0, 0.0, 16.0);

	@Override
	public MapCodec<TallOozetrickleFilamentsBlock> getCodec() {
		return CODEC;
	}

	public TallOozetrickleFilamentsBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(HALF, DoubleBlockHalf.LOWER).with(WATERLOGGED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(WATERLOGGED);
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE.offset(state.getModelOffset(pos));
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isIn(AbysmBlockTags.OOZE_VEGETATION_PLANTABLE_ON);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		return state != null
			? withWaterloggedState(ctx.getWorld(), ctx.getBlockPos(), state)
			: null;
	}

	@Override
	protected BlockState getStateForNeighborUpdate(
		BlockState state,
		WorldView world,
		ScheduledTickView tickView,
		BlockPos pos,
		Direction direction,
		BlockPos neighborPos,
		BlockState neighborState,
		Random random
	) {
		if (state.get(WATERLOGGED)) {
			tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
	}
}
