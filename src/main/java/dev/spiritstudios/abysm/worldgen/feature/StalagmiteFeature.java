package dev.spiritstudios.abysm.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.DripstoneHelper;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import org.jetbrains.annotations.Nullable;

// mostly copied from LargeDripstoneFeature
public class StalagmiteFeature extends Feature<StalagmiteFeature.Config> {
	public StalagmiteFeature() {
		super(Config.CODEC);
	}

	@Override
	public boolean generate(FeatureContext<Config> context) {
		StructureWorldAccess world = context.getWorld();
		BlockPos pos = context.getOrigin();
		Config config = context.getConfig();
		Random random = context.getRandom();

		BlockState currentState = world.getBlockState(pos);
		if (!currentState.isAir() && !currentState.isOf(Blocks.WATER)) return false;

		int oceanFloor = world.getTopY(Heightmap.Type.OCEAN_FLOOR, pos);

		int i = (int) ((world.getSeaLevel() - oceanFloor) * config.maxColumnRadiusToCaveHeightRatio);
		int j = MathHelper.clamp(i, config.columnRadius.getMin(), config.columnRadius.getMax());
		int k = MathHelper.nextBetween(random, config.columnRadius.getMin(), j);

		StalagmiteGenerator stalagmite = createGenerator(
			config.stateProvider,
			pos.withY(world.getTopY(Heightmap.Type.OCEAN_FLOOR, pos) + 1), random, k, config.bluntness, config.heightScale
		);
		WindModifier windModifier = stalagmite.generateWind(config) ?
			new WindModifier(pos.getY(), random, config.windSpeed) :
			WindModifier.create();

		if (stalagmite.canGenerate(world, windModifier)) stalagmite.generate(world, random, windModifier);

		return true;
	}

	private static StalagmiteGenerator createGenerator(
		BlockStateProvider stateProvider, BlockPos pos, Random random, int scale, FloatProvider bluntness, FloatProvider heightScale
	) {
		return new StalagmiteGenerator(stateProvider, pos, scale, bluntness.get(random), heightScale.get(random));
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

		private static boolean canGenerateOrLava(WorldAccess world, BlockPos pos) {
			return world.testBlockState(pos, DripstoneHelper::canGenerateOrLava);
		}

		private static boolean canGenerateBase(StructureWorldAccess world, BlockPos pos, int height) {
			if (canGenerateOrLava(world, pos)) {
				return false;
			} else {
				float g = 6.0F / height;

				for (float theta = 0.0F; theta < MathHelper.PI * 2; theta += g) {
					int i = (int) (MathHelper.cos(theta) * height);
					int j = (int) (MathHelper.sin(theta) * height);
					if (canGenerateOrLava(world, pos.add(i, 0, j))) {
						return false;
					}
				}

				return true;
			}
		}

		boolean canGenerate(StructureWorldAccess world, WindModifier wind) {
			while (this.scale > 1) {
				BlockPos.Mutable mutable = this.pos.mutableCopy();
				int scale = Math.min(10, this.getBaseScale());

				for (int i = 0; i < scale; i++) {
					if (world.getBlockState(mutable).isOf(Blocks.LAVA)) {
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

		void generate(StructureWorldAccess world, Random random, WindModifier wind) {
			for (int x = -this.scale; x <= this.scale; x++) {
				for (int z = -this.scale; z <= this.scale; z++) {
					float horizontalLength = MathHelper.sqrt(x * x + z * z);
					if (horizontalLength > this.scale) continue;

					int scale = this.scale(horizontalLength);
					if (scale <= 0) continue;

					if (random.nextFloat() < 0.2) {
						scale = (int) (scale * MathHelper.nextBetween(random, 0.8F, 1.0F));
					}

					BlockPos.Mutable pos = this.pos.add(x, 0, z).mutableCopy();
					boolean bl = false;
					int l = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, pos.getX(), pos.getZ());

					for (int m = 0; m < scale && pos.getY() < l; m++) {
						BlockPos blockPos = wind.modify(pos);
						BlockState state = world.getBlockState(blockPos);
						if (state.isAir() || state.isOf(Blocks.WATER) || state.isOf(Blocks.LAVA)) {
							bl = true;
							world.setBlockState(blockPos, stateProvider.get(random, blockPos), Block.NOTIFY_LISTENERS);
						} else if (bl && state.isIn(BlockTags.BASE_STONE_OVERWORLD)) break;

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
		private final Vec3d wind;

		WindModifier(int y, Random random, FloatProvider wind) {
			this.y = y;
			float windFactor = wind.get(random);
			float theta = MathHelper.nextBetween(random, 0.0F, MathHelper.PI);
			this.wind = new Vec3d(MathHelper.cos(theta) * windFactor, 0.0, MathHelper.sin(theta) * windFactor);
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

			Vec3d windFactor = this.wind.multiply(this.y - pos.getY());
			return pos.add(MathHelper.floor(windFactor.x), 0, MathHelper.floor(windFactor.z));
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
		float maxColumnRadiusToCaveHeightRatio
	) implements FeatureConfig {
		public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BlockStateProvider.TYPE_CODEC.fieldOf("state").forGetter(Config::stateProvider),
			FloatProvider.createValidatedCodec(0.0F, 2.0F).fieldOf("wind_speed").forGetter(Config::windSpeed),
			FloatProvider.createValidatedCodec(0.1F, 10.0F).fieldOf("bluntness").forGetter(Config::bluntness),
			FloatProvider.createValidatedCodec(0.0F, 20.0F).fieldOf("height_scale").forGetter(Config::heightScale),
			Codec.intRange(0, 100).fieldOf("min_radius_for_wind").forGetter(Config::minRadiusForWind),
			Codec.floatRange(0.0F, 5.0F).fieldOf("min_bluntness_for_wind").forGetter(Config::minBluntnessForWind),
			IntProvider.createValidatingCodec(1, 60).fieldOf("column_radius").forGetter(Config::columnRadius),
			Codec.floatRange(0.1F, 1.0F).fieldOf("max_column_radius_to_cave_height_ratio").forGetter(Config::maxColumnRadiusToCaveHeightRatio)
		).apply(instance, Config::new));
	}
}
