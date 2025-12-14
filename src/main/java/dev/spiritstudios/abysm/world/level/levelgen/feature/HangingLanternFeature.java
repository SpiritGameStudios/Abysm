package dev.spiritstudios.abysm.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.material.FluidState;

public class HangingLanternFeature extends Feature<HangingLanternFeature.Config> {

	public HangingLanternFeature(Codec<Config> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean place(FeaturePlaceContext<Config> context) {
		Config config = context.config();
		WorldGenLevel world = context.level();
		BlockPos pos = context.origin();
		RandomSource random = context.random();

		// do not place if supporting block cannot actually support
		if (!stateCanSupportChain(world.getBlockState(pos.above()), world, pos.above())) {
			return false;
		}

		// check how far down the lantern can hang
		int maxLength = config.maxLength.sample(random);
		int length = 0;
		for (int i = 0; i < maxLength; i++) {
			BlockPos checkPos = pos.below(i);
			BlockState checkState = world.getBlockState(checkPos);
			if (canReplaceState(checkState)) {
				length++;
			} else {
				break;
			}
		}

		int emptySpaceBelowLantern = 0;
		int requiredSpaceBelowLantern = config.spaceToFloor.sample(random);
		for (int i = 0; i < requiredSpaceBelowLantern; i++) {
			BlockPos checkPos = pos.below(length + i);
			BlockState checkState = world.getBlockState(checkPos);
			if (!checkState.isRedstoneConductor(world, checkPos)) {
				emptySpaceBelowLantern++;
			} else {
				break;
			}
		}
		length -= (requiredSpaceBelowLantern - emptySpaceBelowLantern);

		// do not place if no blocks would get placed
		if (length == 0) {
			return false;
		}

		// place lantern with chain
		for (int i = 0; i < length; i++) {
			BlockPos placePos = pos.below(i);
			BlockStateProvider provider = (i + 1 == length) ? (config.lanternStateProvider) : (config.chainStateProvider);
			BlockState placeState = provider.getState(context.random(), pos);
			placeWithWaterIfPresent(placeState, placePos, world);
		}

		return true;
	}

	private boolean canReplaceState(BlockState state) {
		return state.canBeReplaced();
	}

	private boolean stateCanSupportChain(BlockState state, BlockGetter world, BlockPos pos) {
		return state.isFaceSturdy(world, pos, Direction.DOWN);
	}

	private void placeWithWaterIfPresent(BlockState state, BlockPos pos, WorldGenLevel world) {
		FluidState fluidState = world.getFluidState(pos);
		if (fluidState.is(FluidTags.WATER)) {
			state = state.trySetValue(BlockStateProperties.WATERLOGGED, true);
		}
		world.setBlock(pos, state, Block.UPDATE_CLIENTS);
	}

	public record Config(BlockStateProvider lanternStateProvider, BlockStateProvider chainStateProvider,
						 IntProvider maxLength, IntProvider spaceToFloor) implements FeatureConfiguration {
		public static final Codec<Config> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					BlockStateProvider.CODEC.fieldOf("lantern_provider").forGetter(Config::lanternStateProvider),
					BlockStateProvider.CODEC.fieldOf("chain_provider").forGetter(Config::chainStateProvider),
					IntProvider.POSITIVE_CODEC.fieldOf("max_length").forGetter(Config::maxLength),
					IntProvider.POSITIVE_CODEC.fieldOf("space_to_floor").forGetter(Config::spaceToFloor)
				)
				.apply(instance, Config::new)
		);
	}
}
