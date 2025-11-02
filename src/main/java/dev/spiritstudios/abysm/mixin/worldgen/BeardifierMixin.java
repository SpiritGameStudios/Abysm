package dev.spiritstudios.abysm.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.spiritstudios.abysm.duck.StructureWeightSamplerDuckInterface;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensityBlobsSamplerCollection;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunctions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Beardifier.class)
public abstract class BeardifierMixin implements DensityFunctions.BeardifierOrMarker, StructureWeightSamplerDuckInterface {
	@Unique
	@Nullable
	private DensityBlobsSamplerCollection abysm$samplerCollection;

	@ModifyReturnValue(method = "forStructuresInChunk", at = @At("RETURN"))
	private static Beardifier adjustCreatedBeardifier(Beardifier original, StructureManager world, ChunkPos pos) {
		DensityBlobsSamplerCollection samplerCollection = DensityBlobsSamplerCollection.create(world, pos);

		// if collection contains any samplers, store it
		if (!samplerCollection.isEmpty()) {
			((BeardifierMixin) (Object) original).abysm$samplerCollection = samplerCollection;
		}

		return original;
	}

	@Override
	public @Nullable DensityBlobsSamplerCollection abysm$getSamplerCollection() {
		return this.abysm$samplerCollection;
	}
}
