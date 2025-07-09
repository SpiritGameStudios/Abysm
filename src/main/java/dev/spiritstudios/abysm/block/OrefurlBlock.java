package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class OrefurlBlock extends AbstractPlantStemBlock implements FluidFillable {
	public static final MapCodec<OrefurlBlock> CODEC = createCodec(OrefurlBlock::new);
	private static final double GROWTH_CHANCE = 0.04;
	private static final VoxelShape SHAPE = Block.createColumnShape(16.0, 0.0, 12.0);

	@Override
	public MapCodec<OrefurlBlock> getCodec() {
		return CODEC;
	}

	public OrefurlBlock(Settings settings) {
		super(settings, Direction.UP, SHAPE, true, GROWTH_CHANCE);
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getStill(false);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8 ? super.getPlacementState(ctx) : null;
	}

	@Override
	protected Block getPlant() {
		return AbysmBlocks.GOLDEN_LAZULI_OREFURL_PLANT;
	}

	@Override
	protected boolean canAttachTo(BlockState state) {
		return canAttachToState(state);
	}

	public static boolean canAttachToState(BlockState state) {
		return !state.isOf(Blocks.MAGMA_BLOCK);
	}

	@Override
	protected boolean chooseStemState(BlockState state) {
		return state.isOf(Blocks.WATER);
	}

	@Override
	public boolean canFillWithFluid(@Nullable LivingEntity filler, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}

	@Override
	protected int getGrowthLength(Random random) {
		return 1;
	}
}
