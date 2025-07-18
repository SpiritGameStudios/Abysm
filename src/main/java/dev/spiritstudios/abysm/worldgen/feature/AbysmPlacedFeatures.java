package dev.spiritstudios.abysm.worldgen.feature;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.BlockFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier;
import net.minecraft.world.gen.placementmodifier.EnvironmentScanPlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.HeightmapPlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import net.minecraft.world.gen.placementmodifier.RandomOffsetPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.world.gen.placementmodifier.SurfaceThresholdFilterPlacementModifier;

import java.util.List;

public class AbysmPlacedFeatures {
	public static final RegistryKey<PlacedFeature> TREES_BLOOMSHROOM = ofKey("trees_bloomshroom");

	public static final RegistryKey<PlacedFeature> ROSY_BLOOMSHROOM = ofKey("rosy_bloomshroom");
	public static final RegistryKey<PlacedFeature> SUNNY_BLOOMSHROOM = ofKey("sunny_bloomshroom");
	public static final RegistryKey<PlacedFeature> MAUVE_BLOOMSHROOM = ofKey("mauve_bloomshroom");

	public static final RegistryKey<PlacedFeature> PATCH_SPRIGS = ofKey("patch_sprigs");
	public static final RegistryKey<PlacedFeature> PATCH_PETALS_UNDERWATER = ofKey("patch_petals_underwater");
	public static final RegistryKey<PlacedFeature> PATCH_PETALS_SURFACE = ofKey("patch_petals_surface");

	public static final RegistryKey<PlacedFeature> FLOROPUMICE_STALAGMITES = ofKey("floropumice_stalagmites");

	public static final RegistryKey<PlacedFeature> PATCH_SEAGRASS_CAVE = ofKey("patch_seagrass_cave");
	public static final RegistryKey<PlacedFeature> PATCH_GOLDEN_LAZULI_OREFURL = ofKey("patch_golden_lazuli_orefurl");

	public static final RegistryKey<PlacedFeature> PATCH_HANGING_LANTERN = ofKey("patch_hanging_lantern");

	public static final RegistryKey<PlacedFeature> ORE_GOLDEN_LAZULI_DREGLOAM = ofKey("ore_golden_lazuli_dregloam");
	public static final RegistryKey<PlacedFeature> ORE_CLAY_DREGLOAM = ofKey("ore_clay_dregloam");

	public static final RegistryKey<PlacedFeature> OOZE_PATCH = ofKey("ooze_patch");
	public static final RegistryKey<PlacedFeature> MIXED_BLOOMED_PATCH = ofKey("mixed_bloomed_patch");

	public static final PlacementModifier RUINS_CAVE_RANGE = HeightRangePlacementModifier.uniform(YOffset.fixed(-56), YOffset.fixed(56));

	public static void bootstrap(Registerable<PlacedFeature> registerable) {
		PlacedFeatureHelper helper = new PlacedFeatureHelper(
			registerable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE),
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
			PlacedFeatures.createCountExtraModifier(1, 0.05F, 1),
			SquarePlacementModifier.of(),
			PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
			wouldSurvive(AbysmBlocks.MAUVE_BLOOMSHROOM),
			belowSurfaceModifier(12),
			BiomePlacementModifier.of()
		);

		helper.add(
			FLOROPUMICE_STALAGMITES, AbysmConfiguredFeatures.FLOROPUMICE_STALAGMITES,
			PlacedFeatures.createCountExtraModifier(1, 0.05F, 1),
			SquarePlacementModifier.of(),
			PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
			BiomePlacementModifier.of()
		);

		helper.add(
			PATCH_SPRIGS, AbysmConfiguredFeatures.PATCH_SPRIGS,
			CountPlacementModifier.of(30),
			PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
			BiomePlacementModifier.of(),
			SquarePlacementModifier.of()
		);

		helper.add(
			PATCH_PETALS_UNDERWATER, AbysmConfiguredFeatures.PATCH_PETALS_UNDERWATER,
			CountPlacementModifier.of(7),
			PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
			BiomePlacementModifier.of(),
			SquarePlacementModifier.of()
		);

		helper.add(
			PATCH_PETALS_SURFACE, AbysmConfiguredFeatures.PATCH_PETALS_SURFACE,
			CountPlacementModifier.of(7),
			PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
			BiomePlacementModifier.of(),
			SquarePlacementModifier.of()
		);

