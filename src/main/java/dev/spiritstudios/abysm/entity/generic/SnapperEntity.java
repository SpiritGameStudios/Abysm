package dev.spiritstudios.abysm.entity.generic;

import dev.spiritstudios.abysm.data.variant.SnapperEntityVariant;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.AbysmTrackedDataHandlers;
import dev.spiritstudios.abysm.entity.SimpleFishEntity;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.FleePredatorsGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.HuntPreyGoal;
import dev.spiritstudios.abysm.entity.variant.Variantable;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class SnapperEntity extends SimpleFishEntity implements Variantable<SnapperEntityVariant> {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.snapper.idle");

	public static final EntityDataAccessor<Holder<SnapperEntityVariant>> VARIANT = SynchedEntityData.defineId(SnapperEntity.class, AbysmTrackedDataHandlers.SNAPPER_VARIANT);

	public SnapperEntity(EntityType<SnapperEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
		this.goalSelector.addGoal(1, new FleePredatorsGoal(this, 10.0F, 1.1, 1.2));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6, 1.4, EntitySelector.NO_SPECTATORS::test));
		this.targetSelector.addGoal(1, new HuntPreyGoal(this, false));

		this.goalSelector.addGoal(4, new RandomSwimmingGoal(
			this,
			1.0F,
			40
		));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
			this,
			Player.class,
			200, // can be altered
			true,
			false,
			(living, world) -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(living)
		).setUnseenMemoryTicks(300));
	}

	@Override
	public boolean doHurtTarget(ServerLevel world, Entity target) {
		boolean tryAttack = super.doHurtTarget(world, target);
		if (tryAttack) {
			if (!this.isHunting() && this.getTarget() != null) {
				this.setTarget(null);
			}
		}
		return tryAttack;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(VARIANT, SnapperEntityVariant.getDefaultEntry(this.registryAccess()));
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.SNAPPER;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(0, event -> event.setAndContinue(IDLE_ANIM)));
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput view) {
		super.addAdditionalSaveData(view);
		view.store("variant", SnapperEntityVariant.ENTRY_CODEC, this.entityData.get(VARIANT));
	}

	@Override
	protected void readAdditionalSaveData(ValueInput view) {
		super.readAdditionalSaveData(view);
		view.read("variant", SnapperEntityVariant.ENTRY_CODEC).ifPresent(this::setVariant);
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
		PriorityProvider.pick(
			this.registryAccess().lookupOrThrow(AbysmRegistryKeys.SNAPPER_ENTITY_VARIANT).listElements(),
			Holder::value, random,
			SpawnContext.create(world, this.blockPosition())
		).ifPresent(this::setVariant);

		return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
	}

	@Override
	public SnapperEntityVariant getVariant() {
		return this.entityData.get(VARIANT).value();
	}

	@Override
	public void setVariant(Holder<SnapperEntityVariant> variant) {
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
