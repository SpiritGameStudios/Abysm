package dev.spiritstudios.abysm.worldgen.feature;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter;

public class AbysmPlacedFeatures {
	public static final ResourceKey<PlacedFeature> TREES_BLOOMSHROOM = ofKey("trees_bloomshroom");

	public static final ResourceKey<PlacedFeature> ROSY_BLOOMSHROOM = ofKey("rosy_bloomshroom");
	public static final ResourceKey<PlacedFeature> SUNNY_BLOOMSHROOM = ofKey("sunny_bloomshroom");
	public static final ResourceKey<PlacedFeature> MAUVE_BLOOMSHROOM = ofKey("mauve_bloomshroom");

	public static final ResourceKey<PlacedFeature> PATCH_SPRIGS = ofKey("patch_sprigs");
	public static final ResourceKey<PlacedFeature> PATCH_PETALS_UNDERWATER = ofKey("patch_petals_underwater");
	public static final ResourceKey<PlacedFeature> PATCH_PETALS_SURFACE = ofKey("patch_petals_surface");

	public static final ResourceKey<PlacedFeature> FLOROPUMICE_STALAGMITES = ofKey("floropumice_stalagmites");
	public static final ResourceKey<PlacedFeature> SURFACE_SMOOTH_FLOROPUMICE_STALAGMITES = ofKey("surface_smooth_floropumice_stalagmites");

	public static final ResourceKey<PlacedFeature> PATCH_SEAGRASS_CAVE = ofKey("patch_seagrass_cave");
	public static final ResourceKey<PlacedFeature> PATCH_GOLDEN_LAZULI_OREFURL = ofKey("patch_golden_lazuli_orefurl");

	public static final ResourceKey<PlacedFeature> PATCH_HANGING_LANTERN = ofKey("patch_hanging_lantern");

	public static final ResourceKey<PlacedFeature> ORE_GOLDEN_LAZULI_DREGLOAM = ofKey("ore_golden_lazuli_dregloam");
	public static final ResourceKey<PlacedFeature> ORE_CLAY_DREGLOAM = ofKey("ore_clay_dregloam");

	public static final ResourceKey<PlacedFeature> OOZE_PATCH = ofKey("ooze_patch");
	public static final ResourceKey<PlacedFeature> MIXED_BLOOMED_PATCH = ofKey("mixed_bloomed_patch");

	public static final PlacementModifier RUINS_CAVE_RANGE = HeightRangePlacement.uniform(VerticalAnchor.absolute(-56), VerticalAnchor.absolute(56));

	public static void bootstrap(BootstrapContext<PlacedFeature> registerable) {
		PlacedFeatureHelper helper = new PlacedFeatureHelper(
			registerable.lookup(Registries.CONFIGURED_FEATURE),
			registerable
		);

		addBloomshroomTree(helper,
			ROSY_BLOOMSHROOM,
			AbysmConfiguredFeatures.ROSY_BLOOMSHROOM,
			AbysmBlocks.ROSY_BLOOMSHROOM
		);

		addBloomshroomTree(helper,
			SUNNY_BLOOMSHROOM,
			AbysmConfiguredFeatures.SUNNY_BLOOMSHROOM,
			AbysmBlocks.SUNNY_BLOOMSHROOM
		);

		addBloomshroomTree(helper,
			MAUVE_BLOOMSHROOM,
			AbysmConfiguredFeatures.MAUVE_BLOOMSHROOM,
			AbysmBlocks.MAUVE_BLOOMSHROOM
		);

		helper.add(
			TREES_BLOOMSHROOM,
			AbysmConfiguredFeatures.TREES_BLOOMSHROOM,
			PlacementUtils.countExtra(1, 0.05F, 1),
			InSquarePlacement.spread(),
			PlacementUtils.HEIGHTMAP_TOP_SOLID,
			wouldSurvive(AbysmBlocks.MAUVE_BLOOMSHROOM),
			belowSurfaceModifier(12),
			BiomeFilter.biome()
		);

		helper.add(
			FLOROPUMICE_STALAGMITES, AbysmConfiguredFeatures.FLOROPUMICE_STALAGMITES,
			PlacementUtils.countExtra(1, 0.05F, 1),
			InSquarePlacement.spread(),
			PlacementUtils.HEIGHTMAP_TOP_SOLID,
			BiomeFilter.biome()
		);

		helper.add(
			SURFACE_SMOOTH_FLOROPUMICE_STALAGMITES, AbysmConfiguredFeatures.SURFACE_SMOOTH_FLOROPUMICE_STALAGMITES,
			PlacementUtils.countExtra(1, 0.1F, 2),
			InSquarePlacement.spread(),
			RUINS_CAVE_RANGE,
			BiomeFilter.biome(),
			PlacementUtils.HEIGHTMAP_TOP_SOLID
		);

		helper.add(
			PATCH_SPRIGS, AbysmConfiguredFeatures.PATCH_SPRIGS,
			CountPlacement.of(30),
			InSquarePlacement.spread(),
			PlacementUtils.HEIGHTMAP_TOP_SOLID,
			BiomeFilter.biome()
		);

		helper.add(
			PATCH_PETALS_UNDERWATER, AbysmConfiguredFeatures.PATCH_PETALS_UNDERWATER,
			CountPlacement.of(7),
			InSquarePlacement.spread(),
			PlacementUtils.HEIGHTMAP_TOP_SOLID,
			BiomeFilter.biome()
		);

		helper.add(
			PATCH_PETALS_SURFACE, AbysmConfiguredFeatures.PATCH_PETALS_SURFACE,
			CountPlacement.of(7),
			InSquarePlacement.spread(),
			PlacementUtils.HEIGHTMAP,
			BiomeFilter.biome()
		);

		helper.add(
			PATCH_SEAGRASS_CAVE, AbysmConfiguredFeatures.PATCH_SEAGRASS_CAVE,
			CountPlacement.of(UniformInt.of(15, 28)),
			InSquarePlacement.spread(),
			RUINS_CAVE_RANGE,
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 20),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		helper.add(
			PATCH_GOLDEN_LAZULI_OREFURL, AbysmConfiguredFeatures.PATCH_GOLDEN_LAZULI_OREFURL,
			CountPlacement.of(80),
			InSquarePlacement.spread(),
			RUINS_CAVE_RANGE,
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 20),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		helper.add(
			PATCH_HANGING_LANTERN, AbysmConfiguredFeatures.PATCH_HANGING_LANTERN,
			CountPlacement.of(UniformInt.of(1, 7)),
			InSquarePlacement.spread(),
			RUINS_CAVE_RANGE,
			EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 20),
			RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
			BiomeFilter.biome()
		);

