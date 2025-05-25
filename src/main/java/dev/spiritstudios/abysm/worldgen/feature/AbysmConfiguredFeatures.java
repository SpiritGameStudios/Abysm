package dev.spiritstudios.abysm.worldgen.feature;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.worldgen.tree.BloomshroomTrunkPlacer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.trunk.TrunkPlacer;

public class AbysmConfiguredFeatures {
	public static final RegistryKey<ConfiguredFeature<?, ?>> BLOOMSHROOM = ofKey("bloomshroom");

	public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> registerable) {
//		registerable.register(
//			BLOOMSHROOM,
//			new ConfiguredFeature<>(
//				Feature.TREE,
//				new TreeFeatureConfig.Builder(
//					BlockStateProvider.of(Blocks.RED_CONCRETE),
//					new BloomshroomTrunkPlacer(5, 2, 2)
//				)
//			)
//		);
	}

	public static RegistryKey<ConfiguredFeature<?, ?>> ofKey(String id) {
		return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Abysm.id(id));
	}
}
