package dev.spiritstudios.abysm.worldgen.noise;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class AbysmNoiseParameters {
	public static final ResourceKey<NormalNoise.NoiseParameters> RUINS_SEDIMENT = keyOf("ruins_sediment");
	public static final ResourceKey<NormalNoise.NoiseParameters> RUINS_SHELL = keyOf("ruins_shell");
	public static final ResourceKey<NormalNoise.NoiseParameters> RUINS_SHELL_BEARD = keyOf("ruins_shell_beard");
	public static final ResourceKey<NormalNoise.NoiseParameters> RUINS_PILLARS = keyOf("ruins_pillars");
	public static final ResourceKey<NormalNoise.NoiseParameters> RUINS_SEDIMENT_TYPE = keyOf("ruins_sediment_type");

	public static void bootstrap(BootstrapContext<NormalNoise.NoiseParameters> noiseParametersRegisterable) {
		register(noiseParametersRegisterable, RUINS_SEDIMENT, -5, 0.9, 0.5, 0.4);
		register(noiseParametersRegisterable, RUINS_SHELL, -6, 0.5, 6.0, 7.0, 2.0);
		register(noiseParametersRegisterable, RUINS_SHELL_BEARD, -6, 0.5, 6.0, 7.0, 2.0);
		register(noiseParametersRegisterable, RUINS_PILLARS, -4, 0.6, 0.8, 2.5);
		register(noiseParametersRegisterable, RUINS_SEDIMENT_TYPE, -2, 1.0, 1.0);
	}

	private static ResourceKey<NormalNoise.NoiseParameters> keyOf(String id) {
		return ResourceKey.create(Registries.NOISE, Abysm.id(id));
	}

	private static void register(
		BootstrapContext<NormalNoise.NoiseParameters> noiseParametersRegisterable,
		ResourceKey<NormalNoise.NoiseParameters> key,
		int firstOctave,
		double firstAmplitude,
		double... amplitudes
	) {
		noiseParametersRegisterable.register(key, new NormalNoise.NoiseParameters(firstOctave, firstAmplitude, amplitudes));
	}
}
