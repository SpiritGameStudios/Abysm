package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class DregloamOozeBlock extends Block {
	public static final MapCodec<DregloamOozeBlock> CODEC = createCodec(DregloamOozeBlock::new);

	@Override
	public MapCodec<DregloamOozeBlock> getCodec() {
		return CODEC;
	}

	public DregloamOozeBlock(Settings settings) {
		super(settings);
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockPos downPos = pos.down();
		BlockState downState = world.getBlockState(downPos);
		if(downState.isOf(AbysmBlocks.DREGLOAM)) {
			world.setBlockState(downPos, AbysmBlocks.OOZING_DREGLOAM.getDefaultState());
		}
	}
}
