package dev.spiritstudios.abysm.mixin.worldgen;

import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(Structure.class)
public interface StructureAccessor {

	@Invoker("getStructurePosition")
	Optional<Structure.StructurePosition> abysm$invokeGetStructurePosition(Structure.Context context);
}
