package dev.spiritstudios.abysm.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.Box;

public record DensitySphere(double innerRadius, double outerRadius, double valueInsideShell) implements DensityBlob {
	public static final MapCodec<DensitySphere> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				Codec.DOUBLE.fieldOf("inner_radius").forGetter(DensitySphere::innerRadius),
				Codec.DOUBLE.fieldOf("outer_radius").forGetter(DensitySphere::outerRadius),
				Codec.DOUBLE.fieldOf("value_inside_shell").forGetter(DensitySphere::valueInsideShell)
			)
			.apply(instance, DensitySphere::new)
	);
	public static final CodecHolder<DensitySphere> CODEC_HOLDER = CodecHolder.of(CODEC);

	@Override
	public MapCodec<? extends DensityBlob> getCodec() {
		return CODEC;
	}

	public static double getDistSqr(double x, double y, double z) {
		// treat the sphere as being centered at (0, 0, 0)
		return x * x + y * y + z * z;
	}

	public static double getDistance(double x, double y, double z) {
		return Math.sqrt(getDistSqr(x, y, z));
	}

	public double getNormalisedShellDepth(double distance) {
		// returns 0.0 outside outerRadius, 1.0 inside innerRadius, and a linear value in (0.0,1.0) when between these values
		if (distance > this.outerRadius) {
			return 0.0;
		} else if (distance <= this.innerRadius) {
			return 1.0;
		} else {
			return (this.outerRadius - distance) / (this.outerRadius - this.innerRadius);
		}
	}

	public double getDensity(double distance) {
		return this.getNormalisedShellDepth(distance) * this.valueInsideShell;
	}

	@Override
	public double sampleDensity(int x, int y, int z) {
		return getDensity(getDistance(x, y, z));
	}

	@Override
	public Box getBoundingBox() {
		return new Box(-this.outerRadius, -this.outerRadius, -this.outerRadius, this.outerRadius, this.outerRadius, this.outerRadius);
	}
}
