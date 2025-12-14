package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.world.item.AbysmEquipmentAssetKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * @author Ampflower
 **/
public final class AbysmEquipmentAssetProvider implements DataProvider {
	private final PackOutput.PathProvider pathResolver;

	public AbysmEquipmentAssetProvider(FabricDataOutput output) {
		this.pathResolver = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput writer) {
		final Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> map = new HashMap<>();

		registerHumanoidSet(map::put, AbysmEquipmentAssetKeys.DIVING_SUIT);

		return DataProvider.saveAll(writer, EquipmentClientInfo.CODEC, this.pathResolver::json, map);
	}

	public static void registerHumanoidSet(
			final BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> consumer,
			final ResourceKey<EquipmentAsset> asset
	) {
		final var model = EquipmentClientInfo.builder().addHumanoidLayers(asset.identifier()).build();

		consumer.accept(asset, model);
	}

	@Override
	public String getName() {
		return "Abysm Equipment Asset Definer";
	}
}
