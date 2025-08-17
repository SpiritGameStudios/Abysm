package dev.spiritstudios.abysm.mixin.worldgen;

import dev.spiritstudios.abysm.duck.CarverContextDuckInterface;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensityFunctionWrapper;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CarverContext.class)
public abstract class CarverContextMixin extends HeightContext implements CarverContextDuckInterface {
	private CarverContextMixin(ChunkGenerator generator, HeightLimitView world) {
		super(generator, world);
	}

	@Unique
	private DensityFunctionWrapper abysm$sampler;

	@Override
	public void abysm$setFunction(DensityFunctionWrapper sampler) {
		this.abysm$sampler = sampler;
	}

	@Override
	public @Nullable DensityFunctionWrapper abysm$getSampler() {
		return this.abysm$sampler;
	}
}
