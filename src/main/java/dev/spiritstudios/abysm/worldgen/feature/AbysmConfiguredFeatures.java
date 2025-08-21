package dev.spiritstudios.abysm.worldgen.feature;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.registry.tags.AbysmBlockTags;
import dev.spiritstudios.abysm.worldgen.tree.BloomshroomFoliagePlacer;
import dev.spiritstudios.abysm.worldgen.tree.BloomshroomTrunkPlacer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.LeafLitterBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.VerticalSurfaceType;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.math.intprovider.BiasedToBottomIntProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.RandomFeatureConfig;
import net.minecraft.world.gen.feature.RandomFeatureEntry;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.placementmodifier.BlockFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.EnvironmentScanPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RandomOffsetPlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

import java.util.Arrays;
import java.util.List;

public class AbysmConfiguredFeatures {
	public static final BlockPredicate IS_WATER = BlockPredicate.matchingBlocks(Blocks.WATER);

	public static final RegistryKey<ConfiguredFeature<?, ?>> TREES_BLOOMSHROOM = ofKey("trees_bloomshroom");

	public static final RegistryKey<ConfiguredFeature<?, ?>> ROSY_BLOOMSHROOM = ofKey("rosy_bloomshroom");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SUNNY_BLOOMSHROOM = ofKey("sunny_bloomshroom");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MAUVE_BLOOMSHROOM = ofKey("mauve_bloomshroom");

	public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_SPRIGS = ofKey("patch_sprigs");
	public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_PETALS_UNDERWATER = ofKey("patch_petals_underwater");
	public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_PETALS_SURFACE = ofKey("patch_petals_surface");

	public static final RegistryKey<ConfiguredFeature<?, ?>> BLOOMSHROOM_VEGETATION = ofKey("bloomshroom_vegetation");

	public static final RegistryKey<ConfiguredFeature<?, ?>> ROSY_BLOOMSHROOM_VEGETATION = ofKey("rosy_bloomshroom_vegetation");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SUNNY_BLOOMSHROOM_VEGETATION = ofKey("sunny_bloomshroom_vegetation");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MAUVE_BLOOMSHROOM_VEGETATION = ofKey("mauve_bloomshroom_vegetation");

	public static final RegistryKey<ConfiguredFeature<?, ?>> FLOROPUMICE_STALAGMITES = ofKey("floropumice_stalagmites");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SURFACE_SMOOTH_FLOROPUMICE_STALAGMITES = ofKey("surface_smooth_floropumice_stalagmites");

	public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_SEAGRASS_CAVE = ofKey("patch_seagrass_cave");
	public static final RegistryKey<ConfiguredFeature<?, ?>> GOLDEN_LAZULI_OREFURL = ofKey("golden_lazuli_orefurl");
	public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_GOLDEN_LAZULI_OREFURL = ofKey("patch_golden_lazuli_orefurl");
	public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_OOZE_VEGETATION = ofKey("patch_ooze_vegetation");

	public static final RegistryKey<ConfiguredFeature<?, ?>> HANGING_LANTERN = ofKey("hanging_lantern");
	public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_HANGING_LANTERN = ofKey("patch_hanging_lantern");

	public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_GOLDEN_LAZULI_DREGLOAM = ofKey("ore_golden_lazuli_dregloam");
	public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_CLAY_DREGLOAM = ofKey("ore_clay_dregloam");

	public static final RegistryKey<ConfiguredFeature<?, ?>> OOZE_PATCH = ofKey("ooze_patch");
	public static final RegistryKey<ConfiguredFeature<?, ?>> ROSEBLOOMED_PATCH = ofKey("rosebloomed_patch");
	public static final RegistryKey<ConfiguredFeature<?, ?>> ROSEBLOOMED_PATCH_BONEMEAL = ofKey("rosebloomed_patch_bonemeal");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SUNBLOOMED_PATCH = ofKey("sunbloomed_patch");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SUNBLOOMED_PATCH_BONEMEAL = ofKey("sunbloomed_patch_bonemeal");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MALLOWBLOOMED_PATCH = ofKey("mallowbloomed_patch");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MALLOWBLOOMED_PATCH_BONEMEAL = ofKey("mallowbloomed_patch_bonemeal");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MIXED_BLOOMED_PATCH = ofKey("mixed_bloomed_patch");

