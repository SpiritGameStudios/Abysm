package dev.spiritstudios.abysm.entity.ai.goal.ecosystem;

import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Predicate;

import static dev.spiritstudios.abysm.entity.ai.goal.ecosystem.HuntPreyGoal.assertIsEcologicalEntity;

/**
 * @see net.minecraft.entity.ai.goal.FleeEntityGoal
 */
public class FleePredatorsGoal extends Goal {

	protected final PathAwareEntity mob;
	private final double slowSpeed;
	private final double fastSpeed;
	@Nullable
	protected MobEntity targetEntity;
	protected final float fleeDistance;
	@Nullable
	protected Path fleePath;
	protected final EntityNavigation fleeingEntityNavigation;
	protected final Predicate<LivingEntity> extraInclusionSelector;
	protected final Predicate<LivingEntity> inclusionSelector;
	private final TargetPredicate withinRangePredicate;

	public FleePredatorsGoal(PathAwareEntity mob, float distance, double slowSpeed, double fastSpeed) {
		this(mob, entity -> true, distance, slowSpeed, fastSpeed, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR::test);
	}

	public FleePredatorsGoal(
		PathAwareEntity mob,
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
		this.setControls(EnumSet.of(Goal.Control.MOVE));
		this.withinRangePredicate = TargetPredicate.createAttackable()
			.setBaseMaxDistance(distance)
			.setPredicate((entity, world) -> inclusionSelector.test(entity) && extraInclusionSelector.test(entity));
	}

	public FleePredatorsGoal(
		PathAwareEntity fleeingEntity,
		float fleeDistance,
		double fleeSlowSpeed,
		double fleeFastSpeed,
		Predicate<LivingEntity> inclusionSelector
	) {
		this(fleeingEntity, entity -> true, fleeDistance, fleeSlowSpeed, fleeFastSpeed, inclusionSelector);
	}

	@Override
	public boolean canStart() {
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
	}

	@Override
	public boolean shouldContinue() {
		return !this.fleeingEntityNavigation.isIdle();
	}

	@Override
	public void start() {
		this.fleeingEntityNavigation.startMovingAlong(this.fleePath, this.slowSpeed);
	}

	@Override
	public void stop() {
		this.targetEntity = null;
	}

	@Override
	public void tick() {
		if (this.mob.squaredDistanceTo(this.targetEntity) < 49.0) {
			this.mob.getNavigation().setSpeed(this.fastSpeed);
		} else {
			this.mob.getNavigation().setSpeed(this.slowSpeed);
		}
	}
}
