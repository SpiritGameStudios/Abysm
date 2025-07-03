package dev.spiritstudios.abysm.entity.ruins;

import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.entity.AbstractSchoolingFishEntity;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Base entity class for all Enchanted Fish
 */
@SuppressWarnings("unused")
public abstract class EnchantedFishEntity extends AbstractSchoolingFishEntity implements EcologicalEntity {

	// use nbt if sync is unnecessary
	public static final TrackedData<Integer> ENCHANTMENT_LEVEL = DataTracker.registerData(EnchantedFishEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Integer> ENCHANTMENT_ID = DataTracker.registerData(EnchantedFishEntity.class, TrackedDataHandlerRegistry.INTEGER);

	public EnchantedFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(ENCHANTMENT_LEVEL, 0);
		builder.add(ENCHANTMENT_ID, 0);
	}

	public int getEnchantmentLevel() {
		return this.dataTracker.get(ENCHANTMENT_LEVEL);
	}

	@Nullable
	public FishEnchantment getEnchantment() {
		return this.getWorld().getRegistryManager()
			.getOptional(AbysmRegistries.FISH_ENCHANTMENT)
			.map(registry ->
				registry.get(this.dataTracker.get(ENCHANTMENT_ID)))
			.orElse(null);
	}
}
