package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.util.Codecs;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.tick.ScheduledTickView;

import java.util.Optional;

public class SmallBloomshroomBlock extends PlantBlock implements Fertilizable, Waterloggable {
	public static final MapCodec<SmallBloomshroomBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				RegistryKey.createCodec(RegistryKeys.CONFIGURED_FEATURE).fieldOf("feature").forGetter(block -> block.featureKey),
				Codecs.PARTICLE_TYPE_CODEC.forGetter(block -> block.particle),
				createSettingsCodec()
			)
			.apply(instance, SmallBloomshroomBlock::new)
	);
	private static final VoxelShape SHAPE = Block.createColumnShape(6.0, 0.0, 10.0);
	private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	private final RegistryKey<ConfiguredFeature<?, ?>> featureKey;
	public final SimpleParticleType particle;

	@Override
	protected MapCodec<? extends SmallBloomshroomBlock> getCodec() {
		return CODEC;
	}

	public SmallBloomshroomBlock(RegistryKey<ConfiguredFeature<?, ?>> featureKey, SimpleParticleType particle, Settings settings) {
		super(settings);
		this.featureKey = featureKey;
		this.particle = particle;
		this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE.offset(state.getModelOffset(pos));
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		// bloomshroom flowers can additionally be planted on grass, moss etc. for decoration, but can only be grown into a big bloomshroom when underwater (or on mycelium)
		return floor.isIn(AbysmBlockTags.BLOOMSHROOM_PLANTABLE_ON) || super.canPlantOnTop(floor, world, pos);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return this.getDefaultState()
			.with(WATERLOGGED, fluidState.isEqualAndStill(Fluids.WATER));
	}

	@Override
	protected BlockState getStateForNeighborUpdate(
		BlockState state,
		WorldView world,
		ScheduledTickView tickView,
		BlockPos pos,
		Direction direction,
		BlockPos neighborPos,
		BlockState neighborState,
		Random random
	) {
		BlockState newState = super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
		if(!newState.isAir()) {
			if (state.get(WATERLOGGED)) {
				tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
			}
		}
		return newState;
	}

	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
		return state.get(WATERLOGGED) || world.getBlockState(pos.down()).isOf(Blocks.MYCELIUM);
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return random.nextFloat() < 0.4;
	}

	private Optional<? extends RegistryEntry<ConfiguredFeature<?, ?>>> getFeatureEntry(WorldView world) {
		return world.getRegistryManager().getOrThrow(RegistryKeys.CONFIGURED_FEATURE).getOptional(this.featureKey);
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		this.getFeatureEntry(world)
			.ifPresent(featureEntry -> featureEntry.value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos));
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);

		boolean waterlogged = state.get(WATERLOGGED, false);

		if(random.nextInt(waterlogged ? 1 : 4) == 0) {
			Vec3d offset = state.getModelOffset(pos);
			double x = pos.getX() + offset.x + 0.45 + 0.1 * random.nextFloat();
			double y = pos.getY() + offset.y + 0.6 + 0.1 * random.nextFloat();
			double z = pos.getZ() + offset.z + 0.45 + 0.1 * random.nextFloat();

			double vx = random.nextGaussian() * (waterlogged ? 0.015 : 0.008);
			double vy = (waterlogged ? 0.02 : 0.01) + random.nextFloat() * (waterlogged ? 0.08F : 0.04F);
			double vz = random.nextGaussian() * (waterlogged ? 0.015 : 0.008);

			world.addParticleClient(this.particle, x, y, z, vx, vy, vz);
		}
	}
}
