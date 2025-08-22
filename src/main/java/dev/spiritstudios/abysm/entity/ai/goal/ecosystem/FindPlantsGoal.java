package dev.spiritstudios.abysm.entity.ai.goal.ecosystem;

import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.networking.HappyEntityParticlesS2CPayload;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @see net.minecraft.entity.passive.BeeEntity.PollinateGoal what am I doing
 */
@SuppressWarnings("JavadocReference")
public class FindPlantsGoal extends Goal {
	protected final PathAwareEntity mob;
	protected final boolean forceAquatic;
	protected final float navigationSpeed;
	protected final float moveControlSpeed;
	protected Supplier<Integer> rangeSupplier;

	private int eatingTicks; // this goal's version of pollination
	private int lastEatingTick;
	private boolean running;
	@Nullable
	private Vec3d nextTarget;
	private int ticks;
	private Long2LongOpenHashMap unreachablePosCache = new Long2LongOpenHashMap();

	private BlockPos plantPos = null;

	public FindPlantsGoal(PathAwareEntity mob) {
		this(mob, true);
	}

	public FindPlantsGoal(PathAwareEntity mob, boolean forceAquatic) {
		this(mob, 1.2F, 0.35F, forceAquatic);
	}

	public FindPlantsGoal(PathAwareEntity mob, float navigationSpeed, float moveControlSpeed, boolean forceAquatic) {
//		if (!(mob instanceof PlantEater)) {
//			throw new IllegalArgumentException("Tried to create FindPlantsGoal for non PlantEater entity");
//		}

		this.mob = mob;
		this.navigationSpeed = navigationSpeed;
		this.moveControlSpeed = moveControlSpeed;
		this.forceAquatic = forceAquatic;

		this.setControls(EnumSet.of(Goal.Control.MOVE));
	}

	public void setRangeSupplier(Supplier<Integer> supplier) {
		this.rangeSupplier = supplier;
	}

	@Override
	public boolean canStart() {
		if (!this.testAquatic()) {
			return false;
		}

		EcologicalEntity ecologicalEntity = (EcologicalEntity) this.mob;
		if(!ecologicalEntity.shouldScavenge()) return false;

		// Only check for nearby plants every second to reduce lage
		if(this.mob.age % 20 != 1) return false;

//		PlantEater plantEater = (PlantEater) this.obj;
//		if (!plantEater.isHungryHerbivore()) {
//			return false;
//		}

		return this.getPlant()
			.map(pos -> {
				this.plantPos = pos;
				this.mob.getNavigation().startMovingTo(
					pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
					this.navigationSpeed
				);

				return true;
			}).orElseGet(() -> {
//				plantEater.setTicksUntilHunger(MathHelper.nextInt(this.mob.getRandom(), 20, 60));
				return false;
			});
	}

	/**
	 * Ensures that the entity is in a valid position to start
	 *
	 * @return false if the test fails (is aquatic and not in water)
	 */
	public boolean testAquatic() {
		return !this.forceAquatic || this.mob.isTouchingWater();
	}

	@Override
	public boolean shouldContinue() {
		return this.running &&
			this.plantPos != null &&
			this.testAquatic() &&
			(!this.ate() || this.mob.getRandom().nextFloat() < 0.2F);
	}

	protected boolean ate() {
		// done eating, I assume?
		return this.eatingTicks > 200;
	}

	@SuppressWarnings("unused")
	public boolean isRunning() {
		return this.running;
	}

	/**
	 * This method should be called when the entity takes damage from a source it is
	 * (likely) not invulnerable to
	 *
	 * @see dev.spiritstudios.abysm.entity.ruins.LectorfinEntity#damage(ServerWorld, DamageSource, float)
	 */
	public void cancel() {
		this.running = false;
	}

	@Override
	public void start() {
		this.eatingTicks = 0;
		this.ticks = 0;
		this.lastEatingTick = 0;
		this.running = true;
	}

	@Override
	public void stop() {
		if (this.ate()) {
			if (!this.mob.getWorld().isClient) {
				new HappyEntityParticlesS2CPayload(this.mob, ParticleTypes.HAPPY_VILLAGER, 5).send(this.mob);
			}
		}

		this.running = false;
		this.plantPos = null;
		this.mob.getNavigation().stop();
		((EcologicalEntity) this.mob).setShouldScavenge(false);
	}

