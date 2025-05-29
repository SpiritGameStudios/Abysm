package dev.spiritstudios.abysm.worldgen.feature;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.abysm.registry.AbysmFeatures;
import dev.spiritstudios.abysm.worldgen.tree.BloomshroomFoliagePlacer;
import dev.spiritstudios.abysm.worldgen.tree.BloomshroomTrunkPlacer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NetherForestVegetationFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

public class AbysmConfiguredFeatures {
	public static final RegistryKey<ConfiguredFeature<?, ?>> ROSY_BLOOMSHROOM = ofKey("rosy_bloomshroom");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SUNNY_BLOOMSHROOM = ofKey("sunny_bloomshroom");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MAUVE_BLOOMSHROOM = ofKey("mauve_bloomshroom");

	public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_SPRIGS = ofKey("patch_sprigs");

	public static final RegistryKey<ConfiguredFeature<?, ?>> ROSY_BLOOMSHROOM_VEGETATION = ofKey("rosy_bloomshroom_vegetation");
	public static final RegistryKey<ConfiguredFeature<?, ?>> ROSY_BLOOMSHROOM_VEGETATION_BONEMEAL = ofKey("rosy_bloomshroom_vegetation_bonemeal");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SUNNY_BLOOMSHROOM_VEGETATION = ofKey("sunny_bloomshroom_vegetation");
	public static final RegistryKey<ConfiguredFeature<?, ?>> SUNNY_BLOOMSHROOM_VEGETATION_BONEMEAL = ofKey("sunny_bloomshroom_vegetation_bonemeal");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MAUVE_BLOOMSHROOM_VEGETATION = ofKey("mauve_bloomshroom_vegetation");
	public static final RegistryKey<ConfiguredFeature<?, ?>> MAUVE_BLOOMSHROOM_VEGETATION_BONEMEAL = ofKey("mauve_bloomshroom_vegetation_bonemeal");

	public static final RegistryKey<ConfiguredFeature<?, ?>> FLOROPUMICE_STALAGMITES = ofKey("floropumice_stalagmites");

