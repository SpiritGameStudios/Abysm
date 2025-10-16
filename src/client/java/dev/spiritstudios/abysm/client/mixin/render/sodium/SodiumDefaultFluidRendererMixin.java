package dev.spiritstudios.abysm.client.mixin.render.sodium;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.spiritstudios.abysm.fluids.AbysmFluids;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.DefaultFluidRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = DefaultFluidRenderer.class, remap = false)
public class SodiumDefaultFluidRendererMixin {

    @ModifyReturnValue(method = "isFullBlockFluidOccluded", at = @At("RETURN"), remap = false)
    private boolean cullWaterNextToBrine(boolean original, BlockRenderView world, BlockPos pos, Direction dir, BlockState blockState, FluidState fluid) {
        return original || fluid.getFluid().matchesType(Fluids.WATER) && world.getFluidState(pos.offset(dir)).getFluid().matchesType(AbysmFluids.BRINE);
    }

}
