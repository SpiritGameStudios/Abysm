package dev.spiritstudios.abysm.mixin.worldgen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.duck.ChunkNoiseSamplerDuckInterface;
import dev.spiritstudios.abysm.worldgen.densityfunction.AbysmDensityFunctionTypes;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensityBlobsSamplerCollection;
import dev.spiritstudios.abysm.worldgen.densityfunction.ExtraBlockStateSamplers;
import dev.spiritstudios.abysm.worldgen.noise.NoiseConfigAttachment;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ChunkNoiseSampler.class)
public abstract class ChunkNoiseSamplerMixin implements ChunkNoiseSamplerDuckInterface {

	@Shadow
	@Final
	private DensityFunctionTypes.Beardifying beardifying;

	@Shadow
	protected abstract DensityFunction getActualDensityFunction(DensityFunction function);

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
	private void addBlockStateSampler(
		int horizontalCellCount,
		NoiseConfig noiseConfig,
		int startBlockX,
		int startBlockZ,
		GenerationShapeConfig generationShapeConfig,
		DensityFunctionTypes.Beardifying beardifying,
		ChunkGeneratorSettings chunkGeneratorSettings,
		AquiferSampler.FluidLevelSampler fluidLevelSampler,
		Blender blender,
		CallbackInfo ci,
		@Local(ordinal = 0) List<ChunkNoiseSampler.BlockStateSampler> stateSamplerList
	) {
		ExtraBlockStateSamplers.addSamplersToStart(
			stateSamplerList,
			this::getActualDensityFunction,
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

	@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/densityfunction/DensityFunctionTypes;add(Lnet/minecraft/world/gen/densityfunction/DensityFunction;Lnet/minecraft/world/gen/densityfunction/DensityFunction;)Lnet/minecraft/world/gen/densityfunction/DensityFunction;", ordinal = 0))
	private DensityFunction addBeardifierAddition(
		DensityFunction a,
		DensityFunction b,
		Operation<DensityFunction> original,
		int horizontalCellCount,
		NoiseConfig noiseConfig,
		int startBlockX,
		int startBlockZ,
		GenerationShapeConfig generationShapeConfig,
		DensityFunctionTypes.Beardifying beardifying,
		ChunkGeneratorSettings chunkGeneratorSettings,
		AquiferSampler.FluidLevelSampler fluidLevelSampler,
		Blender blender
	) {
		DensityFunction ogFunction = original.call(a, b);

		NoiseConfigAttachment noiseConfigAttachment = NoiseConfigAttachment.get(noiseConfig);
		DensityFunction function = noiseConfigAttachment.getBeardifierAddition();
		if (function == null) {
			return ogFunction;
		} else {
			return DensityFunctionTypes.add(ogFunction, function);
		}
	}

	@Inject(method = "getActualDensityFunctionImpl", at = @At("HEAD"), cancellable = true)
	private void getDensityBlobsSamplerFunction(DensityFunction function, CallbackInfoReturnable<DensityFunction> cir) {
		// replace the dummy shell cave function with its actual implementation
		if (function instanceof AbysmDensityFunctionTypes.DummyDensityBlobsSampler dummy) {
			Identifier identifier = dummy.getIdentifier();
			DensityBlobsSamplerCollection sampler = DensityBlobsSamplerCollection.get(this.beardifying);
			if (sampler != null) {
				DensityFunction samplerFunction = sampler.getDensityFunction(identifier);
				if (samplerFunction != null) {
					cir.setReturnValue(samplerFunction);
				}
			}
		}
	}

	@Override
	public @Nullable DensityFunction abysm$getShellCaveFunction(NoiseConfig noiseConfig) {
		// get the abysm:ruins_shell_cave function, used to change biome and block carvers
		NoiseConfigAttachment noiseConfigAttachment = NoiseConfigAttachment.get(noiseConfig);
		DensityFunction densityFunction = noiseConfigAttachment.getRuinsShellCave();
		if (densityFunction == null) {
			return null;
		} else {
			return densityFunction.apply(this::getActualDensityFunction);
		}
	}
}
