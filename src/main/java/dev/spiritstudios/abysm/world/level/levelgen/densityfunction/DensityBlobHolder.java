package dev.spiritstudios.abysm.world.level.levelgen.densityfunction;

import net.minecraft.resources.Identifier;

public interface DensityBlobHolder {
	DensityBlob getDensityBlob();

	Identifier getIdentifier();
}
