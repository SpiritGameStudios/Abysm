package dev.spiritstudios.abysm.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.property.Properties;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class UnderwaterVegetationFeature extends Feature<UnderwaterVegetationFeature.Config> {
	public UnderwaterVegetationFeature(Codec<UnderwaterVegetationFeature.Config> codec) {
		super(codec);
	}

	@Override
	public boolean generate(FeatureContext<UnderwaterVegetationFeature.Config> context) {
		StructureWorldAccess world = context.getWorld();
		BlockPos pos = context.getOrigin();
		BlockState state = world.getBlockState(pos.down());
		UnderwaterVegetationFeature.Config config = context.getConfig();
		Random random = context.getRandom();

		if (!state.isIn(config.validSurface)) {
			return false;
		} else {
			int i = pos.getY();
			if (i >= world.getBottomY() + 1 && i + 1 <= world.getTopYInclusive()) {
				int placedBlocks = 0;

				for (int k = 0; k < config.spreadWidth * config.spreadWidth; k++) {
					BlockPos targetPos = pos.add(
						random.nextInt(config.spreadWidth) - random.nextInt(config.spreadWidth),
						random.nextInt(config.spreadHeight) - random.nextInt(config.spreadHeight),
						random.nextInt(config.spreadWidth) - random.nextInt(config.spreadWidth)
					);
					BlockState newState = config.stateProvider.get(random, targetPos);
					if (tryPlaceBlock(targetPos, world, newState)) {
						placedBlocks++;
					}
				}

				return placedBlocks > 0;
			} else {
				return false;
			}
		}
	}

	protected boolean tryPlaceBlock(BlockPos targetPos, StructureWorldAccess world, BlockState newState) {
		BlockState targetState = world.getBlockState(targetPos);
		boolean targetWater = targetState.isOf(Blocks.WATER);
		if ((targetState.isAir() || targetWater) && targetPos.getY() > world.getBottomY()) {
			if (newState.canPlaceAt(world, targetPos)) {
				BlockState actualNewState = newState.withIfExists(Properties.WATERLOGGED, targetWater);

				if (newState.getBlock() instanceof TallPlantBlock) {
					BlockPos upPos = targetPos.up();
					BlockState upState = world.getBlockState(upPos);
					boolean upWater = upState.isOf(Blocks.WATER);
					if (upState.isAir() || upWater) {
						BlockState actualNewStateTop = newState.withIfExists(Properties.WATERLOGGED, upWater).withIfExists(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);

						world.setBlockState(targetPos, actualNewState, Block.NOTIFY_LISTENERS);
						world.setBlockState(upPos, actualNewStateTop, Block.NOTIFY_LISTENERS);
						return true;
					}
				} else {
					world.setBlockState(targetPos, actualNewState, Block.NOTIFY_LISTENERS);
					return true;
				}
			}
		}

		return false;
	}

	public record Config(TagKey<Block> validSurface, BlockStateProvider stateProvider, int spreadWidth,
						 int spreadHeight) implements FeatureConfig {
		public static final Codec<UnderwaterVegetationFeature.Config> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					TagKey.codec(RegistryKeys.BLOCK).fieldOf("valid_surface").forGetter(config -> config.validSurface),
					BlockStateProvider.TYPE_CODEC.fieldOf("state_provider").forGetter(config -> config.stateProvider),
					Codecs.POSITIVE_INT.fieldOf("spread_width").forGetter(config -> config.spreadWidth),
					Codecs.POSITIVE_INT.fieldOf("spread_height").forGetter(config -> config.spreadHeight)
				)
				.apply(instance, UnderwaterVegetationFeature.Config::new)
		);
	}
}
