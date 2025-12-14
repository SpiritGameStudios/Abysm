package dev.spiritstudios.abysm.world.level.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class DregloamOozeBlock extends Block {
	public static final MapCodec<DregloamOozeBlock> CODEC = simpleCodec(DregloamOozeBlock::new);

	@Override
	public MapCodec<DregloamOozeBlock> codec() {
		return CODEC;
	}

	public DregloamOozeBlock(Properties settings) {
		super(settings);
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		BlockPos downPos = pos.below();
		BlockState downState = world.getBlockState(downPos);
		if (downState.is(AbysmBlocks.DREGLOAM)) {
			world.setBlockAndUpdate(downPos, AbysmBlocks.OOZING_DREGLOAM.defaultBlockState());
		}
	}
}
