package dev.spiritstudios.abysm.worldgen.densityfunction;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.worldgen.noise.AbysmNoiseParameters;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public class AbysmDensityFunctions {
	public static final RegistryKey<DensityFunction> RUINS_SEDIMENT_NOISE = keyOf("ruins_sediment_noise");
	public static final RegistryKey<DensityFunction> RUINS_SHELL_NOISE = keyOf("ruins_shell_noise");
	public static final RegistryKey<DensityFunction> RUINS_PILLAR_NOISE = keyOf("ruins_pillar_noise");
	public static final RegistryKey<DensityFunction> RUINS_SHELL_CAVE = keyOf("ruins_shell_cave");
	public static final RegistryKey<DensityFunction> RUINS_SHELL_CAVE_WITH_PILLARS = keyOf("ruins_shell_cave_with_pillars");
	public static final RegistryKey<DensityFunction> BEARDIFIER_ADDITION = keyOf("beardifier_addition");

	public static void bootstrap(Registerable<DensityFunction> registerable) {
		RegistryEntryLookup<DensityFunction> lookup = registerable.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION);
		RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseLookup = registerable.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS);

		registerable.register(
			RUINS_SEDIMENT_NOISE,
			DensityFunctionTypes.noise(
				noiseLookup.getOrThrow(AbysmNoiseParameters.RUINS_SEDIMENT),
				1.0,
				2.5
			)
		);

		registerable.register(
			RUINS_SHELL_NOISE,
			DensityFunctionTypes.noise(
				noiseLookup.getOrThrow(AbysmNoiseParameters.RUINS_SHELL)
			)
		);

		registerable.register(
			RUINS_PILLAR_NOISE,
			DensityFunctionTypes.noise(
				noiseLookup.getOrThrow(AbysmNoiseParameters.RUINS_PILLARS),
				1.0,
				0.04
			)
		);

		registerable.register(
			RUINS_SHELL_CAVE,
			DensityFunctionTypes.cacheOnce(
				DensityFunctionTypes.mul(
					AbysmDensityFunctionTypes.ShellCaveDummy.INSTANCE,
					DensityFunctionTypes.add(
						DensityFunctionTypes.constant(1.0),
						DensityFunctionTypes.mul(
							DensityFunctionTypes.constant(0.9),
							entryHolder(lookup, RUINS_SEDIMENT_NOISE).square()
						)
					)
				)
			)
		);

		registerable.register(
			RUINS_SHELL_CAVE_WITH_PILLARS,
			DensityFunctionTypes.rangeChoice(
				entryHolder(lookup, RUINS_SHELL_CAVE),
				1.5,
				100000.0,
				DensityFunctionTypes.max(
					DensityFunctionTypes.constant(1.5),
					DensityFunctionTypes.add(
						entryHolder(lookup, RUINS_SHELL_CAVE),
						DensityFunctionTypes.min(
							DensityFunctionTypes.constant(0.0),
							DensityFunctionTypes.mul(
								DensityFunctionTypes.constant(-80.0),
								DensityFunctionTypes.add(
									DensityFunctionTypes.constant(-0.065),
									entryHolder(lookup, RUINS_PILLAR_NOISE).square()
								)
							)
						)
					)
				),
				entryHolder(lookup, RUINS_SHELL_CAVE)
			)
		);

		registerable.register(
			BEARDIFIER_ADDITION,
			DensityFunctionTypes.mul(
				entryHolder(lookup, RUINS_SHELL_CAVE),
				DensityFunctionTypes.add(
					DensityFunctionTypes.constant(1.0),
					DensityFunctionTypes.mul(
						DensityFunctionTypes.constant(5.5),
						DensityFunctionTypes.noise(
							noiseLookup.getOrThrow(AbysmNoiseParameters.RUINS_SHELL_BEARD)
						).square()
					)
				)
			).square().square()
		);
	}

	private static RegistryKey<DensityFunction> keyOf(String id) {
		return RegistryKey.of(RegistryKeys.DENSITY_FUNCTION, Abysm.id(id));
	}

	private static DensityFunction entryHolder(RegistryEntryLookup<DensityFunction> densityFunctionRegisterable, RegistryKey<DensityFunction> key) {
		return new DensityFunctionTypes.RegistryEntryHolder(densityFunctionRegisterable.getOrThrow(key));
	}
}
