package dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem;

import dev.spiritstudios.abysm.world.ecosystem.entity.EcologicalEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class RepopulateGoal extends Goal {
	protected final ServerLevel level;
	protected final Mob mob;
	protected final double speed;
	@Nullable
	protected Mob mate;
	protected int timer;
	protected TargetingConditions targetPredicate;

	public RepopulateGoal(Mob mob, double speed) {
		this(mob, speed, (target, world1) -> target instanceof EcologicalEntity ecologicalTarget && ecologicalTarget.canBreed());
	}

	public RepopulateGoal(
		Mob mob,
		double speed,
		@Nullable TargetingConditions.Selector targetPredicate
	) {
		this.mob = mob;
		assertIsEcologicalEntity(mob);
		this.level = getServerLevel(mob);
		this.speed = speed;

		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		this.targetPredicate = TargetingConditions.forCombat().range(this.getFollowRange()).selector(targetPredicate);
	}

	public static void assertIsEcologicalEntity(Mob mob) {
		if (!(mob instanceof EcologicalEntity)) {
			throw new IllegalArgumentException("MobEntity " + mob + " must be an instance of " + EcologicalEntity.class.getName());
		}
	}

	@Override
	public boolean canUse() {
		EcologicalEntity ecologicalEntity = (EcologicalEntity) this.mob;
		if(!ecologicalEntity.canBreedAndRepopulate()) return false;

		this.findClosestMate();
		return this.mate != null;
	}

	@Override
	public void tick() {
		this.mob.getLookControl().setLookAt(this.mate, 10f, this.mob.getMaxHeadXRot());
		this.mob.getNavigation().moveTo(this.mate, this.speed);
		this.timer++;
		if (this.mob.tickCount % 5 == 0) {
			this.level.sendParticles(ParticleTypes.HEART, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), 1, 0, 0, 0, 0);
		}
//		if(this.timer >= this.getTickCount(60) && this.mob.squaredDistanceTo(this.mate) < 9) {
		if(this.mob.distanceToSqr(this.mate) < 9) {
			this.breed();
		}
	}

	@Override
	public boolean canContinueToUse() {
		if(this.mate == null || this.timer > 60 || this.mate.isDeadOrDying()) return false;

		return ((EcologicalEntity) this.mob).canBreedAndRepopulate();
	}

	public void breed() {
		EcologicalEntity ecologicalEntity = (EcologicalEntity) this.mob;
		ecologicalEntity.breed(this.level, this.mate);
	}

	@Override
	public void stop() {
		this.mate = null;
		this.timer = 0;
	}

	protected void findClosestMate() {
		this.mate = this.level.getNearestEntity(
			this.level.getEntities(EntityTypeTest.forClass(Mob.class),
				this.getSearchBox(this.getFollowRange()),
				mobEntity -> {
					if (!mobEntity.getType().equals(this.mob.getType())) {
						return false;
					}
					if (!(mobEntity instanceof EcologicalEntity eco)) {
						return false;
					}

					// FIXME - Double the amount of entities are spawned in because of this
					// Because both entities are marked as "shouldRepopulate", both end up finding each other
					// and both end up calling EcologicalEntity#breed(), which spawns in the litter twice

					// A quick fix is to only search for "eco.canBreed()", but I think that would end up causing
					// more entities overall, which is a problem for us considering our lack of predators

					// A better system would be alerting the other entity that they are trying to breed,
					// and then one is chosen to spawn in the new entities, but that isn't on my to-do list for now
					return eco.canBreedAndRepopulate();
				}),
			this.getAndUpdateTargetPredicate(),
			this.mob,
			this.mob.getX(),
			this.mob.getEyeY(),
			this.mob.getZ()
		);
	}

	protected double getFollowRange() {
		return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
	}

	protected AABB getSearchBox(double distance) {
		return this.mob.getBoundingBox().inflate(distance, distance, distance);
	}

	private TargetingConditions getAndUpdateTargetPredicate() {
		return this.targetPredicate.range(this.getFollowRange());
	}
}
