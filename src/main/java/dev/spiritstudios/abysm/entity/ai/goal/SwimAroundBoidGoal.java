package dev.spiritstudios.abysm.entity.ai.goal;

import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

// I got this idea from looking at @Tomate0613's mod, I also referenced their code a bit
public class SwimAroundBoidGoal extends Goal {
	private static final float NEARBY_RANGE = 6.0F;

	protected final PathAwareEntity entity;

	private List<? extends PathAwareEntity> nearby;

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

	public SwimAroundBoidGoal(PathAwareEntity entity, float separationRange, float alignmentAngle, float cohesionAngle, float separationCoefficient, float alignmentCoefficient, float cohesionCoefficient, float randomCoefficient, float avoidAirCoefficient, float minSpeed, float maxSpeed) {
		this.entity = entity;

		this.separationRange = separationRange;
		this.cosAlignmentAngle = MathHelper.cos(alignmentAngle);
		this.cosCohesionAngle = MathHelper.cos(cohesionAngle);
		this.separationCoefficient = separationCoefficient;
		this.alignmentCoefficient = alignmentCoefficient;
		this.cohesionCoefficient = cohesionCoefficient;
		this.randomCoefficient = randomCoefficient;
		this.avoidAirCoefficient = avoidAirCoefficient;
		this.minSpeed = minSpeed;
		this.maxSpeed = maxSpeed;
	}

	@Override
	public boolean shouldRunEveryTick() {
		return true;
	}

	@Override
	public boolean canStart() {
		return true;
	}

	@Override
	public void tick() {
		super.tick();

		if (entity.getWorld().random.nextInt(200) == 1 || nearby == null) {
			nearby = entity.getWorld().getNonSpectatingEntities(
				entity.getClass(),
				entity.getBoundingBox().expand(NEARBY_RANGE)
			);

			nearby.remove(entity);
		}

		nearby.removeIf(entity -> entity.isDead() || entity.isRemoved());

		// why does Vec3d not have divide

		Vec3d separation = Vec3d.ZERO;
		Vec3d alignment = Vec3d.ZERO;
		Vec3d cohesion = Vec3d.ZERO;

		int alignmentIterations = 0;
		int cohesionIterations = 0;

		for (PathAwareEntity other : nearby) {
			Vec3d delta = other.getPos().subtract(entity.getPos());

			float distance = (float) delta.length();
			float distanceReciprocal = 1.0F / Math.max(MathHelper.EPSILON, distance);
			float cosAngle = (float) entity.getRotationVector().dotProduct(delta.normalize());

			if (separationRange > distance) {
				separation = separation.add(delta.multiply(-(distanceReciprocal - (1.0 / separationRange))));
			}

			if (cosAngle >= cosAlignmentAngle) {
				alignment = alignment.add(other.getVelocity().normalize());
				alignmentIterations++;
			}

			if (cosAngle >= cosCohesionAngle) {
				cohesion = cohesion.add(other.getPos());
				cohesionIterations++;
			}
		}

		separation = separation.multiply(separationCoefficient);

		alignment = alignmentIterations == 0 ?
			Vec3d.ZERO :
			alignment.multiply(1.0F / alignmentIterations).multiply(alignmentCoefficient);

		cohesion = cohesionIterations == 0 ?
			Vec3d.ZERO :
			cohesion.multiply(1.0 / cohesionIterations).subtract(entity.getPos()).multiply(cohesionCoefficient);

		Vec3d random = new Vec3d(
			entity.getRandom().nextFloat() * randomCoefficient,
			entity.getRandom().nextFloat() * randomCoefficient,
			entity.getRandom().nextFloat() * randomCoefficient
		).subtract(randomCoefficient / 2.0F);


		Vec3d avoidAir = Vec3d.ZERO;

		if (avoidAirCoefficient > 0.0F) {
			if (!entity.getWorld().isWater(entity.getBlockPos().up()))
				avoidAir = avoidAir.add(0.0, -avoidAirCoefficient, 0.0);
		}

		entity.addVelocity(separation.add(alignment).add(cohesion).add(random).add(avoidAir));

		Vec3d velocity = entity.getVelocity();
		float speed = (float) velocity.length();

		if (speed < minSpeed) velocity = velocity.normalize().multiply(minSpeed);
		if (speed > maxSpeed) velocity = velocity.normalize().multiply(maxSpeed);

		entity.setVelocity(velocity);
		entity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, entity.getEyePos().add(velocity.multiply(5.0)));
	}
}
