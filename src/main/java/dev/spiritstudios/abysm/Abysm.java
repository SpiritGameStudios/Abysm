package dev.spiritstudios.abysm;

import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.block.entity.AbysmBlockEntityTypes;
import dev.spiritstudios.abysm.command.AbysmCommands;
import dev.spiritstudios.abysm.component.AbysmDataComponentTypes;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.entity.AbysmSpawnRestrictions;
import dev.spiritstudios.abysm.entity.AbysmTrackedDataHandlers;
import dev.spiritstudios.abysm.entity.ai.AbysmSensorTypes;
import dev.spiritstudios.abysm.entity.attribute.AbysmEntityAttributeModifiers;
import dev.spiritstudios.abysm.entity.attribute.AbysmEntityAttributes;
import dev.spiritstudios.abysm.entity.effect.AbysmStatusEffects;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.item.AbysmPotions;
import dev.spiritstudios.abysm.loot.AbysmLootTableModifications;
import dev.spiritstudios.abysm.networking.EntityUpdateBlueS2CPayload;
import dev.spiritstudios.abysm.networking.HappyEntityParticlesS2CPayload;
import dev.spiritstudios.abysm.networking.NowHuntingS2CPayload;
import dev.spiritstudios.abysm.networking.UpdateDensityBlobBlockC2SPayload;
import dev.spiritstudios.abysm.networking.UserTypedForbiddenWordC2SPayload;
import dev.spiritstudios.abysm.particle.AbysmParticleTypes;
import dev.spiritstudios.abysm.recipe.AbysmBrewingRecipes;
import dev.spiritstudios.abysm.registry.AbysmAttachments;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import dev.spiritstudios.abysm.worldgen.AbysmBiomeModifications;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import dev.spiritstudios.abysm.worldgen.densityfunction.AbysmDensityBlobTypes;
import dev.spiritstudios.abysm.worldgen.densityfunction.AbysmDensityFunctionTypes;
import dev.spiritstudios.abysm.worldgen.feature.AbysmFeatures;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructurePieceTypes;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructureTypes;
import dev.spiritstudios.abysm.worldgen.tree.AbysmFoliagePlacerTypes;
import dev.spiritstudios.abysm.worldgen.tree.AbysmTrunkPlacerTypes;
import dev.spiritstudios.specter.api.registry.RegistryHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Abysm implements ModInitializer {
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

		AbysmBrewingRecipes.init();

		AbysmBiomeModifications.init();

		EcosystemType.init();

		// manually add item name component here since doing it normally results in it getting overwritten in the Item constructor
		DefaultItemComponentEvents.MODIFY.register((callback) -> callback.modify(AbysmItems.MYSTERIOUS_BLOB_SPAWN_EGG, builder -> builder.set(
				DataComponents.ITEM_NAME,
				Component.translatable("item.abysm.mysterious_blob_spawn_egg.fancy", Component.translatable("entity.abysm.mysterious_blob").withStyle(ChatFormatting.OBFUSCATED))
			)
		));
	}

	private void initRegistries() {
		AbysmRegistries.init();

		// register blocks & items
		AbysmBlocks.init();
		AbysmDataComponentTypes.init();
		AbysmItems.init();
		RegistryHelper.registerBlockEntityTypes(AbysmBlockEntityTypes.class, MODID);

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
			.register(content -> content.accept(AbysmItems.MUSIC_DISC_RENAISSANCE));

		// register entities & related
		AbysmEntityAttributes.init();
		AbysmEntityAttributeModifiers.init();
		AbysmEntityTypes.init();
		AbysmTrackedDataHandlers.init();
		AbysmEcosystemTypes.init();
		AbysmSensorTypes.init();

		AbysmStatusEffects.init();
		AbysmPotions.init();

		// region worldgen
		// structures
		AbysmStructurePieceTypes.init();
		registerFields(BuiltInRegistries.STRUCTURE_TYPE, StructureType.class, AbysmStructureTypes.class);

		// features
		registerFields(BuiltInRegistries.FEATURE, Feature.class, AbysmFeatures.class);
		registerFields(BuiltInRegistries.TRUNK_PLACER_TYPE, TrunkPlacerType.class, AbysmTrunkPlacerTypes.class);
		registerFields(BuiltInRegistries.FOLIAGE_PLACER_TYPE, FoliagePlacerType.class, AbysmFoliagePlacerTypes.class);

		AbysmDensityFunctionTypes.init();
		AbysmDensityBlobTypes.init();
		// endregion

		// misc
		AbysmSoundEvents.init();
		AbysmParticleTypes.init();
		AbysmCommands.init();
		AbysmBiomes.Metatags.init();
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
		PayloadTypeRegistry.playC2S().register(UpdateDensityBlobBlockC2SPayload.ID, UpdateDensityBlobBlockC2SPayload.PACKET_CODEC);

		PayloadTypeRegistry.playS2C().register(HappyEntityParticlesS2CPayload.ID, HappyEntityParticlesS2CPayload.PACKET_CODEC);
		PayloadTypeRegistry.playS2C().register(EntityUpdateBlueS2CPayload.ID, EntityUpdateBlueS2CPayload.PACKET_CODEC);
		PayloadTypeRegistry.playS2C().register(NowHuntingS2CPayload.ID, NowHuntingS2CPayload.PACKET_CODEC);

		ServerPlayNetworking.registerGlobalReceiver(UserTypedForbiddenWordC2SPayload.ID, UserTypedForbiddenWordC2SPayload::receive);
		ServerPlayNetworking.registerGlobalReceiver(UpdateDensityBlobBlockC2SPayload.ID, UpdateDensityBlobBlockC2SPayload::receive);
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}
}
