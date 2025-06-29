package dev.spiritstudios.abysm.mixin.worldgen;

import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.duck.StructureWeightSamplerDuckInterface;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChunkNoiseSampler.class)
public class ChunkNoiseSamplerMixin {

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
	private void addBlockStateSampler(
		int horizontalCellCount,
		NoiseConfig noiseConfig,
		int startBlockX,
		int startBlockZ,
		GenerationShapeConfig generationShapeConfig,
		DensityFunctionTypes.Beardifying beardifying,
		ChunkGeneratorSettings chunkGeneratorSettings,
		AquiferSampler.FluidLevelSampler fluidLevelSampler,
		Blender blender,
		CallbackInfo ci,
		@Local(ordinal = 0) List<ChunkNoiseSampler.BlockStateSampler> stateSamplerList)
	{
		if(beardifying instanceof StructureWeightSampler structureWeightSampler) {
			StructureWeightSamplerDuckInterface sampler = ((StructureWeightSamplerDuckInterface)structureWeightSampler);

			if(sampler.abysm$shouldSampleSDF()) {
				// add a block state sampler to the start of the list

				BlockState water = Blocks.WATER.getDefaultState();
				BlockState shell = AbysmBlocks.SMOOTH_FLOROPUMICE.getDefaultState();

				stateSamplerList.add(pos -> {
					double signedDistance = sampler.abysm$sampleSDF(pos.blockX(), pos.blockY(), pos.blockZ());

					if (signedDistance < -3.5) {
						return water;
					} else if (signedDistance < 0.0) {
						return shell;
					} else {
						return null;
					}
				});
			}
		}
	}
}
