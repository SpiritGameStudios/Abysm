package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SegmentableBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BloomPetalsBlock extends VegetationBlock implements SimpleWaterloggedBlock, BonemealableBlock, SegmentableBlock {
	public static final MapCodec<BloomPetalsBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(ParticleTypes.CODEC.fieldOf("particle").forGetter(block -> block.particle), propertiesCodec()).apply(instance, BloomPetalsBlock::new)
	);
	public static final EnumProperty<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private final Function<BlockState, VoxelShape> shapeFunction;
	public final ParticleOptions particle;

	@Override
	public MapCodec<BloomPetalsBlock> codec() {
		return CODEC;
	}

	public BloomPetalsBlock(ParticleOptions particle, Properties settings) {
		super(settings);
		this.particle = particle;
		this.registerDefaultState(this.stateDefinition.any()
			.setValue(HORIZONTAL_FACING, Direction.NORTH)
			.setValue(this.getSegmentAmountProperty(), 1)
			.setValue(WATERLOGGED, false)
		);
		this.shapeFunction = this.createShapeFunction();
	}

	private Function<BlockState, VoxelShape> createShapeFunction() {
		return this.getShapeForEachState(this.getShapeCalculator(HORIZONTAL_FACING, this.getSegmentAmountProperty()));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, this.getSegmentAmountProperty());
		builder.add(WATERLOGGED);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(HORIZONTAL_FACING, rotation.rotate(state.getValue(HORIZONTAL_FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(HORIZONTAL_FACING)));
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState placementState = this.getStateForPlacement(ctx, this, this.getSegmentAmountProperty(), HORIZONTAL_FACING);
		if (placementState == null) {
			return null;
		} else {
			FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
			return placementState.setValue(WATERLOGGED, fluidState.isSourceOfType(Fluids.WATER));
		}
	}

	@Override
	protected BlockState updateShape(
		BlockState state,
		LevelReader world,
		ScheduledTickAccess tickView,
		BlockPos pos,
		Direction direction,
		BlockPos neighborPos,
		BlockState neighborState,
		RandomSource random
	) {
		BlockState newState = super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
		if (!newState.isAir()) {
			if (state.getValue(WATERLOGGED)) {
				tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
			}
		}
		return newState;
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BlockPos downPos = pos.below();
		return this.mayPlaceOn(world.getBlockState(downPos), world, downPos);
	}

	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		if (floor.isFaceSturdy(world, pos, Direction.UP)) {
			return true;
		} else {
			FluidState fluidState = world.getFluidState(pos);
			FluidState fluidStateUp = world.getFluidState(pos.above());
			return fluidState.getType() == Fluids.WATER && fluidStateUp.getType() == Fluids.EMPTY;
		}
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		return this.canBeReplaced(state, context, this.getSegmentAmountProperty()) || super.canBeReplaced(state, context);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return this.shapeFunction.apply(state);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		int i = state.getValue(AMOUNT);
		if (i < 4) {
			world.setBlock(pos, state.setValue(AMOUNT, i + 1), Block.UPDATE_CLIENTS);
		} else {
			popResource(world, pos, new ItemStack(this));
		}
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);

		BlockPos downPos = pos.below();
		BlockState downState = world.getBlockState(downPos);

		if (!downState.isFaceSturdy(world, pos, Direction.UP)) {
			if (random.nextInt(10) == 0) {
				double x = pos.getX() + 0.05 + 0.9 * random.nextFloat();
				double y = pos.getY() - 0.1 + 0.1 * random.nextFloat();
				double z = pos.getZ() + 0.05 + 0.9 * random.nextFloat();

				world.addParticle(this.particle, x, y, z, 0, 0, 0);
			}
		}
	}
}
