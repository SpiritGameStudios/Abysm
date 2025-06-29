package dev.spiritstudios.abysm.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.spiritstudios.abysm.duck.StructureWeightSamplerDuckInterface;
import dev.spiritstudios.abysm.registry.AbysmStructureTypes;
import dev.spiritstudios.abysm.structure.DeepSeaRuinsGenerator;
import dev.spiritstudios.abysm.worldgen.sdf.SDFObject;
import dev.spiritstudios.abysm.worldgen.sdf.ShellCaveSampler;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(StructureWeightSampler.class)
public abstract class StructureWeightSamplerMixin implements DensityFunctionTypes.Beardifying, StructureWeightSamplerDuckInterface {

	@Unique @Nullable
	ShellCaveSampler shellCaveSampler;

	@ModifyReturnValue(method = "createStructureWeightSampler", at = @At("RETURN"))
	private static StructureWeightSampler adjustCreatedSampler(StructureWeightSampler original, StructureAccessor world, ChunkPos pos) {
		List<SDFObject> sdfObjects = new ArrayList<>();
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
					SDFObject sphereObject = sphereCave.getSphereObject();
					sdfObjects.add(sphereObject);
				}
			}
		}));

		// if sdf objects isn't empty, store it
		if(!sdfObjects.isEmpty()) {
			StructureWeightSamplerMixin sampler = (StructureWeightSamplerMixin) (Object) original;
			sampler.shellCaveSampler = new ShellCaveSampler(sdfObjects);
		}

		return original;
	}

	@ModifyReturnValue(method = "sample", at = @At("RETURN"))
	private double adjustSampling(double original, DensityFunction.NoisePos pos) {
		// tweak the density used for beardifying (adjusting the density near structures) to account for the shell caves
		if(this.shellCaveSampler != null) {
			double density = this.shellCaveSampler.sampleDensity(pos.blockX(), pos.blockY(), pos.blockZ());

			original += density * density * density * density;
		}

		return original;
	}

	@Override
	public double abysm$sampleSDF(int x, int y, int z) {
		if(this.shellCaveSampler != null) {
			return this.shellCaveSampler.sampleSDF(x, y, z);
		} else {
			return 0.0;
		}
	}

	@Override
	public boolean abysm$shouldSampleSDF() {
		return this.shellCaveSampler != null;
	}
}
