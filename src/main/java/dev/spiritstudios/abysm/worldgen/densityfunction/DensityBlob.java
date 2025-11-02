package dev.spiritstudios.abysm.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import java.util.function.Function;
import net.minecraft.world.phys.AABB;

public interface DensityBlob {
	Codec<DensityBlob> CODEC = AbysmRegistries.DENSITY_BLOB_TYPE.byNameCodec().dispatch(DensityBlob::getCodec, Function.identity());

	MapCodec<? extends DensityBlob> getCodec();

	double sampleDensity(int x, int y, int z);

	AABB getBoundingBox();
}
