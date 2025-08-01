package dev.spiritstudios.abysm.worldgen.densityfunction;

import net.minecraft.util.Identifier;

public interface DensityBlobHolder {
	DensityBlob getDensityBlob();

	Identifier getIdentifier();
}
