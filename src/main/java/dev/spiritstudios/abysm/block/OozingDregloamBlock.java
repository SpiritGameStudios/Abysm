package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.particle.AbysmParticleTypes;
import dev.spiritstudios.abysm.worldgen.feature.AbysmConfiguredFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
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

import java.util.Optional;

public class OozingDregloamBlock extends Block implements Fertilizable {
	public static final MapCodec<OozingDregloamBlock> CODEC = createCodec(OozingDregloamBlock::new);

	@Override
	public MapCodec<OozingDregloamBlock> getCodec() {
		return CODEC;
	}

	public OozingDregloamBlock(Settings settings) {
		super(settings);
	}

	@Override
	public Fertilizable.FertilizableType getFertilizableType() {
		return Fertilizable.FertilizableType.NEIGHBOR_SPREADER;
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!stayAlive(state, world, pos)) {
			world.setBlockState(pos, AbysmBlocks.DREGLOAM.getDefaultState());
		}
	}

	public static boolean stayAlive(BlockState state, WorldView world, BlockPos pos) {
		BlockPos upPos = pos.up();
		FluidState fluidState = world.getFluidState(upPos);
		if (fluidState.isIn(FluidTags.WATER)) {
			return true;
		} else {
			BlockState blockState = world.getBlockState(upPos);
			if (blockState.isOf(AbysmBlocks.DREGLOAM_OOZE)) {
				return true;
			} else {
				int i = ChunkLightProvider.getRealisticOpacity(state, blockState, Direction.UP, blockState.getOpacity());
				return i < 15;
			}
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

	private Optional<? extends RegistryEntry<ConfiguredFeature<?, ?>>> getFeatureEntry(WorldView world) {
		return world.getRegistryManager().getOrThrow(RegistryKeys.CONFIGURED_FEATURE).getOptional(AbysmConfiguredFeatures.PATCH_OOZE_VEGETATION);
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		BlockPos upPos = pos.up();
		ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
		this.getFeatureEntry(world)
			.ifPresent(featureEntry -> featureEntry.value().generate(world, chunkGenerator, random, upPos));
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		world.addParticleClient(AbysmParticleTypes.POGGDRYGLL_SPORES, pos.getX() + random.nextDouble(), pos.getY() + 1.1, pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
	}
}
