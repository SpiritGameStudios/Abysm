package dev.spiritstudios.abysm.mixin.fluid;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.fluids.AbysmFluids;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WaterFluid.class)
public class WaterFluidMixin {

    @ModifyReturnValue(method = "canBeReplacedWith", at = @At("RETURN"))
    private boolean replaceWithBrine(boolean original, @Local(argsOnly = true) Fluid fluid) {
        return original || fluid.matchesType(AbysmFluids.BRINE);
    }

}
