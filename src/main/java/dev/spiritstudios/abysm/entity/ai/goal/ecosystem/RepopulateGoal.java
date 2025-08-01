package dev.spiritstudios.abysm.entity.ai.goal.ecosystem;

import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class RepopulateGoal extends Goal {
	protected final ServerWorld world;
	protected final MobEntity mob;
	protected final double speed;
	@Nullable
	protected MobEntity mate;
	protected int timer;
	protected TargetPredicate targetPredicate;

	public RepopulateGoal(MobEntity mob, double speed) {
		this(mob, speed, (target, world1) -> target instanceof EcologicalEntity ecologicalTarget && ecologicalTarget.canBreed());
	}

	public RepopulateGoal(
		MobEntity mob,
		double speed,
		@Nullable TargetPredicate.EntityPredicate targetPredicate
	) {
		this.mob = mob;
		assertIsEcologicalEntity(mob);
		this.world = getServerWorld(mob);
		this.speed = speed;

		this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
		this.targetPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(this.getFollowRange()).setPredicate(targetPredicate);
	}

	public static void assertIsEcologicalEntity(MobEntity mob) {
		if (!(mob instanceof EcologicalEntity)) {
			throw new IllegalArgumentException("MobEntity " + mob + " must be an instance of " + EcologicalEntity.class.getName());
		}
	}

	@Override
	public boolean canStart() {
		EcologicalEntity ecologicalEntity = (EcologicalEntity) this.mob;
		if(!ecologicalEntity.shouldRepopulate() || !ecologicalEntity.canBreed()) return false;

		this.findClosestMate();
		return this.mate != null;
	}

	@Override
	public void tick() {
		this.mob.getLookControl().lookAt(this.mate, 10f, this.mob.getMaxLookPitchChange());
		this.mob.getNavigation().startMovingTo(this.mate, this.speed);
		this.timer++;
		if(this.mob.age % 5 == 0) {
			this.world.spawnParticles(ParticleTypes.HEART, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), 1, 0, 0, 0, 0);
		}
//		if(this.timer >= this.getTickCount(60) && this.mob.squaredDistanceTo(this.mate) < 9) {
		if(this.mob.squaredDistanceTo(this.mate) < 9) {
			this.breed();
		}
	}

	@Override
	public boolean shouldContinue() {
		if(this.mate == null || this.timer > 60 || this.mate.isDead()) return false;

		EcologicalEntity ecologicalMate = (EcologicalEntity) mate;
		return ecologicalMate.shouldRepopulate();
	}

	public void breed() {
		EcologicalEntity ecologicalEntity = (EcologicalEntity) this.mob;
		ecologicalEntity.breed(this.world, this.mate);
	}

	@Override
	public void stop() {
		this.mate = null;
		this.timer = 0;
	}

	protected void findClosestMate() {
		this.mate = this.world.getClosestEntity(
			this.world.getEntitiesByType(TypeFilter.instanceOf(MobEntity.class),
				this.getSearchBox(this.getFollowRange()),
				mobEntity -> mobEntity.getType().equals(this.mob.getType())),
			this.getAndUpdateTargetPredicate(),
			this.mob,
			this.mob.getX(),
			this.mob.getEyeY(),
			this.mob.getZ()
		);
	}

	protected double getFollowRange() {
		return this.mob.getAttributeValue(EntityAttributes.FOLLOW_RANGE);
	}

	protected Box getSearchBox(double distance) {
		return this.mob.getBoundingBox().expand(distance, distance, distance);
	}

	private TargetPredicate getAndUpdateTargetPredicate() {
		return this.targetPredicate.setBaseMaxDistance(this.getFollowRange());
	}
}
