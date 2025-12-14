package dev.spiritstudios.abysm.world.level.levelgen.densityfunction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.DensityFunction;

public class DensityFunctionWrapper implements DensityFunction.FunctionContext {

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

	public double sample(DensityFunction.FunctionContext pos) {
		return this.wrapped.compute(pos);
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
