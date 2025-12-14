package dev.spiritstudios.abysm.world.entity;

import dev.spiritstudios.abysm.core.registries.AbysmSoundEvents;
import dev.spiritstudios.abysm.world.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.world.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.world.entity.ai.GracefulLookControl;
import dev.spiritstudios.abysm.world.entity.ai.GracefulMoveControl;
import dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.FleePredatorsGoal;
import dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.HuntPreyGoal;
import dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.world.entity.floralreef.BloomrayEntity;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class ReticulatedFliprayEntity extends SimpleFishEntity implements EcologicalEntity {
	protected EcosystemLogic ecosystemLogic;

	public ReticulatedFliprayEntity(EntityType<? extends SimpleFishEntity> entityType, Level world) {
		super(entityType, world);
		this.ecosystemLogic = createEcosystemLogic(this);
		this.moveControl = new GracefulMoveControl(this, 90, 5, 0.02F, 0.1F, true);
		this.lookControl = new GracefulLookControl(this, 30);
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
		this.alertEcosystemOfSpawn();
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
	}

	@Override
	public EcosystemLogic getEcosystemLogic() {
		return this.ecosystemLogic;
	}

	@Override
	public void onRemoval(RemovalReason reason) {
		this.alertEcosystemOfDeath();
		super.onRemoval(reason);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickEcosystemLogic();
	}

	public static AttributeSupplier.Builder createRayAttributes() {
		return BloomrayEntity.createRayAttributes()
			.add(Attributes.MAX_HEALTH, 40);
	}

	@Override
	public int getMaxHeadXRot() {
		return 1;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FleePredatorsGoal(this, 10.0F, 1.1, 1.2));
		this.goalSelector.addGoal(2, new RepopulateGoal(this, 1.25));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, false));
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 0.5F, 10));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HuntPreyGoal(this, false));
	}

	@Override
	protected @Nullable SoundEvent getHurtSound(DamageSource source) {
		return AbysmSoundEvents.ENTITY_RETICULATED_FLIPRAY_HURT;
	}

	@Override
	protected @Nullable SoundEvent getDeathSound() {
		return AbysmSoundEvents.ENTITY_RETICULATED_FLIPRAY_DEATH;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return AbysmSoundEvents.ENTITY_RETICULATED_FLIPRAY_FLOP;
	}

	// TODO: Bucket
	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(AbysmItems.PADDLEFISH_BUCKET);
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 1;
	}

	@Override
	public void onInsideBubbleColumn(boolean drag) {
	}
}
