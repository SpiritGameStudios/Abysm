package dev.spiritstudios.abysm.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.worldgen.feature.UnderwaterVegetationPatchFeature.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class UnderwaterVegetationPatchFeature extends Feature<Config> {
	// this is mostly just a modified version of VegetationPatchFeature, but does not extend it since it needs a different config
	public UnderwaterVegetationPatchFeature(Codec<Config> codec) {
		super(codec);
	}

	@Override
	public boolean generate(FeatureContext<Config> context) {
		// same as original
		StructureWorldAccess structureWorldAccess = context.getWorld();
		Config vegetationPatchFeatureConfig = context.getConfig();
		Random random = context.getRandom();
		BlockPos blockPos = context.getOrigin();
		Predicate<BlockState> predicate = state -> state.isIn(vegetationPatchFeatureConfig.replaceable);
		int radiusX = vegetationPatchFeatureConfig.horizontalRadius.get(random) + 1;
		int radiusZ = vegetationPatchFeatureConfig.horizontalRadius.get(random) + 1;

		Set<BlockPos> set = this.placeGroundAndGetPositions(structureWorldAccess, vegetationPatchFeatureConfig, random, blockPos, predicate, radiusX, radiusZ);
		this.generateVegetation(context, structureWorldAccess, vegetationPatchFeatureConfig, random, set);

		return !set.isEmpty();
	}

	protected Set<BlockPos> placeGroundAndGetPositions(StructureWorldAccess world, Config config, Random random, BlockPos pos, Predicate<BlockState> replaceable, int radiusX, int radiusZ) {
		// mostly same as original, except tidied, and replaces air checks with air-or-water checks
		Direction surfaceDirection = config.surface.getDirection();
		Direction oppositeDirection = surfaceDirection.getOpposite();
		TagKey<Block> treatAsAir = config.treatAsAir;
		Predicate<BlockState> isAirPredicate = state -> this.treatAsAir(state, treatAsAir);
		Predicate<BlockState> isNotAirPredicate = isAirPredicate.negate();

		Set<BlockPos> placedBlockPositions = new HashSet<>();

		BlockPos.Mutable mutable = pos.mutableCopy();
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
					mutable.set(pos, dx, 0, dz);

					// move towards surface until inside a block, or range is exceeded
					for (int k = 0; world.testBlockState(mutable, isAirPredicate) && k < config.verticalRange; k++) {
						mutable.move(surfaceDirection);
					}

					// move away from surface until not inside a block, or range is exceeded
					for (int k = 0; world.testBlockState(mutable, isNotAirPredicate) && k < config.verticalRange; k++) {
						mutable.move(oppositeDirection);
					}

					// check the position is in fact not inside a block
					if (world.testBlockState(mutable, isAirPredicate)) {
						// move back into what should hopefully be a block
						mutable.move(surfaceDirection);
						BlockState blockState = world.getBlockState(mutable);
						// check that the position is in fact inside a block now, and a sufficiently solid one
						if (blockState.isSideSolidFullSquare(world, mutable, oppositeDirection)) {
							// place ground from this position
							BlockPos blockPos = mutable.toImmutable();
							int depth = config.depth.get(random) + (config.extraBottomBlockChance > 0.0F && random.nextFloat() < config.extraBottomBlockChance ? 1 : 0);
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
		return state.isIn(treatAsAir);
	}

	protected boolean placeGround(StructureWorldAccess world, Config config, Predicate<BlockState> replaceable, Random random, BlockPos.Mutable mutable, int depth) {
		// same as original
		for (int i = 0; i < depth; i++) {
			BlockState newState = config.groundState.get(random, mutable);
			BlockState oldState = world.getBlockState(mutable);
			if (!newState.isOf(oldState.getBlock())) {
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

				world.setBlockState(mutable, newState, Block.NOTIFY_LISTENERS);
				mutable.move(config.surface.getDirection());
			}
		}

		return true;
	}

	protected void generateVegetation(
		FeatureContext<Config> context,
		StructureWorldAccess world,
		Config config,
		Random random,
		Set<BlockPos> positions
	) {
		// same as original, except without the unused radius fields
		for (BlockPos blockPos : positions) {
			if (config.vegetationChance > 0.0F && random.nextFloat() < config.vegetationChance) {
				this.generateVegetationFeature(world, config, context.getGenerator(), random, blockPos);
			}
		}
	}

	protected boolean generateVegetationFeature(
		StructureWorldAccess world, Config config, ChunkGenerator generator, Random random, BlockPos pos
	) {
		// same as original
		return config.vegetationFeature.value().generateUnregistered(world, generator, random, pos.offset(config.surface.getDirection().getOpposite()));
	}

	public record Config(
		TagKey<Block> treatAsAir,
		TagKey<Block> replaceable,
		BlockStateProvider groundState,
		RegistryEntry<PlacedFeature> vegetationFeature,
		VerticalSurfaceType surface,
		IntProvider depth,
		float extraBottomBlockChance,
		int verticalRange,
		float vegetationChance,
		IntProvider horizontalRadius,
		float extraEdgeColumnChance,
		boolean onlyPlaceWhenExposed
	) implements FeatureConfig {
		public static final Codec<Config> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					TagKey.codec(RegistryKeys.BLOCK).fieldOf("treat_as_air").forGetter(config -> config.treatAsAir),
					TagKey.codec(RegistryKeys.BLOCK).fieldOf("replaceable").forGetter(config -> config.replaceable),
					BlockStateProvider.TYPE_CODEC.fieldOf("ground_state").forGetter(config -> config.groundState),
					PlacedFeature.REGISTRY_CODEC.fieldOf("vegetation_feature").forGetter(config -> config.vegetationFeature),
					VerticalSurfaceType.CODEC.fieldOf("surface").forGetter(config -> config.surface),
					IntProvider.createValidatingCodec(1, 128).fieldOf("depth").forGetter(config -> config.depth),
					Codec.floatRange(0.0F, 1.0F).fieldOf("extra_bottom_block_chance").forGetter(config -> config.extraBottomBlockChance),
					Codec.intRange(1, 256).fieldOf("vertical_range").forGetter(config -> config.verticalRange),
					Codec.floatRange(0.0F, 1.0F).fieldOf("vegetation_chance").forGetter(config -> config.vegetationChance),
					IntProvider.VALUE_CODEC.fieldOf("xz_radius").forGetter(config -> config.horizontalRadius),
					Codec.floatRange(0.0F, 1.0F).fieldOf("extra_edge_column_chance").forGetter(config -> config.extraEdgeColumnChance),
					Codec.BOOL.fieldOf("only_place_when_exposed").forGetter(config -> config.onlyPlaceWhenExposed)
				)
				.apply(instance, Config::new)
		);
	}
}
