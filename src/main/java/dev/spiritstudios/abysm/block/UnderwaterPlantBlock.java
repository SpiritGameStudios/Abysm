package dev.spiritstudios.abysm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public abstract class UnderwaterPlantBlock extends PlantBlock {
	private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	public UnderwaterPlantBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isIn(AbysmBlockTags.BLOOMSHROOM_PLANTABLE_ON) || super.canPlantOnTop(floor, world, pos);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return this.getDefaultState()
			.with(WATERLOGGED, fluidState.isEqualAndStill(Fluids.WATER));
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
		BlockState newState = super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
		if(!newState.isAir()) {
			if (state.get(WATERLOGGED)) {
				tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
			}
		}
		return newState;
	}
}
