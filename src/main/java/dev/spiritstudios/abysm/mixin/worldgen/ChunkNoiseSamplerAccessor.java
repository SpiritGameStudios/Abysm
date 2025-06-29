package dev.spiritstudios.abysm.mixin.worldgen;

import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkNoiseSampler.class)
public interface ChunkNoiseSamplerAccessor {
	@Accessor
	DensityFunctionTypes.Beardifying getBeardifying();
}
