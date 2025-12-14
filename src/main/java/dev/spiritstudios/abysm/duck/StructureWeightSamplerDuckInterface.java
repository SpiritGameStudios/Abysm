package dev.spiritstudios.abysm.duck;

import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.DensityBlobsSamplerCollection;
import org.jetbrains.annotations.Nullable;

public interface StructureWeightSamplerDuckInterface {
	@Nullable
	DensityBlobsSamplerCollection abysm$getSamplerCollection();
}
