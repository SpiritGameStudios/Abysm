package dev.spiritstudios.abysm.worldgen.densityfunction;

import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.worldgen.noise.NoiseConfigAttachment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.List;

public class ExtraBlockStateSamplers {

	public static void addSamplersToStart(
		List<ChunkNoiseSampler.BlockStateSampler> stateSamplerList,
		DensityFunction.DensityFunctionVisitor getActualDensityFunction,
		int horizontalCellCount,
		NoiseConfig noiseConfig,
		int startBlockX,
		int startBlockZ,
		GenerationShapeConfig generationShapeConfig,
		DensityFunctionTypes.Beardifying beardifying,
		ChunkGeneratorSettings chunkGeneratorSettings,
		AquiferSampler.FluidLevelSampler fluidLevelSampler,
		Blender blender
	) {
		DensityBlobsSamplerCollection samplerCollection = DensityBlobsSamplerCollection.get(beardifying);
		// only add block state sampler if there are density blobs to be sampling
		if (samplerCollection != null) {
			NoiseConfigAttachment noiseConfigAttachment = NoiseConfigAttachment.get(noiseConfig);
			// add a block state sampler to the start of the list
			stateSamplerList.add(ExtraBlockStateSamplers.createBlockStateSampler(noiseConfigAttachment, getActualDensityFunction, beardifying, chunkGeneratorSettings));
		}
	}


	public static ChunkNoiseSampler.BlockStateSampler createBlockStateSampler(NoiseConfigAttachment noiseConfigAttachment, DensityFunction.DensityFunctionVisitor getActualDensityFunction, DensityFunctionTypes.Beardifying beardifying, ChunkGeneratorSettings chunkGeneratorSettings) {
		// apply transformations
		NoiseConfigAttachment appliedNCA = noiseConfigAttachment.apply(getActualDensityFunction);

		DensityFunction ruinsSediment = appliedNCA.getRuinsSediment();
		DensityFunction ruinsCavePillars = appliedNCA.getRuinsShellCaveWithPillars();

		if (ruinsSediment == null || ruinsCavePillars == null) {
			return pos -> null;
		} else {
			BlockState water = Blocks.WATER.getDefaultState();
			BlockState shell = AbysmBlocks.SMOOTH_FLOROPUMICE.getDefaultState();
			BlockState sediment = chunkGeneratorSettings.defaultBlock(); // this gets replaced later by a material rule, so should be the default material

			return pos -> {
				double amountInsideShell = ruinsCavePillars.sample(pos) - 1.0;

				if (amountInsideShell <= 0.0) {
					return null;
				} else {
					double amountPastShell = amountInsideShell - 3.5;
					if (amountPastShell <= 0.0) {
						return shell;
					} else {
						double beard = beardifying.sample(pos);
						if (beard > 0.5) {
							return shell;
						} else {
							double sedimentDensity = ruinsSediment.sample(pos) + beard;
							if (sedimentDensity - amountPastShell * 0.005 > 0) {
								return sediment;
							} else {
								return water;
							}
						}
					}
				}
			};
		}
	}
}
