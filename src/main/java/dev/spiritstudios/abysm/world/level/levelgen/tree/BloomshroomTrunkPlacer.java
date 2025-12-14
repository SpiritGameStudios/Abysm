package dev.spiritstudios.abysm.world.level.levelgen.tree;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.spectre.api.core.math.SpectreMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.function.BiConsumer;

public class BloomshroomTrunkPlacer extends TrunkPlacer {
	public static final MapCodec<BloomshroomTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
		instance -> trunkPlacerParts(instance)
			.and(instance.group(
				IntProvider.CODEC.fieldOf("petal_offset").forGetter(placer -> placer.petalOffset),
				BlockStateProvider.CODEC.fieldOf("petal_provider").forGetter(placer -> placer.petalProvider)
			))
			.apply(instance, BloomshroomTrunkPlacer::new)
	);

	private final IntProvider petalOffset;
	private final BlockStateProvider petalProvider;

	public BloomshroomTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight, IntProvider petalOffset, BlockStateProvider petalProvider) {
		super(baseHeight, firstRandomHeight, secondRandomHeight);
		this.petalOffset = petalOffset;
		this.petalProvider = petalProvider;
	}

	@Override
	protected TrunkPlacerType<?> type() {
		return AbysmTrunkPlacerTypes.BLOOMSHROOM;
	}

	protected void getAndSetPetal(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, BlockPos pos) {
		if (!this.validTreePos(world, pos)) return;

		replacer.accept(
			pos,
			petalProvider.getState(random, pos)
				.trySetValue(BlockStateProperties.WATERLOGGED,
					world.isFluidAtPosition(pos, fluidState -> fluidState.isSourceOfType(Fluids.WATER)))
		);
	}

	@Override
	public List<FoliagePlacer.FoliageAttachment> placeTrunk(
		LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, int height, BlockPos startPos, TreeConfiguration config
	) {
		// trunk
		for (int i = 0; i < height; i++) {
			this.placeLog(world, replacer, random, startPos.above(i), config);
		}

		for (Direction direction : SpectreMath.HORIZONTAL_DIRECTIONS) {
			BlockPos p = startPos.relative(direction);
			if (this.placeLog(world, replacer, random, p, config)) {
				int downAmount = 1 + random.nextInt(2);
				for (int i = 1; i <= downAmount; i++) {
					boolean placed = this.placeLog(world, replacer, random, p.below(i), config);
					if (!placed) break;
				}

				int upAmount = random.nextInt(2);
				for (int i = 1; i <= upAmount; i++) {
					boolean placed = this.placeLog(world, replacer, random, p.above(i), config);
					if (!placed) break;
				}
			}
		}

		// leaves
		BlockPos petalStart = startPos.above(height - petalOffset.sample(random));

		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				if (dx == 0 && dz == 0) continue;

				boolean isCorner = dx != 0 && dz != 0;
				if (!isCorner || random.nextFloat() < 0.4F) {
					this.getAndSetPetal(world, replacer, random, petalStart.offset(dx, 0, dz));
				}
				if (!isCorner && random.nextFloat() < 0.3F) {
					this.getAndSetPetal(world, replacer, random, petalStart.offset(dx, -1, dz));
				}
			}
		}

		return ImmutableList.of(new FoliagePlacer.FoliageAttachment(startPos.above(height), 0, false));
	}
}
