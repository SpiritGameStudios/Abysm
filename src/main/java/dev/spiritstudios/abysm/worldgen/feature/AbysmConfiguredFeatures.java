package dev.spiritstudios.abysm.worldgen.feature;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.abysm.registry.AbysmFeatures;
import dev.spiritstudios.abysm.worldgen.tree.BloomshroomFoliagePlacer;
import dev.spiritstudios.abysm.worldgen.tree.BloomshroomTrunkPlacer;
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
	public static final RegistryKey<ConfiguredFeature<?, ?>> BLOOMSHROOM = ofKey("bloomshroom");

	public static final RegistryKey<ConfiguredFeature<?, ?>> PATCH_SPRIGS = ofKey("patch_sprigs");

	public static final RegistryKey<ConfiguredFeature<?, ?>> ROSY_BLOOMSHROOM_VEGETATION = ofKey("rosy_bloomshroom_vegetation");
	public static final RegistryKey<ConfiguredFeature<?, ?>> ROSY_BLOOMSHROOM_VEGETATION_BONEMEAL = ofKey("rosy_bloomshroom_vegetation_bonemeal");

	public static final RegistryKey<ConfiguredFeature<?, ?>> FLOROPUMICE_STALAGMITES = ofKey("floropumice_stalagmites");

	public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> registerable) {
		registerable.register(
			BLOOMSHROOM,
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
			PATCH_SPRIGS,
			new ConfiguredFeature<>(
				AbysmFeatures.SPRIGS,
				StateProviderFeatureConfig.create(AbysmBlocks.ROSY_SPRIGS)
			)
		);

		WeightedBlockStateProvider rosyVegetationBlockstateProvider = new WeightedBlockStateProvider(
			Pool.<BlockState>builder()
				.add(AbysmBlocks.ROSY_SPRIGS.getDefaultState(), 87)
				.add(AbysmBlocks.ROSY_BLOOMSHROOM.getDefaultState(), 11)
		);
		registerable.register(
			ROSY_BLOOMSHROOM_VEGETATION,
			new ConfiguredFeature<>(
				AbysmFeatures.BLOOMSHROOM_VEGETATION,
				new NetherForestVegetationFeatureConfig(rosyVegetationBlockstateProvider, 8, 4)
			)
		);
		registerable.register(
			ROSY_BLOOMSHROOM_VEGETATION_BONEMEAL,
			new ConfiguredFeature<>(
				AbysmFeatures.BLOOMSHROOM_VEGETATION,
				new NetherForestVegetationFeatureConfig(rosyVegetationBlockstateProvider, 3, 1)
			)
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
}
