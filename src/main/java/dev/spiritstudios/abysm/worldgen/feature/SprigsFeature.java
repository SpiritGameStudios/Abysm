package dev.spiritstudios.abysm.worldgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SprigsFeature extends Feature<StateProviderFeatureConfig> {
	public SprigsFeature() {
		super(StateProviderFeatureConfig.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<StateProviderFeatureConfig> context) {
		boolean placedBlock = false;
		RandomSource random = context.random();
		WorldGenLevel world = context.level();
		BlockPos origin = context.origin();
		StateProviderFeatureConfig config = context.config();

		int xOffset = random.nextInt(8) - random.nextInt(8);
		int zOffset = random.nextInt(8) - random.nextInt(8);

		int y = world.getHeight(Heightmap.Types.OCEAN_FLOOR, origin.getX() + xOffset, origin.getZ() + zOffset);

		BlockPos pos = new BlockPos(origin.getX() + xOffset, y, origin.getZ() + zOffset);
		if (world.getBlockState(pos).is(Blocks.WATER)) {
			BlockState state = config.stateProvider().getState(random, pos)
				.trySetValue(BlockStateProperties.WATERLOGGED, true);
			if (state.canSurvive(world, pos)) {
				world.setBlock(pos, state, Block.UPDATE_CLIENTS);
				placedBlock = true;
			}
		}

		return placedBlock;
	}
}
