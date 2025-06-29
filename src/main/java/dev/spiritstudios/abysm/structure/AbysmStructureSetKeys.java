package dev.spiritstudios.abysm.structure;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructureSet;

public interface AbysmStructureSetKeys {
	RegistryKey<StructureSet> DEEP_SEA_RUINS = of("deep_sea_ruins");

	private static RegistryKey<StructureSet> of(String id) {
		return RegistryKey.of(RegistryKeys.STRUCTURE_SET, Abysm.id(id));
	}
}
