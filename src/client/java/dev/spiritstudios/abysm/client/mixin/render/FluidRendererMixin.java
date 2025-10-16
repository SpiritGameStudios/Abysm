package dev.spiritstudios.abysm.client.mixin.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.spiritstudios.abysm.fluids.AbysmFluids;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {

    @ModifyReturnValue(method = "isSameFluid", at = @At("RETURN"))
    private static boolean cullWaterNextToBrine(boolean original, FluidState a, FluidState b) {
        return original || a.getFluid().matchesType(Fluids.WATER) && b.getFluid().matchesType(AbysmFluids.BRINE);
    }

}