		helper.add(
			PATCH_SEAGRASS_CAVE, AbysmConfiguredFeatures.PATCH_SEAGRASS_CAVE,
			CountPlacementModifier.of(UniformIntProvider.create(15, 28)),
			SquarePlacementModifier.of(),
			RUINS_CAVE_RANGE,
			EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.IS_AIR_OR_WATER, 20),
			RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(1)),
			BiomePlacementModifier.of()
		);

		helper.add(
			PATCH_GOLDEN_LAZULI_OREFURL, AbysmConfiguredFeatures.PATCH_GOLDEN_LAZULI_OREFURL,
			CountPlacementModifier.of(80),
			SquarePlacementModifier.of(),
			RUINS_CAVE_RANGE,
			EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.IS_AIR_OR_WATER, 20),
			RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(1)),
			BiomePlacementModifier.of()
		);

		helper.add(
			PATCH_HANGING_LANTERN, AbysmConfiguredFeatures.PATCH_HANGING_LANTERN,
			CountPlacementModifier.of(UniformIntProvider.create(1, 7)),
			SquarePlacementModifier.of(),
			RUINS_CAVE_RANGE,
			EnvironmentScanPlacementModifier.of(Direction.UP, BlockPredicate.solid(), BlockPredicate.IS_AIR_OR_WATER, 20),
			RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(-1)),
			BiomePlacementModifier.of()
		);

		helper.add(
			ORE_GOLDEN_LAZULI_DREGLOAM, AbysmConfiguredFeatures.ORE_GOLDEN_LAZULI_DREGLOAM,
			CountPlacementModifier.of(100),
			SquarePlacementModifier.of(),
			RUINS_CAVE_RANGE,
			BiomePlacementModifier.of()
		);

		helper.add(
			ORE_CLAY_DREGLOAM, AbysmConfiguredFeatures.ORE_CLAY_DREGLOAM,
			CountPlacementModifier.of(22),
			SquarePlacementModifier.of(),
			RUINS_CAVE_RANGE,
			BiomePlacementModifier.of()
		);

		helper.add(
			OOZE_PATCH, AbysmConfiguredFeatures.OOZE_PATCH,
			CountPlacementModifier.of(95),
			SquarePlacementModifier.of(),
			RUINS_CAVE_RANGE,
			EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.IS_AIR_OR_WATER, 12),
			RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(1)),
			BiomePlacementModifier.of()
		);

		helper.add(
			MIXED_BLOOMED_PATCH, AbysmConfiguredFeatures.MIXED_BLOOMED_PATCH,
			CountPlacementModifier.of(8),
			SquarePlacementModifier.of(),
			HeightmapPlacementModifier.of(Heightmap.Type.OCEAN_FLOOR_WG),
			BiomePlacementModifier.of()
		);
	}

	private static RegistryKey<PlacedFeature> ofKey(String id) {
		return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Abysm.id(id));
	}

	private static PlacementModifier wouldSurvive(Block block) {
		return wouldSurvive(block.getDefaultState());
	}

	private static PlacementModifier wouldSurvive(BlockState state) {
		return BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(state, BlockPos.ORIGIN));
	}

	private record PlacedFeatureHelper(RegistryEntryLookup<ConfiguredFeature<?, ?>> lookup,
									   Registerable<PlacedFeature> registerable) {
		public void add(RegistryKey<PlacedFeature> key, RegistryKey<ConfiguredFeature<?, ?>> configuredKey, PlacementModifier... modifiers) {
			registerable.register(
				key,
				new PlacedFeature(
					lookup.getOrThrow(configuredKey),
					List.of(modifiers)
				)
			);
		}
	}

	private static void addBloomshroomTree(PlacedFeatureHelper helper, RegistryKey<PlacedFeature> placeFeature, RegistryKey<ConfiguredFeature<?, ?>> configuredFeature, Block bloomShroom) {
		helper.add(
			placeFeature, configuredFeature,
			PlacedFeatures.createCountExtraModifier(1, 0.05F, 1),
			SquarePlacementModifier.of(),
			PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP,
			wouldSurvive(bloomShroom),
			belowSurfaceModifier(12),
			BiomePlacementModifier.of()
		);
	}

	private static PlacementModifier belowSurfaceModifier(int depth) {
		return SurfaceThresholdFilterPlacementModifier.of(Heightmap.Type.WORLD_SURFACE_WG, Integer.MIN_VALUE, -depth);
	}
}
