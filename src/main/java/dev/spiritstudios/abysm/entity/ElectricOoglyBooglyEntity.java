package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.data.variant.ElectricOoglyBooglyVariant;
import dev.spiritstudios.abysm.entity.variant.Variantable;
import dev.spiritstudios.abysm.particle.OoglyBooglyFumesParticleEffect;
import dev.spiritstudios.abysm.registry.AbysmParticleTypes;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.List;

// TODO - (datagen'd?) tags
// TODO - proper health
public class ElectricOoglyBooglyEntity extends AbstractSchoolingFishEntity implements Variantable<ElectricOoglyBooglyVariant> {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.ooglyboogly.idle");
	public static final RawAnimation BONK_ANIM = RawAnimation.begin().thenPlay("animation.ooglyboogly.bonk");
	public static final RawAnimation BLOWING_UP_WITH_MIND_ANIM = RawAnimation.begin().thenLoop("animation.ooglyboogly.blowingupwithmind");

	public static final TrackedData<Integer> VARIANT_ID = DataTracker.registerData(ElectricOoglyBooglyEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Boolean> BLOWING_UP_WITH_MIND = DataTracker.registerData(ElectricOoglyBooglyEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public int ticksSinceBlowingUp = 0;

	public ElectricOoglyBooglyEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		int variants = this.getRegistryManager().getOrThrow(AbysmRegistries.ELECTRIC_OOGLY_BOOGLY_VARIANT).size();
		int index = this.getRandom().nextInt(variants);
		ElectricOoglyBooglyVariant variant = this.getRegistryManager().getOrThrow(AbysmRegistries.ELECTRIC_OOGLY_BOOGLY_VARIANT).get(index);
		this.setVariant(variant);
		return super.initialize(world, difficulty, spawnReason, entityData);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(BLOWING_UP_WITH_MIND, false);
		builder.add(VARIANT_ID, ElectricOoglyBooglyVariant.getDefaultIntId(this.getRegistryManager()));
	}

	@Override
	public void tick() {
		super.tick();
		if(this.isBlowingUpWithMind()) {
			this.ticksSinceBlowingUp++;
			spawnElectricityParticle();
			spawnFumesParticle();
			if(this.ticksSinceBlowingUp % 10 == 0) {
				this.spawnElectricitySpiralParticles();
				this.spawnElectricitySpeckParticles();
				if(!this.getWorld().isClient) {
					ServerWorld serverWorld = (ServerWorld) this.getWorld();
					serverWorld.playSoundFromEntity(this, this, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.HOSTILE, 1f, 0.2f + (this.ticksSinceBlowingUp / 60f));
				}
			}
			if(this.ticksSinceBlowingUp >= 200) {
				if(!this.getWorld().isClient) {
					float power = this.getVariant().isDeadly() ? 8f : 0f;
					ServerWorld serverWorld = (ServerWorld) this.getWorld();
					serverWorld.createExplosion(this, this.getX(), this.getY(), this.getZ(), power, World.ExplosionSourceType.MOB);
					this.discard();
				}
			}
		}

		if(this.getWorld().isClient) return;
		if(this.age % 2 != 0) return;
		this.checkForBonk();
	}

	public void checkForBonk() {
		List<Entity> nearbyPlayers = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.2f, -0.01f, 0.2f), this::canHit);
		if(nearbyPlayers.isEmpty()) return;

