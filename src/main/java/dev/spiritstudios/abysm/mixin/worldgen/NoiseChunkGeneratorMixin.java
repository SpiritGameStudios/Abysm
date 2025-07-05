package dev.spiritstudios.abysm.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.duck.CarverContextDuckInterface;
import dev.spiritstudios.abysm.duck.ChunkNoiseSamplerDuckInterface;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensityFunctionWrapper;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSupplier;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(NoiseChunkGenerator.class)
public class NoiseChunkGeneratorMixin {

	@Inject(method = "carve", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ProtoChunk;getOrCreateCarvingMask()Lnet/minecraft/world/gen/carver/CarvingMask;"))
	private void setupContext(
		ChunkRegion chunkRegion,
		long seed,
		NoiseConfig noiseConfig,
		BiomeAccess biomeAccess,
		StructureAccessor structureAccessor,
		Chunk chunk,
		CallbackInfo ci,
		@Local(ordinal = 0)CarverContext context
		) {
		ChunkNoiseSampler chunkNoiseSampler = ((CarverContextAccessor) context).getChunkNoiseSampler();
		DensityFunction shellCaveFunction = ((ChunkNoiseSamplerDuckInterface)chunkNoiseSampler).abysm$getShellCaveFunction(noiseConfig);
		if (shellCaveFunction != null) {
			// setup carver context for later, this means it will check the sampler to see if it should cancel
			((CarverContextDuckInterface) context).abysm$setFunction(new DensityFunctionWrapper(shellCaveFunction));
		}
	}

	@ModifyExpressionValue(method = "populateBiomes(Lnet/minecraft/world/gen/chunk/Blender;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/chunk/Chunk;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/BelowZeroRetrogen;getBiomeSupplier(Lnet/minecraft/world/biome/source/BiomeSupplier;Lnet/minecraft/world/chunk/Chunk;)Lnet/minecraft/world/biome/source/BiomeSupplier;"))
	private BiomeSupplier modifySupplier(
		BiomeSupplier original,
		Blender blender,
		NoiseConfig noiseConfig,
		StructureAccessor structureAccessor,
		Chunk chunk,
		@Local(ordinal = 0) ChunkNoiseSampler chunkNoiseSampler
	) {
		DensityFunction shellCaveFunction = ((ChunkNoiseSamplerDuckInterface)chunkNoiseSampler).abysm$getShellCaveFunction(noiseConfig);
		if (shellCaveFunction != null) {
			// get target biome
			WorldAccess worldAccess = ((StructureAccessorAccessor)structureAccessor).getWorld();
			Registry<Biome> biomeRegistry = worldAccess.getRegistryManager().getOrThrow(RegistryKeys.BIOME);
			Optional<RegistryEntry.Reference<Biome>> biomeEntryOptional = biomeRegistry.getEntry(AbysmBiomes.DEEP_SEA_RUINS.getValue());
			if(biomeEntryOptional.isPresent()) {
				RegistryEntry<Biome> biomeEntry = biomeEntryOptional.get();

				// sample sdf, if inside shape then place target biome
				DensityFunctionWrapper wrapper = new DensityFunctionWrapper(shellCaveFunction);
				return ((biomeX, biomeY, biomeZ, noise) -> {
					double density = wrapper.sample(
						BiomeCoords.toBlock(biomeX) + 2,
						BiomeCoords.toBlock(biomeY) + 2,
						BiomeCoords.toBlock(biomeZ) + 2
					);
					if (density >= 1.0) {
						return biomeEntry;
					} else {
						return original.getBiome(biomeX, biomeY, biomeZ, noise);
					}
				});
			}
		}

		return original;
	}
}
