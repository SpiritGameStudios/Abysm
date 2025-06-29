package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.floralreef.BigFloralFishEntity;
import dev.spiritstudios.abysm.entity.floralreef.SmallFloralFishEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class AbysmEcosystemTypes {
	public static final EcosystemType<SmallFloralFishEntity> SMALL_FLORAL_FISH = register(
		"small_floral_fish",
		EcosystemType.Builder
			.create(AbysmEntityTypes.SMALL_FLORAL_FISH)
			.setPredators(AbysmEntityTypes.BIG_FLORAL_FISH, AbysmEntityTypes.BLOOMRAY)
			.setPrey(AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY)
			.setPlants(AbysmBlocks.PURPLE_SCABIOSA, AbysmBlocks.PINK_SCABIOSA, AbysmBlocks.RED_SCABIOSA)
	);

	public static final EcosystemType<BigFloralFishEntity> BIG_FLORAL_FISH = register(
		"big_floral_fish",
		EcosystemType.Builder
			.create(AbysmEntityTypes.BIG_FLORAL_FISH)
			.setPrey(AbysmEntityTypes.SMALL_FLORAL_FISH)
			.setTargetPopulation(3)
	);

//	public static <T extends MobEntity & EcologicalEntity> EcosystemType<T> register(EcosystemType.Builder<T> builder) {
//		return register(, builder);
//	}

	public static <T extends MobEntity & EcologicalEntity> EcosystemType<T> register(String id, EcosystemType.Builder<T> builder) {
		return register(keyOf(id), builder);
	}

	public static <T extends MobEntity & EcologicalEntity> EcosystemType<T> register(RegistryKey<EcosystemType<?>> key, EcosystemType.Builder<T> builder) {
		return Registry.register(AbysmRegistries.ECOSYSTEM_TYPE_REGISTRY, key, builder.build());
	}

	private static RegistryKey<EcosystemType<?>> keyOf(String id) {
		return RegistryKey.of(AbysmRegistries.ECOSYSTEM_TYPE_KEY, Abysm.id(id));
	}

	public static void init() {
		// NO-OP
	}
}
