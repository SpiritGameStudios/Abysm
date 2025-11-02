package dev.spiritstudios.abysm.entity.floralreef;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.entity.AbysmDamageTypes;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.registry.tags.AbysmEntityTypeTags;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ManOWarEntity extends WaterAnimal {

	protected static final EntityDataAccessor<Boolean> CHILD = SynchedEntityData.defineId(ManOWarEntity.class, EntityDataSerializers.BOOLEAN);
	protected int breedingAge = 0;

	public static final Vec3[] STARTING_OFFSETS = Util.make(new Vec3[49], array -> {
		int index = 0;

		for (int i = 0; i <= 6; i++) {
			int z = 2 * i - 6;

			array[index++] = new Vec3(0, 0, z).scale(0.05);
			array[index++] = new Vec3(1, 0, z).scale(0.05);
			array[index++] = new Vec3(-1, 0, z).scale(0.05);
			array[index++] = new Vec3(2, 0, z).scale(0.05);
			array[index++] = new Vec3(-2, 0, z).scale(0.05);
			array[index++] = new Vec3(3, 0, z).scale(0.05);
			array[index++] = new Vec3(-3, 0, z).scale(0.05);
		}
	});

	public final List<TentacleData> tentacleData;

	public static final int MIN_SWAY_OFFSET = 100;
	public static final int MAX_SWAY_OFFSET = 2000;

	public static final float INVERSE_MAX_SWAY_OFFSET = 1f / MAX_SWAY_OFFSET;

	protected Vec3 prevVelocity = Vec3.ZERO;

	public static final double BASE_TENTACLE_LENGTH = 2.5; // remember that by default, scale is 2 and this value is for scale = 1

	public ManOWarEntity(EntityType<? extends WaterAnimal> entityType, Level world) {
		super(entityType, world);
		this.moveControl = new GarbageMoveControl(this);
		ImmutableList.Builder<TentacleData> builder = ImmutableList.builder();
		RandomSource random = this.getRandom();
		for (Vec3 vec3d : STARTING_OFFSETS) {
			builder.add(new TentacleData(vec3d, random.nextIntBetweenInclusive(MIN_SWAY_OFFSET, MAX_SWAY_OFFSET)));
		}
		tentacleData = builder.build();
		this.refreshDimensions();
	}

	public static AttributeSupplier.Builder createManOWarAttributes() {
		return Mob.createMobAttributes()
			.add(Attributes.MAX_HEALTH, 15)
			.add(Attributes.MOVEMENT_SPEED, 0.4)
			.add(Attributes.SCALE, 2)
			.add(Attributes.ATTACK_DAMAGE, 5);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new DriftToRandomPlaceGoal(this));
	}

	@Override
	protected PathNavigation createNavigation(Level world) {
		return new DriftNavigation(this, world);
	}

	@Override
	public void tick() {
		this.prevVelocity = this.getDeltaMovement();
		super.tick();
	}

	@Override
	protected void customServerAiStep(ServerLevel serverWorld) {
		super.customServerAiStep(serverWorld);
		Holder<DamageType> damageType = AbysmDamageTypes.getOrThrow(serverWorld, AbysmDamageTypes.CNIDOCYTE_STING);
		DamageSource source = new DamageSource(damageType, this);
		TargetingConditions targetPredicate = TargetingConditions.forCombat();
		float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
		AtomicBoolean hasCreatedChild = new AtomicBoolean(false);
		serverWorld.getEntities(EntityTypeTest.forClass(LivingEntity.class), this.getTentacleBox(), living -> {
			//noinspection CodeBlock2Expr
			return living.isAlive() && !living.getType().is(AbysmEntityTypeTags.MAN_O_WAR_FRIEND) && targetPredicate.test(serverWorld, this, living);
		}).forEach(living -> {
			living.hurtServer(serverWorld, source, damage);
			living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 4), this);
			living.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 4), this);
			living.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 200, 2), this);
			MinecraftServer server = serverWorld.getServer();
			server.schedule(new TickTask(server.getTickCount(), () -> {
				if (hasCreatedChild.get()) {
					return;
				}
				hasCreatedChild.set(true);
				if (!this.isBaby()
					&& (living.isDeadOrDying() || living.isRemoved())
					&& living.getType().is(AbysmEntityTypeTags.MAN_O_WAR_PREY)
					&& this.random.nextIntBetweenInclusive(0, 2) == 0) {
					ManOWarEntity child = AbysmEntityTypes.MAN_O_WAR.create(serverWorld, EntitySpawnReason.BREEDING);
					if (child == null) {
						return;
					}
					child.setBaby(true);
					child.snapTo(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
					serverWorld.addFreshEntity(child);
				}
			}));
		});
	}

	@Override
	public float getAgeScale() {
		return 1.0F;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(CHILD, false);
	}

	public int getBreedingAge() {
		if (this.level().isClientSide) {
			return this.entityData.get(CHILD) ? -1 : 1;
		} else {
			return this.breedingAge;
		}
	}

	public void setBreedingAge(int age) {
		int i = this.getBreedingAge();
		this.breedingAge = age;
		if (i < 0 && age >= 0 || i >= 0 && age < 0) {
			this.entityData.set(CHILD, age < 0);
			this.onGrowUp();
		}
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
		if (CHILD.equals(data)) {
			this.refreshDimensions();
		}

		super.onSyncedDataUpdated(data);
	}

	protected void onGrowUp() {
		if (!this.isBaby() && this.isPassenger() && this.getVehicle() instanceof AbstractBoat abstractBoatEntity && !abstractBoatEntity.hasEnoughSpaceFor(this)) {
			this.stopRiding();
		}
	}

	@Override
	protected void readAdditionalSaveData(ValueInput view) {
		super.readAdditionalSaveData(view);
		this.setBreedingAge(view.getIntOr("breedingAge", 0));
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput view) {
		super.addAdditionalSaveData(view);
		view.putInt("breedingAge", this.getBreedingAge());
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
	protected void pickUpItem(ServerLevel world, ItemEntity itemEntity) {
		super.pickUpItem(world, itemEntity);
	}

	public AABB getTentacleBox() {
		return getTentacleBox(0.1 * this.getScale());
	}

	public AABB getTentacleBox(double expand) {
		return getTentacleBox(expand, expand, expand);
	}

	public AABB getTentacleBox(double expandX, double expandY, double expandZ) {
		AABB box = this.getBoundingBox().inflate(expandX, 0, expandZ);
		return box.setMinY(box.minY - this.getScale() * BASE_TENTACLE_LENGTH - expandY).setMaxY(box.maxY + expandY);
	}

	@Override
	public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return super.hurtServer(world, source, amount);
		}
		Entity attacker = source.getEntity();
		if (attacker != null && attacker.getType().is(AbysmEntityTypeTags.MAN_O_WAR_FRIEND)) {
			return false;
		}
		return super.hurtServer(world, source, amount);
	}

	public Vec3 getPrevVelocity() {
		return this.prevVelocity;
	}

	@Override
	public void travel(Vec3 movementInput) {
		if (this.isInWater()) {
			this.moveRelative(this.getSpeed() * 0.025F, movementInput);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
			if (this.getTarget() == null) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.0001, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}

	@Override
	public void aiStep() {
		if (!this.isInWater() && this.onGround() && this.verticalCollision) {
			this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F, 0.4F, (this.random.nextFloat() * 2.0F - 1.0F) * 0.05F));
			this.setOnGround(false);
			this.hasImpulse = true;
			this.makeSound(this.getFlopSound());
		}

		super.aiStep();

		if (!this.level().isClientSide && this.isAlive()) {
			int i = this.getBreedingAge();
			if (i < 0) {
				this.setBreedingAge(++i);
			} else if (i > 0) {
				this.setBreedingAge(--i);
			}
		}
	}

	protected SoundEvent getFlopSound() {
		return SoundEvents.GUARDIAN_FLOP;
	}

	public static class GarbageMoveControl extends MoveControl {

		public GarbageMoveControl(WaterAnimal owner) {
			super(owner);
		}

		@Override
		public void tick() {
			if (this.mob.isEyeInFluid(FluidTags.WATER)) {
				this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, 0.005, 0.0));
			}

			if (this.operation == MoveControl.Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
				float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
				this.mob.setSpeed(Mth.lerp(0.125F, this.mob.getSpeed(), speed));
				double distanceX = this.wantedX - this.mob.getX();
				double distanceY = this.wantedY - this.mob.getY();
				double distanceZ = this.wantedZ - this.mob.getZ();
				if (distanceY != 0.0) {
					double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, this.mob.getSpeed() * (distanceY / distance) * 0.1, 0.0));
				}

				if (distanceX != 0.0 || distanceZ != 0.0) {
					float i = (float) (Mth.atan2(distanceZ, distanceX) * Mth.RAD_TO_DEG) - 90.0F;
					this.mob.setYRot(this.rotlerp(this.mob.getYRot(), i, 90.0F));
					this.mob.yBodyRot = this.mob.getYRot();
				}
			} else {
				this.mob.setSpeed(0.0F);
			}
		}
	}

	public static class DriftToRandomPlaceGoal extends RandomSwimmingGoal {

		public DriftToRandomPlaceGoal(ManOWarEntity owner) {
			super(owner, 1.0, 40);
		}

		@Override
		public boolean canUse() {
			return super.canUse();
		}

		@Nullable
		@Override
		protected Vec3 getPosition() {
			Vec3 wander = super.getPosition();
			if (wander == null) {
				return null;
			}
			return new Vec3(wander.x, Math.max(this.mob.getY(), wander.y), wander.z);
		}
	}

	public static class DriftNavigation extends WaterBoundPathNavigation {

		public DriftNavigation(Mob mobEntity, Level world) {
			super(mobEntity, world);
		}

		@Override
		protected double getGroundY(Vec3 pos) {
			return Math.max(this.mob.getY(), super.getGroundY(pos));
		}
	}

	@SuppressWarnings("unused")
	public record TentacleData(Vec3 relativePosition, int swayOffset) {
		public static final Codec<TentacleData> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Vec3.CODEC.fieldOf("relativePosition").forGetter(data -> data.relativePosition),
					Codec.INT.fieldOf("swayOffset").forGetter(data -> data.swayOffset)
				)
				.apply(instance, TentacleData::new)
		);
		public static final Codec<List<TentacleData>> LIST_CODEC = CODEC.listOf();
	}
}
