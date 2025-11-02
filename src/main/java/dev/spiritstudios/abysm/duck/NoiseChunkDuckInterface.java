package dev.spiritstudios.abysm.duck;

import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.RandomState;
import org.jetbrains.annotations.Nullable;

public interface NoiseChunkDuckInterface {
	@Nullable
	DensityFunction abysm$getShellCaveFunction(RandomState noiseConfig);
}
