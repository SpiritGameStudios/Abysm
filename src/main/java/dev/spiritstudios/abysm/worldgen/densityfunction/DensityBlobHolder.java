package dev.spiritstudios.abysm.worldgen.densityfunction;

import net.minecraft.resources.ResourceLocation;

public interface DensityBlobHolder {
	DensityBlob getDensityBlob();

	ResourceLocation getIdentifier();
}
