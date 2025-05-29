package dev.spiritstudios.abysm.worldgen.feature;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;

public class AbysmPlacedFeatures {
	public static final RegistryKey<PlacedFeature> TREES_ROSY_BLOOMSHROOM = ofKey("trees_rosy_bloomshroom");
	public static final RegistryKey<PlacedFeature> TREES_SUNNY_BLOOMSHROOM = ofKey("trees_sunny_bloomshroom");
	public static final RegistryKey<PlacedFeature> TREES_MAUVE_BLOOMSHROOM = ofKey("trees_mauve_bloomshroom");

	public static final RegistryKey<PlacedFeature> PATCH_SPRIGS = ofKey("patch_sprigs");

	public static final RegistryKey<PlacedFeature> FLOROPUMICE_STALAGMITES = ofKey("floropumice_stalagmites");

	public static void bootstrap(Registerable<PlacedFeature> registerable) {
		PlacedFeatureHelper helper = new PlacedFeatureHelper(
			registerable.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE),
			registerable
		);

		addBloomshroomTree(helper,
			TREES_ROSY_BLOOMSHROOM,
			AbysmConfiguredFeatures.ROSY_BLOOMSHROOM,
			AbysmBlocks.ROSY_BLOOMSHROOM
		);
		addBloomshroomTree(helper,
			TREES_SUNNY_BLOOMSHROOM,
			AbysmConfiguredFeatures.SUNNY_BLOOMSHROOM,
			AbysmBlocks.SUNNY_BLOOMSHROOM
		);
		addBloomshroomTree(helper,
			TREES_MAUVE_BLOOMSHROOM,
			AbysmConfiguredFeatures.MAUVE_BLOOMSHROOM,
			AbysmBlocks.MAUVE_BLOOMSHROOM
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
	}

	private static RegistryKey<PlacedFeature> ofKey(String id) {
		return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Abysm.id(id));
	}

	private static PlacementModifier wouldSurvive(Block block) {
		return wouldSurvive(block.getDefaultState());
	}

	private static PlacementModifier wouldSurvive(BlockState state) {
		return  BlockFilterPlacementModifier.of(BlockPredicate.wouldSurvive(state, BlockPos.ORIGIN));
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
			BiomePlacementModifier.of()
		);
	}
}
