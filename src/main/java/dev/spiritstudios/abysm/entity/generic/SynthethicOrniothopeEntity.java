package dev.spiritstudios.abysm.entity.generic;

import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.SimpleVanillaSchoolingFishEntity;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.item.AbysmItems;
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

public class SynthethicOrniothopeEntity extends SimpleVanillaSchoolingFishEntity {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.synthethic_orniothope.idle");

	public SynthethicOrniothopeEntity(EntityType<SynthethicOrniothopeEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(4, new SwimToRandomPlaceGoal(this, 1.0F));
		this.goalSelector.add(2, new RepopulateGoal(this, 1.25));
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(0, event -> event.setAndContinue(IDLE_ANIM)));
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.SYNTHETHIC_ORNIOTHOPE;
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
	public ItemStack getBucketItem() {
		return new ItemStack(AbysmItems.PADDLEFISH_BUCKET);
	}
}
