package dev.spiritstudios.abysm.entity.floralreef;

import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import dev.spiritstudios.abysm.entity.AbstractSchoolingFishEntity;
import dev.spiritstudios.abysm.entity.variant.Variantable;
import dev.spiritstudios.abysm.registry.AbysmEntityAttributes;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.task.TargetUtil;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class BloomrayEntity extends AbstractSchoolingFishEntity implements GeoEntity, Variantable<BloomrayEntityVariant> {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.bloomray.idle");
	public static final TrackedData<Integer> VARIANT_ID = DataTracker.registerData(BloomrayEntity.class, TrackedDataHandlerRegistry.INTEGER);

	// TODO - Adjust stats for bigger entity now
	// TODO - Mauve variant texture
	// TODO - Programmatically animate the swimming animation instead of manual animation because that's the only animation that'll probably be needed
	// TODO - Custom swimming AI to make it glide through the water, reducing the up/down movement amount
	// TODO - Custom AI for hiding in Bloomshroom crowns when scared(player nearby? Bigger bloomray/TBD enemy nearby?)
	public BloomrayEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(VARIANT_ID, BloomrayEntityVariant.getDefaultIntId(this.getRegistryManager()));
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("variantId", this.getVariantIntId());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setVariantIntId(nbt.getInt("variantId").orElse(BloomrayEntityVariant.getDefaultIntId(this.getRegistryManager())));
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		this.getRegistryManager().getOrThrow(AbysmRegistries.BLOOMRAY_ENTITY_VARIANT)
			.getRandom(random)
			.ifPresentOrElse(
				entry -> setVariant(entry.value()),
				() -> setVariant(BloomrayEntityVariant.DEFAULT)
			);
		return super.initialize(world, difficulty, spawnReason, entityData);
	}

	public static DefaultAttributeContainer.Builder createRayAttributes() {
		return FishEntity.createFishAttributes()
			.add(EntityAttributes.MOVEMENT_SPEED, 0.85);
	}

	@Override
	public void travel(Vec3d movementInput) {
		if (this.isTouchingWater()) {
			this.updateVelocity(this.getMovementSpeed() * 0.02F, movementInput);
			this.move(MovementType.SELF, this.getVelocity());
			this.setVelocity(this.getVelocity().multiply(0.9));
			if (this.getTarget() == null) {
				this.setVelocity(this.getVelocity().add(0.0, -0.005, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}
	@Override
	protected EntityNavigation createNavigation(World world) {
		return super.createNavigation(world);
	}

	public WanderAroundGoal createWanderGoal() {
		return new GlideToRandomPlaceGoal(this);
	}

	@Override
	protected void pushAway(Entity entity) {
		if (entity instanceof BloomrayEntity) {
			return;
		}
		super.pushAway(entity);
	}

	@Override
	public void tickMovement() {
		super.tickMovement();

		if (this.getWorld().isClient && random.nextFloat() < 0.15) {
			Vec3d facing = Vec3d.fromPolar(getPitch(), bodyYaw).multiply(0.5);

			int glimmerCount = 1 + random.nextInt(3);
			for (int i = 0; i < glimmerCount; i++) {
				spawnParticles(this.getPos().add(facing), random, this.getVariant().glimmerParticle, 0.4F, 1F, 1F);
			}

			int thornsCount = 2 + random.nextInt(2);
			for (int i = 0; i < thornsCount; i++) {
				spawnParticles(this.getPos().add(facing), random, this.getVariant().thornsParticle, 0.9F, 2.6F, 1.4F);
			}
		}
	}

	protected void spawnParticles(Vec3d pos, Random random, SimpleParticleType particle, float width, float orthogonalVelocityMultiplier, float normalVelocityMultiplier) {
		double x = pos.getX() + width * (random.nextFloat() - 0.5);
		double y = pos.getY() + 0.5F - 0.45F;
		double z = pos.getZ() + width * (random.nextFloat() - 0.5);

		double vx = random.nextGaussian() * (0.015) * orthogonalVelocityMultiplier + getVelocity().x;
		double vy = random.nextGaussian() * (0.015) * orthogonalVelocityMultiplier;
		double vz = random.nextGaussian() * (0.015) * orthogonalVelocityMultiplier + getVelocity().z;

		vy *= 0.1F;
		vy += random.nextFloat() * 0.12F * normalVelocityMultiplier;

		getWorld().addParticleClient(particle, x, y, z, vx, vy, vz);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
		AnimationController<AbstractFloralFishEntity> animController = new AnimationController<>(ANIM_CONTROLLER_STRING, 5, event -> {
			// Slower idle animation speed whilst not moving until I make a swimming animation
			if (!event.isMoving()) event.setControllerSpeed(0.75f);
			else event.setControllerSpeed(1f);

			return event.setAndContinue(IDLE_ANIM);
		});

		controllerRegistrar.add(animController);
	}

	@Override
	public BloomrayEntityVariant getVariant() {
		return BloomrayEntityVariant.fromIntId(this.getRegistryManager(), this.getVariantIntId());
	}

	@Override
	public void setVariant(BloomrayEntityVariant variant) {
		this.setVariantIntId(BloomrayEntityVariant.toIntId(this.getRegistryManager(), variant));
	}

	@Override
	public int getVariantIntId() {
		return this.dataTracker.get(VARIANT_ID);
	}

	@Override
	public void setVariantIntId(int variantId) {
		this.dataTracker.set(VARIANT_ID, variantId);
	}

	public static class GlideToRandomPlaceGoal extends SwimAroundGoal {
		private final BloomrayEntity bloomray;

		public GlideToRandomPlaceGoal(BloomrayEntity bloomray) {
			super(bloomray, 1.0, 10);
			this.bloomray = bloomray;
		}

		@Override
		public boolean canStart() {
			return this.bloomray.hasSelfControl() && super.canStart();
		}

		@Nullable
		@Override
		protected Vec3d getWanderTarget() {
			return TargetUtil.find(this.mob, 18, 1);
		}
	}
}
