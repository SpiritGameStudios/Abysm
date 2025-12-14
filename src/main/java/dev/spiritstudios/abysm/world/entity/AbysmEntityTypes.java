package dev.spiritstudios.abysm.world.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.entity.depths.MysteriousBlobEntity;
import dev.spiritstudios.abysm.world.entity.floralreef.BigFloralFish;
import dev.spiritstudios.abysm.world.entity.floralreef.BloomrayEntity;
import dev.spiritstudios.abysm.world.entity.floralreef.ManOWarEntity;
import dev.spiritstudios.abysm.world.entity.floralreef.PaddlefishEntity;
import dev.spiritstudios.abysm.world.entity.floralreef.SmallFloralFish;
import dev.spiritstudios.abysm.world.entity.generic.ArowanaMagiciiEntity;
import dev.spiritstudios.abysm.world.entity.generic.GupGupEntity;
import dev.spiritstudios.abysm.world.entity.generic.SnapperEntity;
import dev.spiritstudios.abysm.world.entity.generic.SynthethicOrniothopeEntity;
import dev.spiritstudios.abysm.world.entity.harpoon.HarpoonEntity;
import dev.spiritstudios.abysm.world.entity.leviathan.pseudo.SkeletonSharkEntity;
import dev.spiritstudios.abysm.world.entity.ruins.LectorfinEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class AbysmEntityTypes {
	public static final EntityType<SmallFloralFish> SMALL_FLORAL_FISH = register(
		"floral_fish_small",
		EntityType.Builder
			.of(SmallFloralFish::new, MobCategory.WATER_AMBIENT)
			.sized(0.4f, 0.4f)
			.eyeHeight(0.35f)
			.clientTrackingRange(4)
	);

	public static final EntityType<BigFloralFish> BIG_FLORAL_FISH = register(
		"floral_fish_big",
		EntityType.Builder
			.of(BigFloralFish::new, MobCategory.WATER_AMBIENT)
			.sized(0.6f, 0.5f)
			.eyeHeight(0.375f)
			.clientTrackingRange(4)
	);

	public static final EntityType<PaddlefishEntity> PADDLEFISH = register(
		"paddlefish",
		EntityType.Builder
			.of(PaddlefishEntity::new, MobCategory.WATER_AMBIENT)
			.sized(0.4f, 0.5f)
			.eyeHeight(0.35f)
			.clientTrackingRange(4)
	);

	public static final EntityType<SnapperEntity> SNAPPER = register(
		"snapper",
		EntityType.Builder
			.of(SnapperEntity::new, MobCategory.WATER_AMBIENT)
			.sized(0.4f, 0.5f)
			.eyeHeight(0.35f)
			.clientTrackingRange(4)
	);

	public static final EntityType<GupGupEntity> GUP_GUP = register(
		"gup_gup",
		EntityType.Builder
			.of(GupGupEntity::new, MobCategory.WATER_AMBIENT)
			.sized(0.1f, 0.1f)
			.eyeHeight(0.05f)
			.clientTrackingRange(4)
	);

	public static final EntityType<ArowanaMagiciiEntity> AROWANA_MAGICII = register(
		"arowana_magicii",
		EntityType.Builder
			.of(ArowanaMagiciiEntity::new, MobCategory.WATER_AMBIENT)
			.sized(0.7F, 0.4F).eyeHeight(0.26F)
			.clientTrackingRange(4)
	);

	public static final EntityType<SynthethicOrniothopeEntity> SYNTHETHIC_ORNIOTHOPE = register(
		"synthethic_orniothope",
		EntityType.Builder
			.of(SynthethicOrniothopeEntity::new, MobCategory.WATER_AMBIENT)
			.sized(0.4f, 0.5f)
			.eyeHeight(0.35f)
			.clientTrackingRange(4)
	);

	public static final EntityType<BloomrayEntity> BLOOMRAY = register(
		"bloomray",
		EntityType.Builder
			.of(BloomrayEntity::new, MobCategory.WATER_CREATURE)
			.sized(1.5f, 0.5f)
			.clientTrackingRange(4)
	);

	public static final EntityType<ElectricOoglyBooglyEntity> ELECTRIC_OOGLY_BOOGLY = register(
		"electric_oogly_boogly",
		EntityType.Builder
			.of(ElectricOoglyBooglyEntity::new, MobCategory.WATER_AMBIENT)
			.sized(1f, 1.5f)
			.eyeHeight(0.5f)
			.clientTrackingRange(4)
	);

	public static final EntityType<HarpoonEntity> FLYING_HARPOON = register(
		"harpoon",
		EntityType.Builder
			.<HarpoonEntity>of(HarpoonEntity::new, MobCategory.MISC)
			.sized(1.5f, 0.5f)
			.sized(0.25F, 0.25F)
			.clientTrackingRange(16)
			.updateInterval(5)
	);

	public static final EntityType<ManOWarEntity> MAN_O_WAR = register(
		"man_o_war",
		EntityType.Builder
			.of(ManOWarEntity::new, MobCategory.WATER_AMBIENT)
			.sized(0.75f, 0.4f)
			.clientTrackingRange(4)
	);

	public static final EntityType<MysteriousBlobEntity> MYSTERIOUS_BLOB = register(
		"mysterious_blob",
		EntityType.Builder
			.of(MysteriousBlobEntity::new, MobCategory.WATER_AMBIENT)
			.sized(2.2F, 2F).eyeHeight(1.3F)
	);

	public static final EntityType<LectorfinEntity> LECTORFIN = register(
		"lectorfin",
		EntityType.Builder.of(LectorfinEntity::new, MobCategory.WATER_AMBIENT)
			.sized(0.7F, 0.4F).eyeHeight(0.26F)
			.clientTrackingRange(4)
	);

//	public static final EntityType<Lehydrathan> TEST_LEVIATHAN = register(
//		"lehydrathan",
//		EntityType.Builder.of(Lehydrathan::new, MobCategory.MISC)
//			.sized(1F, 1F)
//	);

	public static final EntityType<ReticulatedFliprayEntity> RETICULATED_FLIPRAY = register(
		"reticulated_flipray",
		EntityType.Builder.of(ReticulatedFliprayEntity::new, MobCategory.WATER_AMBIENT)
			.sized(1.3F, 0.2F)
	);

	public static final EntityType<SkeletonSharkEntity> SKELETON_SHARK = register(
		"skeleton_shark",
		EntityType.Builder.of(SkeletonSharkEntity::new, MobCategory.WATER_AMBIENT)
			.sized(0.85F, 0.7F).eyeHeight(0.5F)
	);

	private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> key, EntityType.Builder<T> type) {
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, type.build(key));
	}

	private static ResourceKey<EntityType<?>> keyOf(String id) {
		return ResourceKey.create(Registries.ENTITY_TYPE, Abysm.id(id));
	}

	private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
		return register(keyOf(id), type);
	}

	public static void init() {
		FabricDefaultAttributeRegistry.register(SMALL_FLORAL_FISH, SmallFloralFish.createAttributes());
		FabricDefaultAttributeRegistry.register(BIG_FLORAL_FISH, BigFloralFish.createAttributes());
		FabricDefaultAttributeRegistry.register(PADDLEFISH, LectorfinEntity.createPredatoryFishAttributes());
		FabricDefaultAttributeRegistry.register(SNAPPER, LectorfinEntity.createPredatoryFishAttributes());
		FabricDefaultAttributeRegistry.register(GUP_GUP, LectorfinEntity.createPredatoryFishAttributes());
		FabricDefaultAttributeRegistry.register(AROWANA_MAGICII, ArowanaMagiciiEntity.createPredatoryFishAttributes());
		FabricDefaultAttributeRegistry.register(SYNTHETHIC_ORNIOTHOPE, SynthethicOrniothopeEntity.createPredatoryFishAttributes());
		FabricDefaultAttributeRegistry.register(BLOOMRAY, BloomrayEntity.createRayAttributes());
		FabricDefaultAttributeRegistry.register(ELECTRIC_OOGLY_BOOGLY, ElectricOoglyBooglyEntity.createAttributes());
		FabricDefaultAttributeRegistry.register(MAN_O_WAR, ManOWarEntity.createManOWarAttributes());
		FabricDefaultAttributeRegistry.register(LECTORFIN, LectorfinEntity.createPredatoryFishAttributes());
		FabricDefaultAttributeRegistry.register(MYSTERIOUS_BLOB, MysteriousBlobEntity.createVaseAttributes());
		FabricDefaultAttributeRegistry.register(RETICULATED_FLIPRAY, ReticulatedFliprayEntity.createRayAttributes());
//		FabricDefaultAttributeRegistry.register(TEST_LEVIATHAN, Leviathan.createLeviathanAttributes());
		FabricDefaultAttributeRegistry.register(SKELETON_SHARK, SkeletonSharkEntity.createSansAttributes());
	}

}
