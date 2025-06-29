package dev.spiritstudios.abysm;

import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import dev.spiritstudios.abysm.loot.AbysmLootTableModifications;
import dev.spiritstudios.abysm.networking.UserTypedForbiddenWordC2SPayload;
import dev.spiritstudios.abysm.registry.*;
import dev.spiritstudios.abysm.structure.AbysmStructurePieceTypes;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import dev.spiritstudios.specter.api.registry.RegistryHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.registry.Registries;
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
		AbysmSoundEvents.init();
		AbysmBlocks.init();
		AbysmDataComponentTypes.init();
		AbysmItems.init();

		AbysmEntityTypes.init();
		AbysmEntityAttributes.init();
		AbysmSpawnRestrictions.init();
		AbysmEcosystemTypes.init();

		AbysmParticleTypes.init();

		AbysmRegistries.init();
		EntityPattern.init();

		RegistryHelper.registerFields(
			Registries.TRUNK_PLACER_TYPE, RegistryHelper.fixGenerics(TrunkPlacerType.class),
			AbysmTrunkPlacerTypes.class,
			MODID
		);

		RegistryHelper.registerFields(
			Registries.FOLIAGE_PLACER_TYPE, RegistryHelper.fixGenerics(FoliagePlacerType.class),
			AbysmFoliagePlacerTypes.class,
			MODID
		);

		RegistryHelper.registerFields(
			Registries.FEATURE, RegistryHelper.fixGenerics(Feature.class),
			AbysmFeatures.class,
			MODID
		);

		RegistryHelper.registerFields(
			Registries.STRUCTURE_TYPE, RegistryHelper.fixGenerics(StructureType.class),
			AbysmStructureTypes.class,
			MODID
		);

		AbysmStructurePieceTypes.init();

		AbysmBiomes.addAllToGenerator();

		AbysmLootTableModifications.init();

		registerNetworking();
    }

	private void registerNetworking() {
		PayloadTypeRegistry.playC2S().register(UserTypedForbiddenWordC2SPayload.ID, UserTypedForbiddenWordC2SPayload.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(UserTypedForbiddenWordC2SPayload.ID, UserTypedForbiddenWordC2SPayload.Receiver.INSTANCE);
	}

	public static Identifier id(String path) {
		return Identifier.of(MODID, path);
	}
}
