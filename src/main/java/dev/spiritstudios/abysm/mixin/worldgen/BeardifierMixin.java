package dev.spiritstudios.abysm.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.spiritstudios.abysm.duck.StructureWeightSamplerDuckInterface;
import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.DensityBlobsSamplerCollection;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunctions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(Beardifier.class)
public abstract class BeardifierMixin implements DensityFunctions.BeardifierOrMarker, StructureWeightSamplerDuckInterface {
	@Unique
	@Nullable
	private DensityBlobsSamplerCollection abysm$samplerCollection;

	@ModifyReturnValue(method = "forStructuresInChunk", at = @At("RETURN"))
	private static Beardifier adjustCreatedBeardifier(Beardifier original, StructureManager world, ChunkPos pos) {
		DensityBlobsSamplerCollection samplerCollection = DensityBlobsSamplerCollection.create(world, pos);

		Beardifier out = original;
		// if collection contains any samplers, store it
		if (!samplerCollection.isEmpty()) {
			if (out.equals(Beardifier.EMPTY)) {
				// if beardifier is the static EMPTY one then we need to create a non-static instance to avoid modifying and referencing EMPTY from multiple worldgen threads
				out = new Beardifier(List.of(), List.of(), null);
			}
			((BeardifierMixin) (Object) out).abysm$samplerCollection = samplerCollection;
		}

		return out;
	}

	@Override
	public @Nullable DensityBlobsSamplerCollection abysm$getSamplerCollection() {
		return this.abysm$samplerCollection;
	}
}
