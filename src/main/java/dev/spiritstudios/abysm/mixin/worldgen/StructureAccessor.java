package dev.spiritstudios.abysm.mixin.worldgen;

import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(Structure.class)
public interface StructureAccessor {
	@Invoker
	Optional<Structure.GenerationStub> invokeFindGenerationPoint(Structure.GenerationContext context);
}
