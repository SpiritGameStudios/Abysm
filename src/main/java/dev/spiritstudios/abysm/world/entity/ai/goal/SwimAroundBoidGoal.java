package dev.spiritstudios.abysm.world.entity.ai.goal;

import java.util.List;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

// I got this idea from looking at @Tomate0613's mod, I also referenced their code a bit
public class SwimAroundBoidGoal extends Goal {
	private static final float NEARBY_RANGE = 6.0F;

	protected final PathfinderMob entity;

	private List<? extends PathfinderMob> nearby;

	private final float separationRange;

	private final float cosAlignmentAngle;
	private final float cosCohesionAngle;

	private final float separationCoefficient;
	private final float alignmentCoefficient;
	private final float cohesionCoefficient;
	private final float randomCoefficient;
	private final float avoidAirCoefficient;

	private final float minSpeed;
	private final float maxSpeed;

	public SwimAroundBoidGoal(PathfinderMob entity, float separationRange, float alignmentAngle, float cohesionAngle, float separationCoefficient, float alignmentCoefficient, float cohesionCoefficient, float randomCoefficient, float avoidAirCoefficient, float minSpeed, float maxSpeed) {
		this.entity = entity;

		this.separationRange = separationRange;
		this.cosAlignmentAngle = Mth.cos(alignmentAngle);
		this.cosCohesionAngle = Mth.cos(cohesionAngle);
		this.separationCoefficient = separationCoefficient;
		this.alignmentCoefficient = alignmentCoefficient;
		this.cohesionCoefficient = cohesionCoefficient;
		this.randomCoefficient = randomCoefficient;
		this.avoidAirCoefficient = avoidAirCoefficient;
		this.minSpeed = minSpeed;
		this.maxSpeed = maxSpeed;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public boolean canUse() {
		return true;
	}

	@Override
	public void tick() {
		super.tick();

		if (entity.level().random.nextInt(200) == 1 || nearby == null) {
			nearby = entity.level().getEntitiesOfClass(
				entity.getClass(),
				entity.getBoundingBox().inflate(NEARBY_RANGE)
			);

			nearby.remove(entity);
		}

		nearby.removeIf(entity -> entity.isDeadOrDying() || entity.isRemoved());

		// why does Vec3d not have divide

		Vec3 separation = Vec3.ZERO;
		Vec3 alignment = Vec3.ZERO;
		Vec3 cohesion = Vec3.ZERO;

		int alignmentIterations = 0;
		int cohesionIterations = 0;

		for (PathfinderMob other : nearby) {
			Vec3 delta = other.position().subtract(entity.position());

			float distance = (float) delta.length();
			float distanceReciprocal = 1.0F / Math.max(Mth.EPSILON, distance);
			float cosAngle = (float) entity.getLookAngle().dot(delta.normalize());

			if (separationRange > distance) {
				separation = separation.add(delta.scale(-(distanceReciprocal - (1.0 / separationRange))));
			}

			if (cosAngle >= cosAlignmentAngle) {
				alignment = alignment.add(other.getDeltaMovement().normalize());
				alignmentIterations++;
			}

			if (cosAngle >= cosCohesionAngle) {
				cohesion = cohesion.add(other.position());
				cohesionIterations++;
			}
		}

		separation = separation.scale(separationCoefficient);

		alignment = alignmentIterations == 0 ?
			Vec3.ZERO :
			alignment.scale(1.0F / alignmentIterations).scale(alignmentCoefficient);

		cohesion = cohesionIterations == 0 ?
			Vec3.ZERO :
			cohesion.scale(1.0 / cohesionIterations).subtract(entity.position()).scale(cohesionCoefficient);

		Vec3 random = new Vec3(
			entity.getRandom().nextFloat() * randomCoefficient,
			entity.getRandom().nextFloat() * randomCoefficient,
			entity.getRandom().nextFloat() * randomCoefficient
		).subtract(randomCoefficient / 2.0F);


		Vec3 avoidAir = Vec3.ZERO;

		if (avoidAirCoefficient > 0.0F) {
			if (!entity.level().isWaterAt(entity.blockPosition().above()))
				avoidAir = avoidAir.add(0.0, -avoidAirCoefficient, 0.0);
		}

		entity.push(separation.add(alignment).add(cohesion).add(random).add(avoidAir));

		Vec3 velocity = entity.getDeltaMovement();
		float speed = (float) velocity.length();

		if (speed < minSpeed) velocity = velocity.normalize().scale(minSpeed);
		if (speed > maxSpeed) velocity = velocity.normalize().scale(maxSpeed);

		entity.setDeltaMovement(velocity);
		entity.lookAt(EntityAnchorArgument.Anchor.EYES, entity.getEyePosition().add(velocity.scale(5.0)));
	}
}
