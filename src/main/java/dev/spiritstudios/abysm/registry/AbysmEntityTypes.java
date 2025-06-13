package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.entity.BigFloralFishEntity;
import dev.spiritstudios.abysm.entity.BloomrayEntity;
import dev.spiritstudios.abysm.entity.HarpoonEntity;
import dev.spiritstudios.abysm.entity.SmallFloralFishEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class AbysmEntityTypes {
	public static final EntityType<SmallFloralFishEntity> SMALL_FLORAL_FISH = register(
		"floral_fish_small",
		EntityType.Builder
			.create(SmallFloralFishEntity::new, SpawnGroup.WATER_AMBIENT)
			.dimensions(0.8f, 0.6f)
			.eyeHeight(0.35f)
			.maxTrackingRange(4)
	);

	public static final EntityType<BigFloralFishEntity> BIG_FLORAL_FISH = register(
		"floral_fish_big",
		EntityType.Builder
			.create(BigFloralFishEntity::new, SpawnGroup.WATER_AMBIENT)
			.dimensions(1.2f, 0.8f)
			.eyeHeight(0.375f)
			.maxTrackingRange(4)
	);

	public static final EntityType<BloomrayEntity> BLOOMRAY = register(
		"bloomray",
		EntityType.Builder
			.create(BloomrayEntity::new, SpawnGroup.WATER_AMBIENT)
			.dimensions(5f, 1.5f)
			.maxTrackingRange(4)
	);

	public static final EntityType<HarpoonEntity> FLYING_HARPOON = register(
		"harpoon",
		EntityType.Builder
			.<HarpoonEntity>create(HarpoonEntity::new, SpawnGroup.MISC)
			.dimensions(1.5f, 0.5f)
			.dimensions(0.25F, 0.25F)
			.maxTrackingRange(4)
			.trackingTickInterval(5)
	);

	private static <T extends Entity> EntityType<T> register(RegistryKey<EntityType<?>> key, EntityType.Builder<T> type) {
		return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
	}

	private static RegistryKey<EntityType<?>> keyOf(String id) {
		return RegistryKey.of(RegistryKeys.ENTITY_TYPE, Abysm.id(id));
	}

	private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
		return register(keyOf(id), type);
	}

	public static void init() {
		FabricDefaultAttributeRegistry.register(SMALL_FLORAL_FISH, SmallFloralFishEntity.createFishAttributes());
		FabricDefaultAttributeRegistry.register(BIG_FLORAL_FISH, BigFloralFishEntity.createFishAttributes());
		FabricDefaultAttributeRegistry.register(BLOOMRAY, BloomrayEntity.createFishAttributes());
	}

}
