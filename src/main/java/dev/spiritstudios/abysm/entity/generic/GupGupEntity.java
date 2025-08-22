package dev.spiritstudios.abysm.entity.generic;

import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.SimpleFishEntity;
import dev.spiritstudios.abysm.entity.ai.goal.SwimAroundBoidGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class GupGupEntity extends SimpleFishEntity {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.gup_gup.idle");

	public GupGupEntity(EntityType<GupGupEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(2, new RepopulateGoal(this, 1.25));
		this.goalSelector.add(4, new SwimAroundBoidGoal(
			this,
			2.0F,
			100 * MathHelper.RADIANS_PER_DEGREE,
			70 * MathHelper.RADIANS_PER_DEGREE,
			0.1F, 0.4F, 0.4F, 0.005F, 2F,
			0.025F, 0.05F
		));
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.GUP_GUP;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(0, event -> event.setAndContinue(IDLE_ANIM)));
	}

	@Override
	public boolean shouldRender(double distance) {
		double d = this.getBoundingBox().getAverageSideLength();
		if (Double.isNaN(d)) {
			d = 1.0;
		}

		d *= 64.0 * 5;
		return distance < d * d;
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
