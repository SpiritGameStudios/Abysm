package dev.spiritstudios.abysm.worldgen.tree;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.specter.api.core.math.SpecterMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.material.Fluids;

public class BloomshroomFoliagePlacer extends FoliagePlacer {
	public static final MapCodec<BloomshroomFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(
		instance -> foliagePlacerParts(instance)
			.and(
				BlockStateProvider.CODEC.fieldOf("leaves_provider").forGetter(placer -> placer.leavesProvider)
			)
			.and(
				BlockStateProvider.CODEC.fieldOf("nectarsap_provider").forGetter(placer -> placer.nectarsapProvider)
			)
			.and(
				BlockStateProvider.CODEC.fieldOf("crown_provider").forGetter(placer -> placer.crownProvider)
			)
			.and(
				ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("horizontal_top_petal_chance").forGetter(placer -> placer.horizontalTopPetalChance)
			)
			.and(
				ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("diagonal_top_petal_chance").forGetter(placer -> placer.diagonalTopPetalChance)
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
	protected FoliagePlacerType<?> type() {
		return AbysmFoliagePlacerTypes.BLOOMSHROOM;
	}

	protected void generateSquareNoCorners(
		LevelSimulatedReader world, FoliagePlacer.FoliageSetter placer, RandomSource random, TreeConfiguration config, BlockPos centerPos, int radius, int y
	) {
		for (BlockPos offsetPos : BlockPos.betweenClosed(centerPos.offset(-radius, y, -radius), centerPos.offset(radius, y, radius))) {
			int xDistance = offsetPos.getX() - centerPos.getX();
			int zDistance = offsetPos.getZ() - centerPos.getZ();
			int xDistAbs = Mth.abs(xDistance);
			int zDistAbs = Mth.abs(zDistance);

			if (xDistAbs == radius && zDistAbs == radius) continue;
			tryPlaceLeaf(world, placer, random, config, offsetPos);
		}
	}

	protected void generateSquareNoCornersOrMiddleEnds(
		LevelSimulatedReader world, FoliagePlacer.FoliageSetter placer, RandomSource random, TreeConfiguration config, BlockPos centerPos, int radius, int y
	) {
		for (BlockPos offsetPos : BlockPos.betweenClosed(centerPos.offset(-radius, y, -radius), centerPos.offset(radius, y, radius))) {
			int xDistance = offsetPos.getX() - centerPos.getX();
			int zDistance = offsetPos.getZ() - centerPos.getZ();
			int xDistAbs = Mth.abs(xDistance);
			int zDistAbs = Mth.abs(zDistance);

			if (xDistAbs == radius && zDistAbs == radius) continue;
			if (xDistAbs == 0 && zDistAbs == radius) continue;
			if (xDistAbs == radius && zDistAbs == 0) continue;

			tryPlaceLeaf(world, placer, random, config, offsetPos);
		}
	}

	protected boolean placeLeavesBlock(LevelSimulatedReader world, FoliagePlacer.FoliageSetter placer, RandomSource random, BlockPos pos) {
		return this.placeBlock(world, placer, random, pos, this.leavesProvider);
	}

	protected boolean placeNectarsapBlock(LevelSimulatedReader world, FoliagePlacer.FoliageSetter placer, RandomSource random, BlockPos pos) {
		return this.placeBlock(world, placer, random, pos, this.nectarsapProvider);
	}

	protected boolean placeCrownBlock(LevelSimulatedReader world, FoliagePlacer.FoliageSetter placer, RandomSource random, BlockPos pos) {
		return this.placeBlock(world, placer, random, pos, this.crownProvider);
	}

	protected boolean placeBlock(LevelSimulatedReader world, FoliagePlacer.FoliageSetter placer, RandomSource random, BlockPos pos, BlockStateProvider provider) {
		if (world.isStateAtPosition(pos, state -> state.getValueOrElse(BlockStateProperties.PERSISTENT, false)) ||
			!TreeFeature.validTreePos(world, pos)) {
			return false;
		}

		placer.set(
			pos,
			provider.getState(random, pos)
				.trySetValue(
					BlockStateProperties.WATERLOGGED,
					world.isFluidAtPosition(pos, fluidState -> fluidState.isSourceOfType(Fluids.WATER))
				)
		);

		return true;
	}

	protected void fillCuboid(LevelSimulatedReader world, FoliagePlacer.FoliageSetter placer, RandomSource random, BlockPos pos, BlockStateProvider provider, int dx, int dy, int dz) {
		BlockPos.betweenClosed(pos, pos.offset(dx, dy, dz)).forEach(p -> placeBlock(world, placer, random, p, provider));
	}

	@Override
	protected void createFoliage(LevelSimulatedReader world, FoliageSetter placer, RandomSource random, TreeConfiguration config, int trunkHeight, FoliageAttachment treeNode, int foliageHeight, int radius, int offset) {
		// TODO ideally un-hardcode some of these numbers at some point

		BlockPos pos = treeNode.pos().above(offset);

		int y = -foliageHeight;
		int r = radius + treeNode.radiusOffset();

		// place nectarsap
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				if (dx == 0 || dz == 0) {
					fillCuboid(world, placer, random, pos.offset(dx, y - 1, dz), this.nectarsapProvider, 0, -3, 0);
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
		placeCrownBlock(world, placer, random, pos.above(y + 1));

		// place lower leaves
		for (int i = -3; i <= 3; i += 2) {
			for (int j = -3; j <= 3; j += 2) {
				int xDistAbs = Mth.abs(i);
				int zDistAbs = Mth.abs(j);

				if (xDistAbs == 3 && zDistAbs == 3) continue;
				if (xDistAbs != 3 && zDistAbs != 3) continue;

				placeLeavesBlock(world, placer, random, pos.offset(i, y - 2, j));
			}
		}

		for (int i = -2; i <= 2; i += 2) {
			for (int j = -2; j <= 2; j += 2) {
				int xDistAbs = Mth.abs(i);
				int zDistAbs = Mth.abs(j);

				if (xDistAbs != 2 && zDistAbs != 2) continue;

				fillCuboid(world, placer, random, pos.offset(i, y - 3, j), this.leavesProvider, 0, 2, 0);
			}
		}

		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				fillCuboid(world, placer, random, pos.offset(i, y - 4, j), this.leavesProvider, 0, 2, 0);
			}
		}

		// place upper leaves
		if (random.nextFloat() < this.horizontalTopPetalChance) {
			// horizontal leaves
			for (Direction direction : SpecterMath.HORIZONTAL_DIRECTIONS) {
				BlockPos p = pos.offset(0, y - 1, 0).relative(direction, radius);
				fillCuboid(world, placer, random, p, this.leavesProvider, 0, 2, 0);

				p = p.above(2);
				p = p.relative(direction);
				fillCuboid(world, placer, random, p, this.leavesProvider, 0, 1, 0);
			}
		}

		if (random.nextFloat() < this.diagonalTopPetalChance) {
			// corner leaves
			for (Direction direction : SpecterMath.HORIZONTAL_DIRECTIONS) {
				Direction perpDir = direction.getClockWise(Direction.Axis.Y);
				BlockPos p = pos.offset(0, y, 0).relative(direction, radius - 1).relative(perpDir, radius - 1);

				fillCuboid(world, placer, random, p, this.leavesProvider, 0, 2, 0);

				boolean tallerSide = random.nextBoolean();
				fillCuboid(world, placer, random, p.relative(direction), this.leavesProvider, 0, tallerSide ? 1 : 0, 0);
				fillCuboid(world, placer, random, p.relative(perpDir), this.leavesProvider, 0, tallerSide ? 0 : 1, 0);
			}
		}
	}

	@Override
	public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
		return 0;
	}

	@Override
	protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
		return false;
	}
}
