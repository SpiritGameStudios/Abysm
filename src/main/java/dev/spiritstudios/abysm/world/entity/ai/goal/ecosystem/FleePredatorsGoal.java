package dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem;

import dev.spiritstudios.abysm.world.ecosystem.entity.EcologicalEntity;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.pathfinder.Path;

import static dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.HuntPreyGoal.assertIsEcologicalEntity;

/**
 * If this entity is being hunted, swim away from it
 *
 * @see net.minecraft.world.entity.ai.goal.AvoidEntityGoal
 */
public class FleePredatorsGoal extends Goal {

	protected final PathfinderMob mob;
	private final double slowSpeed;
	private final double fastSpeed;
	@Nullable
	protected Mob targetEntity;
	protected final float fleeDistance;
	@Nullable
	protected Path fleePath;
	protected final PathNavigation fleeingEntityNavigation;
	protected final Predicate<LivingEntity> extraInclusionSelector;
	protected final Predicate<LivingEntity> inclusionSelector;
	private final TargetingConditions withinRangePredicate;

	public FleePredatorsGoal(PathfinderMob mob, float distance, double slowSpeed, double fastSpeed) {
		this(mob, distance, slowSpeed, fastSpeed, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
	}

	public FleePredatorsGoal(
		PathfinderMob mob,
		Predicate<LivingEntity> extraInclusionSelector,
		float distance,
		double slowSpeed,
		double fastSpeed,
		Predicate<LivingEntity> inclusionSelector
	) {
		this.mob = mob;
		assertIsEcologicalEntity(mob);
		this.extraInclusionSelector = extraInclusionSelector;
		this.fleeDistance = distance;
		this.slowSpeed = slowSpeed;
		this.fastSpeed = fastSpeed;
		this.inclusionSelector = inclusionSelector;
		this.fleeingEntityNavigation = mob.getNavigation();
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		this.withinRangePredicate = TargetingConditions.forCombat()
			.range(distance)
			.selector((entity, world) -> inclusionSelector.test(entity) && extraInclusionSelector.test(entity));
	}

	public FleePredatorsGoal(
		PathfinderMob fleeingEntity,
		float fleeDistance,
		double fleeSlowSpeed,
		double fleeFastSpeed,
		Predicate<LivingEntity> inclusionSelector
	) {
		this(fleeingEntity, entity -> true, fleeDistance, fleeSlowSpeed, fleeFastSpeed, inclusionSelector);
	}

	@Override
	public boolean canUse() {
		return ((EcologicalEntity) this.mob).isBeingHunted();
		/*
		ServerWorld serverWorld = getServerWorld(this.mob);
		Set<EntityType<? extends MobEntity>> predators = ((EcologicalEntity) this.mob).getEcosystemType().predators();
		this.targetEntity = serverWorld
			.getClosestEntity(
				serverWorld.getEntitiesByType(TypeFilter.instanceOf(MobEntity.class),
					this.mob.getBoundingBox().expand(this.fleeDistance, 3.0, this.fleeDistance),
					mobEntity -> predators.contains(mobEntity.getType())),
				this.withinRangePredicate,
				this.mob,
				this.mob.getX(),
				this.mob.getY(),
				this.mob.getZ()
			);
		if (this.targetEntity == null) {
			return false;
		} else {
			Vec3d vec3d = NoPenaltyTargeting.findFrom(this.mob, 16, 7, this.targetEntity.getPos());
			if (vec3d == null) {
				return false;
			} else if (this.targetEntity.squaredDistanceTo(vec3d.x, vec3d.y, vec3d.z) < this.targetEntity.squaredDistanceTo(this.mob)) {
				return false;
			} else {
				this.fleePath = this.fleeingEntityNavigation.findPathTo(vec3d.x, vec3d.y, vec3d.z, 0);
				return this.fleePath != null;
			}
		}
		*/
	}

	@Override
	public boolean canContinueToUse() {
//		return !this.fleeingEntityNavigation.isIdle();
		return super.canContinueToUse();
	}

	@Override
	public void start() {
		this.fleeingEntityNavigation.moveTo(this.fleePath, this.slowSpeed);
//		((EcologicalEntity) this.mob).setFleeing(true);
	}

	@Override
	public void stop() {
		((EcologicalEntity) this.mob).onHuntEnd();
		getServerLevel(this.mob).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.mob.getX(), this.mob.getY(), this.mob.getZ(), 3, 0, 0, 0, 0);
		this.targetEntity = null;
	}

	@Override
	public void tick() {
		if(this.targetEntity == null) return;
		if (this.mob.distanceToSqr(this.targetEntity) < 49.0) {
			this.mob.getNavigation().setSpeedModifier(this.fastSpeed);
		} else {
			this.mob.getNavigation().setSpeedModifier(this.slowSpeed);
		}
	}
}
