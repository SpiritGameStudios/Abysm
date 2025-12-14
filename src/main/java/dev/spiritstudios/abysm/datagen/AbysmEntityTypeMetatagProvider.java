package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.core.registries.AbysmMetatags;
import dev.spiritstudios.abysm.world.ecosystem.registry.EcosystemData;
import dev.spiritstudios.abysm.world.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import dev.spiritstudios.spectre.api.core.registry.metatag.MetatagsProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.SharedConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class AbysmEntityTypeMetatagProvider extends MetatagsProvider.EntityTypeMetatagProvider {
	public AbysmEntityTypeMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(dataOutput, registriesFuture);
	}

	@Override
	protected void addMetatags(HolderLookup.Provider wrapperLookup) {
		builder(AbysmMetatags.ECOSYSTEM_DATA)
			.put(
				AbysmEntityTypes.SMALL_FLORAL_FISH,
				new EcosystemData.Builder()
					.predators(AbysmEntityTypes.BLOOMRAY, AbysmEntityTypes.SKELETON_SHARK)
					.plants(AbysmBlocks.PURPLE_SCABIOSA, AbysmBlocks.PINK_SCABIOSA, AbysmBlocks.RED_SCABIOSA)
					.targetPopulation(7)
					.unfavoredHuntSpeed(ConstantFloat.of(-0.35f))
					.litterSize(UniformInt.of(1, 4))
					.build()
			)
			.put(
				AbysmEntityTypes.BIG_FLORAL_FISH,
				new EcosystemData.Builder()
					.predators(
						AbysmEntityTypes.BLOOMRAY,
						AbysmEntityTypes.SKELETON_SHARK
					)
					.targetPopulation(5)
					.unfavoredHuntSpeed(ConstantFloat.of(-0.25F))
					.litterSize(UniformInt.of(1, 2))
					.build()
			)
			.put(
				AbysmEntityTypes.PADDLEFISH,
				new EcosystemData.Builder()
					.predators(
						AbysmEntityTypes.BLOOMRAY,
						AbysmEntityTypes.SKELETON_SHARK
					)
					.plants(
						AbysmBlocks.ROSY_BLOOMSHROOM, AbysmBlocks.MAUVE_BLOOMSHROOM, AbysmBlocks.SUNNY_BLOOMSHROOM
					)
					.targetPopulation(7)
					.build()
			)
			.put(
				AbysmEntityTypes.SNAPPER,
				new EcosystemData.Builder()
					.predators(
						AbysmEntityTypes.BLOOMRAY,
						AbysmEntityTypes.SKELETON_SHARK
					)
					.targetPopulation(5)
					.build()
			)
			.put(
				AbysmEntityTypes.GUP_GUP,
				new EcosystemData.Builder()
					.predators(AbysmEntityTypes.SKELETON_SHARK)
					.targetPopulation(50)
					.build()
			)
			.put(
				AbysmEntityTypes.AROWANA_MAGICII,
				new EcosystemData.Builder()
					.predators(AbysmEntityTypes.SKELETON_SHARK)
					.targetPopulation(4)
					.build()
			)
			.put(
				AbysmEntityTypes.SYNTHETHIC_ORNIOTHOPE,
				new EcosystemData.Builder()
					.predators(AbysmEntityTypes.SKELETON_SHARK)
					.targetPopulation(4)
					.build()
			)
			.put(
				AbysmEntityTypes.BLOOMRAY,
				new EcosystemData.Builder()
					.prey(
						AbysmEntityTypes.SMALL_FLORAL_FISH,
						AbysmEntityTypes.BIG_FLORAL_FISH
					)
					.predators(
						AbysmEntityTypes.SKELETON_SHARK
					)
					.targetPopulation(3)
					.huntDuration(UniformInt.of(
						20 * SharedConstants.TICKS_PER_SECOND,
						50 * SharedConstants.TICKS_PER_SECOND
					))
					.favorChance(0.9f)
					.favoredHuntSpeed(ConstantFloat.of(0.3f))
					.unfavoredHuntSpeed(ConstantFloat.of(-0.2f))
					.breedCooldown(ConstantInt.of(20 * SharedConstants.TICKS_PER_SECOND))
					.build()
			)
			.put(
				AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY,
				new EcosystemData.Builder()
					.targetPopulation(3)
					.build()
			)
			.put(
				AbysmEntityTypes.LECTORFIN,
				new EcosystemData.Builder()
					.plants(AbysmBlocks.GOLDEN_LAZULI_OREFURL,
						AbysmBlocks.GOLDEN_LAZULI_OREFURL_PLANT)
					.predators(AbysmEntityTypes.SKELETON_SHARK)
					.targetPopulation(10)
					.build()
			)
			.put(
				AbysmEntityTypes.RETICULATED_FLIPRAY,
				new EcosystemData.Builder()
					.targetPopulation(2)
					.build()
			)
			.put(
				AbysmEntityTypes.SKELETON_SHARK,
				new EcosystemData.Builder()
					.targetPopulation(2)
					.prey(
						AbysmEntityTypes.LECTORFIN,
						AbysmEntityTypes.SMALL_FLORAL_FISH,
						AbysmEntityTypes.BIG_FLORAL_FISH,
						AbysmEntityTypes.AROWANA_MAGICII,
						AbysmEntityTypes.PADDLEFISH,
						AbysmEntityTypes.GUP_GUP,
						AbysmEntityTypes.SNAPPER,
						AbysmEntityTypes.BLOOMRAY,
						EntityType.PLAYER
					)
					.build()
			);
	}

	@Override
	public String getName() {
		return "Entity Type Metatags";
	}
}
