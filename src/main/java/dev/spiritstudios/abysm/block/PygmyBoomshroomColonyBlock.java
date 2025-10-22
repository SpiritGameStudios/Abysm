package dev.spiritstudios.abysm.block;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;

import dev.spiritstudios.abysm.entity.effect.SalinationEffect;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author axialeaa
 */
public class PygmyBoomshroomColonyBlock extends UnderwaterPlantBlock implements Fertilizable {

	public static final BooleanProperty NORTH_EAST = BooleanProperty.of("north_east");
	public static final BooleanProperty NORTH_WEST = BooleanProperty.of("north_west");
	public static final BooleanProperty SOUTH_EAST = BooleanProperty.of("south_east");
	public static final BooleanProperty SOUTH_WEST = BooleanProperty.of("south_west");
	public static final BooleanProperty[] CORNER_PROPERTIES = new BooleanProperty[] { NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST };

	private static final int SHAPE_HEIGHT = 9;

	private static final VoxelShape COMPLETE_SHAPE = createColumnShape(16, 0, SHAPE_HEIGHT);
	private static final Map<BooleanProperty, VoxelShape> SHAPE_MAP = Util.make(Maps.newHashMap(), map -> {
		map.put(NORTH_EAST, createCuboidShape(8, 0, 0, 16, SHAPE_HEIGHT, 8));
		map.put(NORTH_WEST, createCuboidShape(0, 0, 0, 8, SHAPE_HEIGHT, 8));
		map.put(SOUTH_EAST, createCuboidShape(8, 0, 8, 16, SHAPE_HEIGHT, 16));
		map.put(SOUTH_WEST, createCuboidShape(0, 0, 8, 8, SHAPE_HEIGHT, 16));
	});

	private static final MapCodec<PygmyBoomshroomColonyBlock> CODEC = createCodec(PygmyBoomshroomColonyBlock::new);
	private static final ExplosionBehavior EXPLOSION_BEHAVIOR = new AdvancedExplosionBehavior(false, false, Optional.of(0.0F), Optional.empty());

	@Override
	public MapCodec<PygmyBoomshroomColonyBlock> getCodec() {
		return CODEC;
	}

	public PygmyBoomshroomColonyBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState()
			.with(NORTH_EAST, true)
			.with(NORTH_WEST, true)
			.with(SOUTH_EAST, true)
			.with(SOUTH_WEST, true)
		);
	}

	private static VoxelShape getEnclosingShape(BlockState state) {
		// Simple bypass. Otherwise the entire list might get iterated through only for the shape to not change.
		if (state.get(NORTH_EAST) && state.get(SOUTH_WEST) || state.get(NORTH_WEST) && state.get(SOUTH_EAST))
			return COMPLETE_SHAPE;

		VoxelShape shape = VoxelShapes.empty();

		for (BooleanProperty property : getNonEmptyCornerProperties(state))
			shape = VoxelShapes.union(shape, SHAPE_MAP.get(property));

		return shape;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return getEnclosingShape(state);
	}

	@Override
	protected boolean canReplace(BlockState state, ItemPlacementContext context) {
		return this.shouldAddCorner(state, context) || super.canReplace(state, context);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState placementState = super.getPlacementState(ctx);

		if (placementState == null)
			return null;

		World world = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();

		BlockState blockState = world.getBlockState(blockPos);

		if (!blockState.isOf(this)) {
			blockState = placementState
				.with(NORTH_EAST, false)
				.with(NORTH_WEST, false)
				.with(SOUTH_EAST, false)
				.with(SOUTH_WEST, false);
		}

		return blockState.with(getPropertyFromRelativePos(ctx.getHitPos(), blockPos), true);
	}

    private static BooleanProperty getPropertyFromRelativePos(Vec3d hitPos, BlockPos blockPos) {
        return getPropertyFromRelativePos(hitPos.x - blockPos.getX(), hitPos.z - blockPos.getZ());
    }

    private static BooleanProperty getPropertyFromRelativePos(double x, double z) {
		if (x < 0.5)
			return z < 0.5 ? NORTH_WEST : SOUTH_WEST;

		return z < 0.5 ? NORTH_EAST : SOUTH_EAST;
	}

	private boolean shouldAddCorner(BlockState state, ItemPlacementContext ctx) {
		if (ctx.shouldCancelInteraction() || !ctx.getStack().isOf(this.asItem()))
			return false;

		Vec3d hitPos = ctx.getHitPos();
		BlockPos blockPos = ctx.getBlockPos();

		BooleanProperty property = getPropertyFromRelativePos(hitPos.x - blockPos.getX(), hitPos.z - blockPos.getZ());

		return !state.get(property);
	}

	@Override
	protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
		for (BooleanProperty property : getNonEmptyCornerProperties(state)) {
			VoxelShape shape = SHAPE_MAP.get(property).offset(pos);
			Box shapeBounds = shape.getBoundingBox();

			if (!shapeBounds.intersects(entity.getBoundingBox()))
				continue;

			state = state.with(property, false);

			world.setBlockState(pos, isEmpty(state) ? world.getFluidState(pos).getBlockState() : state, NOTIFY_ALL);
			explode(world, shapeBounds.getCenter());
		}
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.getMainHandStack().isOf(Items.SHEARS) && !isEmpty(state))
			forEachNonEmptyCorner(state, pos, (property, shape) -> explode(world, shape.getBoundingBox().getCenter()));

        return super.onBreak(world, pos, state, player);
	}

	private static void forEachNonEmptyCorner(BlockState state, BlockPos pos, BiConsumer<BooleanProperty, VoxelShape> callback) {
		for (BooleanProperty property : getNonEmptyCornerProperties(state))
			callback.accept(property, SHAPE_MAP.get(property).offset(pos));
	}

	private static List<BooleanProperty> getNonEmptyCornerProperties(BlockState state) {
		return Arrays.stream(CORNER_PROPERTIES).filter(state::get).toList();
	}

	private static boolean isEmpty(BlockState state) {
		return getNonEmptyCornerProperties(state).isEmpty();
	}

	private static boolean isIncomplete(BlockState state) {
		return getNonEmptyCornerProperties(state).size() < 4;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder.add(CORNER_PROPERTIES));
	}

	private static void explode(World world, Vec3d pos) {
		SalinationEffect.spawnBoomshroomAoeCloud(world, pos);
		world.createExplosion(null, null, EXPLOSION_BEHAVIOR, pos, 1, false, World.ExplosionSourceType.BLOCK);
	}

	@Override
	public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
		return isIncomplete(state);
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		for (BooleanProperty property : Util.copyShuffled(CORNER_PROPERTIES, random)) {
            if (!state.get(property)) {
                world.setBlockState(pos, state.with(property, true), NOTIFY_ALL);
                return;
            }
        }
	}

}
