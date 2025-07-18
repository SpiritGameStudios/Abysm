package dev.spiritstudios.abysm.entity.floralreef;

import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.AbstractSchoolingFishEntity;
import dev.spiritstudios.abysm.entity.AbysmTrackedDataHandlers;
import dev.spiritstudios.abysm.entity.variant.Variantable;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.task.TargetUtil;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;

public class BloomrayEntity extends AbstractSchoolingFishEntity implements GeoEntity, Variantable<BloomrayEntityVariant>, EcologicalEntity {
	public static final TrackedData<RegistryEntry<BloomrayEntityVariant>> VARIANT = DataTracker.registerData(BloomrayEntity.class, AbysmTrackedDataHandlers.BLOOMRAY_VARIANT);

	protected EcosystemLogic ecosystemLogic;

	// TODO - Custom AI for hiding in Bloomshroom crowns when scared(player nearby? Bigger bloomray/TBD enemy nearby?)
	public BloomrayEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
		this.ecosystemLogic = createEcosystemLogic(this);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(VARIANT, BloomrayEntityVariant.getDefaultEntry(this.getRegistryManager()));
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		RegistryOps<NbtElement> ops = this.getRegistryManager().getOps(NbtOps.INSTANCE);

		nbt.put("variant", BloomrayEntityVariant.ENTRY_CODEC, ops, this.dataTracker.get(VARIANT));
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		RegistryOps<NbtElement> ops = this.getRegistryManager().getOps(NbtOps.INSTANCE);

		this.setVariant(
			nbt.get("variant", BloomrayEntityVariant.ENTRY_CODEC, ops)
				.orElse(BloomrayEntityVariant.getDefaultEntry(this.getRegistryManager()))
		);
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		this.getRegistryManager().getOrThrow(AbysmRegistryKeys.BLOOMRAY_ENTITY_VARIANT)
			.getRandom(this.random)
			.ifPresentOrElse(
				this::setVariant,
				() -> setVariant(BloomrayEntityVariant.getDefaultEntry(world.getRegistryManager()))
			);
		this.alertEcosystemOfSpawn();
		return super.initialize(world, difficulty, spawnReason, entityData);
	}

	public static DefaultAttributeContainer.Builder createRayAttributes() {
		return AbstractSchoolingFishEntity.createPredatoryFishAttributes()
			.add(EntityAttributes.MOVEMENT_SPEED, 0.85)
			.add(EntityAttributes.MAX_HEALTH, 14)
			.add(EntityAttributes.ATTACK_DAMAGE, 1.5);
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
	public void tick() {
		/*
		// for some reason, the bloomray's yaw temporarily desyncs (sometimes) when moving to a new location
		float diff = Math.abs(this.bodyYaw - this.lastBodyYaw);
		if (diff > 1) {
			Abysm.LOGGER.info("Change in bodyYaw was {}, bodyYaw was {}, lastBodyYaw was {}", diff, this.bodyYaw, this.lastBodyYaw);
		}
		 */
		super.tick();
		this.tickEcosystemLogic();
	}

	@Override
	public void onRemove(RemovalReason reason) {
		this.alertEcosystemOfDeath();
		super.onRemove(reason);
	}

	@Override
	public EcosystemLogic getEcosystemLogic() {
		return this.ecosystemLogic;
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.BLOOMRAY;
	}

	@Override
	public void tickMovement() {
		super.tickMovement();

		World world = this.getWorld();
		if (world.isClient() && this.random.nextFloat() < 0.15) {
			Vec3d facing = Vec3d.fromPolar(this.getPitch(), this.bodyYaw).multiply(0.5);
			Vec3d pos = this.getPos().add(facing);

			BloomrayEntityVariant variant = this.getVariant();

			int glimmerCount = 1 + this.random.nextInt(3);
			for (int i = 0; i < glimmerCount; i++) {
				spawnParticles(world, pos, this.random, variant.glimmerParticle, 0.4F, 1F, 1F);
			}

			int thornsCount = 2 + this.random.nextInt(2);
			for (int i = 0; i < thornsCount; i++) {
				spawnParticles(world, pos, random, variant.thornsParticle, 0.9F, 2.6F, 1.4F);
			}
		}
	}

	protected void spawnParticles(World world, Vec3d pos, Random random, ParticleEffect particle, float width, float orthogonalVelocityMultiplier, float normalVelocityMultiplier) {
		double x = pos.getX() + width * (random.nextFloat() - 0.5);
		double y = pos.getY() + 0.5F - 0.45F;
		double z = pos.getZ() + width * (random.nextFloat() - 0.5);

		double vx = random.nextGaussian() * (0.015) * orthogonalVelocityMultiplier + getVelocity().x;
		double vy = random.nextGaussian() * (0.015) * orthogonalVelocityMultiplier;
		double vz = random.nextGaussian() * (0.015) * orthogonalVelocityMultiplier + getVelocity().z;

		vy *= 0.1F;
		vy += random.nextFloat() * 0.12F * normalVelocityMultiplier;

		world.addParticleClient(particle, x, y, z, vx, vy, vz);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
		AnimationController<AbstractFloralFishEntity> animController = new AnimationController<>(ANIM_CONTROLLER_STRING, 5, event -> PlayState.STOP);

		controllerRegistrar.add(animController);
	}

	@Override
	public BloomrayEntityVariant getVariant() {
		return this.dataTracker.get(VARIANT).value();
	}

	@Override
	public void setVariant(RegistryEntry<BloomrayEntityVariant> variant) {
		this.dataTracker.set(VARIANT, variant);
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
			return TargetUtil.find(this.mob, 17, 2);
		}
	}
}
