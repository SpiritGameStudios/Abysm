package dev.spiritstudios.abysm.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record StateProviderFeatureConfig(BlockStateProvider stateProvider) implements FeatureConfig {
	public static final Codec<StateProviderFeatureConfig> CODEC = BlockStateProvider.TYPE_CODEC.fieldOf("stateProvider")
		.xmap(StateProviderFeatureConfig::new, config -> config.stateProvider)
		.codec();

	public static StateProviderFeatureConfig create(BlockState state) {
		return new StateProviderFeatureConfig(BlockStateProvider.of(state));
	}

	public static StateProviderFeatureConfig create(Block block) {
		return new StateProviderFeatureConfig(BlockStateProvider.of(block));
	}
}
