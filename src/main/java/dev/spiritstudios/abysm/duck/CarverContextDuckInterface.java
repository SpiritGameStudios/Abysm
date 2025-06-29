package dev.spiritstudios.abysm.duck;

import org.jetbrains.annotations.Nullable;

public interface CarverContextDuckInterface {
	void abysm$setSampler(StructureWeightSamplerDuckInterface sampler);

	@Nullable
	StructureWeightSamplerDuckInterface abysm$getSampler();
}
