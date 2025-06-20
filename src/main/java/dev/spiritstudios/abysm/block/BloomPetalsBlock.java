package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.util.AbysmCodecs;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import java.util.function.Function;

public class BloomPetalsBlock extends PlantBlock implements Waterloggable, Fertilizable, Segmented {
	public static final MapCodec<BloomPetalsBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(AbysmCodecs.SIMPLE_PARTICLE_TYPE.fieldOf("particle").forGetter(block -> block.particle), createSettingsCodec()).apply(instance, BloomPetalsBlock::new)
	);
	public static final EnumProperty<Direction> HORIZONTAL_FACING = Properties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	private final Function<BlockState, VoxelShape> shapeFunction;
	public final SimpleParticleType particle;

	@Override
	public MapCodec<BloomPetalsBlock> getCodec() {
		return CODEC;
	}

	public BloomPetalsBlock(SimpleParticleType particle, Settings settings) {
		super(settings);
		this.particle = particle;
		this.setDefaultState(this.stateManager.getDefaultState()
			.with(HORIZONTAL_FACING, Direction.NORTH)
			.with(this.getAmountProperty(), 1)
			.with(WATERLOGGED, false)
		);
		this.shapeFunction = this.createShapeFunction();
	}

	private Function<BlockState, VoxelShape> createShapeFunction() {
		return this.createShapeFunction(this.createShapeFunction(HORIZONTAL_FACING, this.getAmountProperty()));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, this.getAmountProperty());
		builder.add(WATERLOGGED);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(HORIZONTAL_FACING, rotation.rotate(state.get(HORIZONTAL_FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(HORIZONTAL_FACING)));
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState placementState = this.getPlacementState(ctx, this, this.getAmountProperty(), HORIZONTAL_FACING);
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
	public boolean canReplace(BlockState state, ItemPlacementContext context) {
		return this.shouldAddSegment(state, context, this.getAmountProperty()) || super.canReplace(state, context);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return this.shapeFunction.apply(state);
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

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);

		BlockPos downPos = pos.down();
		BlockState downState = world.getBlockState(downPos);

		if(!downState.isSideSolidFullSquare(world, pos, Direction.UP)) {
			if (random.nextInt(10) == 0) {
				double x = pos.getX() + 0.05 + 0.9 * random.nextFloat();
				double y = pos.getY() - 0.1 + 0.1 * random.nextFloat();
				double z = pos.getZ() + 0.05 + 0.9 * random.nextFloat();

				world.addParticleClient(this.particle, x, y, z, 0, 0, 0);
			}
		}
	}
}
