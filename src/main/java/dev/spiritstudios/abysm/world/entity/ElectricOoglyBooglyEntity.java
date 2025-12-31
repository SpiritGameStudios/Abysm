package dev.spiritstudios.abysm.world.entity;

import dev.spiritstudios.abysm.core.particles.AbysmParticleTypes;
import dev.spiritstudios.abysm.core.registries.AbysmRegistryKeys;
import dev.spiritstudios.abysm.core.registries.AbysmSoundEvents;
import dev.spiritstudios.abysm.data.variant.ElectricOoglyBooglyVariant;
import dev.spiritstudios.abysm.world.entity.variant.Variantable;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// TODO - (datagen'd?) tags
// TODO - proper health
public class ElectricOoglyBooglyEntity extends SimpleFishEntity implements Variantable<ElectricOoglyBooglyVariant> {

	public static final EntityDataAccessor<Holder<ElectricOoglyBooglyVariant>> VARIANT = SynchedEntityData.defineId(ElectricOoglyBooglyEntity.class, AbysmEntityDataSerializers.ELECTRIC_OOGLY_BOOGLY_VARIANT);
	public static final EntityDataAccessor<Boolean> BLOWING_UP_WITH_MIND = SynchedEntityData.defineId(ElectricOoglyBooglyEntity.class, EntityDataSerializers.BOOLEAN);
	public int ticksSinceBlowingUp = 0;

	public final AnimationState bonk = new AnimationState();

	public ElectricOoglyBooglyEntity(EntityType<? extends SimpleFishEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
		PriorityProvider.pick(
			this.registryAccess().lookupOrThrow(AbysmRegistryKeys.ELECTRIC_OOGLY_BOOGLY_VARIANT).listElements(),
			Holder::value, random,
			SpawnContext.create(world, this.blockPosition())
		).ifPresent(this::setVariant);

		return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(BLOWING_UP_WITH_MIND, false);
		builder.define(VARIANT, ElectricOoglyBooglyVariant.getDefaultEntry(this.registryAccess()));
	}

	@Override
	public void tick() {
		super.tick();
		if (this.isBlowingUpWithMind()) {
			this.ticksSinceBlowingUp++;
			spawnElectricityParticle();
			spawnFumesParticle();
			if (this.ticksSinceBlowingUp % 10 == 0) {
				this.spawnElectricitySpiralParticles();
				this.spawnElectricitySpeckParticles();
				if (!this.level().isClientSide()) {
					ServerLevel serverLevel = (ServerLevel) this.level();
					serverLevel.playSound(this, this, SoundEvents.BEACON_ACTIVATE, SoundSource.HOSTILE, 1f, 0.2f + (this.ticksSinceBlowingUp / 60f));
				}
			}
			if (this.ticksSinceBlowingUp >= 200) {
				if (!this.level().isClientSide()) {
					ServerLevel serverLevel = (ServerLevel) this.level();
					serverLevel.explode(this, this.getX(), this.getY(), this.getZ(), getVariant().explosionPower, Level.ExplosionInteraction.MOB);
					this.discard();
				}
			}
		}

		if (this.level().isClientSide()) return;
		if (this.tickCount % 2 != 0) return;
		this.checkForBonk();
	}

	public void checkForBonk() {
		List<Entity> nearbyPlayers = this.level().getEntities(this, this.getBoundingBox().inflate(0.2f, -0.01f, 0.2f), this::canHit);
		if (nearbyPlayers.isEmpty()) return;

		this.getLookControl().setLookAt(nearbyPlayers.getFirst());
		if (!this.isBlowingUpWithMind()) {
			for (Entity nearbyPlayer : nearbyPlayers) {
				double deltaX = (nearbyPlayer.getX() - this.getX()) * 1.25;
				double deltaY = (nearbyPlayer.getY() - this.getY()) * 1.5;
				double deltaZ = (nearbyPlayer.getZ() - this.getZ()) * 1.25;

				nearbyPlayer.push(deltaX, deltaY, deltaZ);
				nearbyPlayer.hurtMarked = true;
			}
			this.blowUpWithMind();
		}
	}

	protected boolean canHit(Entity entity) {
		return entity.isAlwaysTicking() && entity.canBeHitByProjectile() && !entity.isSpectator();
	}

