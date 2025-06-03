package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class BloomPetalsBlock extends LeafLitterBlock implements Waterloggable, Fertilizable {
	public static final MapCodec<LeafLitterBlock> CODEC = createCodec(BloomPetalsBlock::new);
	private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	@Override
	public MapCodec<LeafLitterBlock> getCodec() {
		return CODEC;
	}

	public BloomPetalsBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false));
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
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState placementState = super.getPlacementState(ctx);
		if(placementState == null) {
			return null;
		} else {
			FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
			return placementState.with(WATERLOGGED, fluidState.isEqualAndStill(Fluids.WATER));
		}
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

	@Override
	protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockPos downPos = pos.down();
		return this.canPlantOnTop(world.getBlockState(downPos), world, downPos);
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		if(floor.isSideSolidFullSquare(world, pos, Direction.UP)) {
			return true;
		} else {
			FluidState fluidState = world.getFluidState(pos);
			FluidState fluidStateUp = world.getFluidState(pos.up());
			return fluidState.getFluid() == Fluids.WATER && fluidStateUp.getFluid() == Fluids.EMPTY;
		}
	}

	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		int i = state.get(SEGMENT_AMOUNT);
		if (i < 4) {
			world.setBlockState(pos, state.with(SEGMENT_AMOUNT, i + 1), Block.NOTIFY_LISTENERS);
		} else {
			dropStack(world, pos, new ItemStack(this));
		}
	}
}
