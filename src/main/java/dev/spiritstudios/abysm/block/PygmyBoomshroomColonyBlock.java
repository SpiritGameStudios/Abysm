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
import net.minecraft.world.tick.ScheduledTickView;

import java.util.*;

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

	private static final Map<BooleanProperty, VoxelShape> SHAPE_MAP = Util.make(Maps.newHashMap(), map -> {
		map.put(NORTH_EAST, createCuboidShape(8, 0, 0, 16, SHAPE_HEIGHT, 8));
		map.put(NORTH_WEST, createCuboidShape(0, 0, 0, 8, SHAPE_HEIGHT, 8));
		map.put(SOUTH_EAST, createCuboidShape(8, 0, 8, 16, SHAPE_HEIGHT, 16));
		map.put(SOUTH_WEST, createCuboidShape(0, 0, 8, 8, SHAPE_HEIGHT, 16));
	});

	private static final MapCodec<PygmyBoomshroomColonyBlock> CODEC = createCodec(PygmyBoomshroomColonyBlock::new);
	private static final ExplosionBehavior EXPLOSION_BEHAVIOR = new AdvancedExplosionBehavior(false, false, Optional.of(0.1F), Optional.empty());

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

	private static VoxelShape getCornerEnclosingShape(BlockState state) {
		VoxelShape shape = VoxelShapes.empty();

		for (BooleanProperty property : CORNER_PROPERTIES) {
			if (state.get(property))
				shape = VoxelShapes.union(shape, SHAPE_MAP.get(property));
		}

		return shape.isEmpty() ? shape : VoxelShapes.cuboid(shape.getBoundingBox());
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return getCornerEnclosingShape(state);
	}

	@Override
	protected boolean canReplace(BlockState state, ItemPlacementContext context) {
		return this.shouldAddCorner(state, context) || super.canReplace(state, context);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		World world = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();

		BlockState blockState = world.getBlockState(blockPos);

		if (!blockState.isOf(this)) {
			blockState = Objects.requireNonNull(super.getPlacementState(ctx))
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
        for (Map.Entry<BooleanProperty, VoxelShape> entry : SHAPE_MAP.entrySet()) {
            BooleanProperty property = entry.getKey();
            VoxelShape shape = entry.getValue();

			Box shapeBounds = shape.offset(pos).getBoundingBox();

			if (state.get(property) && shapeBounds.intersects(entity.getBoundingBox())) {
				world.setBlockState(pos, state.with(property, false), NOTIFY_ALL);
				explode(world, shapeBounds.getCenter());
			}
        }
    }

	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		for (BooleanProperty property : CORNER_PROPERTIES) {
			if (state.get(property))
				return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
		}

		return Blocks.AIR.getDefaultState(); // resolves an invalid (functionally air but placement-blocking) state with all 4 corners set to false
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!player.getMainHandStack().isOf(Items.SHEARS))
			explode(world, pos.toBottomCenterPos());

		return super.onBreak(world, pos, state, player);
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
		return true;
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return !state.get(NORTH_EAST) || !state.get(NORTH_WEST) || !state.get(SOUTH_EAST) || !state.get(SOUTH_WEST);
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		List<BooleanProperty> properties = new ArrayList<>(List.of(CORNER_PROPERTIES));
		Util.shuffle(properties, random);

		for (BooleanProperty property : properties) {
            if (!state.get(property)) {
                world.setBlockState(pos, state.with(property, true), NOTIFY_ALL);
                return;
            }
        }
	}

}
