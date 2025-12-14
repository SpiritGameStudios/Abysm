package dev.spiritstudios.abysm.world.item;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

/**
 * @author Ampflower
 **/
public final class AbysmEquipmentAssetKeys {
	public static final ResourceKey<EquipmentAsset> DIVING_SUIT = registerAsset("diving");

	private static ResourceKey<EquipmentAsset> registerAsset(String abysmAsset) {
		return ResourceKey.create(EquipmentAssets.ROOT_ID, Abysm.id(abysmAsset));
	}
}
