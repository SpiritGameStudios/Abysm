package dev.spiritstudios.abysm.worldgen.tree;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.registry.AbysmTrunkPlacerTypes;
import dev.spiritstudios.specter.api.core.math.SpecterMath;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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
				Codec.INT.fieldOf("petal_offset").forGetter(placer -> placer.petalOffset),
				BlockStateProvider.TYPE_CODEC.fieldOf("petal_provider").forGetter(placer -> placer.petalProvider)
			))
			.apply(instance, BloomshroomTrunkPlacer::new)
	);

	private final int petalOffset;
	private final BlockStateProvider petalProvider;

	public BloomshroomTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight, int petalOffset, BlockStateProvider petalProvider) {
		super(baseHeight, firstRandomHeight, secondRandomHeight);
		this.petalOffset = petalOffset;
		this.petalProvider = petalProvider;
	}

	@Override
	protected TrunkPlacerType<?> getType() {
		return AbysmTrunkPlacerTypes.BLOOMSHROOM;
	}

	protected boolean getAndSetPetal(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, BlockPos pos, Direction direction) {
		if (!this.canReplace(world, pos)) return false;

		replacer.accept(
			pos.offset(direction),
			petalProvider.get(random, pos)
				.withIfExists(Properties.HORIZONTAL_FACING, direction)
				.withIfExists(Properties.NORTH, direction == Direction.SOUTH)
				.withIfExists(Properties.SHORT, direction == Direction.NORTH)
				.withIfExists(Properties.EAST, direction == Direction.WEST)
				.withIfExists(Properties.WEST, direction == Direction.EAST)
		);

		return true;
	}

	@Override
	public List<FoliagePlacer.TreeNode> generate(
		TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config
	) {
		for (int i = 0; i < height; i++) {
			this.getAndSetState(world, replacer, random, startPos.up(i), config);
		}

		BlockPos petalStart = startPos.up(height - petalOffset);

		for (Direction direction : SpecterMath.HORIZONTAL_DIRECTIONS) {
			this.getAndSetPetal(world, replacer, random, petalStart, direction);
		}

		return ImmutableList.of(new FoliagePlacer.TreeNode(startPos.up(height), 0, false));
	}
}
