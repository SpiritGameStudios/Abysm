package dev.spiritstudios.abysm.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import net.minecraft.util.math.Box;

import java.util.function.Function;

public interface DensityBlob {
	Codec<DensityBlob> CODEC = AbysmRegistries.DENSITY_BLOB_TYPE.getCodec().dispatch(DensityBlob::getCodec, Function.identity());

	MapCodec<? extends DensityBlob> getCodec();

	double sampleDensity(int x, int y, int z);

	Box getBoundingBox();
}
