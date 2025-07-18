package dev.spiritstudios.abysm.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.block.OrefurlBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class OrefurlFeature extends Feature<OrefurlFeature.Config> {

	public OrefurlFeature(Codec<Config> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(FeatureContext<Config> context) {
		StructureWorldAccess world = context.getWorld();
		BlockPos originPos = context.getOrigin();
		Random random = context.getRandom();
		Config config = context.getConfig();

		BlockPos targetPos = originPos;

		if (!world.getBlockState(targetPos).isOf(Blocks.WATER)) {
			return false;
		}

		int height = config.maxHeightProvider.get(random);
		int placed = 0;
		for (int j = 0; j < height; j++) {
			BlockState orefurl = config.orefurlStateProvider.get(random, originPos);
			BlockState orefurlPlant = config.orefurlPlantStateProvider.get(random, originPos);

			if (world.getBlockState(targetPos).isOf(Blocks.WATER)
				&& world.getBlockState(targetPos.up()).isOf(Blocks.WATER)
				&& orefurlPlant.canPlaceAt(world, targetPos)) {
				// place block at this y level
				if (j + 1 == height) {
					// place tip
					world.setBlockState(targetPos, orefurl.withIfExists(OrefurlBlock.AGE, random.nextInt(4) + 20), Block.NOTIFY_LISTENERS);
					placed++;
				} else {
					// place body
					world.setBlockState(targetPos, orefurlPlant, Block.NOTIFY_LISTENERS);
				}
			} else if (j != 0) {
				// replace block at y level below with a tip
				BlockPos downPos = targetPos.down();
				if (orefurl.canPlaceAt(world, downPos)) {
					world.setBlockState(downPos, orefurl.withIfExists(OrefurlBlock.AGE, random.nextInt(4) + 20), Block.NOTIFY_LISTENERS);
					placed++;
				}
				break;
			}

			targetPos = targetPos.up();
		}

		return placed > 0;
	}

	public record Config(BlockStateProvider orefurlStateProvider, BlockStateProvider orefurlPlantStateProvider,
						 IntProvider maxHeightProvider) implements FeatureConfig {
		public static final Codec<OrefurlFeature.Config> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					BlockStateProvider.TYPE_CODEC.fieldOf("orefurl_provider").forGetter(OrefurlFeature.Config::orefurlStateProvider),
					BlockStateProvider.TYPE_CODEC.fieldOf("orefurl_plant_provider").forGetter(OrefurlFeature.Config::orefurlPlantStateProvider),
					IntProvider.POSITIVE_CODEC.fieldOf("max_height").forGetter(OrefurlFeature.Config::maxHeightProvider)
				)
				.apply(instance, OrefurlFeature.Config::new)
		);
	}
}
