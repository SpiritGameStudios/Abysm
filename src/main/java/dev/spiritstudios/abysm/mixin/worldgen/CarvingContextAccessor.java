package dev.spiritstudios.abysm.mixin.worldgen;

import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CarvingContext.class)
public interface CarvingContextAccessor {
	@Accessor
	NoiseChunk getNoiseChunk();
}
