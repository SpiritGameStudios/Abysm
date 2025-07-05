package dev.spiritstudios.abysm.worldgen.densityfunction;

import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.duck.StructureWeightSamplerDuckInterface;
import dev.spiritstudios.abysm.structure.DeepSeaRuinsGenerator;
import dev.spiritstudios.abysm.worldgen.noise.NoiseConfigAttachment;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructureTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ShellCaveSampler implements AbysmDensityFunctionTypes.ShellCave, DensityBlob {
	private final List<DensityBlob> densityBlobs;

	public static ShellCaveSampler create(StructureAccessor world, ChunkPos pos) {
		List<DensityBlob> densityBlobs = new ArrayList<>();
		// filter for deep sea ruins
		world.getStructureStarts(
			pos,
			structure -> structure.getType().equals(AbysmStructureTypes.DEEP_SEA_RUINS)
		).forEach(structureStart -> structureStart.getChildren().forEach(piece -> {
			// get each relevant piece
			if (piece instanceof DeepSeaRuinsGenerator.SphereCave sphereCave) {
				if(piece.getBoundingBox().intersectsXZ(
					pos.getStartX(),
					pos.getStartZ(),
					pos.getEndX(),
					pos.getEndZ()
				)) {
					// add piece's sdf objects to list
					DensityBlob sphereObject = sphereCave.getSphereObject();
					densityBlobs.add(sphereObject);
				}
			}
		}));

		return new ShellCaveSampler(densityBlobs);
	}

	public ShellCaveSampler(List<DensityBlob> boxes) {
		this.densityBlobs = boxes;
	}

	@Override
	public double sample(NoisePos pos) {
		return sampleDensity(pos.blockX(), pos.blockY(), pos.blockZ());
	}

	@Override
	public double sampleDensity(int x, int y, int z) {
		// sample a density from the objects. when far from objects, this will be 0.0
		if(this.densityBlobs.isEmpty()) {
			return 0.0;
		}

		double totalDensity = 0.0;
		for(DensityBlob object : this.densityBlobs) {
			totalDensity += object.sampleDensity(x, y, z);
		}
		return totalDensity;
	}

	@Override
	public double minValue() {
		return 0.0;
	}

	@Override
	public double maxValue() {
		return Double.POSITIVE_INFINITY;
	}

	public boolean isEmpty() {
		return this.densityBlobs.isEmpty();
	}

	@Nullable
	public static ShellCaveSampler get(DensityFunctionTypes.Beardifying structureWeightSampler) {
		if(structureWeightSampler instanceof StructureWeightSamplerDuckInterface duck) {
			return duck.abysm$getShellCaveSampler();
		} else {
			return null;
		}
	}

	public static ChunkNoiseSampler.BlockStateSampler createBlockStateSampler(NoiseConfigAttachment noiseConfigAttachment, DensityFunction.DensityFunctionVisitor getActualDensityFunction, DensityFunctionTypes.Beardifying beardifying) {
		// apply transformations
		NoiseConfigAttachment appliedNCA = noiseConfigAttachment.apply(getActualDensityFunction);

		DensityFunction ruinsSediment = appliedNCA.getRuinsSedimentNoise();
		DensityFunction ruinsCavePillars = appliedNCA.getRuinsShellCaveWithPillars();

		if(ruinsSediment == null || ruinsCavePillars == null) {
			return pos -> null;
		} else {
			BlockState water = Blocks.WATER.getDefaultState();
			BlockState shell = AbysmBlocks.SMOOTH_FLOROPUMICE.getDefaultState();
			BlockState inner = Blocks.CLAY.getDefaultState();

			return pos -> {
				double amountInsideShell = ruinsCavePillars.sample(pos) - 1.0;

				if(amountInsideShell <= 0.0) {
					return null;
				} else {
					double amountPastShell = amountInsideShell - 3.5;
					if(amountPastShell <= 0.0) {
						return shell;
					} else {
						double beard = beardifying.sample(pos);
						if(beard > 0.5) {
							return shell;
						} else {
							double d = ruinsSediment.sample(pos) + beard;
							if (d > amountPastShell * 0.005 + 0.18) {
								return inner;
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
