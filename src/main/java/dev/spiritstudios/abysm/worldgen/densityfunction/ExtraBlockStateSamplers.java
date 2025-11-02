package dev.spiritstudios.abysm.worldgen.densityfunction;

import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.worldgen.noise.NoiseConfigAttachment;
import java.util.List;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

public class ExtraBlockStateSamplers {

	public static void addSamplersToStart(
		List<NoiseChunk.BlockStateFiller> stateSamplerList,
		DensityFunction.Visitor getActualDensityFunction,
		int horizontalCellCount,
		RandomState noiseConfig,
		int startBlockX,
		int startBlockZ,
		NoiseSettings generationShapeConfig,
		DensityFunctions.BeardifierOrMarker beardifying,
		NoiseGeneratorSettings chunkGeneratorSettings,
		Aquifer.FluidPicker fluidLevelSampler,
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


	public static NoiseChunk.BlockStateFiller createBlockStateSampler(NoiseConfigAttachment noiseConfigAttachment, DensityFunction.Visitor getActualDensityFunction, DensityFunctions.BeardifierOrMarker beardifying, NoiseGeneratorSettings chunkGeneratorSettings) {
		// apply transformations
		NoiseConfigAttachment appliedNCA = noiseConfigAttachment.apply(getActualDensityFunction);

		DensityFunction ruinsSediment = appliedNCA.getRuinsSediment();
		DensityFunction ruinsCavePillars = appliedNCA.getRuinsShellCaveWithPillars();

		if (ruinsSediment == null || ruinsCavePillars == null) {
			return pos -> null;
		} else {
			BlockState water = Blocks.WATER.defaultBlockState();
			BlockState shell = AbysmBlocks.SMOOTH_FLOROPUMICE.defaultBlockState();
			BlockState sediment = chunkGeneratorSettings.defaultBlock(); // this gets replaced later by a material rule, so should be the default material

			return pos -> {
				double amountInsideShell = ruinsCavePillars.compute(pos) - 1.0;

				if (amountInsideShell <= 0.0) {
					return null;
				} else {
					double amountPastShell = amountInsideShell - 3.5;
					if (amountPastShell <= 0.0) {
						return shell;
					} else {
						double beard = beardifying.compute(pos);
						if (beard > 0.5) {
							return shell;
						} else {
							double sedimentDensity = ruinsSediment.compute(pos) + beard;
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
