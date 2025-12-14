package dev.spiritstudios.abysm.mixin.worldgen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.duck.NoiseChunkDuckInterface;
import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.AbysmDensityFunctionTypes;
import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.DensityBlobsSamplerCollection;
import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.ExtraBlockStateSamplers;
import dev.spiritstudios.abysm.world.level.levelgen.noise.NoiseConfigAttachment;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(NoiseChunk.class)
public abstract class NoiseMixin implements NoiseChunkDuckInterface {

	@Shadow
	@Final
	private DensityFunctions.BeardifierOrMarker beardifier;

	@Shadow
	protected abstract DensityFunction wrap(DensityFunction function);

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
	private void addBlockStateSampler(
		int horizontalCellCount,
		RandomState noiseConfig,
		int startBlockX,
		int startBlockZ,
		NoiseSettings generationShapeConfig,
		DensityFunctions.BeardifierOrMarker beardifying,
		NoiseGeneratorSettings chunkGeneratorSettings,
		Aquifer.FluidPicker fluidLevelSampler,
		Blender blender,
		CallbackInfo ci,
		@Local(ordinal = 0) List<NoiseChunk.BlockStateFiller> stateSamplerList
	) {
		ExtraBlockStateSamplers.addSamplersToStart(
			stateSamplerList,
			this::wrap,
			horizontalCellCount,
			noiseConfig,
			startBlockX,
			startBlockZ,
			generationShapeConfig,
			beardifying,
			chunkGeneratorSettings,
			fluidLevelSampler,
			blender
		);
	}

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/DensityFunctions;add(Lnet/minecraft/world/level/levelgen/DensityFunction;Lnet/minecraft/world/level/levelgen/DensityFunction;)Lnet/minecraft/world/level/levelgen/DensityFunction;", ordinal = 0))
	private DensityFunction addBeardifierAddition(
		DensityFunction a,
		DensityFunction b,
		Operation<DensityFunction> original,
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
		DensityFunction ogFunction = original.call(a, b);

		NoiseConfigAttachment noiseConfigAttachment = NoiseConfigAttachment.get(noiseConfig);
		DensityFunction function = noiseConfigAttachment.getBeardifierAddition();
		if (function == null) {
			return ogFunction;
		} else {
			return DensityFunctions.add(ogFunction, function);
		}
	}

	@Inject(method = "wrapNew", at = @At("HEAD"), cancellable = true)
	private void getDensityBlobsSamplerFunction(DensityFunction function, CallbackInfoReturnable<DensityFunction> cir) {
		// replace the dummy shell cave function with its actual implementation
		if (function instanceof AbysmDensityFunctionTypes.DummyDensityBlobsSampler(Identifier identifier)) {
			DensityBlobsSamplerCollection sampler = DensityBlobsSamplerCollection.get(this.beardifier);
			if (sampler != null) {
				DensityFunction samplerFunction = sampler.getDensityFunction(identifier);
				if (samplerFunction != null) {
					cir.setReturnValue(samplerFunction);
				}
			}
		}
	}

	@Override
	public @Nullable DensityFunction abysm$getShellCaveFunction(RandomState noiseConfig) {
		// get the abysm:ruins_shell_cave function, used to change biome and block carvers
		NoiseConfigAttachment noiseConfigAttachment = NoiseConfigAttachment.get(noiseConfig);
		DensityFunction densityFunction = noiseConfigAttachment.getRuinsShellCave();
		if (densityFunction == null) {
			return null;
		} else {
			return densityFunction.mapAll(this::wrap);
		}
	}
}
