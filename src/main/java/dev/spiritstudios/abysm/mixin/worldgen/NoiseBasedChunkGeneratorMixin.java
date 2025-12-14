package dev.spiritstudios.abysm.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.duck.CarvingContextDuckInterface;
import dev.spiritstudios.abysm.duck.NoiseChunkDuckInterface;
import dev.spiritstudios.abysm.world.level.levelgen.biome.AbysmBiomes;
import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.DensityFunctionWrapper;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin {

	@Inject(method = "applyCarvers", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ProtoChunk;getOrCreateCarvingMask()Lnet/minecraft/world/level/chunk/CarvingMask;"))
	private void setupContext(
		WorldGenRegion chunkRegion,
		long seed,
		RandomState noiseConfig,
		BiomeManager biomeAccess,
		StructureManager structureAccessor,
		ChunkAccess chunk,
		CallbackInfo ci,
		@Local(ordinal = 0) CarvingContext context
	) {
		NoiseChunk chunkNoiseSampler = ((CarvingContextAccessor) context).getNoiseChunk();
		DensityFunction shellCaveFunction = ((NoiseChunkDuckInterface) chunkNoiseSampler).abysm$getShellCaveFunction(noiseConfig);
		if (shellCaveFunction != null) {
			// setup carver context for later, this means it will check the sampler to see if it should cancel
			((CarvingContextDuckInterface) context).abysm$setFunction(new DensityFunctionWrapper(shellCaveFunction));
		}
	}

	@ModifyExpressionValue(method = "doCreateBiomes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/BelowZeroRetrogen;getBiomeResolver(Lnet/minecraft/world/level/biome/BiomeResolver;Lnet/minecraft/world/level/chunk/ChunkAccess;)Lnet/minecraft/world/level/biome/BiomeResolver;"))
	private BiomeResolver modifyResolver(
		BiomeResolver original,
		Blender blender,
		RandomState noiseConfig,
		StructureManager structureAccessor,
		ChunkAccess chunk,
		@Local(ordinal = 0) NoiseChunk chunkNoiseSampler
	) {
		DensityFunction shellCaveFunction = ((NoiseChunkDuckInterface) chunkNoiseSampler).abysm$getShellCaveFunction(noiseConfig);
		if (shellCaveFunction != null) {
			// get target biome
			var deepSeaRuins = ((StructureManagerAccessor) structureAccessor).getLevel().registryAccess()
				.get(AbysmBiomes.DEEP_SEA_RUINS);

			if (deepSeaRuins.isPresent()) {
				Holder<Biome> biomeEntry = deepSeaRuins.get();

				// sample sdf, if inside shape then place target biome
				DensityFunctionWrapper wrapper = new DensityFunctionWrapper(shellCaveFunction);
				return ((biomeX, biomeY, biomeZ, noise) -> {
					double density = wrapper.sample(
						QuartPos.toBlock(biomeX) + 2,
						QuartPos.toBlock(biomeY) + 2,
						QuartPos.toBlock(biomeZ) + 2
					);
					if (density >= 1.5) { // shell starts at 1.0, but pick a higher threshold to avoid biome leaking out
						return biomeEntry;
					} else {
						return original.getNoiseBiome(biomeX, biomeY, biomeZ, noise);
					}
				});
			}
		}

		return original;
	}
}
