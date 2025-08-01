package dev.spiritstudios.abysm.worldgen.noise;

import dev.spiritstudios.abysm.duck.NoiseConfigDuckInterface;
import dev.spiritstudios.abysm.worldgen.densityfunction.AbysmDensityFunctions;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NoiseConfigAttachment {
	private @Nullable DensityFunction ruinsSediment = null;
	private @Nullable DensityFunction ruinsShellCave = null;
	private @Nullable DensityFunction ruinsShellCaveWithPillars = null;
	private @Nullable DensityFunction beardifierAddition = null;

	public void attachBonusFunctions(ServerWorld world, SamplerFunction samplerFunction) {
		Registry<DensityFunction> dfRegistry = world.getRegistryManager().getOrThrow(RegistryKeys.DENSITY_FUNCTION);

		this.ruinsSediment = getDensityFunction(AbysmDensityFunctions.RUINS_SEDIMENT, dfRegistry, samplerFunction);
		this.ruinsShellCave = getDensityFunction(AbysmDensityFunctions.RUINS_SHELL_CAVE, dfRegistry, samplerFunction);
		this.ruinsShellCaveWithPillars = getDensityFunction(AbysmDensityFunctions.RUINS_SHELL_CAVE_WITH_PILLARS, dfRegistry, samplerFunction);
		this.beardifierAddition = getDensityFunction(AbysmDensityFunctions.BEARDIFIER_ADDITION, dfRegistry, samplerFunction);
	}

	private @Nullable DensityFunction getDensityFunction(RegistryKey<DensityFunction> key, Registry<DensityFunction> densityFunctionRegistry, SamplerFunction samplerFunction) {
		Optional<DensityFunction> function = densityFunctionRegistry.getEntry(key.getValue()).map(RegistryEntry.Reference::value);

		Optional<DensityFunction> appliedFunction = function.map(df -> df.apply(new DensityFunction.DensityFunctionVisitor() {
			@Override
			public DensityFunction.Noise apply(DensityFunction.Noise noiseDensityFunction) {
				RegistryEntry<DoublePerlinNoiseSampler.NoiseParameters> registryEntry = noiseDensityFunction.noiseData();
				// assume that legacy_random_source is false or irrelevant
				DoublePerlinNoiseSampler doublePerlinNoiseSampler = samplerFunction.getOrCreateSampler(
					registryEntry.getKey().orElseThrow()
				);
				return new DensityFunction.Noise(registryEntry, doublePerlinNoiseSampler);
			}

			@Override
			public DensityFunction apply(DensityFunction densityFunction) {
				// return original
				return densityFunction;
			}
		}));

		return appliedFunction.orElse(null);
	}

	public @Nullable DensityFunction getRuinsSediment() {
		return ruinsSediment;
	}

	public @Nullable DensityFunction getRuinsShellCave() {
		return ruinsShellCave;
	}

	public @Nullable DensityFunction getRuinsShellCaveWithPillars() {
		return ruinsShellCaveWithPillars;
	}

	public @Nullable DensityFunction getBeardifierAddition() {
		return beardifierAddition;
	}

	public NoiseConfigAttachment apply(DensityFunction.DensityFunctionVisitor visitor) {
		NoiseConfigAttachment newNCA = new NoiseConfigAttachment();

		newNCA.ruinsSediment = apply(visitor, this.ruinsSediment);
		newNCA.ruinsShellCave = apply(visitor, this.ruinsShellCave);
		newNCA.ruinsShellCaveWithPillars = apply(visitor, this.ruinsShellCaveWithPillars);
		newNCA.beardifierAddition = apply(visitor, this.beardifierAddition);

		return newNCA;
	}

	private DensityFunction apply(DensityFunction.DensityFunctionVisitor visitor, @Nullable DensityFunction densityFunction) {
		if (densityFunction == null) {
			return null;
		} else {
			return densityFunction.apply(visitor);
		}
	}

	public static NoiseConfigAttachment get(NoiseConfig noiseConfig) {
		return ((NoiseConfigDuckInterface) (Object) noiseConfig).abysm$getAttachment();
	}

	@FunctionalInterface
	public interface SamplerFunction {
		DoublePerlinNoiseSampler getOrCreateSampler(RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersKey);
	}
}
