package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.mixin.EntityAttributeInstanceAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ManOWar extends WaterCreatureEntity {

	protected float prevScale = 1;
	protected Vec3d prevVelocity = Vec3d.ZERO;

	protected static final double MIN_Y_CHANGE = 0.01;

	public ManOWar(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		super(entityType, world);
		this.moveControl = new GarbageMoveControl(this);
	}

	public static DefaultAttributeContainer.Builder createManOWarAttributes() {
		return MobEntity.createMobAttributes()
			.add(EntityAttributes.MAX_HEALTH, 15)
			.add(EntityAttributes.MOVEMENT_SPEED, 0.4)
			.add(EntityAttributes.SCALE, 2);
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(0, new DriftToRandomPlaceGoal(this));
	}

	@Override
	protected EntityNavigation createNavigation(World world) {
		return new DriftNavigation(this, world);
	}

	@Override
	public void tick() {
		float scale = this.getScale();
		if (this.prevScale != scale) {
			this.prevScale = scale;
			EntityAttributeInstance attribute = this.getAttributes().getCustomInstance(EntityAttributes.SCALE);
			if (attribute != null) {
				((EntityAttributeInstanceAccessor) attribute).abysm$invokeOnUpdate();
			}
		}
		this.prevVelocity = this.getVelocity();
		super.tick();
	}

	public Vec3d getPrevVelocity() {
		return this.prevVelocity;
	}

	@Override
	public void travel(Vec3d movementInput) {
		if (this.isTouchingWater()) {
			this.updateVelocity(0.01F, movementInput);
			this.move(MovementType.SELF, this.getVelocity());
			this.setVelocity(this.getVelocity().multiply(0.9));
			if (this.getTarget() == null) {
				this.setVelocity(this.getVelocity().add(0.0, 0.0001, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}

	@Override
	public void tickMovement() {
		if (!this.isTouchingWater() && this.isOnGround() && this.verticalCollision) {
			this.setVelocity(this.getVelocity().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F, 0.4F, (this.random.nextFloat() * 2.0F - 1.0F) * 0.05F));
			this.setOnGround(false);
			this.velocityDirty = true;
			this.playSound(this.getFlopSound());
		}

		super.tickMovement();
	}

	protected SoundEvent getFlopSound() {
		return SoundEvents.ENTITY_GUARDIAN_FLOP;
	}

	public static class GarbageMoveControl extends MoveControl {
		protected final ManOWar obj;

		public GarbageMoveControl(ManOWar owner) {
			super(owner);
			this.obj = owner;
		}

		@Override
		public void tick() {
			if (this.obj.isSubmergedIn(FluidTags.WATER)) {
				this.obj.setVelocity(this.obj.getVelocity().add(0.0, 0.005, 0.0));
			}

			if (this.state == MoveControl.State.MOVE_TO && !this.obj.getNavigation().isIdle()) {
				float f = (float)(this.speed * this.obj.getAttributeValue(EntityAttributes.MOVEMENT_SPEED));
				this.obj.setMovementSpeed(MathHelper.lerp(0.125F, this.obj.getMovementSpeed(), f));
				double d = this.targetX - this.obj.getX();
				double e = this.targetY - this.obj.getY();
				double g = this.targetZ - this.obj.getZ();
				if (e != 0.0) {
					double h = Math.sqrt(d * d + e * e + g * g);
					this.obj.setVelocity(this.obj.getVelocity().add(0.0, this.obj.getMovementSpeed() * (e / h) * 0.1, 0.0));
				}

				if (d != 0.0 || g != 0.0) {
					float i = (float)(MathHelper.atan2(g, d) * 180.0F / (float)Math.PI) - 90.0F;
					this.obj.setYaw(this.wrapDegrees(this.obj.getYaw(), i, 90.0F));
					this.obj.bodyYaw = this.obj.getYaw();
				}
			} else {
				this.obj.setMovementSpeed(0.0F);
			}
		}
	}

	public static class DriftToRandomPlaceGoal extends SwimAroundGoal {
		protected final ManOWar obj;

		public DriftToRandomPlaceGoal(ManOWar owner) {
			super(owner, 1.0, 40);
			this.obj = owner;
		}

		@Override
		public boolean canStart() {
			return super.canStart();
		}

		@Nullable
		@Override
		protected Vec3d getWanderTarget() {
			Vec3d wander = super.getWanderTarget();
			if (wander == null) {
				return null;
			}
			return new Vec3d(wander.x, Math.max(MIN_Y_CHANGE, wander.y), wander.z);
		}
	}

	public static class DriftNavigation extends SwimNavigation {

		public DriftNavigation(MobEntity mobEntity, World world) {
			super(mobEntity, world);
		}

		@Override
		protected double adjustTargetY(Vec3d pos) {
			return Math.max(MIN_Y_CHANGE, super.adjustTargetY(pos));
		}
	}
}
