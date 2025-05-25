package dev.spiritstudios.abysm.worldgen.tree;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.registry.AbysmTrunkPlacerTypes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class BloomshroomTrunkPlacer extends TrunkPlacer {
	public static final MapCodec<BloomshroomTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
		instance -> fillTrunkPlacerFields(instance).apply(instance, BloomshroomTrunkPlacer::new)
	);

	public BloomshroomTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
		super(baseHeight, firstRandomHeight, secondRandomHeight);
	}

	@Override
	protected TrunkPlacerType<?> getType() {
		return AbysmTrunkPlacerTypes.BLOOMSHROOM;
	}

	@Override
	public List<FoliagePlacer.TreeNode> generate(
		TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config
	) {
		for (int i = 0; i < height; i++) {
			this.getAndSetState(world, replacer, random, startPos.up(i), config);
		}

		return ImmutableList.of(new FoliagePlacer.TreeNode(startPos.up(height), 0, false));
	}
}
