package dev.spiritstudios.abysm.world.entity.generic;

import dev.spiritstudios.abysm.core.registries.AbysmSoundEvents;
import dev.spiritstudios.abysm.world.entity.SimpleEcoSchoolingFishEntity;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SynthethicOrniothopeEntity extends SimpleEcoSchoolingFishEntity {
	public SynthethicOrniothopeEntity(EntityType<SynthethicOrniothopeEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new SwimToRandomPlaceGoal(this, 1.0F));
	}

	@Override
	public int getMaxSchoolSize() {
		return 3;
	}

	@Override
	protected @Nullable SoundEvent getHurtSound(DamageSource source) {
		return AbysmSoundEvents.ENTITY_SYNTHETHIC_ORNIOTHOPE_HURT;
	}

	@Override
	protected @Nullable SoundEvent getDeathSound() {
		return AbysmSoundEvents.ENTITY_SYNTHETHIC_ORNIOTHOPE_DEATH;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return AbysmSoundEvents.ENTITY_SYNTHETHIC_ORNIOTHOPE_FLOP;
	}

	// TODO: Bucket
	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(AbysmItems.PADDLEFISH_BUCKET);
	}
}
