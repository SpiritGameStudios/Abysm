package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.FleePredatorsGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.HuntPreyGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.entity.floralreef.BloomrayEntity;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class ReticulatedFliprayEntity extends SimpleFishEntity implements EcologicalEntity {

	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.flipray.swim");

	protected EcosystemLogic ecosystemLogic;

	public ReticulatedFliprayEntity(EntityType<? extends SimpleFishEntity> entityType, World world) {
		super(entityType, world);
		this.ecosystemLogic = createEcosystemLogic(this);
		this.moveControl = new AquaticMoveControl(this, 85, 10, 0.02F, 0.1F, true);
		this.lookControl = new YawAdjustingLookControl(this, 20);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
		AnimationController<ReticulatedFliprayEntity> animController = new AnimationController<>("default", 0, event -> event.setAndContinue(IDLE_ANIM));

		registrar.add(animController);
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		this.alertEcosystemOfSpawn();
		return super.initialize(world, difficulty, spawnReason, entityData);
	}

	@Override
	public EcosystemLogic getEcosystemLogic() {
		return this.ecosystemLogic;
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.RETICULATED_FLIPRAY;
	}

	@Override
	public void onRemove(RemovalReason reason) {
		this.alertEcosystemOfDeath();
		super.onRemove(reason);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickEcosystemLogic();
	}

	public static DefaultAttributeContainer.Builder createRayAttributes() {
		return BloomrayEntity.createRayAttributes()
			.add(EntityAttributes.MAX_HEALTH, 40);
	}

	@Override
	public int getMaxLookPitchChange() {
		return 1;
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new FleePredatorsGoal(this, 10.0F, 1.1, 1.2));
		this.goalSelector.add(2, new RepopulateGoal(this, 1.25));
		this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0, false));
		this.goalSelector.add(4, new SwimAroundGoal(this, 1.0, 10));
		this.goalSelector.add(4, new LookAroundGoal(this));
		this.targetSelector.add(1, new HuntPreyGoal(this, false));
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
	public ItemStack getBucketItem() {
		return new ItemStack(AbysmItems.PADDLEFISH_BUCKET);
	}
}
