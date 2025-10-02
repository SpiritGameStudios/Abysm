package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.item.AbysmEquipmentAssetKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.registry.RegistryKey;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * @author Ampflower
 **/
public final class AbysmEquipmentAssetProvider implements DataProvider {
	private final DataOutput.PathResolver pathResolver;

	public AbysmEquipmentAssetProvider(FabricDataOutput output) {
		this.pathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "equipment");
	}

	@Override
	public CompletableFuture<?> run(DataWriter writer) {
		final Map<RegistryKey<EquipmentAsset>, EquipmentModel> map = new HashMap<>();

		registerHumanoidSet(map::put, AbysmEquipmentAssetKeys.DIVING_SUIT);

		return DataProvider.writeAllToPath(writer, EquipmentModel.CODEC, this.pathResolver::resolveJson, map);
	}

	public static void registerHumanoidSet(
			final BiConsumer<RegistryKey<EquipmentAsset>, EquipmentModel> consumer,
			final RegistryKey<EquipmentAsset> asset
	) {
		final var model = EquipmentModel.builder().addHumanoidLayers(asset.getValue()).build();

		consumer.accept(asset, model);
	}

	@Override
	public String getName() {
		return "Abysm Equipment Asset Definer";
	}
}
