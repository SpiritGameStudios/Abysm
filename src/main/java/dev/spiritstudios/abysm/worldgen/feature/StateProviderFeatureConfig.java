package dev.spiritstudios.abysm.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record StateProviderFeatureConfig(BlockStateProvider stateProvider) implements FeatureConfiguration {
	public static final Codec<StateProviderFeatureConfig> CODEC = BlockStateProvider.CODEC.fieldOf("stateProvider")
		.xmap(StateProviderFeatureConfig::new, config -> config.stateProvider)
		.codec();

	public static StateProviderFeatureConfig create(BlockState state) {
		return new StateProviderFeatureConfig(BlockStateProvider.simple(state));
	}

	public static StateProviderFeatureConfig create(Block block) {
		return new StateProviderFeatureConfig(BlockStateProvider.simple(block));
	}
}
