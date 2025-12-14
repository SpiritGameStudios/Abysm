package dev.spiritstudios.abysm.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class UnderwaterVegetationFeature extends Feature<UnderwaterVegetationFeature.Config> {
	public UnderwaterVegetationFeature(Codec<UnderwaterVegetationFeature.Config> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<UnderwaterVegetationFeature.Config> context) {
		WorldGenLevel world = context.level();
		BlockPos pos = context.origin();
		BlockState state = world.getBlockState(pos.below());
		UnderwaterVegetationFeature.Config config = context.config();
		RandomSource random = context.random();

		if (!state.is(config.validSurface)) {
			return false;
		} else {
			int i = pos.getY();
			if (i >= world.getMinY() + 1 && i + 1 <= world.getMaxY()) {
				int placedBlocks = 0;

				for (int k = 0; k < config.spreadWidth * config.spreadWidth; k++) {
					BlockPos targetPos = pos.offset(
						random.nextInt(config.spreadWidth) - random.nextInt(config.spreadWidth),
						random.nextInt(config.spreadHeight) - random.nextInt(config.spreadHeight),
						random.nextInt(config.spreadWidth) - random.nextInt(config.spreadWidth)
					);
					BlockState newState = config.stateProvider.getState(random, targetPos);
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

	protected boolean tryPlaceBlock(BlockPos targetPos, WorldGenLevel world, BlockState newState) {
		BlockState targetState = world.getBlockState(targetPos);
		boolean targetWater = targetState.is(Blocks.WATER);
		if ((targetState.isAir() || targetWater) && targetPos.getY() > world.getMinY()) {
			if (newState.canSurvive(world, targetPos)) {
				BlockState actualNewState = newState.trySetValue(BlockStateProperties.WATERLOGGED, targetWater);

				if (newState.getBlock() instanceof DoublePlantBlock) {
					BlockPos upPos = targetPos.above();
					BlockState upState = world.getBlockState(upPos);
					boolean upWater = upState.is(Blocks.WATER);
					if (upState.isAir() || upWater) {
						BlockState actualNewStateTop = newState.trySetValue(BlockStateProperties.WATERLOGGED, upWater).trySetValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);

						world.setBlock(targetPos, actualNewState, Block.UPDATE_CLIENTS);
						world.setBlock(upPos, actualNewStateTop, Block.UPDATE_CLIENTS);
						return true;
					}
				} else {
					world.setBlock(targetPos, actualNewState, Block.UPDATE_CLIENTS);
					return true;
				}
			}
		}

		return false;
	}

	public record Config(TagKey<Block> validSurface, BlockStateProvider stateProvider, int spreadWidth,
						 int spreadHeight) implements FeatureConfiguration {
		public static final Codec<UnderwaterVegetationFeature.Config> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					TagKey.hashedCodec(Registries.BLOCK).fieldOf("valid_surface").forGetter(config -> config.validSurface),
					BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(config -> config.stateProvider),
					ExtraCodecs.POSITIVE_INT.fieldOf("spread_width").forGetter(config -> config.spreadWidth),
					ExtraCodecs.POSITIVE_INT.fieldOf("spread_height").forGetter(config -> config.spreadHeight)
				)
				.apply(instance, UnderwaterVegetationFeature.Config::new)
		);
	}
}
