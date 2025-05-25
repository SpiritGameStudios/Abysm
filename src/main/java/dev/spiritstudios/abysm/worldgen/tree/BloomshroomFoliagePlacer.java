package dev.spiritstudios.abysm.worldgen.tree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.registry.AbysmFoliagePlacerTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class BloomshroomFoliagePlacer extends FoliagePlacer {
	public static final MapCodec<BloomshroomFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> fillFoliagePlacerFields(instance)
		.apply(instance, BloomshroomFoliagePlacer::new));

	public BloomshroomFoliagePlacer(IntProvider radius, IntProvider offset) {
		super(radius, offset);
	}

	public BloomshroomFoliagePlacer(int radius, int offset) {
		super(ConstantIntProvider.create(radius), ConstantIntProvider.create(offset));
	}

	@Override
	protected FoliagePlacerType<?> getType() {
		return AbysmFoliagePlacerTypes.BLOOMSHROOM;
	}

	protected void generateCircle(
		TestableWorld world, FoliagePlacer.BlockPlacer placer, Random random, TreeFeatureConfig config, BlockPos centerPos, int radius, int y
	) {
		for (BlockPos offsetPos : BlockPos.iterate(centerPos.add(-radius, y, -radius), centerPos.add(radius, y, radius))) {
			int xDistance = offsetPos.getX() - centerPos.getX();
			int zDistance = offsetPos.getZ() - centerPos.getZ();
			if (xDistance * xDistance + zDistance * zDistance <= radius * radius) {
				placeFoliageBlock(world, placer, random, config, offsetPos);
			}
		}
	}

	@Override
	protected void generate(TestableWorld world, BlockPlacer placer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {
		BlockPos pos = treeNode.getCenter().up(offset);

		generateCircle(
			world, placer, random, config,
			pos, radius + treeNode.getFoliageRadius(), -1 - foliageHeight
		);

		generateCircle(
			world, placer, random, config,
			pos, radius + treeNode.getFoliageRadius() - 1, -foliageHeight
		);
	}

	@Override
	public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
		return 0;
	}

	@Override
	protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
		return false;
	}
}
