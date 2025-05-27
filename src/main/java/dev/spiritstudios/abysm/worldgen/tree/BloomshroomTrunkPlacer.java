package dev.spiritstudios.abysm.worldgen.tree;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.registry.AbysmTrunkPlacerTypes;
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

	protected void getAndSetPetal(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, BlockPos pos, Direction direction) {
		pos =  pos.offset(direction);
		if (!this.canReplace(world, pos)) return;

		replacer.accept(
			pos,
			petalProvider.get(random, pos)
				.withIfExists(Properties.WATERLOGGED,
					world.testFluidState(pos, fluidState -> fluidState.isEqualAndStill(Fluids.WATER)))
				.withIfExists(Properties.HORIZONTAL_FACING, direction)
				.withIfExists(Properties.NORTH, direction == Direction.SOUTH)
				.withIfExists(Properties.SHORT, direction == Direction.NORTH)
				.withIfExists(Properties.EAST, direction == Direction.WEST)
				.withIfExists(Properties.WEST, direction == Direction.EAST)
		);

	}

	@Override
	public List<FoliagePlacer.TreeNode> generate(
		TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config
	) {
		for (int i = 0; i < height; i++) {
			this.getAndSetState(world, replacer, random, startPos.up(i), config);
		}

		BlockPos petalStart = startPos.up(height - petalOffset.get(random));

		for (Direction direction : SpecterMath.HORIZONTAL_DIRECTIONS) {
			this.getAndSetPetal(world, replacer, random, petalStart, direction);
		}

		return ImmutableList.of(new FoliagePlacer.TreeNode(startPos.up(height), 0, false));
	}
}
