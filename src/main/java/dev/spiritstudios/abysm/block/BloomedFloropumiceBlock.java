package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

public class BloomedFloropumiceBlock extends Block {
	public static final MapCodec<BloomedFloropumiceBlock> CODEC = createCodec(BloomedFloropumiceBlock::new);

	@Override
	public MapCodec<BloomedFloropumiceBlock> getCodec() {
		return CODEC;
	}

	public BloomedFloropumiceBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!stayAlive(state, world, pos)) {
			world.setBlockState(pos, AbysmBlocks.FLOROPUMICE.getDefaultState());
		}
	}

	private static boolean stayAlive(BlockState state, WorldView world, BlockPos pos) {
		BlockPos upPos = pos.up();
		FluidState fluidState = world.getFluidState(upPos);
		if(fluidState.isIn(FluidTags.WATER)) {
			return true;
		} else {
			BlockState blockState = world.getBlockState(upPos);
			int i = ChunkLightProvider.getRealisticOpacity(state, blockState, Direction.UP, blockState.getOpacity());
			return i < 15;
		}
	}
}
