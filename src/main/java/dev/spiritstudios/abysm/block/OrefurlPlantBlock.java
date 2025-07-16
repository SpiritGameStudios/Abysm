package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import static dev.spiritstudios.abysm.block.OrefurlBlock.canAttachToState;

public class OrefurlPlantBlock extends AbstractPlantBlock implements FluidFillable {
	public static final MapCodec<OrefurlPlantBlock> CODEC = createCodec(OrefurlPlantBlock::new);
	private static final VoxelShape SHAPE = Block.createColumnShape(12.0, 0.0, 16.0);

	@Override
	public MapCodec<OrefurlPlantBlock> getCodec() {
		return CODEC;
	}

	public OrefurlPlantBlock(Settings settings) {
		super(settings, Direction.UP, SHAPE, true);
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getStill(false);
	}

	@Override
	protected AbstractPlantStemBlock getStem() {
		return AbysmBlocks.GOLDEN_LAZULI_OREFURL;
	}

	@Override
	protected boolean canAttachTo(BlockState state) {
		return canAttachToState(state);
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
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE.offset(state.getModelOffset(pos));
	}
}
