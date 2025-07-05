package dev.spiritstudios.abysm.duck;

import dev.spiritstudios.abysm.worldgen.densityfunction.DensityFunctionWrapper;
import org.jetbrains.annotations.Nullable;

public interface CarverContextDuckInterface {
	void abysm$setFunction(DensityFunctionWrapper sampler);

	@Nullable
	DensityFunctionWrapper abysm$getSampler();
}
