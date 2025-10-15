package dev.spiritstudios.abysm.fluids;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AbysmFluids {

    public static final FlowableFluid FLOWING_BRINE = register("flowing_brine", new BrineFluid.Flowing());
    public static final FlowableFluid BRINE = register("brine", new BrineFluid.Still());

    private static <T extends Fluid> T register(String path, T value) {
        return Registry.register(Registries.FLUID, Abysm.id(path), value);
    }

    public static void init() {
        // NO-OP
    }

}
