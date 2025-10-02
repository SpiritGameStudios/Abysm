package dev.spiritstudios.abysm.item;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.registry.RegistryKey;

/**
 * @author Ampflower
 **/
public final class AbysmEquipmentAssetKeys {
	public static final RegistryKey<EquipmentAsset> DIVING_SUIT = registerAsset("diving");

	private static RegistryKey<EquipmentAsset> registerAsset(String abysmAsset) {
		return RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, Abysm.id(abysmAsset));
	}
}
