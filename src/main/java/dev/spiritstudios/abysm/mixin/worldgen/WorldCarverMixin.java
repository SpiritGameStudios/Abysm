package dev.spiritstudios.abysm.mixin.worldgen;

import dev.spiritstudios.abysm.duck.CarvingContextDuckInterface;
import dev.spiritstudios.abysm.world.level.levelgen.densityfunction.DensityFunctionWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(WorldCarver.class)
public abstract class WorldCarverMixin<C extends CarverConfiguration> {
	@Inject(method = "carveBlock", at = @At("HEAD"), cancellable = true)
	private void blockCarving(CarvingContext context, C config, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> posToBiome, CarvingMask mask, BlockPos.MutableBlockPos pos, BlockPos.MutableBlockPos tmp, Aquifer aquiferSampler, MutableBoolean replacedGrassy, CallbackInfoReturnable<Boolean> cir) {
		DensityFunctionWrapper sampler = ((CarvingContextDuckInterface) context).abysm$getSampler();
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
