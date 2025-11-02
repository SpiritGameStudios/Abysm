package dev.spiritstudios.abysm.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

// mostly copied from LargeDripstoneFeature
public class StalagmiteFeature extends Feature<StalagmiteFeature.Config> {
	public StalagmiteFeature() {
		super(Config.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<Config> context) {
		WorldGenLevel world = context.level();
		BlockPos pos = context.origin();
		Config config = context.config();
		RandomSource random = context.random();

		BlockState currentState = world.getBlockState(pos);
		if (!currentState.isAir() && !currentState.is(Blocks.WATER)) return false;

		int oceanFloor = world.getHeight(Heightmap.Types.OCEAN_FLOOR, pos);

		int i = (int) ((world.getSeaLevel() - oceanFloor) * config.maxColumnRadiusToCaveHeightRatio);
		int j = Mth.clamp(i, config.columnRadius.getMinValue(), config.columnRadius.getMaxValue());
		int k = Mth.randomBetweenInclusive(random, config.columnRadius.getMinValue(), j);

		StalagmiteGenerator stalagmite = createGenerator(
			config.stateProvider,
			pos.atY(world.getHeight(Heightmap.Types.OCEAN_FLOOR, pos) + 1), random, k, config.bluntness, config.heightScale
		);
		WindModifier windModifier = stalagmite.generateWind(config) ?
			new WindModifier(pos.getY(), random, config.windSpeed) :
			WindModifier.create();

		if (stalagmite.canGenerate(world, windModifier))
			stalagmite.generate(world, random, windModifier, config.maxHeightAboveWorldSurface.sample(random));

		return true;
	}

	private static StalagmiteGenerator createGenerator(
		BlockStateProvider stateProvider, BlockPos pos, RandomSource random, int scale, FloatProvider bluntness, FloatProvider heightScale
	) {
		return new StalagmiteGenerator(stateProvider, pos, scale, bluntness.sample(random), heightScale.sample(random));
	}


	static final class StalagmiteGenerator {
		private final BlockStateProvider stateProvider;
		private BlockPos pos;
		private int scale;
		private final float bluntness;
		private final float heightScale;

		StalagmiteGenerator(BlockStateProvider stateProvider, BlockPos pos, int scale, float bluntness, float heightScale) {
			this.stateProvider = stateProvider;
			this.pos = pos;
			this.scale = scale;
			this.bluntness = bluntness;
			this.heightScale = heightScale;
		}

		private int getBaseScale() {
			return this.scale(0.0F);
		}

		private static boolean canGenerateOrLava(LevelAccessor world, BlockPos pos) {
			return world.isStateAtPosition(pos, DripstoneUtils::isEmptyOrWaterOrLava);
		}

		private static boolean canGenerateBase(WorldGenLevel world, BlockPos pos, int height) {
			if (canGenerateOrLava(world, pos)) {
				return false;
			} else {
				float g = 6.0F / height;

				for (float theta = 0.0F; theta < Mth.PI * 2; theta += g) {
					int i = (int) (Mth.cos(theta) * height);
					int j = (int) (Mth.sin(theta) * height);
					if (canGenerateOrLava(world, pos.offset(i, 0, j))) {
						return false;
					}
				}

				return true;
			}
		}

		boolean canGenerate(WorldGenLevel world, WindModifier wind) {
			while (this.scale > 1) {
				BlockPos.MutableBlockPos mutable = this.pos.mutable();
				int scale = Math.min(10, this.getBaseScale());

				for (int i = 0; i < scale; i++) {
					if (world.getBlockState(mutable).is(Blocks.LAVA)) {
						return false;
					}

					if (canGenerateBase(world, wind.modify(mutable), this.scale)) {
						this.pos = mutable;
						return true;
					}

					mutable.move(Direction.DOWN);
				}

				this.scale /= 2;
			}

			return false;
		}

		private int scale(float height) {
			if (height < bluntness) {
				height = bluntness;
			}

			double e = height / scale * 0.384;
			double f = 0.75 * Math.pow(e, (4.0 / 3.0));
			double g = Math.pow(e, (2.0 / 3.0));
			double h = (1.0 / 3.0) * Math.log(e);
			double i = heightScale * (f - g - h);
			i = Math.max(i, 0.0);
			return (int) (i / 0.384F * scale);
		}

		void generate(WorldGenLevel world, RandomSource random, WindModifier wind, int maxHeightAboveWorldSurface) {
			for (int x = -this.scale; x <= this.scale; x++) {
				for (int z = -this.scale; z <= this.scale; z++) {
					float horizontalLength = Mth.sqrt(x * x + z * z);
					if (horizontalLength > this.scale) continue;

					int scale = this.scale(horizontalLength);
					if (scale <= 0) continue;

					if (random.nextFloat() < 0.2) {
						scale = (int) (scale * Mth.randomBetween(random, 0.8F, 1.0F));
					}

					BlockPos.MutableBlockPos pos = this.pos.offset(x, 0, z).mutable();
					boolean bl = false;
					int l = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ()) + maxHeightAboveWorldSurface;

					for (int m = 0; m < scale && pos.getY() < l; m++) {
						BlockPos blockPos = wind.modify(pos);
						BlockState state = world.getBlockState(blockPos);
						if (state.isAir() || state.is(Blocks.WATER) || state.is(Blocks.LAVA)) {
							bl = true;
							world.setBlock(blockPos, stateProvider.getState(random, blockPos), Block.UPDATE_CLIENTS);
						} else if (bl && state.is(BlockTags.BASE_STONE_OVERWORLD)) break;

						pos.move(Direction.UP);
					}
				}
			}
		}

		boolean generateWind(Config config) {
			return this.scale >= config.minRadiusForWind && this.bluntness >= config.minBluntnessForWind;
		}
	}

