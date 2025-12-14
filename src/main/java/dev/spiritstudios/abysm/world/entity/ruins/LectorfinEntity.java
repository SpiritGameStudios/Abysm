package dev.spiritstudios.abysm.world.entity.ruins;

import dev.spiritstudios.abysm.core.registries.AbysmSoundEvents;
import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.world.ecosystem.entity.PlantEater;
import dev.spiritstudios.abysm.world.entity.AbysmTrackedDataHandlers;
import dev.spiritstudios.abysm.world.entity.SimpleEcoSchoolingFishEntity;
import dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.FindPlantsGoal;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class LectorfinEntity extends SimpleEcoSchoolingFishEntity implements PlantEater {
	protected static final EntityDataAccessor<Integer> ENCHANTMENT_LEVEL = SynchedEntityData.defineId(LectorfinEntity.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<Holder<FishEnchantment>> ENCHANTMENT = SynchedEntityData.defineId(LectorfinEntity.class, AbysmTrackedDataHandlers.FISH_ENCHANTMENT);

	protected @Nullable Holder<FishEnchantment> previousEnchantment = null;

	@Nullable
	protected BlockPos plantPos;
	protected FindPlantsGoal plantsGoal;
	protected int ticksUntilHunger = 0;

	public LectorfinEntity(EntityType<? extends AbstractSchoolingFish> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(ENCHANTMENT_LEVEL, 0);
		builder.define(ENCHANTMENT, FishEnchantment.getDefaultEntry(this.registryAccess()));
	}


	@Override
	protected void readAdditionalSaveData(ValueInput view) {
		super.readAdditionalSaveData(view);

		this.entityData.set(
			ENCHANTMENT,
			view.read("fishEnchantment", FishEnchantment.ENTRY_CODEC)
				.orElse(FishEnchantment.getDefaultEntry(this.registryAccess()))
		);

		this.entityData.set(ENCHANTMENT_LEVEL, view.getIntOr("enchantmentLevel", 0));

		this.ticksUntilHunger = view.getIntOr("ticksUntilHunger", 0);
	}


	@Override
	protected void addAdditionalSaveData(ValueOutput view) {
		super.addAdditionalSaveData(view);

		view.store("fishEnchantment", FishEnchantment.ENTRY_CODEC, this.getEnchantment());
		view.putInt("enchantmentLevel", this.getEnchantmentLevel());
		view.putInt("ticksUntilHunger", this.ticksUntilHunger);
	}

	public int getEnchantmentLevel() {
		return this.entityData.get(ENCHANTMENT_LEVEL);
	}

	public @Nullable Holder<FishEnchantment> getEnchantment() {
		return this.entityData.get(ENCHANTMENT);
	}

	public void setEnchantment(Holder<FishEnchantment> enchantment, int level) {
		if (!Objects.equals(enchantment, this.getEnchantment())) {
			this.entityData.set(ENCHANTMENT, enchantment);
		}
		if (level != this.getEnchantmentLevel()) {
			this.entityData.set(ENCHANTMENT_LEVEL, level);
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level().isClientSide()) {
			this.tickEnchantment();
			if (this.tickCount % 200 == 0) {
				this.heal(0.34F * this.getMaxHealth());
			}
		}
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(4, new SwimToRandomPlaceGoal(this, 1.0F));
		this.plantsGoal = new FindPlantsGoal(this);
		this.plantsGoal.setRangeSupplier(() -> (int) Math.floor(this.getAttributeValue(Attributes.FOLLOW_RANGE) * 0.8));
		this.goalSelector.addGoal(4, this.plantsGoal);
		this.targetSelector.addGoal(2, new LectorfinRevengeGoal(this));
	}

	@Override
	public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
		if (this.isInvulnerableTo(world, source)) {
			return false;
		}
		this.plantsGoal.cancel();
		return super.hurtServer(world, source, amount);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (!this.level().isClientSide()) {
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
		this.ticksUntilHunger = Mth.nextInt(this.random, 20, 60);
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
		final Holder<FishEnchantment> enchantment = this.getEnchantment();
		if (Objects.equals(this.previousEnchantment, enchantment)) {
			return;
		}
		final AttributeMap attributes = this.getAttributes();
		if (this.previousEnchantment != null) {
			this.previousEnchantment.value().applyModifiers((attribute, modifier) -> {
				AttributeInstance entityAttributeInstance = attributes.getInstance(attribute);
				if (entityAttributeInstance != null) {
					entityAttributeInstance.removeModifier(modifier);
				}
			});
		}
		if (enchantment != null) {
			enchantment.value().applyModifiers((attribute, modifier) -> {
				AttributeInstance entityAttributeInstance = attributes.getInstance(attribute);
				if (entityAttributeInstance != null) {
					entityAttributeInstance.removeModifier(modifier.id());
					entityAttributeInstance.addTransientModifier(modifier);
				}
			});
			if (this.previousEnchantment == null) {
				this.removeFreeWill();
				this.registerGoals();
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
	public ItemStack getBucketItemStack() {
		return new ItemStack(AbysmItems.PADDLEFISH_BUCKET);
	}

	public static boolean canSpawn(EntityType<Entity> entityEntityType, ServerLevelAccessor world, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
		return pos.getY() <= world.getSeaLevel() - 33 && world.getBlockState(pos).is(Blocks.WATER);
	}

	public static class LectorfinRevengeGoal extends HurtByTargetGoal {

		public LectorfinRevengeGoal(LectorfinEntity mob, Class<?>... noRevengeTypes) {
			super(mob, noRevengeTypes);
		}

		@Override
		public boolean canUse() {
			return this.mob.isAggressive() && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return this.mob.isAggressive() && super.canContinueToUse();
		}
	}

	@SuppressWarnings("unused")
	public static class EscapeWhenNearDeathGoal extends PanicGoal {

		public EscapeWhenNearDeathGoal(LectorfinEntity mob, double speed) {
			super(mob, speed);
		}

		public EscapeWhenNearDeathGoal(LectorfinEntity mob, double speed, TagKey<DamageType> dangerousDamageTypes) {
			super(mob, speed, dangerousDamageTypes);
		}

		public EscapeWhenNearDeathGoal(LectorfinEntity mob, double speed, Function<PathfinderMob, TagKey<DamageType>> entityToDangerousDamageTypes) {
			super(mob, speed, entityToDangerousDamageTypes);
		}

		@Override
		public boolean canUse() {
			return !((LectorfinEntity) this.mob).isAggressive() && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return !((LectorfinEntity) this.mob).isAggressive() && super.canContinueToUse();
		}
	}

	@SuppressWarnings("unused")
	public static class FleeWhenWeakGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

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
		public boolean canUse() {
			return !((LectorfinEntity) this.mob).isAggressive() && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return !((LectorfinEntity) this.mob).isAggressive() && super.canContinueToUse();
		}
	}

	public static class FollowGroupWhenWeakGoal extends FollowFlockLeaderGoal {

		protected final LectorfinEntity lectorfin;

		public FollowGroupWhenWeakGoal(LectorfinEntity fish) {
			super(fish);
			this.lectorfin = fish;
		}

		@Override
		public boolean canUse() {
			return !this.lectorfin.isAggressive() && super.canUse();
		}

		@Override
		public boolean canContinueToUse() {
			return !this.lectorfin.isAggressive() && super.canContinueToUse();
		}
	}
}
