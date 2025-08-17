package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.entity.depths.MysteriousBlobEntity;
import dev.spiritstudios.abysm.entity.floralreef.*;
import dev.spiritstudios.abysm.entity.harpoon.HarpoonEntity;
import dev.spiritstudios.abysm.entity.leviathan.Leviathan;
import dev.spiritstudios.abysm.entity.leviathan.test.Lehydrathan;
import dev.spiritstudios.abysm.entity.ruins.LectorfinEntity;
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
			.dimensions(0.4f, 0.4f)
			.eyeHeight(0.35f)
			.maxTrackingRange(4)
	);

	public static final EntityType<BigFloralFishEntity> BIG_FLORAL_FISH = register(
		"floral_fish_big",
		EntityType.Builder
			.create(BigFloralFishEntity::new, SpawnGroup.WATER_AMBIENT)
			.dimensions(0.6f, 0.5f)
			.eyeHeight(0.375f)
			.maxTrackingRange(4)
	);

	public static final EntityType<PaddlefishEntity> PADDLEFISH = register(
		"paddlefish",
		EntityType.Builder
			.create(PaddlefishEntity::new, SpawnGroup.WATER_AMBIENT)
			.dimensions(0.4f, 0.5f)
			.eyeHeight(0.35f)
			.maxTrackingRange(4)
	);


	public static final EntityType<BloomrayEntity> BLOOMRAY = register(
		"bloomray",
		EntityType.Builder
			.create(BloomrayEntity::new, SpawnGroup.WATER_CREATURE)
			.dimensions(1.5f, 0.5f)
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

	public static final EntityType<ManOWarEntity> MAN_O_WAR = register(
		"man_o_war",
		EntityType.Builder
			.create(ManOWarEntity::new, SpawnGroup.WATER_AMBIENT)
			.dimensions(0.75f, 0.4f)
			.maxTrackingRange(4)
	);

	public static final EntityType<MysteriousBlobEntity> MYSTERIOUS_BLOB = register(
		"mysterious_blob",
		EntityType.Builder
			.create(MysteriousBlobEntity::new, SpawnGroup.WATER_AMBIENT)
			.dimensions(2.2F, 2F).eyeHeight(1.3F)
	);

	public static final EntityType<LectorfinEntity> LECTORFIN = register(
		"lectorfin",
		EntityType.Builder.create(LectorfinEntity::new, SpawnGroup.WATER_AMBIENT)
			.dimensions(0.52F, 0.3F).eyeHeight(0.195F)
			.maxTrackingRange(4)
	);

	public static final EntityType<Lehydrathan> TEST_LEVIATHAN = register(
		"lehydrathan",
		EntityType.Builder.create(Lehydrathan::new, SpawnGroup.MISC)
			.dimensions(1F, 1F)
	);

	public static final EntityType<ReticulatedFliprayEntity> RETICULATED_FLIPRAY = register(
		"reticulated_flipray",
		EntityType.Builder.create(ReticulatedFliprayEntity::new, SpawnGroup.WATER_AMBIENT)
			.dimensions(1.7F, 0.2F) // I have no idea how big this should be
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
		FabricDefaultAttributeRegistry.register(PADDLEFISH, PaddlefishEntity.createFishAttributes());
		FabricDefaultAttributeRegistry.register(BLOOMRAY, BloomrayEntity.createRayAttributes());
		FabricDefaultAttributeRegistry.register(ELECTRIC_OOGLY_BOOGLY, ElectricOoglyBooglyEntity.createFishAttributes());
		FabricDefaultAttributeRegistry.register(MAN_O_WAR, ManOWarEntity.createManOWarAttributes());
		FabricDefaultAttributeRegistry.register(LECTORFIN, LectorfinEntity.createPredatoryFishAttributes());
		FabricDefaultAttributeRegistry.register(MYSTERIOUS_BLOB, MysteriousBlobEntity.createVaseAttributes());
		FabricDefaultAttributeRegistry.register(TEST_LEVIATHAN, Leviathan.createLeviathanAttributes());
		FabricDefaultAttributeRegistry.register(RETICULATED_FLIPRAY, ReticulatedFliprayEntity.createRayAttributes());
	}

}
