package dev.spiritstudios.abysm.entity.floralreef;

import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.ElectricOoglyBooglyEntity;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.FleePredatorsGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.HuntPreyGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.entity.pattern.AbysmEntityPatternVariants;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.minecraft.core.HolderGetter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class BigFloralFishEntity extends AbstractFloralFishEntity implements EcologicalEntity {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.floral_fish_big.idle");

	public EcosystemLogic ecosystemLogic;

	public BigFloralFishEntity(EntityType<? extends AbstractFloralFishEntity> entityType, Level world) {
		super(entityType, world);
		this.ecosystemLogic = this.createEcosystemLogic(this);
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
	protected void registerGoals() {
		super.registerGoals();

		this.goalSelector.addGoal(1, new FleePredatorsGoal(this, 10.0F, 1.1, 1.2));
		this.goalSelector.addGoal(2, new RepopulateGoal(this, 1.25));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, false));
		this.targetSelector.addGoal(1, new HuntPreyGoal(this, false));
	}

	@Override
	public EcosystemLogic getEcosystemLogic() {
		return this.ecosystemLogic;
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.BIG_FLORAL_FISH;
	}

	@Override
	public EntityPattern getDefaultPattern(HolderGetter<EntityPatternVariant> lookup) {
		return new EntityPattern(
			lookup.getOrThrow(AbysmEntityPatternVariants.FLORAL_FISH_BIG_TERRA),
			DyeColor.LIGHT_BLUE.getTextureDiffuseColor(), DyeColor.PINK.getTextureDiffuseColor()
		);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		AnimationController<ElectricOoglyBooglyEntity> animController = new AnimationController<>(5, event -> event.setAndContinue(IDLE_ANIM));

		controllers.add(animController);
	}

	@Override
	protected @Nullable SoundEvent getHurtSound(DamageSource source) {
		return AbysmSoundEvents.ENTITY_BIG_FLORAL_FISH_HURT;
	}

	@Override
	protected @Nullable SoundEvent getDeathSound() {
		return AbysmSoundEvents.ENTITY_BIG_FLORAL_FISH_DEATH;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return AbysmSoundEvents.ENTITY_BIG_FLORAL_FISH_FLOP;
	}

	@Override
	public ItemStack getBucketItemStack() {
		return new ItemStack(AbysmItems.BIG_FLORAL_FISH_BUCKET);
	}
}
