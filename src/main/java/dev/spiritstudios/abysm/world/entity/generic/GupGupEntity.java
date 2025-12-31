package dev.spiritstudios.abysm.world.entity.generic;

import dev.spiritstudios.abysm.core.registries.AbysmRegistryKeys;
import dev.spiritstudios.abysm.core.registries.AbysmSoundEvents;
import dev.spiritstudios.abysm.data.variant.GupGupVariant;
import dev.spiritstudios.abysm.world.entity.AbysmEntityDataSerializers;
import dev.spiritstudios.abysm.world.entity.SimpleFishEntity;
import dev.spiritstudios.abysm.world.entity.ai.goal.SwimAroundBoidGoal;
import dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.FleePredatorsGoal;
import dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.world.entity.variant.Variantable;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class GupGupEntity extends SimpleFishEntity implements Variantable<GupGupVariant> {
	public static final EntityDataAccessor<Holder<GupGupVariant>> VARIANT = SynchedEntityData.defineId(GupGupEntity.class, AbysmEntityDataSerializers.GUP_GUP_VARIANT);


	public GupGupEntity(EntityType<GupGupEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	protected void registerGoals() {
		// No super, there will be lots of these so we don't want to add goals we don't need.
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6, 1.4, EntitySelector.NO_SPECTATORS::test));
		this.goalSelector.addGoal(1, new FleePredatorsGoal(this, 10.0F, 1.1, 1.2));
		this.goalSelector.addGoal(2, new RepopulateGoal(this, 1.25));
		this.goalSelector.addGoal(4, new SwimAroundBoidGoal(
			this,
			2.0F,
			100 * Mth.DEG_TO_RAD,
			160 * Mth.DEG_TO_RAD,
			0.25F, 0.6F, 0.2F, 0.05F, 2F,
			0.025F, 0.05F
		));
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 50;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(VARIANT, GupGupVariant.getDefaultEntry(this.registryAccess()));
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput view) {
		super.addAdditionalSaveData(view);
		view.store("variant", GupGupVariant.ENTRY_CODEC, this.entityData.get(VARIANT));
	}

	@Override
	protected void readAdditionalSaveData(ValueInput view) {
		super.readAdditionalSaveData(view);
		view.read("variant", GupGupVariant.ENTRY_CODEC).ifPresent(this::setVariant);
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
		PriorityProvider.pick(
			this.registryAccess().lookupOrThrow(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT).listElements(),
			Holder::value, random,
			SpawnContext.create(world, this.blockPosition())
		).ifPresent(this::setVariant);

		this.alertEcosystemOfSpawn();
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		double d = this.getBoundingBox().getSize();
		if (Double.isNaN(d)) {
			d = 1.0;
		}

		d *= 64.0 * 5; // 5x vanilla draw distance
		return distance < d * d;
	}

	@Override
	protected @Nullable SoundEvent getHurtSound(DamageSource source) {
		return AbysmSoundEvents.ENTITY_GUP_GUP_HURT;
	}

	@Override
	protected @Nullable SoundEvent getDeathSound() {
		return AbysmSoundEvents.ENTITY_GUP_GUP_DEATH;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return AbysmSoundEvents.ENTITY_GUP_GUP_FLOP;
	}

	// TODO: Bucket
	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(AbysmItems.PADDLEFISH_BUCKET);
	}

	@Override
	public GupGupVariant getVariant() {
		return this.entityData.get(VARIANT).value();
	}

	@Override
	public void setVariant(Holder<GupGupVariant> variant) {
		this.entityData.set(VARIANT, variant);
	}
}
