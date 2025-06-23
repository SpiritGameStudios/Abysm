package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.floralreef.SmallFloralFishEntity;

public class EcosystemTypes {

	// FIXME - either data-drive this (slightly annoying) or figure out how to create a non-data-driven registry

	public static final EcosystemType<SmallFloralFishEntity> SMALL_FLORAL_FISH = EcosystemType.Builder
		.create(AbysmEntityTypes.SMALL_FLORAL_FISH)
		.setPredators(AbysmEntityTypes.BIG_FLORAL_FISH, AbysmEntityTypes.BLOOMRAY)
		.setPrey(AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY)
		.setPlants(AbysmBlocks.PURPLE_SCABIOSA, AbysmBlocks.PINK_SCABIOSA, AbysmBlocks.RED_SCABIOSA)
		.build();

//	public static <T extends MobEntity & EcologicalEntity> EcosystemType<T> register(RegistryKey<EcosystemType<?>> key, EcosystemType.Builder<?> builder) {
//
//	}
//
//	private static RegistryKey<EcosystemType<?>> keyOf(String id) {
//		return RegistryKey.of(AbysmRegistries.ECOSYSTEM_TYPE_KEY, Abysm.id(id));
//	}
//
//	public static <T extends MobEntity & EcologicalEntity> EcosystemType<T> register(EcosystemType.Builder<T> builder) {
//		return register(builder.getEntityType().getUntranslatedName(), builder);
//	}
//
//	public static <T extends MobEntity & EcologicalEntity> EcosystemType<T> register(String id, EcosystemType.Builder<T> builder) {
//		return register(keyOf(id), builder);
//	}

}
