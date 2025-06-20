package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.entity.floralreef.BigFloralFishEntity;
import dev.spiritstudios.abysm.entity.floralreef.BloomrayEntity;
import dev.spiritstudios.abysm.entity.floralreef.ElectricOoglyBooglyEntity;
import dev.spiritstudios.abysm.entity.floralreef.ManOWar;
import dev.spiritstudios.abysm.entity.harpoon.HarpoonEntity;
import dev.spiritstudios.abysm.entity.floralreef.SmallFloralFishEntity;
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
			.dimensions(4f, 1.5f)
			.maxTrackingRange(4)
	);

	public static final EntityType<ElectricOoglyBooglyEntity> ELECTRIC_OOGLY_BOOGLY = register(
		"electric_oogly_boogly",
		EntityType.Builder
			.create(ElectricOoglyBooglyEntity::new, SpawnGroup.WATER_AMBIENT)
			.dimensions(1f, 1.5f)
			.eyeHeight(0.5f)
			.maxTrackingRange(4)
	);

	public static final EntityType<HarpoonEntity> FLYING_HARPOON = register(
		"harpoon",
		EntityType.Builder
			.<HarpoonEntity>create(HarpoonEntity::new, SpawnGroup.MISC)
			.dimensions(1.5f, 0.5f)
			.dimensions(0.25F, 0.25F)
			.maxTrackingRange(16)
			.trackingTickInterval(5)
	);

	public static final EntityType<ManOWar> MAN_O_WAR = register(
		"man_o_war",
		EntityType.Builder
			.create(ManOWar::new, SpawnGroup.WATER_AMBIENT)
			.dimensions(0.75f, 0.4f)
			.maxTrackingRange(4)
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
		FabricDefaultAttributeRegistry.register(BLOOMRAY, BloomrayEntity.createRayAttributes());
		FabricDefaultAttributeRegistry.register(ELECTRIC_OOGLY_BOOGLY, ElectricOoglyBooglyEntity.createFishAttributes());
		FabricDefaultAttributeRegistry.register(MAN_O_WAR, ManOWar.createManOWarAttributes());
	}

}
