package dev.spiritstudios.abysm.entity.generic;

import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.SimpleEcoSchoolingFishEntity;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.mixin.ecosystem.goal.SchoolingFishEntityAccessor;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class ArowanaMagiciiEntity extends SimpleEcoSchoolingFishEntity {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.arowana_magicii.idle");

	public ArowanaMagiciiEntity(EntityType<ArowanaMagiciiEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(4, new SwimToRandomPlaceGoal(this, 3.0F));
		this.goalSelector.add(2, new RepopulateGoal(this, 1.25));
	}

	public void moveTowardLeader() {
		if (this.hasLeader()) {
			this.getNavigation().startMovingTo(((SchoolingFishEntityAccessor)this).abysm$getLeader(), 3.0F);
		}
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.AROWANA_MAGICII;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(0, event -> event.setAndContinue(IDLE_ANIM)));
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
	public ItemStack getBucketItem() {
		return new ItemStack(AbysmItems.PADDLEFISH_BUCKET);
	}
}
