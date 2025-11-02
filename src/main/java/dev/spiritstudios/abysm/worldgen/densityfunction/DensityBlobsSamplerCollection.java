package dev.spiritstudios.abysm.worldgen.densityfunction;

import dev.spiritstudios.abysm.duck.StructureWeightSamplerDuckInterface;
import dev.spiritstudios.abysm.worldgen.structure.AbysmStructureTypes;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;

public class DensityBlobsSamplerCollection {

	private final Map<ResourceLocation, DensityBlobsSampler> samplerMap;

	public DensityBlobsSamplerCollection(Map<ResourceLocation, DensityBlobsSampler> samplerMap) {
		this.samplerMap = samplerMap;
	}

	public boolean isEmpty() {
		return this.samplerMap.isEmpty();
	}

	@Nullable
	public DensityFunction getDensityFunction(ResourceLocation identifier) {
		return this.samplerMap.getOrDefault(identifier, null);
	}

	public static DensityBlobsSamplerCollection create(StructureManager world, ChunkPos chunkPos) {
		Map<ResourceLocation, DensityBlobsSampler> samplerMap = new HashMap<>();

		world.startsForStructure(
			chunkPos,
			// filter for deep sea ruins
			structure -> structure.type().equals(AbysmStructureTypes.DEEP_SEA_RUINS)
		).forEach(structureStart -> structureStart.getPieces().forEach(piece -> {
			// check piece contains a density blob and affects this chunk
			if (piece instanceof DensityBlobHolder densityBlobHolder) {
				if (piece.getBoundingBox().intersects(
					chunkPos.getMinBlockX(),
					chunkPos.getMinBlockZ(),
					chunkPos.getMaxBlockX(),
					chunkPos.getMaxBlockZ()
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
	public static DensityBlobsSamplerCollection get(DensityFunctions.BeardifierOrMarker beardifying) {
		if (beardifying instanceof StructureWeightSamplerDuckInterface duck) {
			return duck.abysm$getSamplerCollection();
		} else {
			return null;
		}
	}
}
