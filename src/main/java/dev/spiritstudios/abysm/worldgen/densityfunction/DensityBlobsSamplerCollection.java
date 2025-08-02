package dev.spiritstudios.abysm.worldgen.densityfunction;

import dev.spiritstudios.abysm.duck.StructureWeightSamplerDuckInterface;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructureTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DensityBlobsSamplerCollection {

	private final Map<Identifier, DensityBlobsSampler> samplerMap;

	public DensityBlobsSamplerCollection(Map<Identifier, DensityBlobsSampler> samplerMap) {
		this.samplerMap = samplerMap;
	}

	public boolean isEmpty() {
		return this.samplerMap.isEmpty();
	}

	@Nullable
	public DensityFunction getDensityFunction(Identifier identifier) {
		return this.samplerMap.getOrDefault(identifier, null);
	}

	public static DensityBlobsSamplerCollection create(StructureAccessor world, ChunkPos chunkPos) {
		Map<Identifier, DensityBlobsSampler> samplerMap = new HashMap<>();

		world.getStructureStarts(
			chunkPos,
			// filter for deep sea ruins
			structure -> structure.getType().equals(AbysmStructureTypes.DEEP_SEA_RUINS)
		).forEach(structureStart -> structureStart.getChildren().forEach(piece -> {
			// check piece contains a density blob and affects this chunk
			if (piece instanceof DensityBlobHolder densityBlobHolder) {
				if (piece.getBoundingBox().intersectsXZ(
					chunkPos.getStartX(),
					chunkPos.getStartZ(),
					chunkPos.getEndX(),
					chunkPos.getEndZ()
				)) {
					// add blob to sampler
					DensityBlobsSampler blobsSampler = samplerMap.computeIfAbsent(densityBlobHolder.getIdentifier(), DensityBlobsSampler::new);
					blobsSampler.addBlob(densityBlobHolder.getDensityBlob());
				}
			}
		}));

		return new DensityBlobsSamplerCollection(samplerMap);
	}

	@Nullable
	public static DensityBlobsSamplerCollection get(DensityFunctionTypes.Beardifying beardifying) {
		if (beardifying instanceof StructureWeightSamplerDuckInterface duck) {
			return duck.abysm$getSamplerCollection();
		} else {
			return null;
		}
	}
}
