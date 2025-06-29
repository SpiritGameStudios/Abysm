package dev.spiritstudios.abysm.mixin.worldgen;

import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CarverContext.class)
public interface CarverContextAccessor {
	@Accessor
	ChunkNoiseSampler getChunkNoiseSampler();
}
