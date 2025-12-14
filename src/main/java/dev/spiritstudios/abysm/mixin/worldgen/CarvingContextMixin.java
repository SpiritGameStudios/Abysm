package dev.spiritstudios.abysm.mixin.worldgen;

import dev.spiritstudios.abysm.duck.CarvingContextDuckInterface;
import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.DensityFunctionWrapper;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CarvingContext.class)
public abstract class CarvingContextMixin extends WorldGenerationContext implements CarvingContextDuckInterface {
	private CarvingContextMixin(ChunkGenerator generator, LevelHeightAccessor world) {
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