	public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> registerable) {
		RegistryEntryLookup<ConfiguredFeature<?, ?>> configuredFeatureLookup = registerable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
		RegistryEntryLookup<StructureProcessorList> processorListLookup = registerable.getRegistryLookup(RegistryKeys.PROCESSOR_LIST);

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
			AbysmBlocks.SWEET_NECTARSAP,
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
			AbysmBlocks.SOUR_NECTARSAP,
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
			AbysmBlocks.BITTER_NECTARSAP,
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
			new UnderwaterVegetationFeature.Config(
				AbysmBlockTags.BLOOMSHROOM_PLANTABLE_ON,
				petalProvider,
				3,
				2
			)
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
			new UnderwaterVegetationFeature.Config(
				AbysmBlockTags.BLOOMSHROOM_PLANTABLE_ON,
				bloomshroomVegetationProvider,
				8,
				4
			)
		);

		registerBloomshroomVegetation(registerable, AbysmBlocks.ROSY_SPRIGS, AbysmBlocks.ROSY_BLOOMSHROOM, ROSY_BLOOMSHROOM_VEGETATION);
		registerBloomshroomVegetation(registerable, AbysmBlocks.SUNNY_SPRIGS, AbysmBlocks.SUNNY_BLOOMSHROOM, SUNNY_BLOOMSHROOM_VEGETATION);
		registerBloomshroomVegetation(registerable, AbysmBlocks.MAUVE_SPRIGS, AbysmBlocks.MAUVE_BLOOMSHROOM, MAUVE_BLOOMSHROOM_VEGETATION);

		helper.add(
			FLOROPUMICE_STALAGMITES, AbysmFeatures.STALAGMITE,
			new StalagmiteFeature.Config(
				BlockStateProvider.of(AbysmBlocks.FLOROPUMICE),
				UniformFloatProvider.create(0.0F, 0.5F),
				UniformFloatProvider.create(0.4F, 1.0F),
				UniformFloatProvider.create(0.4F, 2.0F),
				4, 0.6F,
				UniformIntProvider.create(3, 19),
				0.33F,
				ConstantIntProvider.ZERO
			)
		);

		helper.add(
			SURFACE_SMOOTH_FLOROPUMICE_STALAGMITES, AbysmFeatures.STALAGMITE,
			new StalagmiteFeature.Config(
				BlockStateProvider.of(AbysmBlocks.SMOOTH_FLOROPUMICE),
				UniformFloatProvider.create(0.2F, 0.6F),
				UniformFloatProvider.create(0.3F, 0.9F),
				UniformFloatProvider.create(0.7F, 1.7F),
				3, 0.4F,
				UniformIntProvider.create(5, 12),
				0.45F,
				ConstantIntProvider.create(100)
			)
		);

		helper.add(
			PATCH_SEAGRASS_CAVE, Feature.RANDOM_PATCH,
			ConfiguredFeatures.createRandomPatchFeatureConfig(
				150,
				PlacedFeatures.createEntry(
					Feature.SIMPLE_BLOCK,
					new SimpleBlockFeatureConfig(BlockStateProvider.of(Blocks.SEAGRASS)),
					createUnderwaterBlockPredicate(List.of(AbysmBlocks.DREGLOAM))
				)
			)
		);

		helper.add(
			GOLDEN_LAZULI_OREFURL, AbysmFeatures.OREFURL,
			new OrefurlFeature.Config(
				BlockStateProvider.of(AbysmBlocks.GOLDEN_LAZULI_OREFURL),
				BlockStateProvider.of(AbysmBlocks.GOLDEN_LAZULI_OREFURL_PLANT),
				BiasedToBottomIntProvider.create(2, 4)
			)
		);

		helper.add(
			PATCH_GOLDEN_LAZULI_OREFURL, Feature.RANDOM_PATCH,
			new RandomPatchFeatureConfig(
				110,
				6,
				3,
				PlacedFeatures.createEntry(
					configuredFeatureLookup.getOrThrow(GOLDEN_LAZULI_OREFURL),
					EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.IS_AIR_OR_WATER, 3),
					RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(1)),
					BlockFilterPlacementModifier.of(createUnderwaterBlockPredicate(List.of(AbysmBlocks.DREGLOAM_GOLDEN_LAZULI_ORE)))
				)
			)
		);

		helper.add(
			PATCH_OOZE_VEGETATION, AbysmFeatures.BLOOMSHROOM_VEGETATION,
			new UnderwaterVegetationFeature.Config(
				AbysmBlockTags.OOZE_VEGETATION_PLANTABLE_ON,
				new WeightedBlockStateProvider(
					Pool.<BlockState>builder()
						.add(AbysmBlocks.OOZETRICKLE_FILAMENTS.getDefaultState(), 5)
						.add(AbysmBlocks.TALL_OOZETRICKLE_FILAMENTS.getDefaultState(), 3)
				),
				3,
				1
			)
		);

		helper.add(
			HANGING_LANTERN, AbysmFeatures.HANGING_LANTERN,
			new HangingLanternFeature.Config(
				BlockStateProvider.of(AbysmBlocks.OOZETRICKLE_LANTERN.getDefaultState().with(LanternBlock.HANGING, true)),
				BlockStateProvider.of(AbysmBlocks.OOZETRICKLE_CORD),
				BiasedToBottomIntProvider.create(3, 18),
				UniformIntProvider.create(3, 5)
			)
		);

		helper.add(
			PATCH_HANGING_LANTERN, Feature.RANDOM_PATCH,
			new RandomPatchFeatureConfig(
				6,
				6,
				3,
				PlacedFeatures.createEntry(
					configuredFeatureLookup.getOrThrow(HANGING_LANTERN),
					EnvironmentScanPlacementModifier.of(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.IS_AIR_OR_WATER, 8),
					RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(-1))
				)
			)
		);

		RuleTest ruleTestDregloam = new BlockMatchRuleTest(AbysmBlocks.DREGLOAM);
		helper.add(
			ORE_GOLDEN_LAZULI_DREGLOAM, Feature.ORE,
			new OreFeatureConfig(
				ruleTestDregloam,
				AbysmBlocks.DREGLOAM_GOLDEN_LAZULI_ORE.getDefaultState(),
				9
			)
		);
		helper.add(
			ORE_CLAY_DREGLOAM, Feature.ORE,
			new OreFeatureConfig(
				ruleTestDregloam,
				Blocks.CLAY.getDefaultState(),
				35
			)
		);

		helper.add(
			OOZE_PATCH, AbysmFeatures.UNDERWATER_VEGETATION_PATCH,
			new UnderwaterVegetationPatchFeature.Config(
				AbysmBlockTags.IS_AIR_OR_WATER,
				AbysmBlockTags.OOZE_REPLACEABLE,
				BlockStateProvider.of(AbysmBlocks.OOZING_DREGLOAM),
				PlacedFeatures.createEntry(configuredFeatureLookup.getOrThrow(PATCH_OOZE_VEGETATION)),
				VerticalSurfaceType.FLOOR,
				ConstantIntProvider.create(1),
				0.6F,
				3,
				0.1F,
				UniformIntProvider.create(1, 2),
				0.3F,
				true
			)
		);

		registerBloomedFloropumicePatches(helper, configuredFeatureLookup, ROSEBLOOMED_PATCH, ROSEBLOOMED_PATCH_BONEMEAL, AbysmBlocks.ROSEBLOOMED_FLOROPUMICE, ROSY_BLOOMSHROOM_VEGETATION);
		registerBloomedFloropumicePatches(helper, configuredFeatureLookup, SUNBLOOMED_PATCH, SUNBLOOMED_PATCH_BONEMEAL, AbysmBlocks.SUNBLOOMED_FLOROPUMICE, SUNNY_BLOOMSHROOM_VEGETATION);
		registerBloomedFloropumicePatches(helper, configuredFeatureLookup, MALLOWBLOOMED_PATCH, MALLOWBLOOMED_PATCH_BONEMEAL, AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE, MAUVE_BLOOMSHROOM_VEGETATION);

		helper.add(
			MIXED_BLOOMED_PATCH, Feature.RANDOM_SELECTOR,
			helper.entry(ROSEBLOOMED_PATCH, 1.0F / 3.0F),
			helper.entry(SUNBLOOMED_PATCH, 1.0F / 3.0F),
			helper.entry(MALLOWBLOOMED_PATCH, 1.0F / 3.0F)
		);
	}

	public static RegistryKey<ConfiguredFeature<?, ?>> ofKey(String id) {
		return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Abysm.id(id));
	}

	private static void registerBloomshroom(ConfiguredFeatureHelper helper, RegistryKey<ConfiguredFeature<?, ?>> key, Block stemBlock, Block capBlock, Block leavesBlock, Block nectarsapBlock, Block crownBlock, int randomHeight, float horizontalTopPetalChance, float diagonalTopPetalChance) {
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
					BlockStateProvider.of(nectarsapBlock),
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
				new UnderwaterVegetationFeature.Config(
					AbysmBlockTags.BLOOMSHROOM_PLANTABLE_ON,
					blockStateProvider,
					3,
					1
				)
			)
		);
	}

	private static void registerBloomedFloropumicePatches(
		ConfiguredFeatureHelper helper,
		RegistryEntryLookup<ConfiguredFeature<?, ?>> configuredFeatureLookup,
		RegistryKey<ConfiguredFeature<?, ?>> featureKey,
		RegistryKey<ConfiguredFeature<?, ?>> bonemealFeatureKey,
		Block bloomedFloropumice,
		RegistryKey<ConfiguredFeature<?, ?>> vegetationFeatureKey
	) {
		registerBloomedFloropumicePatch(helper, configuredFeatureLookup, featureKey, bloomedFloropumice, vegetationFeatureKey, false);
		registerBloomedFloropumicePatch(helper, configuredFeatureLookup, bonemealFeatureKey, bloomedFloropumice, vegetationFeatureKey, true);
	}

	private static void registerBloomedFloropumicePatch(
		ConfiguredFeatureHelper helper,
		RegistryEntryLookup<ConfiguredFeature<?, ?>> configuredFeatureLookup,
		RegistryKey<ConfiguredFeature<?, ?>> featureKey,
		Block bloomedFloropumice,
		RegistryKey<ConfiguredFeature<?, ?>> vegetationFeatureKey,
		boolean bonemeal
	) {
		helper.add(
			featureKey, AbysmFeatures.UNDERWATER_VEGETATION_PATCH,
			new UnderwaterVegetationPatchFeature.Config(
				AbysmBlockTags.IS_AIR_OR_WATER,
				AbysmBlockTags.BLOOMED_FLOROPUMICE_REPLACEABLE,
				BlockStateProvider.of(bloomedFloropumice),
				PlacedFeatures.createEntry(configuredFeatureLookup.getOrThrow(vegetationFeatureKey)),
				VerticalSurfaceType.FLOOR,
				ConstantIntProvider.create(1),
				0.2F,
				5,
				bonemeal ? 0.6F : 0.3F,
				bonemeal ? UniformIntProvider.create(1, 3) : UniformIntProvider.create(1, 2),
				0.8F,
				true
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

	private static BlockPredicate createUnderwaterBlockPredicate(List<Block> validGround) {
		BlockPredicate blockPredicate;
		if (!validGround.isEmpty()) {
			blockPredicate = BlockPredicate.bothOf(IS_WATER, BlockPredicate.matchingBlocks(Direction.DOWN.getVector(), validGround));
		} else {
			blockPredicate = IS_WATER;
		}

		return blockPredicate;
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
