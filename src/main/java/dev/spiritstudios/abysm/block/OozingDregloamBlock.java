package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.particle.AbysmParticleTypes;
import dev.spiritstudios.abysm.worldgen.feature.AbysmConfiguredFeatures;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
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

public class OozingDregloamBlock extends Block implements BonemealableBlock {
	public static final MapCodec<OozingDregloamBlock> CODEC = simpleCodec(OozingDregloamBlock::new);

	@Override
	public MapCodec<OozingDregloamBlock> codec() {
		return CODEC;
	}

	public OozingDregloamBlock(Properties settings) {
		super(settings);
	}

	@Override
	public BonemealableBlock.Type getType() {
		return BonemealableBlock.Type.NEIGHBOR_SPREADER;
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		if (!stayAlive(state, world, pos)) {
			world.setBlockAndUpdate(pos, AbysmBlocks.DREGLOAM.defaultBlockState());
		}
	}

	public static boolean stayAlive(BlockState state, LevelReader world, BlockPos pos) {
		BlockPos upPos = pos.above();
		FluidState fluidState = world.getFluidState(upPos);
		if (fluidState.is(FluidTags.WATER)) {
			return true;
		} else {
			BlockState blockState = world.getBlockState(upPos);
			if (blockState.is(AbysmBlocks.DREGLOAM_OOZE)) {
				return true;
			} else {
				int i = LightEngine.getLightBlockInto(state, blockState, Direction.UP, blockState.getLightBlock());
				return i < 15;
			}
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
		return world.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(AbysmConfiguredFeatures.PATCH_OOZE_VEGETATION);
	}

	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		BlockPos upPos = pos.above();
		ChunkGenerator chunkGenerator = world.getChunkSource().getGenerator();
		this.getFeatureEntry(world)
			.ifPresent(featureEntry -> featureEntry.value().place(world, chunkGenerator, random, upPos));
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		world.addParticle(AbysmParticleTypes.POGGDRYGLL_SPORES, pos.getX() + random.nextDouble(), pos.getY() + 1.1, pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
	}
}
