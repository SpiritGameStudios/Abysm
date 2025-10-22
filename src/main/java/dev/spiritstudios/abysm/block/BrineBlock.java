package dev.spiritstudios.abysm.block;

import com.google.common.collect.Maps;
import dev.spiritstudios.abysm.entity.effect.SalinationEffect;
import dev.spiritstudios.abysm.particle.AbysmParticleTypes;
import net.minecraft.block.*;
import net.minecraft.entity.CollisionEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.ToIntFunction;

/**
 * @author axialeaa
 */
public class BrineBlock extends WaterloggableTranslucentBlock implements FluidDrainable {

    public static final ToIntFunction<BlockState> LUMINANCE_FUNCTION = state -> 5;
    private static final float PARTICLE_EMISSION_CHANCE = 0.3F;

    public static final BooleanProperty UP = Properties.UP;
    public static final BooleanProperty NORTH = Properties.NORTH;
    public static final BooleanProperty SOUTH = Properties.SOUTH;
    public static final BooleanProperty EAST = Properties.EAST;
    public static final BooleanProperty WEST = Properties.WEST;

    public static final Map<Direction, BooleanProperty> CONNECTION_PROPERTIES = Util.make(Maps.newHashMap(), map -> {
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.SOUTH, SOUTH);
        map.put(Direction.EAST, EAST);
        map.put(Direction.WEST, WEST);
    });

    public BrineBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
            .with(UP, false)
            .with(NORTH, false)
            .with(SOUTH, false)
            .with(EAST, false)
            .with(WEST, false)
        );
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = super.getPlacementState(ctx);

        if (blockState == null)
            return null;

        World world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();

        for (Map.Entry<Direction, BooleanProperty> entry : CONNECTION_PROPERTIES.entrySet()) {
            Direction direction = entry.getKey();
            BooleanProperty property = entry.getValue();

            blockState = blockState.with(property, this.isConnectedToOther(world, blockPos, direction));
        }

        return blockState.with(UP, world.getBlockState(blockPos.up()).isOf(this));
    }

    private boolean isConnectedToOther(BlockView world, BlockPos pos, Direction direction) {
        BlockPos neighborPos = pos.offset(direction);
        BlockState neighborState = world.getBlockState(neighborPos);

        return neighborState.isOf(this);
    }

    private static BooleanProperty getConnectionProperty(Direction direction) {
        return CONNECTION_PROPERTIES.get(direction);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(UP) ? VoxelShapes.fullCube() : Block.createColumnShape(16, 0, 14);
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
        if (entity instanceof LivingEntity living)
            living.addStatusEffect(SalinationEffect.createDefaultInstance());

        entity.slowMovement(state, new Vec3d(0.8, 0.75, 0.8));
        handler.addEvent(CollisionEvent.EXTINGUISH);
    }

    @Override
    protected boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if (!stateFrom.isOf(this))
            return false;

        return direction.getAxis().isVertical() || !state.get(UP) || stateFrom.get(UP);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        BlockState blockState = super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);

        if (direction == Direction.UP)
            blockState = blockState.with(UP, neighborState.isOf(this));

        if (direction.getAxis().isHorizontal())
            blockState = blockState.with(getConnectionProperty(direction), this.isConnectedToOther(world, pos, direction));

        return blockState;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextFloat() < PARTICLE_EMISSION_CHANCE)
            ParticleUtil.spawnParticles(world, pos, AbysmParticleTypes.BRINE_SALT, ConstantIntProvider.create(1), Direction.UP, () -> Vec3d.ZERO, 0.5);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public ItemStack tryDrainFluid(@Nullable LivingEntity drainer, WorldAccess world, BlockPos pos, BlockState state) {
        world.removeBlock(pos, false);

        if (!world.isClient())
            world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, pos, Block.getRawIdFromState(state));

        return new ItemStack(AbysmBlocks.BRINE);
    }

    @Override
    public Optional<SoundEvent> getBucketFillSound() {
        return Optional.of(SoundEvents.ITEM_BUCKET_FILL_POWDER_SNOW);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(UP, NORTH, SOUTH, EAST, WEST));
    }

}
