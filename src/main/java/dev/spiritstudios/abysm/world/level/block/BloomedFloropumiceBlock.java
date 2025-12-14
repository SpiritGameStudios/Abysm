package dev.spiritstudios.abysm.world.level.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.material.FluidState;

public class BloomedFloropumiceBlock extends Block implements BonemealableBlock {
	public static final MapCodec<BloomedFloropumiceBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(block -> block.featureKey),
				propertiesCodec()
			)
			.apply(instance, BloomedFloropumiceBlock::new)
	);

	private final ResourceKey<ConfiguredFeature<?, ?>> featureKey;

	@Override
	public MapCodec<BloomedFloropumiceBlock> codec() {
		return CODEC;
	}

	public BloomedFloropumiceBlock(ResourceKey<ConfiguredFeature<?, ?>> featureKey, Properties settings) {
		super(settings);
		this.featureKey = featureKey;
	}

	@Override
	public BonemealableBlock.Type getType() {
		return BonemealableBlock.Type.NEIGHBOR_SPREADER;
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (!stayAlive(state, world, pos)) {
			world.setBlockAndUpdate(pos, AbysmBlocks.FLOROPUMICE.defaultBlockState());
		}
	}

	private static boolean stayAlive(BlockState state, LevelReader world, BlockPos pos) {
		BlockPos upPos = pos.above();
		FluidState fluidState = world.getFluidState(upPos);
		if (fluidState.is(FluidTags.WATER)) {
			return true;
		} else {
			BlockState blockState = world.getBlockState(upPos);
			int i = LightEngine.getLightBlockInto(state, blockState, Direction.UP, blockState.getLightBlock());
			return i < 15;
		}
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		BlockPos upPos = pos.above();
		BlockState upState = world.getBlockState(upPos);
		return upState.isAir() || (upState.canBeReplaced() && world.getFluidState(upPos).is(FluidTags.WATER));
	}

	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	private Optional<? extends Holder<ConfiguredFeature<?, ?>>> getFeatureEntry(LevelReader world) {
		return world.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(this.featureKey);
	}

	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		BlockPos upPos = pos.above();
		ChunkGenerator chunkGenerator = world.getChunkSource().getGenerator();
		this.getFeatureEntry(world)
			.ifPresent(featureEntry -> featureEntry.value().place(world, chunkGenerator, random, upPos));
	}
}