	@Override
	public boolean shouldRunEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		if (this.plantPos == null) return;
		this.ticks++;
		/*
		// debug code
		if (this.obj.getWorld() instanceof ServerWorld serverWorld) {
			var players = serverWorld.getPlayers();
			if (!players.isEmpty()) {
				players.getFirst().sendMessage(Text.literal("ticks: " + this.ticks + ", eatingTicks: " + this.eatingTicks), true);
			}
		}
		 */
		if (this.ticks > 600) {
			this.stop();
			return;
		}

		// visual indicator - this only gets called on the server
		if(this.ticks % 20 == 0) {
			new HappyEntityParticlesS2CPayload(this.mob, new BlockStateParticleEffect(ParticleTypes.BLOCK, mob.getWorld().getBlockState(this.plantPos)), 5).send(this.mob);
		}

		//noinspection DataFlowIssue
		Vec3d potentialTarget = Vec3d.ofBottomCenter(this.plantPos).add(0.0, 0.6F, 0.0);
		if (potentialTarget.distanceTo(this.mob.getPos()) > 1.0) {
			this.nextTarget = potentialTarget;
			this.moveToNextTarget();
			return;
		}
		if (this.nextTarget == null) {
			this.nextTarget = potentialTarget;
		}
		boolean veryClose = this.mob.getPos().distanceTo(this.nextTarget) <= 0.1;
		boolean shouldStartMoving = true;
		if (!veryClose && this.ticks > 600) {
			this.plantPos = null;
			return;
		}

		if (veryClose) {
			if (this.mob.getRandom().nextInt(25) == 0) {
				this.nextTarget = new Vec3d(potentialTarget.getX() + this.getRandomOffset(), potentialTarget.getY(), potentialTarget.getZ() + this.getRandomOffset());
				this.mob.getNavigation().stop();
			} else {
				shouldStartMoving = false;
			}

			this.mob.getLookControl().lookAt(potentialTarget.getX(), potentialTarget.getY(), potentialTarget.getZ());
		}

		if (shouldStartMoving) {
			this.moveToNextTarget();
		}

		this.eatingTicks++;
		if (this.mob.getRandom().nextFloat() < 0.09F && this.eatingTicks > this.lastEatingTick + 40) {
			this.lastEatingTick = this.eatingTicks;
			this.mob.playSound(SoundEvents.ENTITY_GENERIC_EAT.value(), 1.0F, 1.0F);
		}
	}

	protected void moveToNextTarget() {
		if (this.nextTarget == null) {
			return;
		}
		this.mob.getMoveControl().moveTo(this.nextTarget.getX(), this.nextTarget.getY(), this.nextTarget.getZ(), this.moveControlSpeed);
	}

	protected float getRandomOffset() {
		return (this.mob.getRandom().nextFloat() * 2.0F - 1.0F) * (1.0F / 3.0F); // yay magic constants
	}

	protected Optional<BlockPos> getPlant() {
		int range = this.rangeSupplier.get();
		Long2LongOpenHashMap unreachableCache = new Long2LongOpenHashMap();

		for (BlockPos blockPos : BlockPos.iterateOutwards(this.mob.getBlockPos(), range, range, range)) {
			long l = this.unreachablePosCache.getOrDefault(blockPos.asLong(), Long.MIN_VALUE);

			if (this.mob.getWorld().getTime() < l) {
				unreachableCache.put(blockPos.asLong(), l);
			} else {
				BlockState blockState = this.mob.getWorld().getBlockState(blockPos);
				if (((EcologicalEntity) this.mob).getEcosystemType().plants().contains(blockState.getBlock())) {
					Path path = this.mob.getNavigation().findPathTo(blockPos, 1);
					if (path != null && path.reachesTarget()) {
						return Optional.of(blockPos);
					}

					unreachableCache.put(blockPos.asLong(), this.mob.getWorld().getTime() + 600L);
				}
			}
		}

		this.unreachablePosCache = unreachableCache;
		return Optional.empty();
	}
}
