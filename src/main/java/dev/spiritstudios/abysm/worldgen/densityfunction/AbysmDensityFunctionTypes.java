package dev.spiritstudios.abysm.worldgen.densityfunction;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.Abysm;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;

import java.util.Arrays;

public class AbysmDensityFunctionTypes {

	public static void init() {
		register("density_blobs_sampler", DensityBlobsSamplerFunction.CODEC_HOLDER);
	}

	private static MapCodec<? extends DensityFunction> register(String id, CodecHolder<? extends DensityFunction> codecHolder) {
		return Registry.register(Registries.DENSITY_FUNCTION_TYPE, Abysm.id(id), codecHolder.codec());
	}

	public interface DensityBlobsSamplerFunction extends DensityFunction.Base {
		CodecHolder<DensityBlobsSamplerFunction> CODEC_HOLDER = CodecHolder.of(RecordCodecBuilder.mapCodec(
			instance -> instance.group(
				Identifier.CODEC.fieldOf("identifier").forGetter(DensityBlobsSamplerFunction::getIdentifier)
			).apply(instance, DummyDensityBlobsSampler::new)));

		@Override
		default CodecHolder<? extends DensityFunction> getCodecHolder() {
			return CODEC_HOLDER;
		}

		Identifier getIdentifier();
	}

	public static class DummyDensityBlobsSampler implements DensityBlobsSamplerFunction {

		private final Identifier identifier;

		public DummyDensityBlobsSampler(Identifier identifier) {
			this.identifier = identifier;
		}

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

		@Override
		public Identifier getIdentifier() {
			return this.identifier;
		}
	}
}
