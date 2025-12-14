package dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem;

import dev.spiritstudios.abysm.world.ecosystem.entity.EcologicalEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

/**
 * If this entity is allowed to hunt, it'll search for nearby targets and begin a hunt upon finding one.
 *
 * @see TargetGoal
 * @see net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
 */
public class HuntPreyGoal extends TargetGoal {

	private static final int DEFAULT_RECIPROCAL_CHANCE = 10;

	/**
	 * The reciprocal of chance to actually search for a target on every tick
	 * when this goal is not started. This is also the average number of ticks
	 * between each search (as in a poisson distribution).
	 */
	protected final int reciprocalChance;
	@Nullable
	protected Mob targetEntity;
	protected TargetingConditions targetPredicate;

	@SuppressWarnings("unused")
	public HuntPreyGoal(Mob mob, boolean checkVisibility) {
		this(mob, DEFAULT_RECIPROCAL_CHANCE, checkVisibility, false, null);
	}

	@SuppressWarnings("unused")
	public HuntPreyGoal(Mob mob, boolean checkVisibility, TargetingConditions.Selector predicate) {
		this(mob, DEFAULT_RECIPROCAL_CHANCE, checkVisibility, false, predicate);
	}

	@SuppressWarnings("unused")
	public HuntPreyGoal(Mob mob, boolean checkVisibility, boolean checkCanNavigate) {
		this(mob, DEFAULT_RECIPROCAL_CHANCE, checkVisibility, checkCanNavigate, null);
	}

	public HuntPreyGoal(
		Mob mob,
		int reciprocalChance,
		boolean checkVisibility,
		boolean checkCanNavigate,
		@Nullable TargetingConditions.Selector targetPredicate
	) {
		super(mob, checkVisibility, checkCanNavigate);
		assertIsEcologicalEntity(mob);
		this.reciprocalChance = reducedTickDelay(reciprocalChance);
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
		this.targetPredicate = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(targetPredicate);
	}

	public static void assertIsEcologicalEntity(Mob mob) {
		if (!(mob instanceof EcologicalEntity)) {
			throw new IllegalArgumentException("MobEntity " + mob + " must be an instance of " + EcologicalEntity.class.getName());
		}
	}

	@Override
	public boolean canUse() {
//		if (this.reciprocalChance > 0 && this.mob.getRandom().nextInt(this.reciprocalChance) != 0) {
//			return false;
//		}

		if (!((EcologicalEntity) this.mob).shouldHunt()) {
			return false;
		}

		this.findClosestTarget();
		return this.targetEntity != null;
	}

	protected AABB getSearchBox(double distance) {
		return this.mob.getBoundingBox().inflate(distance, distance, distance);
	}

	protected void findClosestTarget() {
		ServerLevel level = getServerLevel(this.mob);
		Set<EntityType<?>> prey = ((EcologicalEntity) this.mob).getEcosystemData().prey();

		this.targetEntity = level.getNearestEntity(
			level.getEntities(EntityTypeTest.forClass(Mob.class),
				this.getSearchBox(this.getFollowDistance()),
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
		((EcologicalEntity) this.mob).theHuntIsOn(this.mob, this.targetEntity);
		super.start();
	}

	@Override
	public boolean canContinueToUse() {
		EcologicalEntity ecologicalMob = (EcologicalEntity) this.mob;
		if (ecologicalMob.shouldFailHunt()) {
			return false;
		}

		return super.canContinueToUse();
	}

	@Override
	public void stop() {
		((EcologicalEntity) this.mob).onHuntEnd();
		if (this.targetEntity != null && this.targetEntity.isAlive()) {
			getServerLevel(this.mob).sendParticles(ParticleTypes.ANGRY_VILLAGER, this.mob.getX(), this.mob.getY(), this.mob.getZ(), 1, 0, 0, 0, 0);
			((EcologicalEntity) this.targetEntity).onHuntEnd();
		}
		super.stop();
	}

	@SuppressWarnings("unused")
	protected void setTargetEntity(@Nullable Mob targetEntity) {
		this.targetEntity = targetEntity;
	}

	private TargetingConditions getAndUpdateTargetPredicate() {
		return this.targetPredicate.range(this.getFollowDistance());
	}
}
