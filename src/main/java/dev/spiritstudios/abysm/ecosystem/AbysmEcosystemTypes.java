package dev.spiritstudios.abysm.ecosystem;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.entity.ReticulatedFliprayEntity;
import dev.spiritstudios.abysm.entity.floralreef.ElectricOoglyBooglyEntity;
import dev.spiritstudios.abysm.entity.generic.ArowanaMagiciiEntity;
import dev.spiritstudios.abysm.entity.generic.GupGupEntity;
import dev.spiritstudios.abysm.entity.generic.SnapperEntity;
import dev.spiritstudios.abysm.entity.floralreef.BigFloralFishEntity;
import dev.spiritstudios.abysm.entity.floralreef.BloomrayEntity;
import dev.spiritstudios.abysm.entity.floralreef.PaddlefishEntity;
import dev.spiritstudios.abysm.entity.floralreef.SmallFloralFishEntity;
import dev.spiritstudios.abysm.entity.ruins.LectorfinEntity;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class AbysmEcosystemTypes {

	public static final EcosystemType<SmallFloralFishEntity> SMALL_FLORAL_FISH = register(
		"small_floral_fish",
		EcosystemType.Builder
			.create(AbysmEntityTypes.SMALL_FLORAL_FISH)
			.setPredators(AbysmEntityTypes.BLOOMRAY)
			.setPlants(AbysmBlocks.PURPLE_SCABIOSA, AbysmBlocks.PINK_SCABIOSA, AbysmBlocks.RED_SCABIOSA)
			.setTargetPopulation(7)
			.setUnfavoredHuntSpeed(-0.35f)
			.setLitterSize(1, 4)
	);

	public static final EcosystemType<BigFloralFishEntity> BIG_FLORAL_FISH = register(
		"big_floral_fish",
		EcosystemType.Builder
			.create(AbysmEntityTypes.BIG_FLORAL_FISH)
			.setPredators(AbysmEntityTypes.BLOOMRAY)
			.setTargetPopulation(5)
			.setUnfavoredHuntSpeed(-0.25f)
			.setLitterSize(1, 2)
	);

	public static final EcosystemType<PaddlefishEntity> PADDLEFISH = register(
		"paddlefish",
		EcosystemType.Builder
			.create(AbysmEntityTypes.PADDLEFISH)
			.setPredators(AbysmEntityTypes.BLOOMRAY)
			.setPlants(AbysmBlocks.ROSY_BLOOMSHROOM, AbysmBlocks.MAUVE_BLOOMSHROOM, AbysmBlocks.SUNNY_BLOOMSHROOM)
			.setTargetPopulation(7)
	);

	// TODO
	public static final EcosystemType<SnapperEntity> SNAPPER = register(
		"snapper",
		EcosystemType.Builder
			.create(AbysmEntityTypes.SNAPPER)
			.setPredators(AbysmEntityTypes.BLOOMRAY)
			.setPrey(EntityType.PLAYER)
			.setTargetPopulation(5)
	);

	// TODO
	public static final EcosystemType<GupGupEntity> GUP_GUP = register(
		"gup_gup",
		EcosystemType.Builder
			.create(AbysmEntityTypes.GUP_GUP)
			.setTargetPopulation(50)
	);

	// TODO
	public static final EcosystemType<ArowanaMagiciiEntity> AROWANA_MAGICII = register(
		"arowana_magicii",
		EcosystemType.Builder
			.create(AbysmEntityTypes.AROWANA_MAGICII)
			.setTargetPopulation(50)
	);

	public static final EcosystemType<BloomrayEntity> BLOOMRAY = register(
		"bloomray",
		EcosystemType.Builder.create(AbysmEntityTypes.BLOOMRAY)
			.setPrey(AbysmEntityTypes.SMALL_FLORAL_FISH, AbysmEntityTypes.BIG_FLORAL_FISH)
			.setTargetPopulation(3)
			.setHuntTicks(400, 1000) // 20 seconds, 50 seconds
			.setHuntFavorChance(0.9f)
			.setHuntSpeedModifiers(0.3f, -0.2f)
			.setBreedCooldownTicks(400) // 20 seconds
	);

	public static final EcosystemType<ElectricOoglyBooglyEntity> ELECTRIC_OOGLY_BOOGLY = register(
		"electric_oogly_boogly",
		EcosystemType.Builder.create(AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY)
			.setTargetPopulation(3)
	);

	public static final EcosystemType<LectorfinEntity> LECTORFIN = register(
		"lectorfin",
		EcosystemType.Builder.create(AbysmEntityTypes.LECTORFIN)
			.setPlants(AbysmBlocks.GOLDEN_LAZULI_OREFURL,
				AbysmBlocks.GOLDEN_LAZULI_OREFURL_PLANT)
			.setTargetPopulation(10)
	);

	public static final EcosystemType<ReticulatedFliprayEntity> RETICULATED_FLIPRAY = register(
		"reticulated_flipray",
		EcosystemType.Builder.create(AbysmEntityTypes.RETICULATED_FLIPRAY)
			.setTargetPopulation(2)
	);

//	public static <T extends MobEntity & EcologicalEntity> EcosystemType<T> register(EcosystemType.Builder<T> builder) {
//		return register(, builder);
//	}

	public static <T extends MobEntity & EcologicalEntity> EcosystemType<T> register(String id, EcosystemType.Builder<T> builder) {
		return register(keyOf(id), builder);
	}

	public static <T extends MobEntity & EcologicalEntity> EcosystemType<T> register(RegistryKey<EcosystemType<?>> key, EcosystemType.Builder<T> builder) {
		return Registry.register(AbysmRegistries.ECOSYSTEM_TYPE, key, builder.build());
	}

	private static RegistryKey<EcosystemType<?>> keyOf(String id) {
		return RegistryKey.of(AbysmRegistryKeys.ECOSYSTEM_TYPE, Abysm.id(id));
	}

	public static void init() {
		// NO-OP
	}
}
