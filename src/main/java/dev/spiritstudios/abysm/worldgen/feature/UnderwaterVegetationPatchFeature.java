package dev.spiritstudios.abysm.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.worldgen.feature.UnderwaterVegetationPatchFeature.Config;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class UnderwaterVegetationPatchFeature extends Feature<Config> {
	// this is mostly just a modified version of VegetationPatchFeature, but does not extend it since it needs a different config
	public UnderwaterVegetationPatchFeature(Codec<Config> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<Config> context) {
		// same as original
		WorldGenLevel structureWorldAccess = context.level();
		Config vegetationPatchFeatureConfig = context.config();
		RandomSource random = context.random();
		BlockPos blockPos = context.origin();
		Predicate<BlockState> predicate = state -> state.is(vegetationPatchFeatureConfig.replaceable);
		int radiusX = vegetationPatchFeatureConfig.horizontalRadius.sample(random) + 1;
		int radiusZ = vegetationPatchFeatureConfig.horizontalRadius.sample(random) + 1;

		Set<BlockPos> set = this.placeGroundAndGetPositions(structureWorldAccess, vegetationPatchFeatureConfig, random, blockPos, predicate, radiusX, radiusZ);
		this.generateVegetation(context, structureWorldAccess, vegetationPatchFeatureConfig, random, set);

		return !set.isEmpty();
	}

	protected Set<BlockPos> placeGroundAndGetPositions(WorldGenLevel world, Config config, RandomSource random, BlockPos pos, Predicate<BlockState> replaceable, int radiusX, int radiusZ) {
		// mostly same as original, except tidied, and replaces air checks with air-or-water checks
		Direction surfaceDirection = config.surface.getDirection();
		Direction oppositeDirection = surfaceDirection.getOpposite();
		TagKey<Block> treatAsAir = config.treatAsAir;
		Predicate<BlockState> isAirPredicate = state -> this.treatAsAir(state, treatAsAir);
		Predicate<BlockState> isNotAirPredicate = isAirPredicate.negate();

		Set<BlockPos> placedBlockPositions = new HashSet<>();

		BlockPos.MutableBlockPos mutable = pos.mutable();
		// iterate over x, z in radius
		for (int dx = -radiusX; dx <= radiusX; dx++) {
			boolean xOnEdge = dx == -radiusX || dx == radiusX;

			for (int dz = -radiusZ; dz <= radiusZ; dz++) {
				boolean zOnEdge = dz == -radiusZ || dz == radiusZ;

				boolean onEdge = xOnEdge || zOnEdge;
				boolean onCorner = xOnEdge && zOnEdge;
				boolean onEdgeNotCorner = onEdge && !onCorner;

				// do not place on corners, only sometimes place on the outer edge
				if (!onCorner && (!onEdgeNotCorner || config.extraEdgeColumnChance != 0.0F && !(random.nextFloat() > config.extraEdgeColumnChance))) {
					mutable.setWithOffset(pos, dx, 0, dz);

					// move towards surface until inside a block, or range is exceeded
					for (int k = 0; world.isStateAtPosition(mutable, isAirPredicate) && k < config.verticalRange; k++) {
						mutable.move(surfaceDirection);
					}

					// move away from surface until not inside a block, or range is exceeded
					for (int k = 0; world.isStateAtPosition(mutable, isNotAirPredicate) && k < config.verticalRange; k++) {
						mutable.move(oppositeDirection);
					}

					// check the position is in fact not inside a block
					if (world.isStateAtPosition(mutable, isAirPredicate)) {
						// move back into what should hopefully be a block
						mutable.move(surfaceDirection);
						BlockState blockState = world.getBlockState(mutable);
						// check that the position is in fact inside a block now, and a sufficiently solid one
						if (blockState.isFaceSturdy(world, mutable, oppositeDirection)) {
							// place ground from this position
							BlockPos blockPos = mutable.immutable();
							int depth = config.depth.sample(random) + (config.extraBottomBlockChance > 0.0F && random.nextFloat() < config.extraBottomBlockChance ? 1 : 0);
							boolean placedGround = this.placeGround(world, config, replaceable, random, mutable, depth);

							if (placedGround) {
								placedBlockPositions.add(blockPos);
							}
						}
					}
				}
			}
		}

		return placedBlockPositions;
	}

	protected boolean treatAsAir(BlockState state, TagKey<Block> treatAsAir) {
		// return if the state is "air"
		return state.is(treatAsAir);
	}

	protected boolean placeGround(WorldGenLevel world, Config config, Predicate<BlockState> replaceable, RandomSource random, BlockPos.MutableBlockPos mutable, int depth) {
		// same as original
		for (int i = 0; i < depth; i++) {
			BlockState newState = config.groundState.getState(random, mutable);
			BlockState oldState = world.getBlockState(mutable);
			if (!newState.is(oldState.getBlock())) {
				if (!replaceable.test(oldState)) {
					return i != 0;
				}

				if (config.onlyPlaceWhenExposed) {
					// get state above or below
					mutable.move(config.surface.getDirection().getOpposite());
					BlockState aboveState = world.getBlockState(mutable);
					mutable.move(config.surface.getDirection());

					// only place on surface
					if (!treatAsAir(aboveState, config.treatAsAir)) {
						return i != 0;
					}
				}

				world.setBlock(mutable, newState, Block.UPDATE_CLIENTS);
				mutable.move(config.surface.getDirection());
			}
		}

		return true;
	}

	protected void generateVegetation(
		FeaturePlaceContext<Config> context,
		WorldGenLevel world,
		Config config,
		RandomSource random,
		Set<BlockPos> positions
	) {
		// same as original, except without the unused radius fields
		for (BlockPos blockPos : positions) {
			if (config.vegetationChance > 0.0F && random.nextFloat() < config.vegetationChance) {
				this.generateVegetationFeature(world, config, context.chunkGenerator(), random, blockPos);
			}
		}
	}

	protected boolean generateVegetationFeature(
		WorldGenLevel world, Config config, ChunkGenerator generator, RandomSource random, BlockPos pos
	) {
		// same as original
		return config.vegetationFeature.value().place(world, generator, random, pos.relative(config.surface.getDirection().getOpposite()));
	}

	public record Config(
		TagKey<Block> treatAsAir,
		TagKey<Block> replaceable,
		BlockStateProvider groundState,
		Holder<PlacedFeature> vegetationFeature,
		CaveSurface surface,
		IntProvider depth,
		float extraBottomBlockChance,
		int verticalRange,
		float vegetationChance,
		IntProvider horizontalRadius,
		float extraEdgeColumnChance,
		boolean onlyPlaceWhenExposed
	) implements FeatureConfiguration {
		public static final Codec<Config> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					TagKey.hashedCodec(Registries.BLOCK).fieldOf("treat_as_air").forGetter(config -> config.treatAsAir),
					TagKey.hashedCodec(Registries.BLOCK).fieldOf("replaceable").forGetter(config -> config.replaceable),
					BlockStateProvider.CODEC.fieldOf("ground_state").forGetter(config -> config.groundState),
					PlacedFeature.CODEC.fieldOf("vegetation_feature").forGetter(config -> config.vegetationFeature),
					CaveSurface.CODEC.fieldOf("surface").forGetter(config -> config.surface),
					IntProvider.codec(1, 128).fieldOf("depth").forGetter(config -> config.depth),
					Codec.floatRange(0.0F, 1.0F).fieldOf("extra_bottom_block_chance").forGetter(config -> config.extraBottomBlockChance),
					Codec.intRange(1, 256).fieldOf("vertical_range").forGetter(config -> config.verticalRange),
					Codec.floatRange(0.0F, 1.0F).fieldOf("vegetation_chance").forGetter(config -> config.vegetationChance),
					IntProvider.CODEC.fieldOf("xz_radius").forGetter(config -> config.horizontalRadius),
					Codec.floatRange(0.0F, 1.0F).fieldOf("extra_edge_column_chance").forGetter(config -> config.extraEdgeColumnChance),
					Codec.BOOL.fieldOf("only_place_when_exposed").forGetter(config -> config.onlyPlaceWhenExposed)
				)
				.apply(instance, Config::new)
		);
	}
}
