package dev.spiritstudios.abysm.worldgen.densityfunction;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.densityfunction.DensityFunction;

public class DensityFunctionWrapper implements DensityFunction.NoisePos {

	private final DensityFunction wrapped;
	private int x = 0;
	private int y = 0;
	private int z = 0;

	public DensityFunctionWrapper(DensityFunction wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public int blockX() {
		return this.x;
	}

	@Override
	public int blockY() {
		return this.y;
	}

	@Override
	public int blockZ() {
		return this.z;
	}

	public double sample(DensityFunction.NoisePos pos) {
		return this.wrapped.sample(pos);
	}

	public double sample() {
		return this.sample(this);
	}

	public double sample(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this.sample();
	}

	public double sample(BlockPos pos) {
		return this.sample(pos.getX(), pos.getY(), pos.getZ());
	}

	public DensityFunction getFunction() {
		return wrapped;
	}
}