	public void blowUpWithMind() {
		this.setIsBlowingUpWithMind(true);
		ServerLevel serverLevel = (ServerLevel) this.level();
		serverLevel.sendParticles(AbysmParticleTypes.OOGLY_BOOGLY_SPARKLE, this.getX(), this.getEyeY(), this.getZ(), 1, 0, 0, 0, 0);
		serverLevel.playSound(this, this, SoundEvents.TRIDENT_THUNDER.value(), SoundSource.NEUTRAL, 0.75f, 2f);
		serverLevel.playSound(this, this, SoundEvents.ALLAY_HURT, SoundSource.NEUTRAL, 1f, 2f);
		this.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 20 * 20, 50, false, false));
		bonk.start(tickCount);
	}

	@Override
	public boolean canSimulateMovement() {
		return !this.isBlowingUpWithMind() && super.canSimulateMovement();
	}

	private void spawnElectricityParticle() {
		this.level().addParticle(
			AbysmParticleTypes.OOGLY_BOOGLY_ELECTRICITY,
			this.getX() + this.random.nextGaussian() / 2f,
			this.getEyeY() + this.random.nextGaussian() / 2f,
			this.getZ() + this.random.nextGaussian() / 2f,
			0, 0, 0
		);
	}

	private void spawnFumesParticle() {
		this.level().addParticle(
			getVariant().fumesParticle,
			this.getX(),
			this.getY() - 0.35f,
			this.getZ(),
			0, 0, 0
		);
	}

	private void spawnElectricitySpiralParticles() {
		this.level().addParticle(
			AbysmParticleTypes.OOGLY_BOOGLY_ELECTRICITY_SPIRAL,
			this.getX(),
			this.getEyeY() + this.random.nextGaussian() / 2f,
			this.getZ(),
			0, 0, 0
		);
	}

	private void spawnElectricitySpeckParticles() {
		this.level().addParticle(
			AbysmParticleTypes.OOGLY_BOOGLY_ELECTRICITY_SPECK,
			this.getX(),
			this.getEyeY() + this.random.nextGaussian() / 2f,
			this.getZ(),
			0, 0, 0
		);
	}

//	@Override
//	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//		AnimationController<ElectricOoglyBooglyEntity> animController = new AnimationController<>("default", 5, event -> {
//			if (event.animatable().isBlowingUpWithMind()) {
//				return event.setAndContinue(BLOWING_UP_WITH_MIND_ANIM);
//			}
//			return event.setAndContinue(IDLE_ANIM);
//		});
//		animController.triggerableAnim(animString(BONK_ANIM), BONK_ANIM);
//
//		controllers.add(animController);
//	}

	@Override
	public void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);

		output.store("variant", ElectricOoglyBooglyVariant.ENTRY_CODEC, this.entityData.get(VARIANT));

		output.putBoolean("blowingUpWithMind", this.isBlowingUpWithMind());
		output.putInt("ticksSinceBlowingUp", this.ticksSinceBlowingUp);
	}

	@Override
	public void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);

		input.read("variant", ElectricOoglyBooglyVariant.ENTRY_CODEC).ifPresent(this::setVariant);

		this.setIsBlowingUpWithMind(input.getBooleanOr("blowingUpWithMind", this.isBlowingUpWithMind()));
		this.ticksSinceBlowingUp = input.getIntOr("ticksSinceBlowingUp", this.ticksSinceBlowingUp);
	}

	public boolean isBlowingUpWithMind() {
		return this.entityData.get(BLOWING_UP_WITH_MIND);
	}

	public void setIsBlowingUpWithMind(boolean isBlowingUpWithMind) {
		this.entityData.set(BLOWING_UP_WITH_MIND, isBlowingUpWithMind);
	}

	@Override
	public ElectricOoglyBooglyVariant getVariant() {
		return this.entityData.get(VARIANT).value();
	}

	@Override
	public void setVariant(Holder<ElectricOoglyBooglyVariant> variant) {
		this.entityData.set(VARIANT, variant);
	}

	// TODO: SoundEvents
	@Override
	protected @Nullable SoundEvent getHurtSound(DamageSource source) {
		return AbysmSoundEvents.ENTITY_PADDLEFISH_HURT;
	}

	@Override
	protected @Nullable SoundEvent getDeathSound() {
		return AbysmSoundEvents.ENTITY_PADDLEFISH_DEATH;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return AbysmSoundEvents.ENTITY_PADDLEFISH_FLOP;
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(AbysmItems.PADDLEFISH_BUCKET);
	}
}
