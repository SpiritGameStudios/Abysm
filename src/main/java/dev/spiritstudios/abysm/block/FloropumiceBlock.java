package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class FloropumiceBlock extends Block implements Fertilizable {
	public static final MapCodec<FloropumiceBlock> CODEC = createCodec(FloropumiceBlock::new);

	@Override
	public MapCodec<FloropumiceBlock> getCodec() {
		return CODEC;
	}

	public FloropumiceBlock(Settings settings) {
		super(settings);
	}
	@Override
	public FertilizableType getFertilizableType() {
		return FertilizableType.NEIGHBOR_SPREADER;
	}

	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
		return world.getBlockState(pos.up()).isTransparent();
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		world.setBlockState(pos, AbysmBlocks.ROSEBLOOMED_FLOROPUMICE.getDefaultState());
	}
}
