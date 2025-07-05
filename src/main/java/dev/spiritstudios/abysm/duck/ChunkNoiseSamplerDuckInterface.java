package dev.spiritstudios.abysm.duck;

import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.jetbrains.annotations.Nullable;

public interface ChunkNoiseSamplerDuckInterface {
	@Nullable
	DensityFunction abysm$getShellCaveFunction(NoiseConfig noiseConfig);
}
