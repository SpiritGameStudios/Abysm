package dev.spiritstudios.abysm.entity.floralreef;

import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.ElectricOoglyBooglyEntity;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class SmallFloralFishEntity extends AbstractFloralFishEntity implements EcologicalEntity {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.floral_fish_small.idle");
	protected EcosystemLogic ecosystemLogic;

	public SmallFloralFishEntity(EntityType<? extends AbstractFloralFishEntity> entityType, Level world) {
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
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.SMALL_FLORAL_FISH;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		AnimationController<ElectricOoglyBooglyEntity> animController = new AnimationController<>(5, event -> event.setAndContinue(IDLE_ANIM));

		controllers.add(animController);
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
