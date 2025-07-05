package dev.spiritstudios.abysm.worldgen.noise;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;

public class AbysmNoiseParameters {
	public static final RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> RUINS_SEDIMENT = keyOf("ruins_sediment");
	public static final RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> RUINS_SHELL = keyOf("ruins_shell");
	public static final RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> RUINS_SHELL_BEARD = keyOf("ruins_shell_beard");
	public static final RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> RUINS_PILLARS = keyOf("ruins_pillars");

	public static void bootstrap(Registerable<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersRegisterable) {
		register(noiseParametersRegisterable, RUINS_SEDIMENT, -5, 0.9, 0.5, 0.4);
		register(noiseParametersRegisterable, RUINS_SHELL, -6, 0.5, 6.0, 7.0, 2.0);
		register(noiseParametersRegisterable, RUINS_SHELL_BEARD, -6, 0.5, 6.0, 7.0, 2.0);
		register(noiseParametersRegisterable, RUINS_PILLARS, -4, 0.6, 0.8, 2.5);
	}

	private static RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> keyOf(String id) {
		return RegistryKey.of(RegistryKeys.NOISE_PARAMETERS, Abysm.id(id));
	}

	private static void register(
		Registerable<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersRegisterable,
		RegistryKey<DoublePerlinNoiseSampler.NoiseParameters> key,
		int firstOctave,
		double firstAmplitude,
		double... amplitudes
	) {
		noiseParametersRegisterable.register(key, new DoublePerlinNoiseSampler.NoiseParameters(firstOctave, firstAmplitude, amplitudes));
	}
}
