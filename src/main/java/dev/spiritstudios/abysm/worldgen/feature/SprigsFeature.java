package dev.spiritstudios.abysm.worldgen.feature;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SprigsFeature extends Feature<StateProviderFeatureConfig> {
	public SprigsFeature() {
		super(StateProviderFeatureConfig.CODEC);
	}

	@Override
	public boolean generate(FeatureContext<StateProviderFeatureConfig> context) {
		boolean placedBlock = false;
		Random random = context.getRandom();
		StructureWorldAccess world = context.getWorld();
		BlockPos origin = context.getOrigin();
		StateProviderFeatureConfig config = context.getConfig();

		int xOffset = random.nextInt(8) - random.nextInt(8);
		int zOffset = random.nextInt(8) - random.nextInt(8);

		int y = world.getTopY(Heightmap.Type.OCEAN_FLOOR, origin.getX() + xOffset, origin.getZ() + zOffset);

		BlockPos pos = new BlockPos(origin.getX() + xOffset, y, origin.getZ() + zOffset);
		if (world.getBlockState(pos).isOf(Blocks.WATER)) {
			BlockState state = config.stateProvider().get(random, pos)
				.withIfExists(Properties.WATERLOGGED, true);
			if (state.canPlaceAt(world, pos)) {
				world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);
				placedBlock = true;
			}
		}

		return placedBlock;
	}
}
