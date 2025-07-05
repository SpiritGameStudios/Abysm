package dev.spiritstudios.abysm.duck;

import dev.spiritstudios.abysm.worldgen.densityfunction.ShellCaveSampler;
import org.jetbrains.annotations.Nullable;

public interface StructureWeightSamplerDuckInterface {
	@Nullable
	ShellCaveSampler abysm$getShellCaveSampler();
}
