package dev.spiritstudios.abysm;

import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.AbysmEntityAttributes;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.entity.AbysmSpawnRestrictions;
import dev.spiritstudios.abysm.entity.AbysmTrackedDataHandlers;
import dev.spiritstudios.abysm.entity.ai.AbysmSensorTypes;
import dev.spiritstudios.abysm.item.AbysmDataComponentTypes;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.loot.AbysmLootTableModifications;
import dev.spiritstudios.abysm.networking.EntityFinishedEatingS2CPayload;
import dev.spiritstudios.abysm.networking.UserTypedForbiddenWordC2SPayload;
import dev.spiritstudios.abysm.particle.AbysmParticleTypes;
import dev.spiritstudios.abysm.registry.AbysmAttachments;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import dev.spiritstudios.abysm.worldgen.densityfunction.AbysmDensityFunctionTypes;
import dev.spiritstudios.abysm.worldgen.feature.AbysmFeatures;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructurePieceTypes;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructureTypes;
import dev.spiritstudios.abysm.worldgen.tree.AbysmFoliagePlacerTypes;
import dev.spiritstudios.abysm.worldgen.tree.AbysmTrunkPlacerTypes;
import dev.spiritstudios.specter.api.registry.RegistryHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.structure.StructureType;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Abysm implements ModInitializer {
	public static final String MODID = "abysm";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		AbysmConfig.HOLDER.load();

		initRegistries();

		registerNetworking();

		// set up biome placements and surface rules
		AbysmBiomes.addAllToGenerator();

		AbysmAttachments.init();

		AbysmSpawnRestrictions.init();

		AbysmLootTableModifications.init();

		EcosystemType.init();
	}

	private void initRegistries() {
		AbysmRegistries.init();

		// register blocks & items
		AbysmBlocks.init();
		AbysmDataComponentTypes.init();
		AbysmItems.init();

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
			.register(content -> content.add(AbysmItems.MUSIC_DISC_RENAISSANCE));

		// register entities & related
		AbysmEntityAttributes.init();
		AbysmEntityTypes.init();
		AbysmTrackedDataHandlers.init();
		AbysmEcosystemTypes.init();
		AbysmSensorTypes.init();

		// region worldgen
		// structures
		AbysmStructurePieceTypes.init();
		registerFields(Registries.STRUCTURE_TYPE, StructureType.class, AbysmStructureTypes.class);

		// features
		registerFields(Registries.FEATURE, Feature.class, AbysmFeatures.class);
		registerFields(Registries.TRUNK_PLACER_TYPE, TrunkPlacerType.class, AbysmTrunkPlacerTypes.class);
		registerFields(Registries.FOLIAGE_PLACER_TYPE, FoliagePlacerType.class, AbysmFoliagePlacerTypes.class);

		AbysmDensityFunctionTypes.init();
		// endregion

		// misc
		AbysmSoundEvents.init();
		AbysmParticleTypes.init();
	}

	private <T> void registerFields(Registry<T> registry, Class<?> toRegister, Class<?> clazz) {
		RegistryHelper.registerFields(
			registry,
			RegistryHelper.fixGenerics(toRegister),
			clazz,
			MODID
		);
	}

	private void registerNetworking() {
		PayloadTypeRegistry.playC2S().register(UserTypedForbiddenWordC2SPayload.ID, UserTypedForbiddenWordC2SPayload.PACKET_CODEC);
		PayloadTypeRegistry.playS2C().register(EntityFinishedEatingS2CPayload.ID, EntityFinishedEatingS2CPayload.PACKET_CODEC);

		ServerPlayNetworking.registerGlobalReceiver(UserTypedForbiddenWordC2SPayload.ID, UserTypedForbiddenWordC2SPayload.Receiver.INSTANCE);
	}

	public static Identifier id(String path) {
		return Identifier.of(MODID, path);
	}
}
