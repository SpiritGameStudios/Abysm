package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;

import dev.spiritstudios.abysm.entity.effect.SalinationEffect;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.AdvancedExplosionBehavior;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.Optional;

public class BoomshroomBlock extends UnderwaterPlantBlock {

	private static final MapCodec<BoomshroomBlock> CODEC = createCodec(BoomshroomBlock::new);
	private static final VoxelShape SHAPE = Block.createColumnShape(6.0, 0.0, 6.0);
	private static final ExplosionBehavior EXPLOSION_BEHAVIOR = new AdvancedExplosionBehavior(false, true, Optional.empty(), Optional.empty());

	@Override
	public MapCodec<BoomshroomBlock> getCodec() {
		return CODEC;
	}

	public BoomshroomBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
		world.removeBlock(pos, false);
		explode(world, pos);
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!player.getMainHandStack().isOf(Items.SHEARS))
			explode(world, pos);

		return super.onBreak(world, pos, state, player);
	}

	private static void explode(World world, BlockPos pos) {
		Vec3d vec3d = pos.toBottomCenterPos();

		SalinationEffect.spawnBoomshroomAoeCloud(world, vec3d);
		world.createExplosion(null, null, EXPLOSION_BEHAVIOR, vec3d, 1, false, World.ExplosionSourceType.BLOCK);
	}

}
