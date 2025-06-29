package dev.spiritstudios.abysm.worldgen.sdf;

public class SDFSphere implements SDFObject {

	private double x;
	private double y;
	private double z;
	private final double radius;
	private final double outerRadius;
	private final double thickness;

	public SDFSphere(double x, double y, double z, double radius, double outerRadius) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
		this.outerRadius = outerRadius;
		this.thickness = this.outerRadius - this.radius;
	}

	public void translate(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	@Override
	public double sampleSdf(double bx, double by, double bz) {
		double dx = bx - x;
		double dy = by - y;
		double dz = bz - z;
		double distSqr = dx * dx + dy * dy + dz * dz;
		return Math.sqrt(distSqr) - this.radius;
	}

	@Override
	public double sampleDensity(double bx, double by, double bz) {
		// return 0 if outside outer radius, 1 if inside inner radius, and a smooth gradient between
		double innerSd = this.sampleSdf(bx, by, bz);
		if(innerSd <= 0.0) {
			return 1.0;
		} else {
			double outerSd = innerSd - this.thickness;
			if(outerSd >= 0.0) {
				return 0.0;
			} else {
				return 1.0 - (innerSd / this.thickness);
			}
		}
	}
}