		this.getLookControl().lookAt(nearbyPlayers.getFirst());
		if(!this.isBlowingUpWithMind()) {
			for (Entity nearbyPlayer : nearbyPlayers) {
				double deltaX = (nearbyPlayer.getX() - this.getX()) * 1.25;
				double deltaY = (nearbyPlayer.getY() - this.getY()) * 1.5;
				double deltaZ = (nearbyPlayer.getZ() - this.getZ()) * 1.25;

				nearbyPlayer.addVelocity(deltaX, deltaY, deltaZ);
				nearbyPlayer.velocityModified = true;
			}
			this.blowUpWithMind();
		}
	}

	protected boolean canHit(Entity entity) {
		return entity.isPlayer() && entity.canBeHitByProjectile() && !entity.isSpectator();
	}

	public void blowUpWithMind() {
		this.setIsBlowingUpWithMind(true);
		ServerWorld serverWorld = (ServerWorld) this.getWorld();
		serverWorld.spawnParticles(AbysmParticleTypes.OOGLY_BOOGLY_SPARKLE, this.getX(), this.getEyeY(), this.getZ(), 1, 0, 0, 0, 0);
		serverWorld.playSoundFromEntity(this, this, SoundEvents.ITEM_TRIDENT_THUNDER.value(), SoundCategory.NEUTRAL, 0.75f, 2f);
		serverWorld.playSoundFromEntity(this, this, SoundEvents.ENTITY_ALLAY_HURT, SoundCategory.NEUTRAL, 1f, 2f);
		this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 20, 50, false, false));
		this.triggerAnim(ANIM_CONTROLLER_STRING, animString(BONK_ANIM));
	}

	@Override
	public boolean canMoveVoluntarily() {
		return !this.isBlowingUpWithMind() && super.canMoveVoluntarily();
	}

	private void spawnElectricityParticle() {
		this.getWorld().addParticleClient(
			AbysmParticleTypes.OOGLY_BOOGLY_ELECTRICITY,
			this.getX() + this.random.nextGaussian() / 2f,
			this.getEyeY() + this.random.nextGaussian() / 2f,
			this.getZ() + this.random.nextGaussian() / 2f,
			0, 0, 0
		);
	}

	private void spawnFumesParticle() {
		ElectricOoglyBooglyVariant variant = this.getVariant();
		this.getWorld().addParticleClient(
			new OoglyBooglyFumesParticleEffect(variant.getElectricityColor(), variant.isDeadly()),
			this.getX(),
			this.getY() - 0.35f,
			this.getZ(),
			0, 0, 0
		);
	}

	private void spawnElectricitySpiralParticles() {
		this.getWorld().addParticleClient(
			AbysmParticleTypes.OOGLY_BOOGLY_ELECTRICITY_SPIRAL,
			this.getX(),
			this.getEyeY() + this.random.nextGaussian() / 2f,
			this.getZ(),
			0, 0, 0
		);
	}

	private void spawnElectricitySpeckParticles() {
		this.getWorld().addParticleClient(
			AbysmParticleTypes.OOGLY_BOOGLY_ELECTRICITY_SPECK,
			this.getX(),
			this.getEyeY() + this.random.nextGaussian() / 2f,
			this.getZ(),
			0, 0, 0
		);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		AnimationController<ElectricOoglyBooglyEntity> animController = new AnimationController<>(ANIM_CONTROLLER_STRING, 5, event -> {
			if(event.animatable().isBlowingUpWithMind()) {
				return event.setAndContinue(BLOWING_UP_WITH_MIND_ANIM);
			}
			return event.setAndContinue(IDLE_ANIM);
		});
		animController.triggerableAnim(animString(BONK_ANIM), BONK_ANIM);

		controllers.add(animController);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("variantId", ElectricOoglyBooglyVariant.toIntId(this.getRegistryManager(), this.getVariant()));
		nbt.putBoolean("blowing_up_with_mind", this.isBlowingUpWithMind());
		nbt.putInt("ticks_since_blowing_up", this.ticksSinceBlowingUp);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setVariantIntId(nbt.getInt("variantId").orElse(ElectricOoglyBooglyVariant.getDefaultIntId(this.getRegistryManager())));
		this.setIsBlowingUpWithMind(nbt.getBoolean("blowing_up_with_mind").orElse(false));
		this.ticksSinceBlowingUp = nbt.getInt("ticks_since_blowing_up").orElse(0);
	}

	public boolean isBlowingUpWithMind() {
		return this.dataTracker.get(BLOWING_UP_WITH_MIND);
	}

	public void setIsBlowingUpWithMind(boolean isBlowingUpWithMind) {
		this.dataTracker.set(BLOWING_UP_WITH_MIND, isBlowingUpWithMind);
	}

	@Override
	public ElectricOoglyBooglyVariant getVariant() {
		return ElectricOoglyBooglyVariant.fromIntId(this.getRegistryManager(), this.getVariantIntId());
	}

	@Override
	public void setVariant(ElectricOoglyBooglyVariant variant) {
		this.setVariantIntId(ElectricOoglyBooglyVariant.toIntId(this.getRegistryManager(), variant));
	}

	@Override
	public int getVariantIntId() {
		return this.dataTracker.get(VARIANT_ID);
	}

	@Override
	public void setVariantIntId(int variantId) {
		this.dataTracker.set(VARIANT_ID, variantId);
	}

	private static String animString(RawAnimation anim) {
		return anim.getAnimationStages().getFirst().animationName();
	}
}
