package dev.spiritstudios.abysm.worldgen.structure;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.structure.Structure;

public interface AbysmStructureKeys {
	RegistryKey<Structure> DEEP_SEA_RUINS = of("deep_sea_ruins");

	private static RegistryKey<Structure> of(String id) {
		return RegistryKey.of(RegistryKeys.STRUCTURE, Abysm.id(id));
	}
}
