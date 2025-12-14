package dev.spiritstudios.abysm.world.level.levelgen.densityfunction;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.level.levelgen.noise.AbysmNoiseParameters;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class AbysmDensityFunctions {
	public static final ResourceKey<DensityFunction> RUINS_SEDIMENT_NOISE = keyOf("ruins_sediment_noise");
	public static final ResourceKey<DensityFunction> RUINS_SHELL_NOISE = keyOf("ruins_shell_noise");
	public static final ResourceKey<DensityFunction> RUINS_PILLAR_NOISE = keyOf("ruins_pillar_noise");
	public static final ResourceKey<DensityFunction> RUINS_SHELL_CAVE = keyOf("ruins_shell_cave");
	public static final ResourceKey<DensityFunction> RUINS_SHELL_CAVE_WITH_PILLARS = keyOf("ruins_shell_cave_with_pillars");
	public static final ResourceKey<DensityFunction> RUINS_SEDIMENT = keyOf("ruins_sediment");
	public static final ResourceKey<DensityFunction> BEARDIFIER_ADDITION = keyOf("beardifier_addition");

	public static void bootstrap(BootstrapContext<DensityFunction> registerable) {
		HolderGetter<DensityFunction> lookup = registerable.lookup(Registries.DENSITY_FUNCTION);
		HolderGetter<NormalNoise.NoiseParameters> noiseLookup = registerable.lookup(Registries.NOISE);

		registerable.register(
			RUINS_SEDIMENT_NOISE,
			DensityFunctions.noise(
				noiseLookup.getOrThrow(AbysmNoiseParameters.RUINS_SEDIMENT),
				1.0,
				2.5
			)
		);

		registerable.register(
			RUINS_SHELL_NOISE,
			DensityFunctions.noise(
				noiseLookup.getOrThrow(AbysmNoiseParameters.RUINS_SHELL)
			)
		);

		registerable.register(
			RUINS_PILLAR_NOISE,
			DensityFunctions.noise(
				noiseLookup.getOrThrow(AbysmNoiseParameters.RUINS_PILLARS),
				1.0,
				0.04
			)
		);

		registerable.register(
			RUINS_SHELL_CAVE,
			DensityFunctions.cacheOnce(
				DensityFunctions.mul(
					new AbysmDensityFunctionTypes.DummyDensityBlobsSampler(Abysm.id("ruins_shell")),
					DensityFunctions.add(
						DensityFunctions.constant(1.0),
						DensityFunctions.mul(
							DensityFunctions.constant(0.9),
							entryHolder(lookup, RUINS_SEDIMENT_NOISE).square()
						)
					)
				)
			)
		);

		registerable.register(
			RUINS_SHELL_CAVE_WITH_PILLARS,
			DensityFunctions.rangeChoice(
				entryHolder(lookup, RUINS_SHELL_CAVE),
				1.5,
				100000.0,
				DensityFunctions.max(
					DensityFunctions.constant(1.5),
					DensityFunctions.add(
						entryHolder(lookup, RUINS_SHELL_CAVE),
						DensityFunctions.min(
							DensityFunctions.constant(0.0),
							DensityFunctions.mul(
								DensityFunctions.constant(-80.0),
								DensityFunctions.add(
									DensityFunctions.constant(-0.065),
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
			DensityFunctions.mul(
				entryHolder(lookup, RUINS_SHELL_CAVE),
				DensityFunctions.add(
					DensityFunctions.constant(1.0),
					DensityFunctions.mul(
						DensityFunctions.constant(5.5),
						DensityFunctions.noise(
							noiseLookup.getOrThrow(AbysmNoiseParameters.RUINS_SHELL_BEARD)
						).square()
					)
				)
			).square().square()
		);

		registerable.register(
			RUINS_SEDIMENT,
			DensityFunctions.add(
				DensityFunctions.add(
					entryHolder(lookup, RUINS_SEDIMENT_NOISE),
					DensityFunctions.constant(-0.18)
				),
				new AbysmDensityFunctionTypes.DummyDensityBlobsSampler(Abysm.id("ruins_sediment"))
			)
		);
	}

	private static ResourceKey<DensityFunction> keyOf(String id) {
		return ResourceKey.create(Registries.DENSITY_FUNCTION, Abysm.id(id));
	}

	private static DensityFunction entryHolder(HolderGetter<DensityFunction> densityFunctionRegisterable, ResourceKey<DensityFunction> key) {
		return new DensityFunctions.HolderHolder(densityFunctionRegisterable.getOrThrow(key));
	}
}
