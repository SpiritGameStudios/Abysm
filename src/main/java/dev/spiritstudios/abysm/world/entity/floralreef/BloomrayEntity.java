package dev.spiritstudios.abysm.world.entity.floralreef;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.core.registries.AbysmMetatags;
import dev.spiritstudios.abysm.core.registries.AbysmRegistryKeys;
import dev.spiritstudios.abysm.core.registries.AbysmSoundEvents;
import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import dev.spiritstudios.abysm.world.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.world.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.world.ecosystem.registry.EcosystemData;
import dev.spiritstudios.abysm.world.entity.AbysmTrackedDataHandlers;
import dev.spiritstudios.abysm.world.entity.SimpleEcoSchoolingFishEntity;
import dev.spiritstudios.abysm.world.entity.ai.GracefulLookControl;
import dev.spiritstudios.abysm.world.entity.ai.GracefulMoveControl;
import dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.FleePredatorsGoal;
import dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.HuntPreyGoal;
import dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.world.entity.variant.Variantable;
import dev.spiritstudios.spectre.api.core.math.Query;
import dev.spiritstudios.spectre.api.world.entity.animation.AnimationController;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BloomrayEntity extends WaterAnimal implements Variantable<BloomrayEntityVariant>, EcologicalEntity {
	public final AnimationController movement;
	public final AnimationController antenna;

	private final Query query = new Query();
	public static final EntityDataAccessor<Holder<BloomrayEntityVariant>> VARIANT = SynchedEntityData.defineId(BloomrayEntity.class, AbysmTrackedDataHandlers.BLOOMRAY_VARIANT);

	protected EcosystemLogic ecosystemLogic;

	// TODO - Custom AI for hiding in Bloomshroom crowns when scared(player nearby? Bigger bloomray/TBD enemy nearby?)
	public BloomrayEntity(EntityType<? extends WaterAnimal> entityType, Level world) {
		super(entityType, world);
		this.ecosystemLogic = createEcosystemLogic(this);
		this.moveControl = new GracefulMoveControl(this, 85, 10, 0.02F, 0.1F, true);
		this.lookControl = new GracefulLookControl(this, 20);

		movement = AnimationController.create(
			Abysm.id("bloomray"),
			"controller.animation.bloomray.movement",
			this
		);

		antenna = AnimationController.create(
			Abysm.id("bloomray"),
			"controller.animation.bloomray.antenna",
			this
		);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(VARIANT, BloomrayEntityVariant.getDefaultEntry(this.registryAccess()));
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput view) {
		super.addAdditionalSaveData(view);
		view.store("variant", BloomrayEntityVariant.ENTRY_CODEC, this.entityData.get(VARIANT));
	}

	@Override
	protected void readAdditionalSaveData(ValueInput view) {
		super.readAdditionalSaveData(view);
		view.read("variant", BloomrayEntityVariant.ENTRY_CODEC).ifPresent(this::setVariant);
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
		PriorityProvider.pick(
			this.registryAccess().lookupOrThrow(AbysmRegistryKeys.BLOOMRAY_ENTITY_VARIANT).listElements(),
			Holder::value, random,
			SpawnContext.create(world, this.blockPosition())
		).ifPresent(this::setVariant);

		this.alertEcosystemOfSpawn();
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
	}

	public static AttributeSupplier.Builder createRayAttributes() {
		return SimpleEcoSchoolingFishEntity.createPredatoryFishAttributes()
			.add(Attributes.MOVEMENT_SPEED, 1.0F)
			.add(Attributes.MAX_HEALTH, 14)
			.add(Attributes.ATTACK_DAMAGE, 5);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FleePredatorsGoal(this, 10.0F, 1.1, 1.2));
		this.goalSelector.addGoal(2, new RepopulateGoal(this, 1.25));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, false));
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0, 10));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HuntPreyGoal(this, false));
	}

	@Override
	protected void doPush(Entity entity) {
		if (entity instanceof BloomrayEntity) {
			return;
		}
		super.doPush(entity);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickEcosystemLogic();

		query.set(this, 0F);

		movement.tick(query);
		antenna.tick(query);
	}

	@Override
	public void onRemoval(RemovalReason reason) {
		this.alertEcosystemOfDeath();
		super.onRemoval(reason);
	}

	@Override
	public boolean canBeLeashed() {
		return true;
	}

	@Override
	public EcosystemLogic getEcosystemLogic() {
		return this.ecosystemLogic;
	}

	@Override
	public EcosystemData getEcosystemData() {
		return getType().getDataOrThrow(AbysmMetatags.ECOSYSTEM_DATA);
	}

	@Override
	protected @Nullable SoundEvent getAmbientSound() {
		return AbysmSoundEvents.ENTITY_BLOOMRAY_AMBIENT;
	}

	@Override
	protected @Nullable SoundEvent getHurtSound(DamageSource source) {
		return AbysmSoundEvents.ENTITY_BLOOMRAY_HURT;
	}

	@Override
	protected @Nullable SoundEvent getDeathSound() {
		return AbysmSoundEvents.ENTITY_BLOOMRAY_DEATH;
	}

	@Override
	protected PathNavigation createNavigation(Level world) {
		return new WaterBoundPathNavigation(this, world);
	}

	@Override
	public void aiStep() {
		super.aiStep();

		Level world = this.level();
		if (world.isClientSide() && this.random.nextFloat() < 0.15) {
			Vec3 facing = Vec3.directionFromRotation(this.getXRot(), this.yBodyRot).scale(0.5);
			Vec3 pos = this.position().add(facing);

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

	protected void spawnParticles(Level world, Vec3 pos, RandomSource random, ParticleOptions particle, float width, float orthogonalVelocityMultiplier, float normalVelocityMultiplier) {
		double x = pos.x() + width * (random.nextFloat() - 0.5);
		double y = pos.y() + 0.5F - 0.45F;
		double z = pos.z() + width * (random.nextFloat() - 0.5);

		double vx = random.nextGaussian() * (0.015) * orthogonalVelocityMultiplier + getDeltaMovement().x;
		double vy = random.nextGaussian() * (0.015) * orthogonalVelocityMultiplier;
		double vz = random.nextGaussian() * (0.015) * orthogonalVelocityMultiplier + getDeltaMovement().z;

		vy *= 0.1F;
		vy += random.nextFloat() * 0.12F * normalVelocityMultiplier;

		world.addParticle(particle, x, y, z, vx, vy, vz);
	}

	@Override
	public BloomrayEntityVariant getVariant() {
		return this.entityData.get(VARIANT).value();
	}

	@Override
	public void setVariant(Holder<BloomrayEntityVariant> variant) {
		this.entityData.set(VARIANT, variant);
	}

	@Override
	public int getMaxHeadXRot() {
		return 1;
	}
}
