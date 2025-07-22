package dev.spiritstudios.abysm.entity.ruins;

import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.ecosystem.entity.PlantEater;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.AbstractSchoolingFishEntity;
import dev.spiritstudios.abysm.entity.AbysmTrackedDataHandlers;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.FindPlantsGoal;
import dev.spiritstudios.abysm.entity.depths.MysteriousBlobEntity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;

import java.util.Objects;

@SuppressWarnings("unused")
public class LectorfinEntity extends AbstractSchoolingFishEntity implements PlantEater {
	// use nbt if sync is unnecessary
	protected static final TrackedData<Integer> ENCHANTMENT_LEVEL = DataTracker.registerData(LectorfinEntity.class, TrackedDataHandlerRegistry.INTEGER);
	protected static final TrackedData<RegistryEntry<FishEnchantment>> ENCHANTMENT = DataTracker.registerData(LectorfinEntity.class, AbysmTrackedDataHandlers.FISH_ENCHANTMENT);

	protected EcosystemLogic ecosystemLogic;
	protected @Nullable RegistryEntry<FishEnchantment> previousEnchantment = null;

	@Nullable
	protected BlockPos plantPos;
	protected FindPlantsGoal plantsGoal;
	protected int ticksUntilHunger = 0;

	public LectorfinEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
		this.ecosystemLogic = createEcosystemLogic(this);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(ENCHANTMENT_LEVEL, 0);
		builder.add(ENCHANTMENT, FishEnchantment.getDefaultEntry(this.getRegistryManager()));
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);

		RegistryOps<NbtElement> ops = this.getRegistryManager().getOps(NbtOps.INSTANCE);

		this.dataTracker.set(
			ENCHANTMENT,
			nbt.get("fishEnchantment", FishEnchantment.ENTRY_CODEC, ops)
				.orElse(FishEnchantment.getDefaultEntry(this.getRegistryManager()))
		);

		this.dataTracker.set(ENCHANTMENT_LEVEL, nbt.getInt("enchantmentLevel", 0));

		this.ticksUntilHunger = nbt.getInt("ticksUntilHunger", 0);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		RegistryOps<NbtElement> ops = getRegistryManager().getOps(NbtOps.INSTANCE);

		nbt.put("fishEnchantment", FishEnchantment.ENTRY_CODEC, ops, this.getEnchantment());
		nbt.putInt("enchantmentLevel", this.getEnchantmentLevel());
		nbt.putInt("ticksUntilHunger", this.ticksUntilHunger);
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
	public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
		AnimationController<LectorfinEntity> animController = new AnimationController<>(ANIM_CONTROLLER_STRING, 5, event -> PlayState.STOP);

		registrar.add(animController);
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
		if (!this.getWorld().isClient) {
			this.tickEnchantment();
		}
	}

	@Override
	public void onRemove(RemovalReason reason) {
		this.alertEcosystemOfDeath();
		super.onRemove(reason);
	}

	@Override
	public EcosystemLogic getEcosystemLogic() {
		return this.ecosystemLogic;
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.LECTORFIN;
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.plantsGoal = new FindPlantsGoal(this);
		this.plantsGoal.setRangeSupplier(() -> (int) Math.floor(this.getAttributeValue(EntityAttributes.FOLLOW_RANGE) * 0.8));
		this.goalSelector.add(4, this.plantsGoal);
		this.targetSelector.add(2, new RevengeGoal(this).setGroupRevenge());
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
		}
		this.previousEnchantment = enchantment;
	}
}
