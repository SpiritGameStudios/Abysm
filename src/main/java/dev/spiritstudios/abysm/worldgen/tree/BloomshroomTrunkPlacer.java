package dev.spiritstudios.abysm.worldgen.tree;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.specter.api.core.math.SpecterMath;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class BloomshroomTrunkPlacer extends TrunkPlacer {
	public static final MapCodec<BloomshroomTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
		instance -> fillTrunkPlacerFields(instance)
			.and(instance.group(
				IntProvider.VALUE_CODEC.fieldOf("petal_offset").forGetter(placer -> placer.petalOffset),
				BlockStateProvider.TYPE_CODEC.fieldOf("petal_provider").forGetter(placer -> placer.petalProvider)
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
	protected TrunkPlacerType<?> getType() {
		return AbysmTrunkPlacerTypes.BLOOMSHROOM;
	}

	protected void getAndSetPetal(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, BlockPos pos) {
		if (!this.canReplace(world, pos)) return;

		replacer.accept(
			pos,
			petalProvider.get(random, pos)
				.withIfExists(Properties.WATERLOGGED,
					world.testFluidState(pos, fluidState -> fluidState.isEqualAndStill(Fluids.WATER)))
		);
	}

	@Override
	public List<FoliagePlacer.TreeNode> generate(
		TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config
	) {
		// trunk
		for (int i = 0; i < height; i++) {
			this.getAndSetState(world, replacer, random, startPos.up(i), config);
		}

		for (Direction direction : SpecterMath.HORIZONTAL_DIRECTIONS) {
			BlockPos p = startPos.offset(direction);
			if (this.getAndSetState(world, replacer, random, p, config)) {
				int downAmount = 1 + random.nextInt(2);
				for (int i = 1; i <= downAmount; i++) {
					boolean placed = this.getAndSetState(world, replacer, random, p.down(i), config);
					if (!placed) break;
				}

				int upAmount = random.nextInt(2);
				for (int i = 1; i <= upAmount; i++) {
					boolean placed = this.getAndSetState(world, replacer, random, p.up(i), config);
					if (!placed) break;
				}
			}
		}

		// leaves
		BlockPos petalStart = startPos.up(height - petalOffset.get(random));

		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				if (dx == 0 && dz == 0) continue;

				boolean isCorner = dx != 0 && dz != 0;
				if (!isCorner || random.nextFloat() < 0.4F) {
					this.getAndSetPetal(world, replacer, random, petalStart.add(dx, 0, dz));
				}
				if (!isCorner && random.nextFloat() < 0.3F) {
					this.getAndSetPetal(world, replacer, random, petalStart.add(dx, -1, dz));
				}
			}
		}

		return ImmutableList.of(new FoliagePlacer.TreeNode(startPos.up(height), 0, false));
	}
}
