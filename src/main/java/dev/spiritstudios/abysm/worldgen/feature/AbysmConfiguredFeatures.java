package dev.spiritstudios.abysm.worldgen.feature;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.worldgen.tree.BloomshroomFoliagePlacer;
import dev.spiritstudios.abysm.worldgen.tree.BloomshroomTrunkPlacer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeafLitterBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

import java.util.Arrays;

public class AbysmConfiguredFeatures {
	public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_BLOOMSHROOM = ofKey("trees_bloomshroom");

	public static final RegistryKey<ConfiguredFeature<?, ?>> ROSY_BLOOMSHROOM = ofKey("rosy_bloomshroom");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SUNNY_BLOOMSHROOM = ofKey("sunny_bloomshroom");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MAUVE_BLOOMSHROOM = ofKey("mauve_bloomshroom");

	public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_SPRIGS = ofKey("patch_sprigs");
	public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_PETALS_UNDERWATER = ofKey("patch_petals_underwater");
	public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_PETALS_SURFACE = ofKey("patch_petals_surface");

	public static final RegistryKey<ConfiguredFeature<?, ?>> BLOOMSHROOM_VEGETATION = ofKey("bloomshroom_vegetation");

	public static final RegistryKey<ConfiguredFeature<?, ?>> ROSY_BLOOMSHROOM_VEGETATION_BONEMEAL = ofKey("rosy_bloomshroom_vegetation_bonemeal");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SUNNY_BLOOMSHROOM_VEGETATION_BONEMEAL = ofKey("sunny_bloomshroom_vegetation_bonemeal");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MAUVE_BLOOMSHROOM_VEGETATION_BONEMEAL = ofKey("mauve_bloomshroom_vegetation_bonemeal");

	public static final RegistryKey<ConfiguredFeature<?, ?>> FLOROPUMICE_STALAGMITES = ofKey("floropumice_stalagmites");

