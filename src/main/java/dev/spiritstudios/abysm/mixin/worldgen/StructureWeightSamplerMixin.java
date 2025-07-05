package dev.spiritstudios.abysm.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.spiritstudios.abysm.duck.StructureWeightSamplerDuckInterface;
import dev.spiritstudios.abysm.worldgen.densityfunction.ShellCaveSampler;
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

	@Unique @Nullable
	private ShellCaveSampler shellCaveSampler;

	@ModifyReturnValue(method = "createStructureWeightSampler", at = @At("RETURN"))
	private static StructureWeightSampler adjustCreatedSampler(StructureWeightSampler original, StructureAccessor world, ChunkPos pos) {
		ShellCaveSampler caveSampler = ShellCaveSampler.create(world, pos);

		// if sdf objects isn't empty, store it
		if(!caveSampler.isEmpty()) {
			StructureWeightSamplerMixin sampler = (StructureWeightSamplerMixin) (Object) original;
			sampler.shellCaveSampler = caveSampler;
		}

		return original;
	}

	@Override
	public @Nullable ShellCaveSampler abysm$getShellCaveSampler() {
		return this.shellCaveSampler;
	}
}
