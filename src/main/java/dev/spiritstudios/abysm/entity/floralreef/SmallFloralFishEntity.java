package dev.spiritstudios.abysm.entity.floralreef;

import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.ElectricOoglyBooglyEntity;
import dev.spiritstudios.abysm.entity.ai.goal.SwimAroundBoidGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.FleePredatorsGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.HuntPreyGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.entity.pattern.AbysmEntityPatternVariants;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class SmallFloralFishEntity extends AbstractFloralFishEntity implements EcologicalEntity {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.floral_fish_small.idle");
	protected EcosystemLogic ecosystemLogic;

	public SmallFloralFishEntity(EntityType<? extends AbstractFloralFishEntity> entityType, World world) {
		super(entityType, world);
		this.ecosystemLogic = createEcosystemLogic(this);
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		this.alertEcosystemOfSpawn();
		return super.initialize(world, difficulty, spawnReason, entityData);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickEcosystemLogic();
	}

	@Override
	public void onRemove(RemovalReason reason) {
		this.alertEcosystemOfDeath();
		super.onRemove(reason);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new EscapeDangerGoal(this, 1.25));
		this.goalSelector.add(1, new FleePredatorsGoal(this, 10.0F, 1.1, 1.2));
		this.goalSelector.add(2, new FleeEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.6, 1.4, EntityPredicates.EXCEPT_SPECTATOR::test));
		this.goalSelector.add(2, new RepopulateGoal(this, 1.25));
		this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0, false));
		this.goalSelector.add(4, new SwimAroundBoidGoal(
			this,
			1.5F,
			100 * MathHelper.RADIANS_PER_DEGREE,
			70 * MathHelper.RADIANS_PER_DEGREE,
			0.4F, 0.4F, 0.4F, 0.005F, 0.75F,
			0.05F, 0.15F
		));

		this.targetSelector.add(1, new HuntPreyGoal(this, false));
	}

	@Override
	public EntityPattern getDefaultPattern(RegistryEntryLookup<EntityPatternVariant> lookup) {
		return new EntityPattern(
			lookup.getOrThrow(AbysmEntityPatternVariants.FLORAL_FISH_SMALL_COLORFUL),
			DyeColor.PINK.getEntityColor(), DyeColor.LIGHT_BLUE.getEntityColor()
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
	public ItemStack getBucketItem() {
		return new ItemStack(AbysmItems.SMALL_FLORAL_FISH_BUCKET);
	}
}