	public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> registerable) {
		ConfiguredFeatureHelper helper = new ConfiguredFeatureHelper(
			registerable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE),
			registerable
		);

		registerBloomshroom(
			helper,
			ROSY_BLOOMSHROOM,
			AbysmBlocks.ROSY_BLOOMSHROOM_STEM,
			AbysmBlocks.ROSY_BLOOMSHROOM_CAP,
			AbysmBlocks.ROSEBLOOM_PETALEAVES,
			AbysmBlocks.BLOOMSHROOM_GOOP,
			AbysmBlocks.BLOOMING_SODALITE_CROWN,
			3,
			0.8F,
			0.4F
		);
		registerBloomshroom(
			helper,
			SUNNY_BLOOMSHROOM,
			AbysmBlocks.SUNNY_BLOOMSHROOM_STEM,
			AbysmBlocks.SUNNY_BLOOMSHROOM_CAP,
			AbysmBlocks.SUNBLOOM_PETALEAVES,
			AbysmBlocks.BLOOMSHROOM_GOOP,
			AbysmBlocks.BLOOMING_ANYOLITE_CROWN,
			2,
			0.3F,
			0.9F
		);
		registerBloomshroom(
			helper,
			MAUVE_BLOOMSHROOM,
			AbysmBlocks.MAUVE_BLOOMSHROOM_STEM,
			AbysmBlocks.MAUVE_BLOOMSHROOM_CAP,
			AbysmBlocks.MALLOWBLOOM_PETALEAVES,
			AbysmBlocks.BLOOMSHROOM_GOOP,
			AbysmBlocks.BLOOMING_MELILITE_CROWN,
			5,
			0.2F,
			0.5F
		);

		helper.add(
			TREES_BLOOMSHROOM, Feature.RANDOM_SELECTOR,
			helper.entry(ROSY_BLOOMSHROOM, 1.0F / 3.0F),
			helper.entry(SUNNY_BLOOMSHROOM, 1.0F / 3.0F),
			helper.entry(MAUVE_BLOOMSHROOM, 1.0F / 3.0F)
		);

		helper.add(
			PATCH_SPRIGS, AbysmFeatures.SPRIGS,
			new StateProviderFeatureConfig(new WeightedBlockStateProvider(Pool.<BlockState>builder()
				.add(AbysmBlocks.ROSY_SPRIGS.getDefaultState())
				.add(AbysmBlocks.SUNNY_SPRIGS.getDefaultState())
				.add(AbysmBlocks.MAUVE_SPRIGS.getDefaultState())
			))
		);

		WeightedBlockStateProvider petalProvider = new WeightedBlockStateProvider(
			addPoolToPool(addPoolToPool(petal(AbysmBlocks.ROSEBLOOM_PETALS, 1, 4), petal(AbysmBlocks.SUNBLOOM_PETALS, 1, 4)), petal(AbysmBlocks.MALLOWBLOOM_PETALS, 1, 4))
		);

		helper.add(
			PATCH_PETALS_UNDERWATER, AbysmFeatures.BLOOMSHROOM_VEGETATION,
			new NetherForestVegetationFeatureConfig(petalProvider, 3, 2)
		);

		helper.add(
			PATCH_PETALS_SURFACE, Feature.RANDOM_PATCH,
			new RandomPatchFeatureConfig(
				15, 5, 1, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK, new SimpleBlockFeatureConfig(petalProvider))
			)
		);

		WeightedBlockStateProvider bloomshroomVegetationProvider = new WeightedBlockStateProvider(
			Pool.<BlockState>builder()
				.add(AbysmBlocks.ROSY_SPRIGS.getDefaultState(), 29)
				.add(AbysmBlocks.SUNNY_SPRIGS.getDefaultState(), 29)
				.add(AbysmBlocks.MAUVE_SPRIGS.getDefaultState(), 29)
				.add(AbysmBlocks.ROSY_BLOOMSHROOM.getDefaultState(), 3)
				.add(AbysmBlocks.SUNNY_BLOOMSHROOM.getDefaultState(), 3)
				.add(AbysmBlocks.MAUVE_BLOOMSHROOM.getDefaultState(), 3)

		);

		helper.add(
			BLOOMSHROOM_VEGETATION, AbysmFeatures.BLOOMSHROOM_VEGETATION,
			new NetherForestVegetationFeatureConfig(bloomshroomVegetationProvider, 8, 4)
		);

		registerBloomshroomVegetation(registerable, AbysmBlocks.ROSY_SPRIGS, AbysmBlocks.ROSY_BLOOMSHROOM, ROSY_BLOOMSHROOM_VEGETATION_BONEMEAL);
		registerBloomshroomVegetation(registerable, AbysmBlocks.SUNNY_SPRIGS, AbysmBlocks.SUNNY_BLOOMSHROOM, SUNNY_BLOOMSHROOM_VEGETATION_BONEMEAL);
		registerBloomshroomVegetation(registerable, AbysmBlocks.MAUVE_SPRIGS, AbysmBlocks.MAUVE_BLOOMSHROOM, MAUVE_BLOOMSHROOM_VEGETATION_BONEMEAL);

		helper.add(
			FLOROPUMICE_STALAGMITES, AbysmFeatures.STALAGMITE,
			new StalagmiteFeature.Config(
				BlockStateProvider.of(AbysmBlocks.FLOROPUMICE),
				UniformFloatProvider.create(0.0F, 0.5F),
				UniformFloatProvider.create(0.4F, 1.0F),
				UniformFloatProvider.create(0.4F, 2.0F),
				4, 0.6F,
				UniformIntProvider.create(3, 19),
				0.33F
			)
		);
	}

	public static RegistryKey<ConfiguredFeature<?, ?>> ofKey(String id) {
		return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Abysm.id(id));
	}

	private static void registerBloomshroom(ConfiguredFeatureHelper helper, RegistryKey<ConfiguredFeature<?, ?>> key, Block stemBlock, Block capBlock, Block leavesBlock, Block goopBlock, Block crownBlock, int randomHeight, float horizontalTopPetalChance, float diagonalTopPetalChance) {
		helper.add(
			key, Feature.TREE,
			new TreeFeatureConfig.Builder(
				BlockStateProvider.of(stemBlock
					.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y)),
				new BloomshroomTrunkPlacer(
					8, randomHeight, 0,
					UniformIntProvider.create(6, 7),
					BlockStateProvider.of(leavesBlock)
				),
				BlockStateProvider.of(capBlock),
				new BloomshroomFoliagePlacer(
					ConstantIntProvider.create(3),
					ConstantIntProvider.create(0),
					BlockStateProvider.of(leavesBlock),
					BlockStateProvider.of(goopBlock),
					BlockStateProvider.of(crownBlock),
					horizontalTopPetalChance,
					diagonalTopPetalChance
				),
				new TwoLayersFeatureSize(1, 0, 1)
			).build()
		);
	}

	private static void registerBloomshroomVegetation(Registerable<ConfiguredFeature<?, ?>> registerable, Block sprigs, Block bloomshroom, RegistryKey<ConfiguredFeature<?, ?>> vegetationBonemeal) {
		WeightedBlockStateProvider blockStateProvider = new WeightedBlockStateProvider(
			Pool.<BlockState>builder()
				.add(sprigs.getDefaultState(), 87)
				.add(bloomshroom.getDefaultState(), 11)
		);

		registerable.register(
			vegetationBonemeal,
			new ConfiguredFeature<>(
				AbysmFeatures.BLOOMSHROOM_VEGETATION,
				new NetherForestVegetationFeatureConfig(blockStateProvider, 3, 1)
			)
		);
	}

	private static Pool.Builder<BlockState> addPoolToPool(Pool.Builder<BlockState> mainPool, Pool.Builder<BlockState> addedPool) {
		addedPool.build().getEntries().forEach(blockStateWeighted -> mainPool.add(blockStateWeighted.value(), blockStateWeighted.weight()));
		return mainPool;
	}

	public static Pool.Builder<BlockState> petal(Block petal, int min, int max) {
		return segmentedBlock(petal, min, max, LeafLitterBlock.SEGMENT_AMOUNT, LeafLitterBlock.HORIZONTAL_FACING);
	}

	private static Pool.Builder<BlockState> segmentedBlock(Block block, int min, int max, IntProperty amountProperty, EnumProperty<Direction> facingProperty) {
		Pool.Builder<BlockState> builder = Pool.builder();

		for (int i = min; i <= max; i++) {
			for (Direction direction : Direction.Type.HORIZONTAL) {
				builder.add(block.getDefaultState().with(amountProperty, i).with(facingProperty, direction), 1);
			}
		}

		return builder;
	}

	private record ConfiguredFeatureHelper(RegistryEntryLookup<ConfiguredFeature<?, ?>> lookup,
										   Registerable<ConfiguredFeature<?, ?>> registerable) {
		public <F extends Feature<FC>, FC extends FeatureConfig> void add(RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
			registerable.register(
				key,
				new ConfiguredFeature<>(
					feature,
					config
				)
			);
		}

		public <F extends Feature<RandomFeatureConfig>> void add(RegistryKey<ConfiguredFeature<?, ?>> key, F feature, RandomFeatureEntry... entries) {
			registerable.register(
				key,
				new ConfiguredFeature<>(
					feature,
					new RandomFeatureConfig(Arrays.asList(entries), entries[0].feature)
				)
			);
		}

		public RandomFeatureEntry entry(RegistryKey<ConfiguredFeature<?, ?>> key, float chance, PlacementModifier... modifiers) {
			return new RandomFeatureEntry(
				PlacedFeatures.createEntry(lookup.getOrThrow(key), modifiers),
				chance
			);
		}
	}
}
