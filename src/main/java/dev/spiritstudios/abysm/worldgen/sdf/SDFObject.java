package dev.spiritstudios.abysm.worldgen.sdf;

public interface SDFObject {
	double sampleSdf(double bx, double by, double bz);
	double sampleDensity(double bx, double by, double bz);
}
