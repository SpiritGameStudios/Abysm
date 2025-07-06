package dev.spiritstudios.abysm.entity.floralreef;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.entity.AbysmDamageTypes;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.registry.tags.AbysmEntityTypeTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class ManOWarEntity extends WaterCreatureEntity {

	protected static final TrackedData<Boolean> CHILD = DataTracker.registerData(ManOWarEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	protected int breedingAge = 0;

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
			.add(EntityAttributes.SCALE, 2)
			.add(EntityAttributes.ATTACK_DAMAGE, 5);
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
			TargetPredicate targetPredicate = TargetPredicate.createAttackable();
			float damage = (float) this.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
			AtomicBoolean hasCreatedChild = new AtomicBoolean(false);
			serverWorld.getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), this.getTentacleBox(), living -> {
				//noinspection CodeBlock2Expr
				return living.isAlive() && !living.getType().isIn(AbysmEntityTypeTags.MAN_O_WAR_FRIEND) && targetPredicate.test(serverWorld, this, living);
			}).forEach(living -> {
				living.damage(serverWorld, source, damage);
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 4), this);
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 4), this);
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 2), this);
				MinecraftServer server = serverWorld.getServer();
				server.send(new ServerTask(server.getTicks(), () -> {
					if (hasCreatedChild.get()) {
						return;
					}
					hasCreatedChild.set(true);
					if (!this.isBaby()
						&& (living.isDead() || living.isRemoved())
						&& living.getType().isIn(AbysmEntityTypeTags.MAN_O_WAR_PREY)
						&& this.random.nextBetween(0, 2) == 0) {
						ManOWarEntity child = AbysmEntityTypes.MAN_O_WAR.create(serverWorld, SpawnReason.BREEDING);
						if (child == null) {
							return;
						}
						child.setBaby(true);
						child.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
						serverWorld.spawnEntity(child);
					}
				}));
			});
		}
	}

	@Override
	public float getScaleFactor() {
		return 1.0F;
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(CHILD, false);
	}

	public int getBreedingAge() {
		if (this.getWorld().isClient) {
			return this.dataTracker.get(CHILD) ? -1 : 1;
		} else {
			return this.breedingAge;
		}
	}

	public void setBreedingAge(int age) {
		int i = this.getBreedingAge();
		this.breedingAge = age;
		if (i < 0 && age >= 0 || i >= 0 && age < 0) {
			this.dataTracker.set(CHILD, age < 0);
			this.onGrowUp();
		}
	}

	@Override
	public void onTrackedDataSet(TrackedData<?> data) {
		if (CHILD.equals(data)) {
			this.calculateDimensions();
		}

		super.onTrackedDataSet(data);
	}

	protected void onGrowUp() {
		if (!this.isBaby() && this.hasVehicle() && this.getVehicle() instanceof AbstractBoatEntity abstractBoatEntity && !abstractBoatEntity.isSmallerThanBoat(this)) {
			this.stopRiding();
		}
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("breedingAge", this.getBreedingAge());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setBreedingAge(nbt.getInt("breedingAge", 0));
	}

	@Override
	public boolean isBaby() {
		return this.getBreedingAge() < 0;
	}

	@Override
	public void setBaby(boolean baby) {
		this.setBreedingAge(baby ? -24000 : 0);
	}

	@Override
	protected void loot(ServerWorld world, ItemEntity itemEntity) {
		super.loot(world, itemEntity);
	}

	public Box getTentacleBox() {
		return getTentacleBox(0.1 * this.getScale());
	}

	public Box getTentacleBox(double expand) {
		return getTentacleBox(expand, expand, expand);
	}

	public Box getTentacleBox(double expandX, double expandY, double expandZ) {
		Box box = this.getBoundingBox().expand(expandX, 0, expandZ);
		return box.withMinY(box.minY - this.getScale() * BASE_TENTACLE_LENGTH - expandY).withMaxY(box.maxY + expandY);
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
			this.updateVelocity(this.getMovementSpeed() * 0.025F, movementInput);
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

		if (!this.getWorld().isClient && this.isAlive()) {
			int i = this.getBreedingAge();
			if (i < 0) {
				this.setBreedingAge(++i);
			} else if (i > 0) {
				this.setBreedingAge(--i);
			}
		}
	}

	protected SoundEvent getFlopSound() {
		return SoundEvents.ENTITY_GUARDIAN_FLOP;
	}

	public static class GarbageMoveControl extends MoveControl {

		public GarbageMoveControl(WaterCreatureEntity owner) {
			super(owner);
		}

		@Override
		public void tick() {
			if (this.entity.isSubmergedIn(FluidTags.WATER)) {
				this.entity.setVelocity(this.entity.getVelocity().add(0.0, 0.005, 0.0));
			}

			if (this.state == MoveControl.State.MOVE_TO && !this.entity.getNavigation().isIdle()) {
				float speed = (float) (this.speed * this.entity.getAttributeValue(EntityAttributes.MOVEMENT_SPEED));
				this.entity.setMovementSpeed(MathHelper.lerp(0.125F, this.entity.getMovementSpeed(), speed));
				double distanceX = this.targetX - this.entity.getX();
				double distanceY = this.targetY - this.entity.getY();
				double distanceZ = this.targetZ - this.entity.getZ();
				if (distanceY != 0.0) {
					double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
					this.entity.setVelocity(this.entity.getVelocity().add(0.0, this.entity.getMovementSpeed() * (distanceY / distance) * 0.1, 0.0));
				}

				if (distanceX != 0.0 || distanceZ != 0.0) {
					float i = (float) (MathHelper.atan2(distanceZ, distanceX) * MathHelper.DEGREES_PER_RADIAN) - 90.0F;
					this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), i, 90.0F));
					this.entity.bodyYaw = this.entity.getYaw();
				}
			} else {
				this.entity.setMovementSpeed(0.0F);
			}
		}
	}

	public static class DriftToRandomPlaceGoal extends SwimAroundGoal {

		public DriftToRandomPlaceGoal(ManOWarEntity owner) {
			super(owner, 1.0, 40);
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
			return new Vec3d(wander.x, Math.max(this.mob.getY(), wander.y), wander.z);
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
