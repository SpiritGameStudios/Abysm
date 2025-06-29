package dev.spiritstudios.abysm.worldgen.sdf;

import java.util.List;

public class ShellCaveSampler {

	List<SDFObject> sdfObjects;

	public ShellCaveSampler(List<SDFObject> boxes) {
		this.sdfObjects = boxes;
	}

	public double sampleSDF(int x, int y, int z) {
		// sample the sdf to nearby objects. note this only counts nearby objects, so any areas of positive distance may abruptly change to infinity
		if(this.sdfObjects.isEmpty()) {
			return Double.POSITIVE_INFINITY;
		}

		double minSignedDistance = Double.POSITIVE_INFINITY;
		for(SDFObject object : this.sdfObjects) {
			double signedDistance = object.sampleSdf(x, y, z);
			if(signedDistance < minSignedDistance) {
				minSignedDistance = signedDistance;
			}
		}
		return minSignedDistance;
	}

	public double sampleDensity(int x, int y, int z) {
		// sample a density from the objects. when far from objects, this will be 0.0
		if(this.sdfObjects.isEmpty()) {
			return 0.0;
		}

		double maxDensity = 0.0;
		for(SDFObject object : this.sdfObjects) {
			double density = object.sampleDensity(x, y, z);
			if(density > maxDensity) {
				maxDensity = density;
			}
		}
		return maxDensity;
	}
}
