package dev.spiritstudios.abysm.worldgen.densityfunction;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.abysm.Abysm;
import java.util.Arrays;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public class AbysmDensityFunctionTypes {

	public static void init() {
		register("density_blobs_sampler", DensityBlobsSamplerFunction.CODEC_HOLDER);
	}

	private static MapCodec<? extends DensityFunction> register(String id, KeyDispatchDataCodec<? extends DensityFunction> codecHolder) {
		return Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, Abysm.id(id), codecHolder.codec());
	}

	public interface DensityBlobsSamplerFunction extends DensityFunction.SimpleFunction {
		KeyDispatchDataCodec<DensityBlobsSamplerFunction> CODEC_HOLDER = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					ResourceLocation.CODEC.fieldOf("identifier").forGetter(DensityBlobsSamplerFunction::identifier)
				)
				.apply(instance, DummyDensityBlobsSampler::new))
		);

		@Override
		default KeyDispatchDataCodec<? extends DensityFunction> codec() {
			return CODEC_HOLDER;
		}

		ResourceLocation identifier();
	}

	public record DummyDensityBlobsSampler(ResourceLocation identifier) implements DensityBlobsSamplerFunction {
		@Override
		public double compute(FunctionContext pos) {
			return 0.0;
		}

		@Override
		public void fillArray(double[] densities, ContextProvider applier) {
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
