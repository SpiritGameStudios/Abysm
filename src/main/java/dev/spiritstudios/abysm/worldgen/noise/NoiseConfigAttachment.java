package dev.spiritstudios.abysm.worldgen.noise;

import dev.spiritstudios.abysm.duck.RandomStateDuckInterface;
import dev.spiritstudios.abysm.worldgen.densityfunction.AbysmDensityFunctions;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class NoiseConfigAttachment {
	private @Nullable DensityFunction ruinsSediment = null;
	private @Nullable DensityFunction ruinsShellCave = null;
	private @Nullable DensityFunction ruinsShellCaveWithPillars = null;
	private @Nullable DensityFunction beardifierAddition = null;

	public void attachBonusFunctions(ServerLevel world, SamplerFunction samplerFunction) {
		Registry<DensityFunction> dfRegistry = world.registryAccess().lookupOrThrow(Registries.DENSITY_FUNCTION);

		this.ruinsSediment = getDensityFunction(AbysmDensityFunctions.RUINS_SEDIMENT, dfRegistry, samplerFunction);
		this.ruinsShellCave = getDensityFunction(AbysmDensityFunctions.RUINS_SHELL_CAVE, dfRegistry, samplerFunction);
		this.ruinsShellCaveWithPillars = getDensityFunction(AbysmDensityFunctions.RUINS_SHELL_CAVE_WITH_PILLARS, dfRegistry, samplerFunction);
		this.beardifierAddition = getDensityFunction(AbysmDensityFunctions.BEARDIFIER_ADDITION, dfRegistry, samplerFunction);
	}

	private @Nullable DensityFunction getDensityFunction(ResourceKey<DensityFunction> key, Registry<DensityFunction> densityFunctionRegistry, SamplerFunction samplerFunction) {
		Optional<DensityFunction> function = densityFunctionRegistry.get(key.location()).map(Holder.Reference::value);

		Optional<DensityFunction> appliedFunction = function.map(df -> df.mapAll(new DensityFunction.Visitor() {
			@Override
			public DensityFunction.NoiseHolder visitNoise(DensityFunction.NoiseHolder noiseDensityFunction) {
				Holder<NormalNoise.NoiseParameters> registryEntry = noiseDensityFunction.noiseData();
				// assume that legacy_random_source is false or irrelevant
				NormalNoise doublePerlinNoiseSampler = samplerFunction.getOrCreateSampler(
					registryEntry.unwrapKey().orElseThrow()
				);
				return new DensityFunction.NoiseHolder(registryEntry, doublePerlinNoiseSampler);
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

	public NoiseConfigAttachment apply(DensityFunction.Visitor visitor) {
		NoiseConfigAttachment newNCA = new NoiseConfigAttachment();

		newNCA.ruinsSediment = apply(visitor, this.ruinsSediment);
		newNCA.ruinsShellCave = apply(visitor, this.ruinsShellCave);
		newNCA.ruinsShellCaveWithPillars = apply(visitor, this.ruinsShellCaveWithPillars);
		newNCA.beardifierAddition = apply(visitor, this.beardifierAddition);

		return newNCA;
	}

	private DensityFunction apply(DensityFunction.Visitor visitor, @Nullable DensityFunction densityFunction) {
		if (densityFunction == null) {
			return null;
		} else {
			return densityFunction.mapAll(visitor);
		}
	}

	public static NoiseConfigAttachment get(RandomState noiseConfig) {
		return ((RandomStateDuckInterface) (Object) noiseConfig).abysm$getAttachment();
	}

	@FunctionalInterface
	public interface SamplerFunction {
		NormalNoise getOrCreateSampler(ResourceKey<NormalNoise.NoiseParameters> noiseParametersKey);
	}
}
