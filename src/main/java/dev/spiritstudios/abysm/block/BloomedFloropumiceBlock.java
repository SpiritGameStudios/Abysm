package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.abysm.worldgen.feature.AbysmConfiguredFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class BloomedFloropumiceBlock extends Block implements Fertilizable {
	public static final MapCodec<BloomedFloropumiceBlock> CODEC = createCodec(BloomedFloropumiceBlock::new);

	@Override
	public MapCodec<BloomedFloropumiceBlock> getCodec() {
		return CODEC;
	}

	public BloomedFloropumiceBlock(Settings settings) {
		super(settings);
	}

	@Override
	public Fertilizable.FertilizableType getFertilizableType() {
		return Fertilizable.FertilizableType.NEIGHBOR_SPREADER;
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!stayAlive(state, world, pos)) {
			world.setBlockState(pos, AbysmBlocks.FLOROPUMICE.getDefaultState());
		}
	}

	private static boolean stayAlive(BlockState state, WorldView world, BlockPos pos) {
		BlockPos upPos = pos.up();
		FluidState fluidState = world.getFluidState(upPos);
		if(fluidState.isIn(FluidTags.WATER)) {
			return true;
		} else {
			BlockState blockState = world.getBlockState(upPos);
			int i = ChunkLightProvider.getRealisticOpacity(state, blockState, Direction.UP, blockState.getOpacity());
			return i < 15;
		}
	}

	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
		BlockPos upPos = pos.up();
		BlockState upState = world.getBlockState(upPos);
		return upState.isAir() || (upState.isReplaceable() && world.getFluidState(upPos).isIn(FluidTags.WATER));
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		BlockPos blockPos = pos.up();
		ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
		Registry<ConfiguredFeature<?, ?>> registry = world.getRegistryManager().getOrThrow(RegistryKeys.CONFIGURED_FEATURE);

		this.generate(registry, AbysmConfiguredFeatures.ROSY_BLOOMSHROOM_VEGETATION_BONEMEAL, world, chunkGenerator, random, blockPos);
	}

	private void generate(
		Registry<ConfiguredFeature<?, ?>> registry,
		RegistryKey<ConfiguredFeature<?, ?>> key,
		ServerWorld world,
		ChunkGenerator chunkGenerator,
		Random random,
		BlockPos pos
	) {
		registry.getOptional(key).ifPresent(entry -> entry.value().generate(world, chunkGenerator, random, pos));
	}
}
