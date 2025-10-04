package dev.spiritstudios.abysm.entity.ruins;

import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.entity.PlantEater;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.AbysmTrackedDataHandlers;
import dev.spiritstudios.abysm.entity.SimpleEcoSchoolingFishEntity;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.FindPlantsGoal;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.FollowGroupLeaderGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class LectorfinEntity extends SimpleEcoSchoolingFishEntity implements PlantEater {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.lectorfin.idle");

	protected static final TrackedData<Integer> ENCHANTMENT_LEVEL = DataTracker.registerData(LectorfinEntity.class, TrackedDataHandlerRegistry.INTEGER);
	protected static final TrackedData<RegistryEntry<FishEnchantment>> ENCHANTMENT = DataTracker.registerData(LectorfinEntity.class, AbysmTrackedDataHandlers.FISH_ENCHANTMENT);

	protected @Nullable RegistryEntry<FishEnchantment> previousEnchantment = null;

	@Nullable
	protected BlockPos plantPos;
	protected FindPlantsGoal plantsGoal;
	protected int ticksUntilHunger = 0;

	public LectorfinEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(ENCHANTMENT_LEVEL, 0);
		builder.add(ENCHANTMENT, FishEnchantment.getDefaultEntry(this.getRegistryManager()));
	}


	@Override
	protected void readCustomData(ReadView view) {
		super.readCustomData(view);

		this.dataTracker.set(
			ENCHANTMENT,
			view.read("fishEnchantment", FishEnchantment.ENTRY_CODEC)
				.orElse(FishEnchantment.getDefaultEntry(this.getRegistryManager()))
		);

		this.dataTracker.set(ENCHANTMENT_LEVEL, view.getInt("enchantmentLevel", 0));

		this.ticksUntilHunger = view.getInt("ticksUntilHunger", 0);
	}


	@Override
	protected void writeCustomData(WriteView view) {
		super.writeCustomData(view);

		view.put("fishEnchantment", FishEnchantment.ENTRY_CODEC, this.getEnchantment());
		view.putInt("enchantmentLevel", this.getEnchantmentLevel());
		view.putInt("ticksUntilHunger", this.ticksUntilHunger);
	}

	public int getEnchantmentLevel() {
		return this.dataTracker.get(ENCHANTMENT_LEVEL);
	}

	public @Nullable RegistryEntry<FishEnchantment> getEnchantment() {
		return this.dataTracker.get(ENCHANTMENT);
	}

	public void setEnchantment(RegistryEntry<FishEnchantment> enchantment, int level) {
		if (!Objects.equals(enchantment, this.getEnchantment())) {
			this.dataTracker.set(ENCHANTMENT, enchantment);
		}
		if (level != this.getEnchantmentLevel()) {
			this.dataTracker.set(ENCHANTMENT_LEVEL, level);
		}
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(0, event -> event.setAndContinue(IDLE_ANIM)));
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.getWorld().isClient) {
			this.tickEnchantment();
			if (this.age % 200 == 0) {
				this.heal(0.34F * this.getMaxHealth());
			}
		}
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.LECTORFIN;
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(4, new SwimToRandomPlaceGoal(this, 1.0F));
		this.plantsGoal = new FindPlantsGoal(this);
		this.plantsGoal.setRangeSupplier(() -> (int) Math.floor(this.getAttributeValue(EntityAttributes.FOLLOW_RANGE) * 0.8));
		this.goalSelector.add(4, this.plantsGoal);
		this.targetSelector.add(2, new LectorfinRevengeGoal(this));
	}

	@Override
	public boolean damage(ServerWorld world, DamageSource source, float amount) {
		if (this.isInvulnerableTo(world, source)) {
			return false;
		}
		this.plantsGoal.cancel();
		return super.damage(world, source, amount);
	}

	@Override
	public void tickMovement() {
		super.tickMovement();
		if (!this.getWorld().isClient) {
			if (this.ticksUntilHunger > 0) {
				this.ticksUntilHunger--;
			}
		}
	}

	public boolean isAggressive() {
		return this.getEnchantment() != null && this.getEnchantmentLevel() > 0 && (this.getHealth() / this.getMaxHealth()) > 0.2;
	}

	@Override
	public void setPlantPos(@Nullable BlockPos plantPos) {
		this.plantPos = plantPos;
	}

	@Override
	public void clearPlantPos() {
		this.plantPos = null;
		this.ticksUntilHunger = MathHelper.nextInt(this.random, 20, 60);
	}

	@Override
	@Nullable
	public BlockPos getPlantPos() {
		return this.plantPos;
	}

	@Override
	public void setTicksUntilHunger(int ticks) {
		if (this.ticksUntilHunger > ticks) {
			return;
		}
		this.ticksUntilHunger = ticks;
	}

	@Override
	public int ticksUntilHunger() {
		return this.ticksUntilHunger;
	}

	@Override
	public void setNotHungryAnymoreYay() {
		PlantEater.super.setNotHungryAnymoreYay();
		this.heal(this.getMaxHealth() * 0.4F);
	}

	public void tickEnchantment() {
		final RegistryEntry<FishEnchantment> enchantment = this.getEnchantment();
		if (Objects.equals(this.previousEnchantment, enchantment)) {
			return;
		}
		final AttributeContainer attributes = this.getAttributes();
		if (this.previousEnchantment != null) {
			this.previousEnchantment.value().applyModifiers((attribute, modifier) -> {
				EntityAttributeInstance entityAttributeInstance = attributes.getCustomInstance(attribute);
				if (entityAttributeInstance != null) {
					entityAttributeInstance.removeModifier(modifier);
				}
			});
		}
		if (enchantment != null) {
			enchantment.value().applyModifiers((attribute, modifier) -> {
				EntityAttributeInstance entityAttributeInstance = attributes.getCustomInstance(attribute);
				if (entityAttributeInstance != null) {
					entityAttributeInstance.removeModifier(modifier.id());
					entityAttributeInstance.addTemporaryModifier(modifier);
				}
			});
			if (this.previousEnchantment == null) {
				this.clearGoalsAndTasks();
				this.initGoals();
			}
		}
		this.previousEnchantment = enchantment;
	}

	@Override
	protected @Nullable SoundEvent getHurtSound(DamageSource source) {
		return AbysmSoundEvents.ENTITY_LECTORFIN_HURT;
	}

	@Override
	protected @Nullable SoundEvent getDeathSound() {
		return AbysmSoundEvents.ENTITY_LECTORFIN_DEATH;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return AbysmSoundEvents.ENTITY_LECTORFIN_FLOP;
	}

	// TODO: Bucket
	@Override
	public ItemStack getBucketItem() {
		return new ItemStack(AbysmItems.PADDLEFISH_BUCKET);
	}

	public static boolean canSpawn(EntityType<Entity> entityEntityType, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return pos.getY() <= world.getSeaLevel() - 33 && world.getBlockState(pos).isOf(Blocks.WATER);
	}

	public static class LectorfinRevengeGoal extends RevengeGoal {

		public LectorfinRevengeGoal(LectorfinEntity mob, Class<?>... noRevengeTypes) {
			super(mob, noRevengeTypes);
		}

		@Override
		public boolean canStart() {
			return ((LectorfinEntity) this.mob).isAggressive() && super.canStart();
		}

		@Override
		public boolean shouldContinue() {
			return ((LectorfinEntity) this.mob).isAggressive() && super.shouldContinue();
		}
	}

	@SuppressWarnings("unused")
	public static class EscapeWhenNearDeathGoal extends EscapeDangerGoal {

		public EscapeWhenNearDeathGoal(LectorfinEntity mob, double speed) {
			super(mob, speed);
		}

		public EscapeWhenNearDeathGoal(LectorfinEntity mob, double speed, TagKey<DamageType> dangerousDamageTypes) {
			super(mob, speed, dangerousDamageTypes);
		}

		public EscapeWhenNearDeathGoal(LectorfinEntity mob, double speed, Function<PathAwareEntity, TagKey<DamageType>> entityToDangerousDamageTypes) {
			super(mob, speed, entityToDangerousDamageTypes);
		}

		@Override
		public boolean canStart() {
			return !((LectorfinEntity) this.mob).isAggressive() && super.canStart();
		}

		@Override
		public boolean shouldContinue() {
			return !((LectorfinEntity) this.mob).isAggressive() && super.shouldContinue();
		}
	}

	@SuppressWarnings("unused")
	public static class FleeWhenWeakGoal<T extends LivingEntity> extends FleeEntityGoal<T> {

		public FleeWhenWeakGoal(LectorfinEntity mob, Class<T> fleeFromType, float distance, double slowSpeed, double fastSpeed) {
			super(mob, fleeFromType, distance, slowSpeed, fastSpeed);
		}

		public FleeWhenWeakGoal(LectorfinEntity mob, Class<T> fleeFromType, Predicate<LivingEntity> extraInclusionSelector, float distance, double slowSpeed, double fastSpeed, Predicate<LivingEntity> inclusionSelector) {
			super(mob, fleeFromType, extraInclusionSelector, distance, slowSpeed, fastSpeed, inclusionSelector);
		}

		public FleeWhenWeakGoal(LectorfinEntity fleeingEntity, Class<T> classToFleeFrom, float fleeDistance, double fleeSlowSpeed, double fleeFastSpeed, Predicate<LivingEntity> inclusionSelector) {
			super(fleeingEntity, classToFleeFrom, fleeDistance, fleeSlowSpeed, fleeFastSpeed, inclusionSelector);
		}

		@Override
		public boolean canStart() {
			return !((LectorfinEntity) this.mob).isAggressive() && super.canStart();
		}

		@Override
		public boolean shouldContinue() {
			return !((LectorfinEntity) this.mob).isAggressive() && super.shouldContinue();
		}
	}

	public static class FollowGroupWhenWeakGoal extends FollowGroupLeaderGoal {

		protected final LectorfinEntity lectorfin;

		public FollowGroupWhenWeakGoal(LectorfinEntity fish) {
			super(fish);
			this.lectorfin = fish;
		}

		@Override
		public boolean canStart() {
			return !this.lectorfin.isAggressive() && super.canStart();
		}

		@Override
		public boolean shouldContinue() {
			return !this.lectorfin.isAggressive() && super.shouldContinue();
		}
	}
}
