package dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem;

import dev.spiritstudios.abysm.world.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.network.HappyEntityParticlesS2CPayload;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @see net.minecraft.world.entity.animal.Bee.BeePollinateGoal what am I doing
 */
@SuppressWarnings("JavadocReference")
public class FindPlantsGoal extends Goal {
	protected final PathfinderMob mob;
	protected final boolean forceAquatic;
	protected final float navigationSpeed;
	protected final float moveControlSpeed;
	protected Supplier<Integer> rangeSupplier;

	private int eatingTicks; // this goal's version of pollination
	private int lastEatingTick;
	private boolean running;
	@Nullable
	private Vec3 nextTarget;
	private int ticks;
	private Long2LongOpenHashMap unreachablePosCache = new Long2LongOpenHashMap();

	private BlockPos plantPos = null;

	public FindPlantsGoal(PathfinderMob mob) {
		this(mob, true);
	}

	public FindPlantsGoal(PathfinderMob mob, boolean forceAquatic) {
		this(mob, 1.2F, 0.35F, forceAquatic);
	}

	public FindPlantsGoal(PathfinderMob mob, float navigationSpeed, float moveControlSpeed, boolean forceAquatic) {
//		if (!(mob instanceof PlantEater)) {
//			throw new IllegalArgumentException("Tried to create FindPlantsGoal for non PlantEater entity");
//		}

		this.mob = mob;
		this.navigationSpeed = navigationSpeed;
		this.moveControlSpeed = moveControlSpeed;
		this.forceAquatic = forceAquatic;

		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	public void setRangeSupplier(Supplier<Integer> supplier) {
		this.rangeSupplier = supplier;
	}

	@Override
	public boolean canUse() {
		if (!this.testAquatic()) {
			return false;
		}

		EcologicalEntity ecologicalEntity = (EcologicalEntity) this.mob;
		if(!ecologicalEntity.shouldScavenge()) return false;

		// Only check for nearby plants every second to reduce lage
		if(this.mob.tickCount % 20 != 1) return false;

//		PlantEater plantEater = (PlantEater) this.obj;
//		if (!plantEater.isHungryHerbivore()) {
//			return false;
//		}

		return this.getPlant()
			.map(pos -> {
				this.plantPos = pos;
				this.mob.getNavigation().moveTo(
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
		return !this.forceAquatic || this.mob.isInWater();
	}

	@Override
	public boolean canContinueToUse() {
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
	 * @see dev.spiritstudios.abysm.world.entity.ruins.LectorfinEntity#hurtServer(ServerLevel, DamageSource, float)
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
			if (!this.mob.level().isClientSide()) {
				new HappyEntityParticlesS2CPayload(this.mob, ParticleTypes.HAPPY_VILLAGER, 5).send(this.mob);
			}
		}

		this.running = false;
		this.plantPos = null;
		this.mob.getNavigation().stop();
		((EcologicalEntity) this.mob).setShouldScavenge(false);
	}

	@Override
	public boolean requiresUpdateEveryTick() {
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
			new HappyEntityParticlesS2CPayload(this.mob, new BlockParticleOption(ParticleTypes.BLOCK, mob.level().getBlockState(this.plantPos)), 5).send(this.mob);
		}

		//noinspection DataFlowIssue
		Vec3 potentialTarget = Vec3.atBottomCenterOf(this.plantPos).add(0.0, 0.6F, 0.0);
		if (potentialTarget.distanceTo(this.mob.position()) > 1.0) {
			this.nextTarget = potentialTarget;
			this.moveToNextTarget();
			return;
		}
		if (this.nextTarget == null) {
			this.nextTarget = potentialTarget;
		}
		boolean veryClose = this.mob.position().distanceTo(this.nextTarget) <= 0.1;
		boolean shouldStartMoving = true;
		if (!veryClose && this.ticks > 600) {
			this.plantPos = null;
			return;
		}

		if (veryClose) {
			if (this.mob.getRandom().nextInt(25) == 0) {
				this.nextTarget = new Vec3(potentialTarget.x() + this.getRandomOffset(), potentialTarget.y(), potentialTarget.z() + this.getRandomOffset());
				this.mob.getNavigation().stop();
			} else {
				shouldStartMoving = false;
			}

			this.mob.getLookControl().setLookAt(potentialTarget.x(), potentialTarget.y(), potentialTarget.z());
		}

		if (shouldStartMoving) {
			this.moveToNextTarget();
		}

		this.eatingTicks++;
		if (this.mob.getRandom().nextFloat() < 0.09F && this.eatingTicks > this.lastEatingTick + 40) {
			this.lastEatingTick = this.eatingTicks;
			this.mob.playSound(SoundEvents.GENERIC_EAT.value(), 1.0F, 1.0F);
		}
	}

	protected void moveToNextTarget() {
		if (this.nextTarget == null) {
			return;
		}
		this.mob.getMoveControl().setWantedPosition(this.nextTarget.x(), this.nextTarget.y(), this.nextTarget.z(), this.moveControlSpeed);
	}

	protected float getRandomOffset() {
		return (this.mob.getRandom().nextFloat() * 2.0F - 1.0F) * (1.0F / 3.0F); // yay magic constants
	}

	protected Optional<BlockPos> getPlant() {
		int range = this.rangeSupplier.get();
		Long2LongOpenHashMap unreachableCache = new Long2LongOpenHashMap();

		for (BlockPos blockPos : BlockPos.withinManhattan(this.mob.blockPosition(), range, range, range)) {
			long l = this.unreachablePosCache.getOrDefault(blockPos.asLong(), Long.MIN_VALUE);

			if (this.mob.level().getGameTime() < l) {
				unreachableCache.put(blockPos.asLong(), l);
			} else {
				BlockState blockState = this.mob.level().getBlockState(blockPos);
				if (((EcologicalEntity) this.mob).getEcosystemData().plants().contains(blockState.getBlock())) {
					Path path = this.mob.getNavigation().createPath(blockPos, 1);
					if (path != null && path.canReach()) {
						return Optional.of(blockPos);
					}

					unreachableCache.put(blockPos.asLong(), this.mob.level().getGameTime() + 600L);
				}
			}
		}

		this.unreachablePosCache = unreachableCache;
		return Optional.empty();
	}
}
