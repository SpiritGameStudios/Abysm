package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.registry.tags.AbysmBlockTags;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SmallBloomshroomBlock extends VegetationBlock implements BonemealableBlock, SimpleWaterloggedBlock {
	public static final MapCodec<SmallBloomshroomBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				ResourceKey.codec(Registries.CONFIGURED_FEATURE).fieldOf("feature").forGetter(block -> block.featureKey),
				ParticleTypes.CODEC.fieldOf("particle").forGetter(block -> block.particle),
				propertiesCodec()
			)
			.apply(instance, SmallBloomshroomBlock::new)
	);
	private static final VoxelShape SHAPE = Block.column(6.0, 0.0, 10.0);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private final ResourceKey<ConfiguredFeature<?, ?>> featureKey;
	public final ParticleOptions particle;

	@Override
	protected MapCodec<? extends SmallBloomshroomBlock> codec() {
		return CODEC;
	}

	public SmallBloomshroomBlock(ResourceKey<ConfiguredFeature<?, ?>> featureKey, ParticleOptions particle, Properties settings) {
		super(settings);
		this.featureKey = featureKey;
		this.particle = particle;
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE.move(state.getOffset(pos));
	}

	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		// bloomshroom flowers can additionally be planted on grass, moss etc. for decoration, but can only be grown into a big bloomshroom when underwater (or on mycelium)
		return floor.is(AbysmBlockTags.BLOOMSHROOM_PLANTABLE_ON) || super.mayPlaceOn(floor, world, pos);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
		return this.defaultBlockState()
			.setValue(WATERLOGGED, fluidState.isSourceOfType(Fluids.WATER));
	}

	@Override
	protected BlockState updateShape(
		BlockState state,
		LevelReader world,
		ScheduledTickAccess tickView,
		BlockPos pos,
		Direction direction,
		BlockPos neighborPos,
		BlockState neighborState,
		RandomSource random
	) {
		BlockState newState = super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
		if (!newState.isAir()) {
			if (state.getValue(WATERLOGGED)) {
				tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
			}
		}
		return newState;
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
		return state.getValue(WATERLOGGED) || world.getBlockState(pos.below()).is(Blocks.MYCELIUM);
	}

	@Override
	public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
		return random.nextFloat() < 0.4;
	}

	private Optional<? extends Holder<ConfiguredFeature<?, ?>>> getFeatureEntry(LevelReader world) {
		return world.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(this.featureKey);
	}

	@Override
	public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
		this.getFeatureEntry(world)
			.ifPresent(featureEntry -> featureEntry.value().place(world, world.getChunkSource().getGenerator(), random, pos));
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);

		boolean waterlogged = state.getValueOrElse(WATERLOGGED, false);

		if (random.nextInt(waterlogged ? 1 : 4) == 0) {
			Vec3 offset = state.getOffset(pos);
			double x = pos.getX() + offset.x + 0.45 + 0.1 * random.nextFloat();
			double y = pos.getY() + offset.y + 0.6 + 0.1 * random.nextFloat();
			double z = pos.getZ() + offset.z + 0.45 + 0.1 * random.nextFloat();

			double vx = random.nextGaussian() * (waterlogged ? 0.015 : 0.008);
			double vy = (waterlogged ? 0.02 : 0.01) + random.nextFloat() * (waterlogged ? 0.08F : 0.04F);
			double vz = random.nextGaussian() * (waterlogged ? 0.015 : 0.008);

			world.addParticle(this.particle, x, y, z, vx, vy, vz);
		}
	}
}
