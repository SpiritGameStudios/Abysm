package dev.spiritstudios.abysm.entity.generic;

import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.SimpleEcoSchoolingFishEntity;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class SynthethicOrniothopeEntity extends SimpleEcoSchoolingFishEntity {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.synthethic_orniothope.idle");

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
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(0, event -> event.setAndContinue(IDLE_ANIM)));
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.SYNTHETHIC_ORNIOTHOPE;
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
