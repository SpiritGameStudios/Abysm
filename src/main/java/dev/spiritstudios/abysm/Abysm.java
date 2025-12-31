package dev.spiritstudios.abysm;

import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import dev.spiritstudios.abysm.world.level.block.entity.AbysmBlockEntityTypes;
import dev.spiritstudios.abysm.command.AbysmCommands;
import dev.spiritstudios.abysm.core.component.AbysmDataComponents;
import dev.spiritstudios.abysm.world.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.world.entity.AbysmSpawnRestrictions;
import dev.spiritstudios.abysm.world.entity.AbysmEntityDataSerializers;
import dev.spiritstudios.abysm.world.entity.ai.AbysmSensorTypes;
import dev.spiritstudios.abysm.world.entity.attribute.AbysmEntityAttributeModifiers;
import dev.spiritstudios.abysm.world.entity.attribute.AbysmEntityAttributes;
import dev.spiritstudios.abysm.world.entity.effect.AbysmStatusEffects;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import dev.spiritstudios.abysm.world.item.AbysmPotions;
import dev.spiritstudios.abysm.world.level.storage.loot.AbysmLootTableModifications;
import dev.spiritstudios.abysm.network.EntityUpdateBlueS2CPayload;
import dev.spiritstudios.abysm.network.HappyEntityParticlesS2CPayload;
import dev.spiritstudios.abysm.network.NowHuntingS2CPayload;
import dev.spiritstudios.abysm.network.UpdateDensityBlobBlockC2SPayload;
import dev.spiritstudios.abysm.network.UserTypedForbiddenWordC2SPayload;
import dev.spiritstudios.abysm.core.particles.AbysmParticleTypes;
import dev.spiritstudios.abysm.world.item.crafting.AbysmBrewingRecipes;
import dev.spiritstudios.abysm.core.registries.AbysmAttachments;
import dev.spiritstudios.abysm.core.registries.AbysmMetatags;
import dev.spiritstudios.abysm.core.registries.AbysmRegistries;
import dev.spiritstudios.abysm.core.registries.AbysmSoundEvents;
import dev.spiritstudios.abysm.world.level.levelgen.AbysmBiomeModifications;
import dev.spiritstudios.abysm.world.level.levelgen.biome.AbysmBiomes;
import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.AbysmDensityBlobTypes;
import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.AbysmDensityFunctionTypes;
import dev.spiritstudios.abysm.world.level.levelgen.feature.AbysmFeatures;
import dev.spiritstudios.abysm.world.level.levelgen.structure.AbysmStructurePieceTypes;
import dev.spiritstudios.abysm.world.level.levelgen.structure.AbysmStructureTypes;
import dev.spiritstudios.abysm.world.level.levelgen.tree.AbysmFoliagePlacerTypes;
import dev.spiritstudios.abysm.world.level.levelgen.tree.AbysmTrunkPlacerTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Abysm implements ModInitializer {
	public static final String MODID = "abysm";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
//		AbysmConfig.HOLDER.load();

		initRegistries();

		registerNetworking();

		// set up biome placements and surface rules
		AbysmBiomes.addAllToGenerator();

		AbysmAttachments.init();

		AbysmSpawnRestrictions.init();

		AbysmLootTableModifications.init();

		AbysmBrewingRecipes.init();

		AbysmBiomeModifications.init();

		// manually add item name component here since doing it normally results in it getting overwritten in the Item constructor
		DefaultItemComponentEvents.MODIFY.register((callback) -> callback.modify(AbysmItems.MYSTERIOUS_BLOB_SPAWN_EGG, builder -> builder.set(
				DataComponents.ITEM_NAME,
				Component.translatable("item.abysm.mysterious_blob_spawn_egg.fancy", Component.translatable("entity.abysm.mysterious_blob").withStyle(ChatFormatting.OBFUSCATED))
			)
		));
	}

	private void initRegistries() {
		AbysmRegistries.init();
		AbysmMetatags.init();

		// register blocks & items
		AbysmBlocks.init();
		AbysmBlockEntityTypes.init();
		AbysmDataComponents.init();
		AbysmItems.init();

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
			.register(content -> content.accept(AbysmItems.MUSIC_DISC_RENAISSANCE));

		// register entities & related
		AbysmEntityAttributes.init();
		AbysmEntityAttributeModifiers.init();
		AbysmEntityTypes.init();
		AbysmEntityDataSerializers.init();
		AbysmSensorTypes.init();

		AbysmStatusEffects.init();
		AbysmPotions.init();

		// region worldgen
		// structures
		AbysmStructurePieceTypes.init();
		AbysmStructureTypes.init();

		// features
		AbysmFeatures.init();
		AbysmTrunkPlacerTypes.init();
		AbysmFoliagePlacerTypes.init();

		AbysmDensityFunctionTypes.init();
		AbysmDensityBlobTypes.init();
		// endregion

		// misc
		AbysmSoundEvents.init();
		AbysmParticleTypes.init();
		AbysmCommands.init();
	}

	private void registerNetworking() {
		PayloadTypeRegistry.playC2S().register(UserTypedForbiddenWordC2SPayload.ID, UserTypedForbiddenWordC2SPayload.STREAM_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(UserTypedForbiddenWordC2SPayload.ID, UserTypedForbiddenWordC2SPayload::receive);

		PayloadTypeRegistry.playC2S().register(UpdateDensityBlobBlockC2SPayload.ID, UpdateDensityBlobBlockC2SPayload.STREAM_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(UpdateDensityBlobBlockC2SPayload.ID, UpdateDensityBlobBlockC2SPayload::receive);

		PayloadTypeRegistry.playS2C().register(HappyEntityParticlesS2CPayload.ID, HappyEntityParticlesS2CPayload.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(EntityUpdateBlueS2CPayload.ID, EntityUpdateBlueS2CPayload.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(NowHuntingS2CPayload.ID, NowHuntingS2CPayload.STREAM_CODEC);
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MODID, path);
	}
}
