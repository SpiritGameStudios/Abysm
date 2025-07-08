package dev.spiritstudios.abysm.entity.ruins;

import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.ecosystem.entity.PlantEater;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.AbstractSchoolingFishEntity;
import dev.spiritstudios.abysm.entity.AbysmTrackedDataHandlers;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.FindPlantsGoal;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
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

@SuppressWarnings("unused")
public class LectorfinEntity extends AbstractSchoolingFishEntity implements PlantEater {
	// use nbt if sync is unnecessary
	protected static final TrackedData<Integer> ENCHANTMENT_LEVEL = DataTracker.registerData(LectorfinEntity.class, TrackedDataHandlerRegistry.INTEGER);
	protected static final TrackedData<RegistryEntry<FishEnchantment>> ENCHANTMENT = DataTracker.registerData(LectorfinEntity.class, AbysmTrackedDataHandlers.FISH_ENCHANTMENT);

	protected EcosystemLogic ecosystemLogic;

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

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

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
		this.goalSelector.add(4, this.plantsGoal);
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
	public boolean hasPlant() {
		return this.plantPos != null;
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
}
