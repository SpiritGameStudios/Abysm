package dev.spiritstudios.abysm.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class HangingLanternFeature extends Feature<HangingLanternFeature.Config> {

	public HangingLanternFeature(Codec<Config> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(FeatureContext<Config> context) {
		Config config = context.getConfig();
		StructureWorldAccess world = context.getWorld();
		BlockPos pos = context.getOrigin();
		Random random = context.getRandom();

		// do not place if supporting block cannot actually support
		if(!stateCanSupportChain(world.getBlockState(pos.up()), world, pos.up())) {
			return false;
		}

		// check how far down the lantern can hang
		int maxLength = config.maxLength.get(random);
		int length = 0;
		for(int i = 0; i < maxLength; i++) {
			BlockPos checkPos = pos.down(i);
			BlockState checkState = world.getBlockState(checkPos);
			if(canReplaceState(checkState)) {
				length++;
			} else {
				break;
			}
		}

		int emptySpaceBelowLantern = 0;
		int requiredSpaceBelowLantern = config.spaceToFloor.get(random);
		for(int i = 0; i < requiredSpaceBelowLantern; i++) {
			BlockPos checkPos = pos.down(length + i);
			BlockState checkState = world.getBlockState(checkPos);
			if(!checkState.isSolidBlock(world, checkPos)) {
				emptySpaceBelowLantern++;
			} else {
				break;
			}
		}
		length -= (requiredSpaceBelowLantern - emptySpaceBelowLantern);

		// do not place if no blocks would get placed
		if(length == 0) {
			return false;
		}

		// place lantern with chain
		for(int i = 0; i < length; i++) {
			BlockPos placePos = pos.down(i);
			BlockStateProvider provider = (i + 1 == length) ? (config.lanternStateProvider) : (config.chainStateProvider);
			BlockState placeState = provider.get(context.getRandom(), pos);
			placeWithWaterIfPresent(placeState, placePos, world);
		}

		return true;
	}

	private boolean canReplaceState(BlockState state) {
		return state.isReplaceable();
	}

	private boolean stateCanSupportChain(BlockState state, BlockView world, BlockPos pos) {
		return state.isSideSolidFullSquare(world, pos, Direction.DOWN);
	}

	private void placeWithWaterIfPresent(BlockState state, BlockPos pos, StructureWorldAccess world) {
		FluidState fluidState = world.getFluidState(pos);
		if(fluidState.isIn(FluidTags.WATER)) {
			state = state.withIfExists(Properties.WATERLOGGED, true);
		}
		world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);
	}

	public record Config(BlockStateProvider lanternStateProvider, BlockStateProvider chainStateProvider, IntProvider maxLength, IntProvider spaceToFloor) implements FeatureConfig {
		public static final Codec<Config> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					BlockStateProvider.TYPE_CODEC.fieldOf("lantern_provider").forGetter(Config::lanternStateProvider),
					BlockStateProvider.TYPE_CODEC.fieldOf("chain_provider").forGetter(Config::chainStateProvider),
					IntProvider.POSITIVE_CODEC.fieldOf("max_length").forGetter(Config::maxLength),
					IntProvider.POSITIVE_CODEC.fieldOf("space_to_floor").forGetter(Config::spaceToFloor)
				)
				.apply(instance, Config::new)
		);
	}
}
