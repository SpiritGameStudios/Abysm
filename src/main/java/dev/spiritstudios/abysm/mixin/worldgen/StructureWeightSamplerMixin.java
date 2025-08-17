package dev.spiritstudios.abysm.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.spiritstudios.abysm.duck.StructureWeightSamplerDuckInterface;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensityBlobsSamplerCollection;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StructureWeightSampler.class)
public abstract class StructureWeightSamplerMixin implements DensityFunctionTypes.Beardifying, StructureWeightSamplerDuckInterface {

	@Unique
	@Nullable
	private DensityBlobsSamplerCollection abysm$samplerCollection;

	@ModifyReturnValue(method = "createStructureWeightSampler", at = @At("RETURN"))
	private static StructureWeightSampler adjustCreatedSampler(StructureWeightSampler original, StructureAccessor world, ChunkPos pos) {
		DensityBlobsSamplerCollection samplerCollection = DensityBlobsSamplerCollection.create(world, pos);

		// if collection contains any samplers, store it
		if (!samplerCollection.isEmpty()) {
			((StructureWeightSamplerMixin) (Object) original).abysm$samplerCollection = samplerCollection;
		}

		return original;
	}

	@Override
	public @Nullable DensityBlobsSamplerCollection abysm$getSamplerCollection() {
		return this.abysm$samplerCollection;
	}
}
