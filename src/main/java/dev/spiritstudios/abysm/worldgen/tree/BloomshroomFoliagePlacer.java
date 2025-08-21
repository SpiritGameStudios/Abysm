package dev.spiritstudios.abysm.worldgen.tree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.specter.api.core.math.SpecterMath;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class BloomshroomFoliagePlacer extends FoliagePlacer {
	public static final MapCodec<BloomshroomFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(
		instance -> fillFoliagePlacerFields(instance)
			.and(
				BlockStateProvider.TYPE_CODEC.fieldOf("leaves_provider").forGetter(placer -> placer.leavesProvider)
			)
			.and(
				BlockStateProvider.TYPE_CODEC.fieldOf("nectarsap_provider").forGetter(placer -> placer.nectarsapProvider)
			)
			.and(
				BlockStateProvider.TYPE_CODEC.fieldOf("crown_provider").forGetter(placer -> placer.crownProvider)
			)
			.and(
				Codecs.rangedInclusiveFloat(0.0F, 1.0F).fieldOf("horizontal_top_petal_chance").forGetter(placer -> placer.horizontalTopPetalChance)
			)
			.and(
				Codecs.rangedInclusiveFloat(0.0F, 1.0F).fieldOf("diagonal_top_petal_chance").forGetter(placer -> placer.diagonalTopPetalChance)
			)
			.apply(instance, BloomshroomFoliagePlacer::new)
	);

	private final BlockStateProvider leavesProvider;
	private final BlockStateProvider nectarsapProvider;
	private final BlockStateProvider crownProvider;
	private final float horizontalTopPetalChance;
	private final float diagonalTopPetalChance;

	public BloomshroomFoliagePlacer(IntProvider radius, IntProvider offset, BlockStateProvider leavesProvider, BlockStateProvider nectarsapProvider, BlockStateProvider crownProvider, float horizontalTopPetalChance, float diagonalTopPetalChance) {
		super(radius, offset);
		this.leavesProvider = leavesProvider;
		this.nectarsapProvider = nectarsapProvider;
		this.crownProvider = crownProvider;
		this.horizontalTopPetalChance = horizontalTopPetalChance;
		this.diagonalTopPetalChance = diagonalTopPetalChance;
	}

	@Override
	protected FoliagePlacerType<?> getType() {
		return AbysmFoliagePlacerTypes.BLOOMSHROOM;
	}

	protected void generateSquareNoCorners(
		TestableWorld world, FoliagePlacer.BlockPlacer placer, Random random, TreeFeatureConfig config, BlockPos centerPos, int radius, int y
	) {
		for (BlockPos offsetPos : BlockPos.iterate(centerPos.add(-radius, y, -radius), centerPos.add(radius, y, radius))) {
			int xDistance = offsetPos.getX() - centerPos.getX();
			int zDistance = offsetPos.getZ() - centerPos.getZ();
			int xDistAbs = MathHelper.abs(xDistance);
			int zDistAbs = MathHelper.abs(zDistance);

			if (xDistAbs == radius && zDistAbs == radius) continue;
			placeFoliageBlock(world, placer, random, config, offsetPos);
		}
	}

	protected void generateSquareNoCornersOrMiddleEnds(
		TestableWorld world, FoliagePlacer.BlockPlacer placer, Random random, TreeFeatureConfig config, BlockPos centerPos, int radius, int y
	) {
		for (BlockPos offsetPos : BlockPos.iterate(centerPos.add(-radius, y, -radius), centerPos.add(radius, y, radius))) {
			int xDistance = offsetPos.getX() - centerPos.getX();
			int zDistance = offsetPos.getZ() - centerPos.getZ();
			int xDistAbs = MathHelper.abs(xDistance);
			int zDistAbs = MathHelper.abs(zDistance);

			if (xDistAbs == radius && zDistAbs == radius) continue;
			if (xDistAbs == 0 && zDistAbs == radius) continue;
			if (xDistAbs == radius && zDistAbs == 0) continue;

			placeFoliageBlock(world, placer, random, config, offsetPos);
		}
	}

	protected boolean placeLeavesBlock(TestableWorld world, FoliagePlacer.BlockPlacer placer, Random random, BlockPos pos) {
		return this.placeBlock(world, placer, random, pos, this.leavesProvider);
	}

	protected boolean placeNectarsapBlock(TestableWorld world, FoliagePlacer.BlockPlacer placer, Random random, BlockPos pos) {
		return this.placeBlock(world, placer, random, pos, this.nectarsapProvider);
	}

	protected boolean placeCrownBlock(TestableWorld world, FoliagePlacer.BlockPlacer placer, Random random, BlockPos pos) {
		return this.placeBlock(world, placer, random, pos, this.crownProvider);
	}

	protected boolean placeBlock(TestableWorld world, FoliagePlacer.BlockPlacer placer, Random random, BlockPos pos, BlockStateProvider provider) {
		if (world.testBlockState(pos, state -> state.get(Properties.PERSISTENT, false)) ||
			!TreeFeature.canReplace(world, pos)) {
			return false;
		}

		placer.placeBlock(
			pos,
			provider.get(random, pos)
				.withIfExists(
					Properties.WATERLOGGED,
					world.testFluidState(pos, fluidState -> fluidState.isEqualAndStill(Fluids.WATER))
				)
		);

		return true;
	}

	protected void fillCuboid(TestableWorld world, FoliagePlacer.BlockPlacer placer, Random random, BlockPos pos, BlockStateProvider provider, int dx, int dy, int dz) {
		BlockPos.iterate(pos, pos.add(dx, dy, dz)).forEach(p -> placeBlock(world, placer, random, p, provider));
	}

	@Override
	protected void generate(TestableWorld world, BlockPlacer placer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {
		// TODO ideally un-hardcode some of these numbers at some point

		BlockPos pos = treeNode.getCenter().up(offset);

		int y = -foliageHeight;
		int r = radius + treeNode.getFoliageRadius();

		// place nectarsap
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				if (dx == 0 || dz == 0) {
					fillCuboid(world, placer, random, pos.add(dx, y - 1, dz), this.nectarsapProvider, 0, -3, 0);
				}
			}
		}

		// place cap
		generateSquareNoCorners(
			world, placer, random, config,
			pos, r - 1, y
		);

		generateSquareNoCornersOrMiddleEnds(
			world, placer, random, config,
			pos, r, y - 1
		);

		generateSquareNoCornersOrMiddleEnds(
			world, placer, random, config,
			pos, r - 1, y - 2
		);

		// place crown
		placeCrownBlock(world, placer, random, pos.up(y + 1));

		// place lower leaves
		for (int i = -3; i <= 3; i += 2) {
			for (int j = -3; j <= 3; j += 2) {
				int xDistAbs = MathHelper.abs(i);
				int zDistAbs = MathHelper.abs(j);

				if (xDistAbs == 3 && zDistAbs == 3) continue;
				if (xDistAbs != 3 && zDistAbs != 3) continue;

				placeLeavesBlock(world, placer, random, pos.add(i, y - 2, j));
			}
		}

		for (int i = -2; i <= 2; i += 2) {
			for (int j = -2; j <= 2; j += 2) {
				int xDistAbs = MathHelper.abs(i);
				int zDistAbs = MathHelper.abs(j);

				if (xDistAbs != 2 && zDistAbs != 2) continue;

				fillCuboid(world, placer, random, pos.add(i, y - 3, j), this.leavesProvider, 0, 2, 0);
			}
		}

		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				fillCuboid(world, placer, random, pos.add(i, y - 4, j), this.leavesProvider, 0, 2, 0);
			}
		}

		// place upper leaves
		if (random.nextFloat() < this.horizontalTopPetalChance) {
			// horizontal leaves
			for (Direction direction : SpecterMath.HORIZONTAL_DIRECTIONS) {
				BlockPos p = pos.add(0, y - 1, 0).offset(direction, radius);
				fillCuboid(world, placer, random, p, this.leavesProvider, 0, 2, 0);

				p = p.up(2);
				p = p.offset(direction);
				fillCuboid(world, placer, random, p, this.leavesProvider, 0, 1, 0);
			}
		}

		if (random.nextFloat() < this.diagonalTopPetalChance) {
			// corner leaves
			for (Direction direction : SpecterMath.HORIZONTAL_DIRECTIONS) {
				Direction perpDir = direction.rotateClockwise(Direction.Axis.Y);
				BlockPos p = pos.add(0, y, 0).offset(direction, radius - 1).offset(perpDir, radius - 1);

				fillCuboid(world, placer, random, p, this.leavesProvider, 0, 2, 0);

				boolean tallerSide = random.nextBoolean();
				fillCuboid(world, placer, random, p.offset(direction), this.leavesProvider, 0, tallerSide ? 1 : 0, 0);
				fillCuboid(world, placer, random, p.offset(perpDir), this.leavesProvider, 0, tallerSide ? 0 : 1, 0);
			}
		}
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
