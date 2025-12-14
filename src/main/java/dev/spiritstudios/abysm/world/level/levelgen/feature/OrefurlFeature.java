package dev.spiritstudios.abysm.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.world.level.block.OrefurlBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class OrefurlFeature extends Feature<OrefurlFeature.Config> {

	public OrefurlFeature(Codec<Config> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean place(FeaturePlaceContext<Config> context) {
		WorldGenLevel world = context.level();
		BlockPos originPos = context.origin();
		RandomSource random = context.random();
		Config config = context.config();

		BlockPos targetPos = originPos;

		if (!world.getBlockState(targetPos).is(Blocks.WATER)) {
			return false;
		}

		int height = config.maxHeightProvider.sample(random);
		int placed = 0;
		for (int j = 0; j < height; j++) {
			BlockState orefurl = config.orefurlStateProvider.getState(random, originPos);
			BlockState orefurlPlant = config.orefurlPlantStateProvider.getState(random, originPos);

			if (world.getBlockState(targetPos).is(Blocks.WATER)
				&& world.getBlockState(targetPos.above()).is(Blocks.WATER)
				&& orefurlPlant.canSurvive(world, targetPos)) {
				// place block at this y level
				if (j + 1 == height) {
					// place tip
					world.setBlock(targetPos, orefurl.trySetValue(OrefurlBlock.AGE, random.nextInt(4) + 20), Block.UPDATE_CLIENTS);
					placed++;
				} else {
					// place body
					world.setBlock(targetPos, orefurlPlant, Block.UPDATE_CLIENTS);
				}
			} else if (j != 0) {
				// replace block at y level below with a tip
				BlockPos downPos = targetPos.below();
				if (orefurl.canSurvive(world, downPos)) {
					world.setBlock(downPos, orefurl.trySetValue(OrefurlBlock.AGE, random.nextInt(4) + 20), Block.UPDATE_CLIENTS);
					placed++;
				}
				break;
			}

			targetPos = targetPos.above();
		}

		return placed > 0;
	}

	public record Config(BlockStateProvider orefurlStateProvider, BlockStateProvider orefurlPlantStateProvider,
						 IntProvider maxHeightProvider) implements FeatureConfiguration {
		public static final Codec<OrefurlFeature.Config> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					BlockStateProvider.CODEC.fieldOf("orefurl_provider").forGetter(OrefurlFeature.Config::orefurlStateProvider),
					BlockStateProvider.CODEC.fieldOf("orefurl_plant_provider").forGetter(OrefurlFeature.Config::orefurlPlantStateProvider),
					IntProvider.POSITIVE_CODEC.fieldOf("max_height").forGetter(OrefurlFeature.Config::maxHeightProvider)
				)
				.apply(instance, OrefurlFeature.Config::new)
		);
	}
}