	public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> registerable) {
		registerable.register(
			ROSY_BLOOMSHROOM,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeFeatureConfig.Builder(
					BlockStateProvider.of(AbysmBlocks.ROSY_BLOOMSHROOM_STEM
						.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y)),
					new BloomshroomTrunkPlacer(
						5, 5, 0,
						UniformIntProvider.create(4, 5),
						BlockStateProvider.of(AbysmBlocks.BLOOMSHROOM_GOOP)
					),
					BlockStateProvider.of(AbysmBlocks.ROSY_BLOOMSHROOM_CAP),
					new BloomshroomFoliagePlacer(ConstantIntProvider.create(4), ConstantIntProvider.create(0)),
					new TwoLayersFeatureSize(1, 0, 1)
				).build()
			)
		);
		registerable.register(
			SUNNY_BLOOMSHROOM,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeFeatureConfig.Builder(
					BlockStateProvider.of(AbysmBlocks.SUNNY_BLOOMSHROOM_STEM
						.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y)),
					new BloomshroomTrunkPlacer(
						5, 5, 0,
						UniformIntProvider.create(4, 5),
						BlockStateProvider.of(AbysmBlocks.BLOOMSHROOM_GOOP)
					),
					BlockStateProvider.of(AbysmBlocks.SUNNY_BLOOMSHROOM_CAP),
					new BloomshroomFoliagePlacer(ConstantIntProvider.create(4), ConstantIntProvider.create(0)),
					new TwoLayersFeatureSize(1, 0, 1)
				).build()
			)
		);
		registerable.register(
			MAUVE_BLOOMSHROOM,
			new ConfiguredFeature<>(
				Feature.TREE,
				new TreeFeatureConfig.Builder(
					BlockStateProvider.of(AbysmBlocks.MAUVE_BLOOMSHROOM_STEM
						.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y)),
					new BloomshroomTrunkPlacer(
						5, 5, 0,
						UniformIntProvider.create(4, 5),
						BlockStateProvider.of(AbysmBlocks.BLOOMSHROOM_GOOP)
					),
					BlockStateProvider.of(AbysmBlocks.MAUVE_BLOOMSHROOM_CAP),
					new BloomshroomFoliagePlacer(ConstantIntProvider.create(4), ConstantIntProvider.create(0)),
					new TwoLayersFeatureSize(1, 0, 1)
				).build()
			)
		);

		registerable.register(
			PATCH_SPRIGS,
			new ConfiguredFeature<>(
				AbysmFeatures.SPRIGS,
				new StateProviderFeatureConfig(
					new WeightedBlockStateProvider(
						Pool.<BlockState>builder()
							.add(AbysmBlocks.ROSY_SPRIGS.getDefaultState(), 33)
							.add(AbysmBlocks.SUNNY_SPRIGS.getDefaultState(), 33)
							.add(AbysmBlocks.MAUVE_SPRIGS.getDefaultState(), 33)
					)
				)
			)
		);

		registerBloomshroomVegetation(registerable,
			AbysmBlocks.ROSY_SPRIGS,
			AbysmBlocks.ROSY_BLOOMSHROOM,
			ROSY_BLOOMSHROOM_VEGETATION,
			ROSY_BLOOMSHROOM_VEGETATION_BONEMEAL
		);
		registerBloomshroomVegetation(registerable,
			AbysmBlocks.SUNNY_SPRIGS,
			AbysmBlocks.SUNNY_BLOOMSHROOM,
			SUNNY_BLOOMSHROOM_VEGETATION,
			SUNNY_BLOOMSHROOM_VEGETATION_BONEMEAL
		);
		registerBloomshroomVegetation(registerable,
			AbysmBlocks.MAUVE_SPRIGS,
			AbysmBlocks.MAUVE_BLOOMSHROOM,
			MAUVE_BLOOMSHROOM_VEGETATION,
			MAUVE_BLOOMSHROOM_VEGETATION_BONEMEAL
		);

		registerable.register(
			FLOROPUMICE_STALAGMITES,
			new ConfiguredFeature<>(
				AbysmFeatures.STALAGMITE,
				new StalagmiteFeature.Config(
					BlockStateProvider.of(AbysmBlocks.FLOROPUMICE),
					UniformFloatProvider.create(0.0F, 0.5F),
					UniformFloatProvider.create(0.4F, 1.0F),
					UniformFloatProvider.create(0.4F, 2.0F),
					4, 0.6F,
					UniformIntProvider.create(3, 19),
					0.33F
				)
			)
		);
	}

	public static RegistryKey<ConfiguredFeature<?, ?>> ofKey(String id) {
		return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Abysm.id(id));
	}

	private static void registerBloomshroomVegetation(Registerable<ConfiguredFeature<?, ?>> registerable, Block sprigs, Block bloomshroom, RegistryKey<ConfiguredFeature<?, ?>> vegetation, RegistryKey<ConfiguredFeature<?, ?>> vegetationBonemeal) {
		WeightedBlockStateProvider blockStateProvider = new WeightedBlockStateProvider(
			Pool.<BlockState>builder()
				.add(sprigs.getDefaultState(), 87)
				.add(bloomshroom.getDefaultState(), 11)
		);
		registerable.register(
			vegetation,
			new ConfiguredFeature<>(
				AbysmFeatures.BLOOMSHROOM_VEGETATION,
				new NetherForestVegetationFeatureConfig(blockStateProvider, 8, 4)
			)
		);
		registerable.register(
			vegetationBonemeal,
			new ConfiguredFeature<>(
				AbysmFeatures.BLOOMSHROOM_VEGETATION,
				new NetherForestVegetationFeatureConfig(blockStateProvider, 3, 1)
			)
		);
	}
}
