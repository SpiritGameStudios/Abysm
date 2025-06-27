package dev.spiritstudios.abysm.entity.floralreef;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.registry.AbysmDamageTypes;
import dev.spiritstudios.abysm.registry.tags.AbysmEntityTypeTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.Util;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManOWarEntity extends WaterCreatureEntity {
	public static final Vec3d[] STARTING_OFFSETS = Util.make(new Vec3d[49], array -> {
		int index = 0;

		for (int i = 0; i <= 6; i++) {
			int z = 2 * i - 6;

			array[index++] = new Vec3d(0, 0, z).multiply(0.05);
			array[index++] = new Vec3d(1, 0, z).multiply(0.05);
			array[index++] = new Vec3d(-1, 0, z).multiply(0.05);
			array[index++] = new Vec3d(2, 0, z).multiply(0.05);
			array[index++] = new Vec3d(-2, 0, z).multiply(0.05);
			array[index++] = new Vec3d(3, 0, z).multiply(0.05);
			array[index++] = new Vec3d(-3, 0, z).multiply(0.05);
		}
	});

	public final List<TentacleData> tentacleData;

	public static final int MIN_SWAY_OFFSET = 100;
	public static final int MAX_SWAY_OFFSET = 2000;

	public static final float INVERSE_MAX_SWAY_OFFSET = 1f / MAX_SWAY_OFFSET;

	protected Vec3d prevVelocity = Vec3d.ZERO;

	public static final double BASE_TENTACLE_LENGTH = 2.5; // remember that by default, scale is 2 and this value is for scale = 1

	public ManOWarEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		super(entityType, world);
		this.moveControl = new GarbageMoveControl(this);
		ImmutableList.Builder<TentacleData> builder = ImmutableList.builder();
		Random random = this.getRandom();
		for (Vec3d vec3d : STARTING_OFFSETS) {
			builder.add(new TentacleData(vec3d, random.nextBetween(MIN_SWAY_OFFSET, MAX_SWAY_OFFSET)));
		}
		tentacleData = builder.build();
		this.calculateDimensions();
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
		this.prevVelocity = this.getVelocity();
		super.tick();
		if (this.getWorld() instanceof ServerWorld serverWorld) {
			RegistryEntry<DamageType> damageType = AbysmDamageTypes.getFromWorld(serverWorld, AbysmDamageTypes.CNIDOCYTE_STING);
			DamageSource source = new DamageSource(damageType, this);
			final double expand = 0.3;
			Box box = this.getBoundingBox().expand(expand, 0, expand);
			box = box.withMinY(box.minY - this.getScale() * BASE_TENTACLE_LENGTH).withMaxY(box.maxY + expand);
			TargetPredicate targetPredicate = TargetPredicate.createAttackable();
			serverWorld.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), box, living -> {
				//noinspection CodeBlock2Expr
				return living.isAlive() && !living.getType().isIn(AbysmEntityTypeTags.MAN_O_WAR_FRIEND) && targetPredicate.test(serverWorld, this, living);
			}).forEach(living -> {
				living.damage(serverWorld, source, 5f);
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 4), this);
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 4), this);
				if (living.isDead() && living.getType().isIn(AbysmEntityTypeTags.MAN_O_WAR_PREY) && this.random.nextBetween(0, 2) == 0) {
					// asexual reproduction go
				}
			});
		}
	}

	@Override
	public boolean damage(ServerWorld world, DamageSource source, float amount) {
		if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return super.damage(world, source, amount);
		}
		Entity attacker = source.getAttacker();
		if (attacker != null && attacker.getType().isIn(AbysmEntityTypeTags.MAN_O_WAR_FRIEND)) {
			return false;
		}
		return super.damage(world, source, amount);
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
		protected final ManOWarEntity obj;

		public GarbageMoveControl(ManOWarEntity owner) {
			super(owner);
			this.obj = owner;
		}

		@Override
		public void tick() {
			if (this.obj.isSubmergedIn(FluidTags.WATER)) {
				this.obj.setVelocity(this.obj.getVelocity().add(0.0, 0.005, 0.0));
			}

			if (this.state == MoveControl.State.MOVE_TO && !this.obj.getNavigation().isIdle()) {
				float speed = (float) (this.speed * this.obj.getAttributeValue(EntityAttributes.MOVEMENT_SPEED));
				this.obj.setMovementSpeed(MathHelper.lerp(0.125F, this.obj.getMovementSpeed(), speed));
				double distanceX = this.targetX - this.obj.getX();
				double distanceY = this.targetY - this.obj.getY();
				double distanceZ = this.targetZ - this.obj.getZ();
				if (distanceY != 0.0) {
					double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
					this.obj.setVelocity(this.obj.getVelocity().add(0.0, this.obj.getMovementSpeed() * (distanceY / distance) * 0.1, 0.0));
				}

				if (distanceX != 0.0 || distanceZ != 0.0) {
					float i = (float) (MathHelper.atan2(distanceZ, distanceX) * MathHelper.DEGREES_PER_RADIAN) - 90.0F;
					this.obj.setYaw(this.wrapDegrees(this.obj.getYaw(), i, 90.0F));
					this.obj.bodyYaw = this.obj.getYaw();
				}
			} else {
				this.obj.setMovementSpeed(0.0F);
			}
		}
	}

	public static class DriftToRandomPlaceGoal extends SwimAroundGoal {
		protected final ManOWarEntity obj;

		public DriftToRandomPlaceGoal(ManOWarEntity owner) {
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
			return new Vec3d(wander.x, Math.max(this.obj.getY(), wander.y), wander.z);
		}
	}

	public static class DriftNavigation extends SwimNavigation {

		public DriftNavigation(MobEntity mobEntity, World world) {
			super(mobEntity, world);
		}

		@Override
		protected double adjustTargetY(Vec3d pos) {
			return Math.max(this.entity.getY(), super.adjustTargetY(pos));
		}
	}

	@SuppressWarnings("unused")
	public record TentacleData(Vec3d relativePosition, int swayOffset) {
		public static final Codec<TentacleData> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Vec3d.CODEC.fieldOf("relativePosition").forGetter(data -> data.relativePosition),
					Codec.INT.fieldOf("swayOffset").forGetter(data -> data.swayOffset)
				)
				.apply(instance, TentacleData::new)
		);
		public static final Codec<List<TentacleData>> LIST_CODEC = CODEC.listOf();
	}
}