	static final class WindModifier {
		private final int y;
		@Nullable
		private final Vec3 wind;

		WindModifier(int y, RandomSource random, FloatProvider wind) {
			this.y = y;
			float windFactor = wind.sample(random);
			float theta = Mth.randomBetween(random, 0.0F, Mth.PI);
			this.wind = new Vec3(Mth.cos(theta) * windFactor, 0.0, Mth.sin(theta) * windFactor);
		}

		private WindModifier() {
			this.y = 0;
			this.wind = null;
		}

		static WindModifier create() {
			return new WindModifier();
		}

		BlockPos modify(BlockPos pos) {
			if (this.wind == null) return pos;

			Vec3 windFactor = this.wind.scale(this.y - pos.getY());
			return pos.offset(Mth.floor(windFactor.x), 0, Mth.floor(windFactor.z));
		}
	}


	public record Config(
		BlockStateProvider stateProvider,
		FloatProvider windSpeed,
		FloatProvider bluntness,
		FloatProvider heightScale,
		int minRadiusForWind,
		float minBluntnessForWind,
		IntProvider columnRadius,
		float maxColumnRadiusToCaveHeightRatio,
		IntProvider maxHeightAboveWorldSurface
	) implements FeatureConfiguration {
		public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BlockStateProvider.CODEC.fieldOf("state").forGetter(Config::stateProvider),
			FloatProvider.codec(0.0F, 2.0F).fieldOf("wind_speed").forGetter(Config::windSpeed),
			FloatProvider.codec(0.1F, 10.0F).fieldOf("bluntness").forGetter(Config::bluntness),
			FloatProvider.codec(0.0F, 20.0F).fieldOf("height_scale").forGetter(Config::heightScale),
			Codec.intRange(0, 100).fieldOf("min_radius_for_wind").forGetter(Config::minRadiusForWind),
			Codec.floatRange(0.0F, 5.0F).fieldOf("min_bluntness_for_wind").forGetter(Config::minBluntnessForWind),
			IntProvider.codec(1, 60).fieldOf("column_radius").forGetter(Config::columnRadius),
			Codec.floatRange(0.1F, 1.0F).fieldOf("max_column_radius_to_cave_height_ratio").forGetter(Config::maxColumnRadiusToCaveHeightRatio),
			IntProvider.codec(0, 100).fieldOf("max_height_above_world_surface").forGetter(Config::columnRadius)
		).apply(instance, Config::new));
	}
}