		helper.add(
			ORE_GOLDEN_LAZULI_DREGLOAM, AbysmConfiguredFeatures.ORE_GOLDEN_LAZULI_DREGLOAM,
			CountPlacement.of(100),
			InSquarePlacement.spread(),
			RUINS_CAVE_RANGE,
			BiomeFilter.biome()
		);

		helper.add(
			ORE_CLAY_DREGLOAM, AbysmConfiguredFeatures.ORE_CLAY_DREGLOAM,
			CountPlacement.of(22),
			InSquarePlacement.spread(),
			RUINS_CAVE_RANGE,
			BiomeFilter.biome()
		);

		helper.add(
			OOZE_PATCH, AbysmConfiguredFeatures.OOZE_PATCH,
			CountPlacement.of(95),
			InSquarePlacement.spread(),
			RUINS_CAVE_RANGE,
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		helper.add(
			MIXED_BLOOMED_PATCH, AbysmConfiguredFeatures.MIXED_BLOOMED_PATCH,
			CountPlacement.of(8),
			InSquarePlacement.spread(),
			HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
			BiomeFilter.biome()
		);
	}

	private static ResourceKey<PlacedFeature> ofKey(String id) {
		return ResourceKey.create(Registries.PLACED_FEATURE, Abysm.id(id));
	}

	private static PlacementModifier wouldSurvive(Block block) {
		return wouldSurvive(block.defaultBlockState());
	}

	private static PlacementModifier wouldSurvive(BlockState state) {
		return BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(state, BlockPos.ZERO));
	}

	private record PlacedFeatureHelper(HolderGetter<ConfiguredFeature<?, ?>> lookup,
									   BootstrapContext<PlacedFeature> registerable) {
		public void add(ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> configuredKey, PlacementModifier... modifiers) {
			registerable.register(
				key,
				new PlacedFeature(
					lookup.getOrThrow(configuredKey),
					List.of(modifiers)
				)
			);
		}
	}

	private static void addBloomshroomTree(PlacedFeatureHelper helper, ResourceKey<PlacedFeature> placeFeature, ResourceKey<ConfiguredFeature<?, ?>> configuredFeature, Block bloomShroom) {
		helper.add(
			placeFeature, configuredFeature,
			PlacementUtils.countExtra(1, 0.05F, 1),
			InSquarePlacement.spread(),
			PlacementUtils.HEIGHTMAP_TOP_SOLID,
			wouldSurvive(bloomShroom),
			belowSurfaceModifier(12),
			BiomeFilter.biome()
		);
	}

	private static PlacementModifier belowSurfaceModifier(int depth) {
		return SurfaceRelativeThresholdFilter.of(Heightmap.Types.WORLD_SURFACE_WG, Integer.MIN_VALUE, -depth);
	}
}
