package dev.spiritstudios.abysm.world.level.levelgen.feature;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import dev.spiritstudios.abysm.core.registries.tags.AbysmBlockTags;
import dev.spiritstudios.abysm.world.level.levelgen.tree.BloomshroomFoliagePlacer;
import dev.spiritstudios.abysm.world.level.levelgen.tree.BloomshroomTrunkPlacer;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.LeafLitterBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.Arrays;
import java.util.List;

public class AbysmConfiguredFeatures {
	public static final BlockPredicate IS_WATER = BlockPredicate.matchesBlocks(Blocks.WATER);

	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BLOOMSHROOM = ofKey("trees_bloomshroom");

	public static final ResourceKey<ConfiguredFeature<?, ?>> ROSY_BLOOMSHROOM = ofKey("rosy_bloomshroom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SUNNY_BLOOMSHROOM = ofKey("sunny_bloomshroom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MAUVE_BLOOMSHROOM = ofKey("mauve_bloomshroom");

	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SPRIGS = ofKey("patch_sprigs");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_PETALS_UNDERWATER = ofKey("patch_petals_underwater");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_PETALS_SURFACE = ofKey("patch_petals_surface");

	public static final ResourceKey<ConfiguredFeature<?, ?>> BLOOMSHROOM_VEGETATION = ofKey("bloomshroom_vegetation");

	public static final ResourceKey<ConfiguredFeature<?, ?>> ROSY_BLOOMSHROOM_VEGETATION = ofKey("rosy_bloomshroom_vegetation");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SUNNY_BLOOMSHROOM_VEGETATION = ofKey("sunny_bloomshroom_vegetation");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MAUVE_BLOOMSHROOM_VEGETATION = ofKey("mauve_bloomshroom_vegetation");

	public static final ResourceKey<ConfiguredFeature<?, ?>> FLOROPUMICE_STALAGMITES = ofKey("floropumice_stalagmites");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SURFACE_SMOOTH_FLOROPUMICE_STALAGMITES = ofKey("surface_smooth_floropumice_stalagmites");

	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SEAGRASS_CAVE = ofKey("patch_seagrass_cave");
	public static final ResourceKey<ConfiguredFeature<?, ?>> GOLDEN_LAZULI_OREFURL = ofKey("golden_lazuli_orefurl");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GOLDEN_LAZULI_OREFURL = ofKey("patch_golden_lazuli_orefurl");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_OOZE_VEGETATION = ofKey("patch_ooze_vegetation");

	public static final ResourceKey<ConfiguredFeature<?, ?>> HANGING_LANTERN = ofKey("hanging_lantern");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_HANGING_LANTERN = ofKey("patch_hanging_lantern");

	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_GOLDEN_LAZULI_DREGLOAM = ofKey("ore_golden_lazuli_dregloam");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CLAY_DREGLOAM = ofKey("ore_clay_dregloam");

	public static final ResourceKey<ConfiguredFeature<?, ?>> OOZE_PATCH = ofKey("ooze_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ROSEBLOOMED_PATCH = ofKey("rosebloomed_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ROSEBLOOMED_PATCH_BONEMEAL = ofKey("rosebloomed_patch_bonemeal");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SUNBLOOMED_PATCH = ofKey("sunbloomed_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SUNBLOOMED_PATCH_BONEMEAL = ofKey("sunbloomed_patch_bonemeal");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MALLOWBLOOMED_PATCH = ofKey("mallowbloomed_patch");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MALLOWBLOOMED_PATCH_BONEMEAL = ofKey("mallowbloomed_patch_bonemeal");
	public static final ResourceKey<ConfiguredFeature<?, ?>> MIXED_BLOOMED_PATCH = ofKey("mixed_bloomed_patch");

	public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> registerable) {
		HolderGetter<ConfiguredFeature<?, ?>> configuredFeatureLookup = registerable.lookup(Registries.CONFIGURED_FEATURE);
		HolderGetter<StructureProcessorList> processorListLookup = registerable.lookup(Registries.PROCESSOR_LIST);

		ConfiguredFeatureHelper helper = new ConfiguredFeatureHelper(
			registerable.lookup(Registries.CONFIGURED_FEATURE),
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
			new StateProviderFeatureConfig(new WeightedStateProvider(WeightedList.<BlockState>builder()
				.add(AbysmBlocks.ROSY_SPRIGS.defaultBlockState())
				.add(AbysmBlocks.SUNNY_SPRIGS.defaultBlockState())
				.add(AbysmBlocks.MAUVE_SPRIGS.defaultBlockState())
			))
		);

		WeightedStateProvider petalProvider = new WeightedStateProvider(
			addPoolToPool(addPoolToPool(petal(AbysmBlocks.ROSEBLOOM_PETALS, 1, 4), petal(AbysmBlocks.SUNBLOOM_PETALS, 1, 4)), petal(AbysmBlocks.MALLOWBLOOM_PETALS, 1, 4))
		);

		helper.add(
			PATCH_PETALS_UNDERWATER, AbysmFeatures.UNDERWATER_VEGETATION,
			new UnderwaterVegetationFeature.Config(
				AbysmBlockTags.BLOOMSHROOM_PLANTABLE_ON,
				petalProvider,
				3,
				2
			)
		);

		helper.add(
			PATCH_PETALS_SURFACE, Feature.RANDOM_PATCH,
			new RandomPatchConfiguration(
				15, 5, 1, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(petalProvider))
			)
		);

		WeightedStateProvider bloomshroomVegetationProvider = new WeightedStateProvider(
			WeightedList.<BlockState>builder()
				.add(AbysmBlocks.ROSY_SPRIGS.defaultBlockState(), 29)
				.add(AbysmBlocks.SUNNY_SPRIGS.defaultBlockState(), 29)
				.add(AbysmBlocks.MAUVE_SPRIGS.defaultBlockState(), 29)
				.add(AbysmBlocks.ROSY_BLOOMSHROOM.defaultBlockState(), 3)
				.add(AbysmBlocks.SUNNY_BLOOMSHROOM.defaultBlockState(), 3)
				.add(AbysmBlocks.MAUVE_BLOOMSHROOM.defaultBlockState(), 3)

		);

		helper.add(
			BLOOMSHROOM_VEGETATION, AbysmFeatures.UNDERWATER_VEGETATION,
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
				BlockStateProvider.simple(AbysmBlocks.FLOROPUMICE),
				UniformFloat.of(0.0F, 0.5F),
				UniformFloat.of(0.4F, 1.0F),
				UniformFloat.of(0.4F, 2.0F),
				4, 0.6F,
				UniformInt.of(3, 19),
				0.33F,
				ConstantInt.ZERO
			)
		);

		helper.add(
			SURFACE_SMOOTH_FLOROPUMICE_STALAGMITES, AbysmFeatures.STALAGMITE,
			new StalagmiteFeature.Config(
				BlockStateProvider.simple(AbysmBlocks.SMOOTH_FLOROPUMICE),
				UniformFloat.of(0.2F, 0.6F),
				UniformFloat.of(0.3F, 0.9F),
				UniformFloat.of(0.7F, 1.7F),
				3, 0.4F,
				UniformInt.of(5, 12),
				0.45F,
				ConstantInt.of(100)
			)
		);

		helper.add(
			PATCH_SEAGRASS_CAVE, Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(
				150,
				PlacementUtils.filtered(
					Feature.SIMPLE_BLOCK,
					new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SEAGRASS)),
					createUnderwaterBlockPredicate(List.of(AbysmBlocks.DREGLOAM))
				)
			)
		);

		helper.add(
			GOLDEN_LAZULI_OREFURL, AbysmFeatures.OREFURL,
			new OrefurlFeature.Config(
				BlockStateProvider.simple(AbysmBlocks.GOLDEN_LAZULI_OREFURL),
				BlockStateProvider.simple(AbysmBlocks.GOLDEN_LAZULI_OREFURL_PLANT),
				BiasedToBottomInt.of(2, 4)
			)
		);

		helper.add(
			PATCH_GOLDEN_LAZULI_OREFURL, Feature.RANDOM_PATCH,
			new RandomPatchConfiguration(
				110,
				6,
				3,
				PlacementUtils.inlinePlaced(
					configuredFeatureLookup.getOrThrow(GOLDEN_LAZULI_OREFURL),
					EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 3),
					RandomOffsetPlacement.vertical(ConstantInt.of(1)),
					BlockPredicateFilter.forPredicate(createUnderwaterBlockPredicate(List.of(AbysmBlocks.DREGLOAM_GOLDEN_LAZULI_ORE)))
				)
			)
		);

		helper.add(
			PATCH_OOZE_VEGETATION, AbysmFeatures.UNDERWATER_VEGETATION,
			new UnderwaterVegetationFeature.Config(
				AbysmBlockTags.OOZE_VEGETATION_PLANTABLE_ON,
				new WeightedStateProvider(
					WeightedList.<BlockState>builder()
						.add(AbysmBlocks.OOZETRICKLE_FILAMENTS.defaultBlockState(), 5)
						.add(AbysmBlocks.TALL_OOZETRICKLE_FILAMENTS.defaultBlockState(), 3)
				),
				3,
				1
			)
		);

		helper.add(
			HANGING_LANTERN, AbysmFeatures.HANGING_LANTERN,
			new HangingLanternFeature.Config(
				BlockStateProvider.simple(AbysmBlocks.OOZETRICKLE_LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true)),
				BlockStateProvider.simple(AbysmBlocks.OOZETRICKLE_CORD),
				BiasedToBottomInt.of(3, 18),
				UniformInt.of(3, 5)
			)
		);

		helper.add(
			PATCH_HANGING_LANTERN, Feature.RANDOM_PATCH,
			new RandomPatchConfiguration(
				6,
				6,
				3,
				PlacementUtils.inlinePlaced(
					configuredFeatureLookup.getOrThrow(HANGING_LANTERN),
					EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 8),
					RandomOffsetPlacement.vertical(ConstantInt.of(-1))
				)
			)
		);

		RuleTest ruleTestDregloam = new BlockMatchTest(AbysmBlocks.DREGLOAM);
		helper.add(
			ORE_GOLDEN_LAZULI_DREGLOAM, Feature.ORE,
			new OreConfiguration(
				ruleTestDregloam,
				AbysmBlocks.DREGLOAM_GOLDEN_LAZULI_ORE.defaultBlockState(),
				9
			)
		);
		helper.add(
			ORE_CLAY_DREGLOAM, Feature.ORE,
			new OreConfiguration(
				ruleTestDregloam,
				Blocks.CLAY.defaultBlockState(),
				35
			)
		);

		helper.add(
			OOZE_PATCH, AbysmFeatures.UNDERWATER_VEGETATION_PATCH,
			new UnderwaterVegetationPatchFeature.Config(
				AbysmBlockTags.IS_AIR_OR_WATER,
				AbysmBlockTags.OOZE_REPLACEABLE,
				BlockStateProvider.simple(AbysmBlocks.OOZING_DREGLOAM),
				PlacementUtils.inlinePlaced(configuredFeatureLookup.getOrThrow(PATCH_OOZE_VEGETATION)),
				CaveSurface.FLOOR,
				ConstantInt.of(1),
				0.6F,
				3,
				0.1F,
				UniformInt.of(1, 2),
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

	public static ResourceKey<ConfiguredFeature<?, ?>> ofKey(String id) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, Abysm.id(id));
	}

	private static void registerBloomshroom(ConfiguredFeatureHelper helper, ResourceKey<ConfiguredFeature<?, ?>> key, Block stemBlock, Block capBlock, Block leavesBlock, Block nectarsapBlock, Block crownBlock, int randomHeight, float horizontalTopPetalChance, float diagonalTopPetalChance) {
		helper.add(
			key, Feature.TREE,
			new TreeConfiguration.TreeConfigurationBuilder(
				BlockStateProvider.simple(stemBlock
					.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)),
				new BloomshroomTrunkPlacer(
					8, randomHeight, 0,
					UniformInt.of(6, 7),
					BlockStateProvider.simple(leavesBlock)
				),
				BlockStateProvider.simple(capBlock),
				new BloomshroomFoliagePlacer(
					ConstantInt.of(3),
					ConstantInt.of(0),
					BlockStateProvider.simple(leavesBlock),
					BlockStateProvider.simple(nectarsapBlock),
					BlockStateProvider.simple(crownBlock),
					horizontalTopPetalChance,
					diagonalTopPetalChance
				),
				new TwoLayersFeatureSize(1, 0, 1)
			).build()
		);
	}

	private static void registerBloomshroomVegetation(BootstrapContext<ConfiguredFeature<?, ?>> registerable, Block sprigs, Block bloomshroom, ResourceKey<ConfiguredFeature<?, ?>> vegetationBonemeal) {
		WeightedStateProvider blockStateProvider = new WeightedStateProvider(
			WeightedList.<BlockState>builder()
				.add(sprigs.defaultBlockState(), 87)
				.add(bloomshroom.defaultBlockState(), 11)
		);

		registerable.register(
			vegetationBonemeal,
			new ConfiguredFeature<>(
				AbysmFeatures.UNDERWATER_VEGETATION,
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
		HolderGetter<ConfiguredFeature<?, ?>> configuredFeatureLookup,
		ResourceKey<ConfiguredFeature<?, ?>> featureKey,
		ResourceKey<ConfiguredFeature<?, ?>> bonemealFeatureKey,
		Block bloomedFloropumice,
		ResourceKey<ConfiguredFeature<?, ?>> vegetationFeatureKey
	) {
		registerBloomedFloropumicePatch(helper, configuredFeatureLookup, featureKey, bloomedFloropumice, vegetationFeatureKey, false);
		registerBloomedFloropumicePatch(helper, configuredFeatureLookup, bonemealFeatureKey, bloomedFloropumice, vegetationFeatureKey, true);
	}

	private static void registerBloomedFloropumicePatch(
		ConfiguredFeatureHelper helper,
		HolderGetter<ConfiguredFeature<?, ?>> configuredFeatureLookup,
		ResourceKey<ConfiguredFeature<?, ?>> featureKey,
		Block bloomedFloropumice,
		ResourceKey<ConfiguredFeature<?, ?>> vegetationFeatureKey,
		boolean bonemeal
	) {
		helper.add(
			featureKey, AbysmFeatures.UNDERWATER_VEGETATION_PATCH,
			new UnderwaterVegetationPatchFeature.Config(
				AbysmBlockTags.IS_AIR_OR_WATER,
				AbysmBlockTags.BLOOMED_FLOROPUMICE_REPLACEABLE,
				BlockStateProvider.simple(bloomedFloropumice),
				PlacementUtils.inlinePlaced(configuredFeatureLookup.getOrThrow(vegetationFeatureKey)),
				CaveSurface.FLOOR,
				ConstantInt.of(1),
				0.2F,
				5,
				bonemeal ? 0.6F : 0.3F,
				bonemeal ? UniformInt.of(1, 3) : UniformInt.of(1, 2),
				0.8F,
				true
			)
		);
	}

	private static WeightedList.Builder<BlockState> addPoolToPool(WeightedList.Builder<BlockState> mainPool, WeightedList.Builder<BlockState> addedPool) {
		addedPool.build().unwrap().forEach(blockStateWeighted -> mainPool.add(blockStateWeighted.value(), blockStateWeighted.weight()));
		return mainPool;
	}

	public static WeightedList.Builder<BlockState> petal(Block petal, int min, int max) {
		return segmentedBlock(petal, min, max, LeafLitterBlock.AMOUNT, LeafLitterBlock.FACING);
	}

	private static WeightedList.Builder<BlockState> segmentedBlock(Block block, int min, int max, IntegerProperty amountProperty, EnumProperty<Direction> facingProperty) {
		WeightedList.Builder<BlockState> builder = WeightedList.builder();

		for (int i = min; i <= max; i++) {
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				builder.add(block.defaultBlockState().setValue(amountProperty, i).setValue(facingProperty, direction), 1);
			}
		}

		return builder;
	}

	private static BlockPredicate createUnderwaterBlockPredicate(List<Block> validGround) {
		BlockPredicate blockPredicate;
		if (!validGround.isEmpty()) {
			blockPredicate = BlockPredicate.allOf(IS_WATER, BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), validGround));
		} else {
			blockPredicate = IS_WATER;
		}

		return blockPredicate;
	}

	private record ConfiguredFeatureHelper(HolderGetter<ConfiguredFeature<?, ?>> lookup,
										   BootstrapContext<ConfiguredFeature<?, ?>> registerable) {
		public <F extends Feature<FC>, FC extends FeatureConfiguration> void add(ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
			registerable.register(
				key,
				new ConfiguredFeature<>(
					feature,
					config
				)
			);
		}

		public <F extends Feature<RandomFeatureConfiguration>> void add(ResourceKey<ConfiguredFeature<?, ?>> key, F feature, WeightedPlacedFeature... entries) {
			registerable.register(
				key,
				new ConfiguredFeature<>(
					feature,
					new RandomFeatureConfiguration(Arrays.asList(entries), entries[0].feature)
				)
			);
		}

		public WeightedPlacedFeature entry(ResourceKey<ConfiguredFeature<?, ?>> key, float chance, PlacementModifier... modifiers) {
			return new WeightedPlacedFeature(
				PlacementUtils.inlinePlaced(lookup.getOrThrow(key), modifiers),
				chance
			);
		}
	}
}
