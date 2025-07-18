package dev.spiritstudios.abysm.worldgen.densityfunction;

public class DensitySphere implements DensityBlob {

	private double x;
	private double y;
	private double z;
	private final double radius;
	private final double thickness;

	public DensitySphere(double x, double y, double z, double radius, double outerRadius) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
		this.thickness = outerRadius - this.radius;
	}

	public void translate(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public double sampleSdf(double bx, double by, double bz) {
		double dx = bx - x;
		double dy = by - y;
		double dz = bz - z;
		double distSqr = dx * dx + dy * dy + dz * dz;
		return Math.sqrt(distSqr) - this.radius;
	}

	@Override
	public double sampleDensity(int x, int y, int z) {
		// return 0 if outside outer radius, 1 if inside inner radius, and a smooth gradient between
		double innerSd = this.sampleSdf(x, y, z);
		if(innerSd <= 0.0) {
			// return greater than 1.0 further within inner radius
			return 1.0 - innerSd;
		} else {
			double outerSd = innerSd - this.thickness;
			if(outerSd >= 0.0) {
				// return 0.0 if outside outer radius
				return 0.0;
			} else {
				// smoothly transition between 0.0 on outer radius and 1.0 on inner radius
				return 1.0 - (innerSd / this.thickness);
			}
		}
	}
}
