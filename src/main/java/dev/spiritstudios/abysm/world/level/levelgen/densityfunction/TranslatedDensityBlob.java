package dev.spiritstudios.abysm.world.level.levelgen.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.phys.AABB;

public record TranslatedDensityBlob(DensityBlob densityBlob, int dx, int dy, int dz) implements DensityBlob {
	public static final MapCodec<TranslatedDensityBlob> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				DensityBlob.CODEC.fieldOf("blob").forGetter(TranslatedDensityBlob::densityBlob),
				Codec.INT.fieldOf("dx").forGetter(TranslatedDensityBlob::dx),
				Codec.INT.fieldOf("dy").forGetter(TranslatedDensityBlob::dy),
				Codec.INT.fieldOf("dz").forGetter(TranslatedDensityBlob::dz)
			)
			.apply(instance, TranslatedDensityBlob::new)
	);
	public static final KeyDispatchDataCodec<TranslatedDensityBlob> CODEC_HOLDER = KeyDispatchDataCodec.of(CODEC);

	@Override
	public MapCodec<? extends DensityBlob> getCodec() {
		return CODEC;
	}

	@Override
	public double sampleDensity(int x, int y, int z) {
		return this.densityBlob.sampleDensity(x - this.dx, y - this.dy, z - this.dz);
	}

	@Override
	public AABB getBoundingBox() {
		return this.densityBlob.getBoundingBox().move(this.dx, this.dy, this.dz);
	}
}
