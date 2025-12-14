package dev.spiritstudios.abysm.world.level.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class OrefurlBlock extends GrowingPlantHeadBlock implements LiquidBlockContainer {
	public static final MapCodec<OrefurlBlock> CODEC = simpleCodec(OrefurlBlock::new);
	private static final double GROWTH_CHANCE = 0.04;
	private static final VoxelShape SHAPE = Block.column(12.0, 0.0, 12.0);

	@Override
	public MapCodec<OrefurlBlock> codec() {
		return CODEC;
	}

	public OrefurlBlock(Properties settings) {
		super(settings, Direction.UP, SHAPE, true, GROWTH_CHANCE);
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getSource(false);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
		return fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8 ? super.getStateForPlacement(ctx) : null;
	}

	@Override
	protected Block getBodyBlock() {
		return AbysmBlocks.GOLDEN_LAZULI_OREFURL_PLANT;
	}

	@Override
	protected boolean canAttachTo(BlockState state) {
		return canAttachToState(state);
	}

	public static boolean canAttachToState(BlockState state) {
		return !state.is(Blocks.MAGMA_BLOCK);
	}

	@Override
	protected boolean canGrowInto(BlockState state) {
		return state.is(Blocks.WATER);
	}

	@Override
	public boolean canPlaceLiquid(@Nullable LivingEntity filler, BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}

	@Override
	protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
		return 1;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE.move(state.getOffset(pos));
	}
}
