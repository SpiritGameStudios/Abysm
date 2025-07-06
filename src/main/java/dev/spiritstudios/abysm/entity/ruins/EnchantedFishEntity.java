package dev.spiritstudios.abysm.entity.ruins;

import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.entity.AbstractSchoolingFishEntity;
import dev.spiritstudios.abysm.entity.AbysmTrackedDataHandlers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;

@SuppressWarnings("unused")
public abstract class EnchantedFishEntity extends AbstractSchoolingFishEntity implements EcologicalEntity {
	// use nbt if sync is unnecessary
	protected static final TrackedData<Integer> ENCHANTMENT_LEVEL = DataTracker.registerData(EnchantedFishEntity.class, TrackedDataHandlerRegistry.INTEGER);
	protected static final TrackedData<RegistryEntry<FishEnchantment>> ENCHANTMENT = DataTracker.registerData(EnchantedFishEntity.class, AbysmTrackedDataHandlers.FISH_ENCHANTMENT);

	public EnchantedFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
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
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		RegistryOps<NbtElement> ops = getRegistryManager().getOps(NbtOps.INSTANCE);

		nbt.put("fishEnchantment", FishEnchantment.ENTRY_CODEC, ops, this.getEnchantment());
		nbt.putInt("enchantmentLevel", this.getEnchantmentLevel());
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
}
