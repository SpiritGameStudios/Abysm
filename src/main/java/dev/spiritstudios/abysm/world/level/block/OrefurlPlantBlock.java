package dev.spiritstudios.abysm.world.level.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static dev.spiritstudios.abysm.world.level.block.OrefurlBlock.canAttachToState;

public class OrefurlPlantBlock extends GrowingPlantBodyBlock implements LiquidBlockContainer {
	public static final MapCodec<OrefurlPlantBlock> CODEC = simpleCodec(OrefurlPlantBlock::new);
	private static final VoxelShape SHAPE = Block.column(12.0, 0.0, 16.0);

	@Override
	public MapCodec<OrefurlPlantBlock> codec() {
		return CODEC;
	}

	public OrefurlPlantBlock(Properties settings) {
		super(settings, Direction.UP, SHAPE, true);
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getSource(false);
	}

	@Override
	protected GrowingPlantHeadBlock getHeadBlock() {
		return AbysmBlocks.GOLDEN_LAZULI_OREFURL;
	}

	@Override
	protected boolean canAttachTo(BlockState state) {
		return canAttachToState(state);
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
	protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE.move(state.getOffset(pos));
	}
}
