package dev.spiritstudios.abysm.world.entity.generic;

import dev.spiritstudios.abysm.core.registries.AbysmSoundEvents;
import dev.spiritstudios.abysm.mixin.ecosystem.goal.AbstractSchoolingFishMixin;
import dev.spiritstudios.abysm.world.entity.SimpleEcoSchoolingFishEntity;
import dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ArowanaMagiciiEntity extends SimpleEcoSchoolingFishEntity {
	public ArowanaMagiciiEntity(EntityType<ArowanaMagiciiEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new SwimToRandomPlaceGoal(this, 3.0F));
		this.goalSelector.addGoal(2, new RepopulateGoal(this, 1.25));
	}

	public void pathToLeader() {
		if (this.isFollower()) {
			this.getNavigation().moveTo(((AbstractSchoolingFishMixin)this).getLeader(), 3.0F);
		}
	}

	@Override
	protected @Nullable SoundEvent getHurtSound(DamageSource source) {
		return AbysmSoundEvents.ENTITY_AROWANA_MAGICII_HURT;
	}

	@Override
	protected @Nullable SoundEvent getDeathSound() {
		return AbysmSoundEvents.ENTITY_AROWANA_MAGICII_DEATH;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return AbysmSoundEvents.ENTITY_AROWANA_MAGICII_FLOP;
	}

	// TODO: Bucket item
	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(AbysmItems.PADDLEFISH_BUCKET);
	}
}
