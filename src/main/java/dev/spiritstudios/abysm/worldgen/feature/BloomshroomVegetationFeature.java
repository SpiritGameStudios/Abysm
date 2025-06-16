package dev.spiritstudios.abysm.worldgen.feature;

import com.mojang.serialization.Codec;
import dev.spiritstudios.abysm.registry.tags.AbysmBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NetherForestVegetationFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class BloomshroomVegetationFeature extends Feature<NetherForestVegetationFeatureConfig> {
	public BloomshroomVegetationFeature(Codec<NetherForestVegetationFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(FeatureContext<NetherForestVegetationFeatureConfig> context) {
		StructureWorldAccess world = context.getWorld();
		BlockPos pos = context.getOrigin();
		BlockState state = world.getBlockState(pos.down());
		NetherForestVegetationFeatureConfig config = context.getConfig();
		Random random = context.getRandom();

		if (!state.isIn(AbysmBlockTags.BLOOMSHROOM_PLANTABLE_ON)) {
			return false;
		} else {
			int i = pos.getY();
			if (i >= world.getBottomY() + 1 && i + 1 <= world.getTopYInclusive()) {
				int placedBlocks = 0;

				for (int k = 0; k < config.spreadWidth * config.spreadWidth; k++) {
					BlockPos targetPos = pos.add(
						random.nextInt(config.spreadWidth) - random.nextInt(config.spreadWidth),
						random.nextInt(config.spreadHeight) - random.nextInt(config.spreadHeight),
						random.nextInt(config.spreadWidth) - random.nextInt(config.spreadWidth)
					);
					BlockState newState = config.stateProvider.get(random, targetPos);
					BlockState targetState = world.getBlockState(targetPos);
					boolean targetWater = targetState.isOf(Blocks.WATER);
					if ((targetState.isAir() || targetWater)
						&& targetPos.getY() > world.getBottomY()
						&& newState.canPlaceAt(world, targetPos)) {
						BlockState actualNewState = newState.contains(Properties.WATERLOGGED)
							? newState.with(Properties.WATERLOGGED, targetWater)
							: newState;

						world.setBlockState(targetPos, actualNewState, Block.NOTIFY_LISTENERS);
						placedBlocks++;
					}
				}

				return placedBlocks > 0;
			} else {
				return false;
			}
		}
	}
}
