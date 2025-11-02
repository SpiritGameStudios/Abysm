package dev.spiritstudios.abysm.worldgen.densityfunction;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public class DensityBlobsSampler implements AbysmDensityFunctionTypes.DensityBlobsSamplerFunction {

	private final List<DensityBlob> densityBlobs;
	private final ResourceLocation identifier;

	public DensityBlobsSampler(ResourceLocation identifier) {
		this.densityBlobs = new ArrayList<>();
		this.identifier = identifier;
	}

	public void addBlob(DensityBlob blob) {
		this.densityBlobs.add(blob);
	}

	public boolean isEmpty() {
		return this.densityBlobs.isEmpty();
	}

	public double sampleDensity(int x, int y, int z) {
		// sample a density from the objects. when far from objects, this will be 0.0
		if (this.densityBlobs.isEmpty()) {
			return 0.0;
		}

		double totalDensity = 0.0;
		for (DensityBlob object : this.densityBlobs) {
			totalDensity += object.sampleDensity(x, y, z);
		}
		return totalDensity;
	}

	// TODO consider optimising sample and/or fill if needed
	@Override
	public double compute(FunctionContext pos) {
		return sampleDensity(pos.blockX(), pos.blockY(), pos.blockZ());
	}

	@Override
	public double minValue() {
		return Double.MIN_VALUE;
	}

	@Override
	public double maxValue() {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public ResourceLocation identifier() {
		return this.identifier;
	}
}
