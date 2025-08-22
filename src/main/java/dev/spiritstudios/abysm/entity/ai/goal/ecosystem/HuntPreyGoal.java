package dev.spiritstudios.abysm.entity.ai.goal.ecosystem;

import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

/**
 * If this entity is allowed to hunt, it'll search for nearby targets and begin a hunt upon finding one.
 *
 * @see TrackTargetGoal
 * @see net.minecraft.entity.ai.goal.ActiveTargetGoal
 */
public class HuntPreyGoal extends TrackTargetGoal {

	private static final int DEFAULT_RECIPROCAL_CHANCE = 10;

	/**
	 * The reciprocal of chance to actually search for a target on every tick
	 * when this goal is not started. This is also the average number of ticks
	 * between each search (as in a poisson distribution).
	 */
	protected final int reciprocalChance;
	@Nullable
	protected MobEntity targetEntity;
	protected TargetPredicate targetPredicate;

	@SuppressWarnings("unused")
	public HuntPreyGoal(MobEntity mob, boolean checkVisibility) {
		this(mob, DEFAULT_RECIPROCAL_CHANCE, checkVisibility, false, null);
	}

	@SuppressWarnings("unused")
	public HuntPreyGoal(MobEntity mob, boolean checkVisibility, TargetPredicate.EntityPredicate predicate) {
		this(mob, DEFAULT_RECIPROCAL_CHANCE, checkVisibility, false, predicate);
	}

	@SuppressWarnings("unused")
	public HuntPreyGoal(MobEntity mob, boolean checkVisibility, boolean checkCanNavigate) {
		this(mob, DEFAULT_RECIPROCAL_CHANCE, checkVisibility, checkCanNavigate, null);
	}

	public HuntPreyGoal(
		MobEntity mob,
		int reciprocalChance,
		boolean checkVisibility,
		boolean checkCanNavigate,
		@Nullable TargetPredicate.EntityPredicate targetPredicate
	) {
		super(mob, checkVisibility, checkCanNavigate);
		assertIsEcologicalEntity(mob);
		this.reciprocalChance = toGoalTicks(reciprocalChance);
		this.setControls(EnumSet.of(Goal.Control.TARGET));
		this.targetPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(this.getFollowRange()).setPredicate(targetPredicate);
	}

	public static void assertIsEcologicalEntity(MobEntity mob) {
		if (!(mob instanceof EcologicalEntity)) {
			throw new IllegalArgumentException("MobEntity " + mob + " must be an instance of " + EcologicalEntity.class.getName());
		}
	}

	@Override
	public boolean canStart() {
//		if (this.reciprocalChance > 0 && this.mob.getRandom().nextInt(this.reciprocalChance) != 0) {
//			return false;
//		}

		if (!((EcologicalEntity) this.mob).shouldHunt()) {
			return false;
		}

		this.findClosestTarget();
		return this.targetEntity != null;
	}

	protected Box getSearchBox(double distance) {
		return this.mob.getBoundingBox().expand(distance, distance, distance);
	}

	protected void findClosestTarget() {
		ServerWorld serverWorld = getServerWorld(this.mob);
		Set<EntityType<? extends LivingEntity>> prey = ((EcologicalEntity) this.mob).getEcosystemType().prey();
		this.targetEntity = serverWorld.getClosestEntity(
			serverWorld.getEntitiesByType(TypeFilter.instanceOf(MobEntity.class),
				this.getSearchBox(this.getFollowRange()),
				mobEntity -> prey.contains(mobEntity.getType())),
			this.getAndUpdateTargetPredicate(),
			this.mob,
			this.mob.getX(),
			this.mob.getEyeY(),
			this.mob.getZ()
		);
	}

	@Override
	public void start() {
		this.mob.setTarget(this.targetEntity);
		((EcologicalEntity) this.mob).theHuntIsOn(this.mob.getWorld(), this.targetEntity);
		super.start();
	}

	@Override
	public boolean shouldContinue() {
		EcologicalEntity ecologicalMob = (EcologicalEntity) this.mob;
		if (ecologicalMob.shouldFailHunt()) {
			return false;
		}

		return super.shouldContinue();
	}

	@Override
	public void stop() {
		((EcologicalEntity) this.mob).onHuntEnd();
		if (this.targetEntity != null && this.targetEntity.isAlive()) {
			getServerWorld(this.mob).spawnParticles(ParticleTypes.ANGRY_VILLAGER, this.mob.getX(), this.mob.getY(), this.mob.getZ(), 1, 0, 0, 0, 0);
			((EcologicalEntity) this.targetEntity).onHuntEnd();
		}
		super.stop();
	}

	@SuppressWarnings("unused")
	protected void setTargetEntity(@Nullable MobEntity targetEntity) {
		this.targetEntity = targetEntity;
	}

	private TargetPredicate getAndUpdateTargetPredicate() {
		return this.targetPredicate.setBaseMaxDistance(this.getFollowRange());
	}
}
