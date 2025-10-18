package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.entity.effect.AbysmStatusEffects;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * A cauldron filled with lava.
 */
public class BrineCauldronBlock extends AbstractCauldronBlock {

	public static final MapCodec<BrineCauldronBlock> CODEC = createCodec(BrineCauldronBlock::new);
	public static final CauldronBehavior.CauldronBehaviorMap BEHAVIOR = CauldronBehavior.createMap("brine");

	private static final VoxelShape BRINE_SHAPE = Block.createColumnShape(12.0, 4.0, 15.0);
	private static final VoxelShape INSIDE_COLLISION_SHAPE = VoxelShapes.union(OUTLINE_SHAPE, BRINE_SHAPE);

	@Override
	public MapCodec<BrineCauldronBlock> getCodec() {
		return CODEC;
	}

	public BrineCauldronBlock(AbstractBlock.Settings settings) {
		super(settings, BEHAVIOR);
	}

	@Override
	protected double getFluidHeight(BlockState state) {
		return BRINE_SHAPE.getMax(Direction.Axis.Y) / 16;
	}

	@Override
	public boolean isFull(BlockState state) {
		return true;
	}

	@Override
	protected VoxelShape getInsideCollisionShape(BlockState state, BlockView world, BlockPos pos, Entity entity) {
		return INSIDE_COLLISION_SHAPE;
	}

	@Override
	protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
		if (entity instanceof LivingEntity living)
			living.addStatusEffect(new StatusEffectInstance(AbysmStatusEffects.SALINATION));
	}

	@Override
	protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return 3;
	}

}
