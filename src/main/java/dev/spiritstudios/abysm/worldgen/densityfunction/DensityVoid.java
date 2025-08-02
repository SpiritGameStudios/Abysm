package dev.spiritstudios.abysm.worldgen.densityfunction;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.Box;

public class DensityVoid implements DensityBlob {
	public static final DensityVoid INSTANCE = new DensityVoid();
	public static final MapCodec<DensityVoid> CODEC = MapCodec.unit(INSTANCE);
	public static final CodecHolder<DensityVoid> CODEC_HOLDER = CodecHolder.of(CODEC);
	private static final Box BOX = new Box(0, 0, 0, 0, 0, 0);

	private DensityVoid() {
	}

	@Override
	public MapCodec<? extends DensityBlob> getCodec() {
		return CODEC;
	}

	@Override
	public double sampleDensity(int x, int y, int z) {
		return 0;
	}

	@Override
	public Box getBoundingBox() {
		return BOX;
	}
}
