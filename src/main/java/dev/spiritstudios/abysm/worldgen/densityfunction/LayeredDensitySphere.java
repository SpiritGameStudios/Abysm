package dev.spiritstudios.abysm.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.phys.AABB;

public class LayeredDensitySphere implements DensityBlob {
	// TODO consider changing how this works to something more general
	public static final MapCodec<LayeredDensitySphere> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
				Codec.DOUBLE.fieldOf("inner_radius").forGetter(LayeredDensitySphere::getInnerRadius),
				Codec.DOUBLE.fieldOf("outer_radius").forGetter(LayeredDensitySphere::getOuterRadius)
			)
			.apply(instance, LayeredDensitySphere::new)
	);
	public static final KeyDispatchDataCodec<LayeredDensitySphere> CODEC_HOLDER = KeyDispatchDataCodec.of(CODEC);

	@Override
	public MapCodec<? extends DensityBlob> getCodec() {
		return CODEC;
	}

	private final double innerRadius;
	private final double outerRadius;
	private final DensitySphere innerSphere;
	private final DensitySphere outerSphere;

	public LayeredDensitySphere(double innerRadius, double outerRadius) {
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		this.outerSphere = new DensitySphere(innerRadius, outerRadius, 1.0);
		this.innerSphere = new DensitySphere(0, innerRadius, innerRadius);
	}

	public double getInnerRadius() {
		return innerRadius;
	}

	public double getOuterRadius() {
		return outerRadius;
	}

	@Override
	public double sampleDensity(int x, int y, int z) {
		double distance = DensitySphere.getDistance(x, y, z);
		return this.innerSphere.getDensity(distance) + this.outerSphere.getDensity(distance);
	}

	@Override
	public AABB getBoundingBox() {
		return new AABB(-this.outerRadius, -this.outerRadius, -this.outerRadius, this.outerRadius, this.outerRadius, this.outerRadius);
	}
}
