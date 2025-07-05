package dev.spiritstudios.abysm.entity.ruins;

import com.mojang.serialization.Codec;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;

import java.util.Objects;

@SuppressWarnings("unused")
public abstract class EnchantedFishEntity extends AbstractSchoolingFishEntity implements EcologicalEntity {

	// use nbt if sync is unnecessary
	protected static final TrackedData<Integer> ENCHANTMENT_LEVEL = DataTracker.registerData(EnchantedFishEntity.class, TrackedDataHandlerRegistry.INTEGER);
	protected static final TrackedData<FishEnchantment> ENCHANTMENT = DataTracker.registerData(EnchantedFishEntity.class, AbysmTrackedDataHandlers.FISH_ENCHANTMENT);

	public EnchantedFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(ENCHANTMENT_LEVEL, 0);
		builder.add(ENCHANTMENT, FishEnchantment.DEFAULT);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("fishEnchantment")) {
			FishEnchantment enchantment = nbt.get("fishEnchantment", this.getEnchantmentCodec()).orElse(FishEnchantment.DEFAULT);
			if (!Objects.equals(enchantment, this.getEnchantment())) {
				this.dataTracker.set(ENCHANTMENT, enchantment);
			}
		}
		if (nbt.contains("enchantmentLevel")) {
			int level = nbt.getInt("enchantmentLevel", 0);
			if (level != this.getEnchantmentLevel()) {
				this.dataTracker.set(ENCHANTMENT_LEVEL, level);
			}
		}
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.put("fishEnchantment", this.getEnchantmentCodec(), this.getEnchantment());
		nbt.putInt("enchantmentLevel", this.getEnchantmentLevel());
	}

	public Codec<FishEnchantment> getEnchantmentCodec() {
		return FishEnchantment.getRegistry(this.getWorld()).getCodec();
	}

	public int getEnchantmentLevel() {
		return this.dataTracker.get(ENCHANTMENT_LEVEL);
	}

	@Nullable
	public FishEnchantment getEnchantment() {
		return this.dataTracker.get(ENCHANTMENT);
	}

	public void setEnchantment(@NotNull FishEnchantment enchantment, int level) {
		this.dataTracker.set(ENCHANTMENT_LEVEL, level);
		this.dataTracker.set(ENCHANTMENT, enchantment);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

	}
}
