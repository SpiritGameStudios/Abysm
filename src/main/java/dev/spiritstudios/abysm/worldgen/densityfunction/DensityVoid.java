package dev.spiritstudios.abysm.worldgen.densityfunction;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.phys.AABB;

public class DensityVoid implements DensityBlob {
	public static final DensityVoid INSTANCE = new DensityVoid();
	public static final MapCodec<DensityVoid> CODEC = MapCodec.unit(INSTANCE);
	public static final KeyDispatchDataCodec<DensityVoid> CODEC_HOLDER = KeyDispatchDataCodec.of(CODEC);
	private static final AABB BOX = new AABB(0, 0, 0, 0, 0, 0);

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
	public AABB getBoundingBox() {
		return BOX;
	}
}
