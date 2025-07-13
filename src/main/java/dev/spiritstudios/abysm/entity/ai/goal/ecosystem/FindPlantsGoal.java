package dev.spiritstudios.abysm.entity.ai.goal.ecosystem;

import dev.spiritstudios.abysm.ecosystem.entity.PlantEater;
import dev.spiritstudios.abysm.networking.EntityFinishedEatingS2CPayload;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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

	protected final PathAwareEntity obj;
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

	public FindPlantsGoal(PathAwareEntity mob) {
		this(mob, true);
	}

	public FindPlantsGoal(PathAwareEntity mob, boolean forceAquatic) {
		this(mob, 1.2F, 0.35F, forceAquatic);
	}

	public FindPlantsGoal(PathAwareEntity mob, float navigationSpeed, float moveControlSpeed, boolean forceAquatic) {
		if (!(mob instanceof PlantEater)) {
			throw new IllegalArgumentException("Tried to create FindPlantsGoal for non PlantEater entity");
		}

		this.obj = mob;
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

		PlantEater plantEater = (PlantEater) this.obj;
		if (!plantEater.isHungryHerbivore()) {
			return false;
		}

		return this.getPlant()
			.map(pos -> {
				plantEater.setPlantPos(pos);
				this.obj.getNavigation().startMovingTo(
					pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
					this.navigationSpeed
				);

				return true;
			}).orElseGet(() -> {
				plantEater.setTicksUntilHunger(MathHelper.nextInt(this.obj.getRandom(), 20, 60));
				return false;
			});
	}

	/**
	 * Ensures that the entity is in a valid position to start
	 *
	 * @return false if the test fails (is aquatic and not in water)
	 */
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean testAquatic() {
		return !this.forceAquatic || this.obj.isTouchingWater();
	}

	@Override
	public boolean shouldContinue() {
		return this.running &&
			((PlantEater) this.obj).hasPlant() &&
			this.testAquatic() &&
			(!this.ate() || this.obj.getRandom().nextFloat() < 0.2F);
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
		((PlantEater) this.obj).resetTicksUntilHunger();
	}

	@Override
	public void stop() {
		PlantEater plantEater = (PlantEater) this.obj;
		if (this.ate()) {
			plantEater.setNotHungryAnymoreYay();
			if (!this.obj.getWorld().isClient) {
				EntityFinishedEatingS2CPayload payload = new EntityFinishedEatingS2CPayload(this.obj, ParticleTypes.HAPPY_VILLAGER);
				PlayerLookup.tracking(this.obj).forEach(serverPlayerEntity -> {
					ServerPlayNetworking.send(serverPlayerEntity, payload);
				});
			}
		}

		this.running = false;
		this.obj.getNavigation().stop();
		plantEater.setTicksUntilHunger(200);
	}

	@Override
	public boolean shouldRunEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		PlantEater plantEater = (PlantEater) this.obj;
		if (!plantEater.hasPlant()) {
			return;
		}
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
			plantEater.clearPlantPos();
			this.running = false;
			plantEater.setTicksUntilHunger(200);
			return;
		}
		//noinspection DataFlowIssue
		Vec3d potentialTarget = Vec3d.ofBottomCenter(plantEater.getPlantPos()).add(0.0, 0.6F, 0.0);
		if (potentialTarget.distanceTo(this.obj.getPos()) > 1.0) {
			this.nextTarget = potentialTarget;
			this.moveToNextTarget();
			return;
		}
		if (this.nextTarget == null) {
			this.nextTarget = potentialTarget;
		}
		boolean veryClose = this.obj.getPos().distanceTo(this.nextTarget) <= 0.1;
		boolean shouldStartMoving = true;
		if (!veryClose && this.ticks > 600) {
			plantEater.clearPlantPos();
			return;
		}

		if (veryClose) {
			if (this.obj.getRandom().nextInt(25) == 0) {
				this.nextTarget = new Vec3d(potentialTarget.getX() + this.getRandomOffset(), potentialTarget.getY(), potentialTarget.getZ() + this.getRandomOffset());
				this.obj.getNavigation().stop();
			} else {
				shouldStartMoving = false;
			}

			this.obj.getLookControl().lookAt(potentialTarget.getX(), potentialTarget.getY(), potentialTarget.getZ());
		}

		if (shouldStartMoving) {
			this.moveToNextTarget();
		}

		this.eatingTicks++;
		if (this.obj.getRandom().nextFloat() < 0.09F && this.eatingTicks > this.lastEatingTick + 40) {
			this.lastEatingTick = this.eatingTicks;
			this.obj.playSound(SoundEvents.ENTITY_GENERIC_EAT.value(), 1.0F, 1.0F);
		}
	}

	protected void moveToNextTarget() {
		if (this.nextTarget == null) {
			return;
		}
		this.obj.getMoveControl().moveTo(this.nextTarget.getX(), this.nextTarget.getY(), this.nextTarget.getZ(), this.moveControlSpeed);
	}

	protected float getRandomOffset() {
		return (this.obj.getRandom().nextFloat() * 2.0F - 1.0F) * (1.0F / 3.0F); // yay magic constants
	}

	protected Optional<BlockPos> getPlant() {
		int range = this.rangeSupplier.get();
		Long2LongOpenHashMap unreachableCache = new Long2LongOpenHashMap();

		for (BlockPos blockPos : BlockPos.iterateOutwards(this.obj.getBlockPos(), range, range, range)) {
			long l = this.unreachablePosCache.getOrDefault(blockPos.asLong(), Long.MIN_VALUE);

			if (this.obj.getWorld().getTime() < l) {
				unreachableCache.put(blockPos.asLong(), l);
			} else {
				BlockState blockState = this.obj.getWorld().getBlockState(blockPos);
				if (((PlantEater) this.obj).getEcosystemType().plants().contains(blockState.getBlock())) {
					Path path = this.obj.getNavigation().findPathTo(blockPos, 1);
					if (path != null && path.reachesTarget()) {
						return Optional.of(blockPos);
					}

					unreachableCache.put(blockPos.asLong(), this.obj.getWorld().getTime() + 600L);
				}
			}
		}

		this.unreachablePosCache = unreachableCache;
		return Optional.empty();
	}
}
