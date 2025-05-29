package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		BlockPos upPos = pos.up();
		BlockState upState = world.getBlockState(upPos);
		return upState.isTransparent() || (!upState.isOpaque() && world.getFluidState(upPos).isIn(FluidTags.WATER));
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		BlockState startState = getRandomAdjacentBloomedFloropumice(world, pos, random).orElseGet(() -> getRandomBloomedFloropumice(random).getDefaultState());
		world.setBlockState(pos, startState);

		BlockPos.Mutable targetPos = new BlockPos.Mutable();
		for(int i = 0; i < 20; i++) {
			int dx = random.nextInt(5) - 2;
			int dy = random.nextInt(3) - 1;
			int dz = random.nextInt(5) - 2;
			targetPos.set(pos, dx, dy, dz);

			BlockState targetState = world.getBlockState(targetPos);
			if(targetState.isOf(AbysmBlocks.FLOROPUMICE) && isFertilizable(world, targetPos, targetState)) {
				BlockState newState = getRandomAdjacentBloomedFloropumice(world, targetPos, random).orElse(startState);
				world.setBlockState(targetPos, newState);
			}
		}
	}

	public Optional<BlockState> getRandomAdjacentBloomedFloropumice(ServerWorld world, BlockPos pos, Random random) {
		boolean foundRosy = false;
		boolean foundSunny = false;
		boolean foundMauve = false;

		BlockPos.Mutable adjPos = new BlockPos.Mutable();
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(i == 0 && j == 0) continue;

				adjPos.set(pos, i, 0, j);
				BlockState adjState = world.getBlockState(adjPos);

				if(adjState.isIn(AbysmBlockTags.BLOOMED_FLOROPUMICE)) {
					if (adjState.isOf(AbysmBlocks.ROSEBLOOMED_FLOROPUMICE)) {
						foundRosy = true;
					} else if (adjState.isOf(AbysmBlocks.SUNBLOOMED_FLOROPUMICE)) {
						foundSunny = true;
					} else if (adjState.isOf(AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE)) {
						foundMauve = true;
					}
				} else {
					adjPos.move(0, adjState.isOpaque() ? 1 : -1, 0);
					adjState = world.getBlockState(adjPos);
					if(adjState.isIn(AbysmBlockTags.BLOOMED_FLOROPUMICE)) {
						if (adjState.isOf(AbysmBlocks.ROSEBLOOMED_FLOROPUMICE)) {
							foundRosy = true;
						} else if (adjState.isOf(AbysmBlocks.SUNBLOOMED_FLOROPUMICE)) {
							foundSunny = true;
						} else if (adjState.isOf(AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE)) {
							foundMauve = true;
						}
					}
				}

				if(foundRosy && foundSunny && foundMauve) {
					break;
				}
			}
		}

		if(foundRosy || foundSunny || foundMauve) {
			List<Block> potentialBlocks = new ArrayList<>();
			if(foundRosy) potentialBlocks.add(AbysmBlocks.ROSEBLOOMED_FLOROPUMICE);
			if(foundSunny) potentialBlocks.add(AbysmBlocks.SUNBLOOMED_FLOROPUMICE);
			if(foundMauve) potentialBlocks.add(AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE);

			Block block = Util.getRandom(potentialBlocks, random);
			return Optional.of(block.getDefaultState());
		} else {
			return Optional.empty();
		}
	}

	public Block getRandomBloomedFloropumice(Random random) {
		int i = random.nextInt(3);
		return switch(i) {
			case 0 -> AbysmBlocks.ROSEBLOOMED_FLOROPUMICE;
			case 1 -> AbysmBlocks.SUNBLOOMED_FLOROPUMICE;
			default -> AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE;
		};
	}
}
