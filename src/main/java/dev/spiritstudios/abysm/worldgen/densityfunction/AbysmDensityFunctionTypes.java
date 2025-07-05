package dev.spiritstudios.abysm.worldgen.densityfunction;

import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.Abysm;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;

import java.util.Arrays;

public class AbysmDensityFunctionTypes {

	public static void init() {
		register("shell_cave", AbysmDensityFunctionTypes.ShellCave.CODEC_HOLDER);
	}

	private static MapCodec<? extends DensityFunction> register(String id, CodecHolder<? extends DensityFunction> codecHolder) {
		return Registry.register(Registries.DENSITY_FUNCTION_TYPE, Abysm.id(id), codecHolder.codec());
	}

	public interface ShellCave extends DensityFunction.Base {
		CodecHolder<DensityFunction> CODEC_HOLDER = CodecHolder.of(MapCodec.unit(ShellCaveDummy.INSTANCE));

		@Override
		default CodecHolder<? extends DensityFunction> getCodecHolder() {
			return CODEC_HOLDER;
		}
	}

	public enum ShellCaveDummy implements ShellCave {
		INSTANCE;

		@Override
		public double sample(DensityFunction.NoisePos pos) {
			return 0.0;
		}

		@Override
		public void fill(double[] densities, DensityFunction.EachApplier applier) {
			Arrays.fill(densities, 0.0);
		}

		@Override
		public double minValue() {
			return 0.0;
		}

		@Override
		public double maxValue() {
			return 0.0;
		}
	}
}
