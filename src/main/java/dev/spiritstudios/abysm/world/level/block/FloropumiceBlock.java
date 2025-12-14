package dev.spiritstudios.abysm.world.level.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.core.registries.tags.AbysmBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FloropumiceBlock extends Block implements BonemealableBlock {
	public static final MapCodec<FloropumiceBlock> CODEC = simpleCodec(FloropumiceBlock::new);

	@Override
	public MapCodec<FloropumiceBlock> codec() {
		return CODEC;
	}

	public FloropumiceBlock(Properties settings) {
		super(settings);
	}

	@Override
	public Type getType() {
		return Type.NEIGHBOR_SPREADER;
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		BlockPos upPos = pos.above();
		BlockState upState = world.getBlockState(upPos);
		return upState.propagatesSkylightDown() || (!upState.canOcclude() && world.getFluidState(upPos).is(FluidTags.WATER));
	}

	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		BlockState startState = getRandomAdjacentBloomedFloropumice(world, pos, random).orElseGet(() -> getRandomBloomedFloropumice(random).defaultBlockState());
		world.setBlockAndUpdate(pos, startState);

		BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos();
		for (int i = 0; i < 20; i++) {
			int dx = random.nextInt(5) - 2;
			int dy = random.nextInt(3) - 1;
			int dz = random.nextInt(5) - 2;
			targetPos.setWithOffset(pos, dx, dy, dz);

			BlockState targetState = world.getBlockState(targetPos);
			if (targetState.is(AbysmBlocks.FLOROPUMICE) && isValidBonemealTarget(world, targetPos, targetState)) {
				BlockState newState = getRandomAdjacentBloomedFloropumice(world, targetPos, random).orElse(startState);
				world.setBlockAndUpdate(targetPos, newState);
			}
		}
	}

	public Optional<BlockState> getRandomAdjacentBloomedFloropumice(ServerLevel world, BlockPos pos, RandomSource random) {
		boolean foundRosy = false;
		boolean foundSunny = false;
		boolean foundMauve = false;

		BlockPos.MutableBlockPos adjPos = new BlockPos.MutableBlockPos();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) continue;

				adjPos.setWithOffset(pos, i, 0, j);
				BlockState adjState = world.getBlockState(adjPos);

				if (adjState.is(AbysmBlockTags.BLOOMED_FLOROPUMICE)) {
					if (adjState.is(AbysmBlocks.ROSEBLOOMED_FLOROPUMICE)) {
						foundRosy = true;
					} else if (adjState.is(AbysmBlocks.SUNBLOOMED_FLOROPUMICE)) {
						foundSunny = true;
					} else if (adjState.is(AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE)) {
						foundMauve = true;
					}
				} else {
					adjPos.move(0, adjState.canOcclude() ? 1 : -1, 0);
					adjState = world.getBlockState(adjPos);
					if (adjState.is(AbysmBlockTags.BLOOMED_FLOROPUMICE)) {
						if (adjState.is(AbysmBlocks.ROSEBLOOMED_FLOROPUMICE)) {
							foundRosy = true;
						} else if (adjState.is(AbysmBlocks.SUNBLOOMED_FLOROPUMICE)) {
							foundSunny = true;
						} else if (adjState.is(AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE)) {
							foundMauve = true;
						}
					}
				}

				if (foundRosy && foundSunny && foundMauve) {
					break;
				}
			}
		}

		if (foundRosy || foundSunny || foundMauve) {
			List<Block> potentialBlocks = new ArrayList<>();
			if (foundRosy) potentialBlocks.add(AbysmBlocks.ROSEBLOOMED_FLOROPUMICE);
			if (foundSunny) potentialBlocks.add(AbysmBlocks.SUNBLOOMED_FLOROPUMICE);
			if (foundMauve) potentialBlocks.add(AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE);

			Block block = Util.getRandom(potentialBlocks, random);
			return Optional.of(block.defaultBlockState());
		} else {
			return Optional.empty();
		}
	}

	public Block getRandomBloomedFloropumice(RandomSource random) {
		int i = random.nextInt(3);
		return switch (i) {
			case 0 -> AbysmBlocks.ROSEBLOOMED_FLOROPUMICE;
			case 1 -> AbysmBlocks.SUNBLOOMED_FLOROPUMICE;
			default -> AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE;
		};
	}
}
