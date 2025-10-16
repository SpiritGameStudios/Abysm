package dev.spiritstudios.abysm.fluids;

import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.entity.effect.AbysmStatusEffects;
import dev.spiritstudios.abysm.entity.effect.SalinationEffect;
import dev.spiritstudios.abysm.item.AbysmItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class BrineFluid extends FlowableFluid {

	@Override
	public Fluid getFlowing() {
		return AbysmFluids.FLOWING_BRINE;
	}

	@Override
	public Fluid getStill() {
		return AbysmFluids.BRINE;
	}

	@Override
	public Item getBucketItem() {
		return AbysmItems.BRINE_BUCKET;
	}

	@Override
	public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
        if (!world.getBlockState(pos).isAir() || world.getBlockState(pos).isOpaqueFullCube())
            return;

        if (random.nextInt(100) == 0) {
            double d = pos.getX() + random.nextDouble();
            double e = pos.getY() + 1.0;
            double f = pos.getZ() + random.nextDouble();
            world.addParticleClient(ParticleTypes.LAVA, d, e, f, 0.0, 0.0, 0.0);
            world.playSoundClient(
                d, e, f, SoundEvents.BLOCK_LAVA_POP, SoundCategory.AMBIENT, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false
            );
        }

        if (random.nextInt(200) == 0) {
            world.playSoundClient(
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                SoundEvents.BLOCK_LAVA_AMBIENT,
                SoundCategory.AMBIENT,
                0.2F + random.nextFloat() * 0.2F,
                0.9F + random.nextFloat() * 0.15F,
                false
            );
        }
    }

	@Nullable
	@Override
	public ParticleEffect getParticle() {
		return ParticleTypes.DRIPPING_WATER;
	}

	@Override
	protected boolean isInfinite(ServerWorld world) {
		return false;
	}

	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
		BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Block.dropStacks(state, world, pos, blockEntity);
	}

	@Override
	protected void onEntityCollision(World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
		if (entity instanceof LivingEntity living)
			living.addStatusEffect(new StatusEffectInstance(AbysmStatusEffects.SALINATION, SalinationEffect.BRINE_CONTACT_EFFECT_DURATION, 1));
	}

	@Override
	public int getMaxFlowDistance(WorldView world) {
		return 4;
	}

	@Override
	public BlockState toBlockState(FluidState state) {
		return AbysmBlocks.BRINE.getDefaultState().with(FluidBlock.LEVEL, getBlockStateLevel(state));
	}

	public static boolean canReplaceWithWater(ServerWorld world, BlockPos pos) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		int i = 0;

		for (Direction direction : Direction.Type.HORIZONTAL) {
			BlockPos blockPos = mutable.set(pos, direction);
			FluidState fluidState = world.getFluidState(blockPos);

			if (fluidState.isEqualAndStill(Fluids.WATER))
				i++;
		}

		return i >= 2;
	}

	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == AbysmFluids.BRINE || fluid == AbysmFluids.FLOWING_BRINE;
	}

	@Override
	public int getLevelDecreasePerBlock(WorldView world) {
		return 1;
	}

	@Override
	public int getTickRate(WorldView world) {
		return 20;
	}

	@Override
	public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
		return fluid.matchesType(Fluids.LAVA);
	}

	@Override
	protected float getBlastResistance() {
		return 100.0F;
	}

	@Override
	public Optional<SoundEvent> getBucketFillSound() {
		return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
	}

	public static class Flowing extends BrineFluid {

		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}

		@Override
		public int getLevel(FluidState state) {
			return state.get(LEVEL);
		}

		@Override
		public boolean isStill(FluidState state) {
			return false;
		}

	}

	public static class Still extends BrineFluid {

		@Override
		public int getLevel(FluidState state) {
			return 8;
		}

		@Override
		public boolean isStill(FluidState state) {
			return true;
		}

	}

}