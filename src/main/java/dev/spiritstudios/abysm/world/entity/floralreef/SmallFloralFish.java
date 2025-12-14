package dev.spiritstudios.abysm.world.entity.floralreef;

import dev.spiritstudios.abysm.core.registries.AbysmSoundEvents;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.world.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.world.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.world.entity.pattern.AbysmEntityPatternVariants;
import dev.spiritstudios.abysm.world.entity.pattern.EntityPattern;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class SmallFloralFish extends AbstractFloralFish implements EcologicalEntity {
	protected EcosystemLogic ecosystemLogic;

	public SmallFloralFish(EntityType<? extends AbstractFloralFish> entityType, Level world) {
		super(entityType, world);
		this.ecosystemLogic = createEcosystemLogic(this);
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
		this.alertEcosystemOfSpawn();
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickEcosystemLogic();
	}

	@Override
	public void onRemoval(RemovalReason reason) {
		this.alertEcosystemOfDeath();
		super.onRemoval(reason);
	}

	@Override
	public EntityPattern getDefaultPattern(HolderGetter<EntityPatternVariant> lookup) {
		return new EntityPattern(
			lookup.getOrThrow(AbysmEntityPatternVariants.FLORAL_FISH_SMALL_COLORFUL),
			DyeColor.PINK.getTextureDiffuseColor(), DyeColor.LIGHT_BLUE.getTextureDiffuseColor()
		);
	}

	@Override
	public EcosystemLogic getEcosystemLogic() {
		return this.ecosystemLogic;
	}

	@Override
	protected @Nullable SoundEvent getHurtSound(DamageSource source) {
		return AbysmSoundEvents.ENTITY_SMALL_FLORAL_FISH_HURT;
	}

	@Override
	protected @Nullable SoundEvent getDeathSound() {
		return AbysmSoundEvents.ENTITY_SMALL_FLORAL_FISH_DEATH;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return AbysmSoundEvents.ENTITY_SMALL_FLORAL_FISH_FLOP;
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(AbysmItems.SMALL_FLORAL_FISH_BUCKET);
	}
}
