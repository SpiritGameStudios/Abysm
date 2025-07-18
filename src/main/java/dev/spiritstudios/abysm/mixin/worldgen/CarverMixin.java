package dev.spiritstudios.abysm.mixin.worldgen;

import dev.spiritstudios.abysm.duck.CarverContextDuckInterface;
import dev.spiritstudios.abysm.worldgen.densityfunction.DensityFunctionWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CarvingMask;
import net.minecraft.world.gen.chunk.AquiferSampler;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(Carver.class)
public abstract class CarverMixin<C extends CarverConfig> {

	@Inject(method = "carveAtPoint", at = @At("HEAD"), cancellable = true)
	private void blockCarving(CarverContext context, C config, Chunk chunk, Function<BlockPos, RegistryEntry<Biome>> posToBiome, CarvingMask mask, BlockPos.Mutable pos, BlockPos.Mutable tmp, AquiferSampler aquiferSampler, MutableBoolean replacedGrassy, CallbackInfoReturnable<Boolean> cir) {
		DensityFunctionWrapper sampler = ((CarverContextDuckInterface) context).abysm$getSampler();
		// only sample if needed (i.e. if the sampler has been set for this chunk's CarverContext)
		if (sampler != null) {
			double density = sampler.sample(pos);

			// do not carve inside of shell caves
			if (density >= 1.0) {
				cir.setReturnValue(false);

				// DEBUG: place glass
				//chunk.setBlockState(pos, Blocks.RED_STAINED_GLASS.getDefaultState());
			}
		}
	}
}
