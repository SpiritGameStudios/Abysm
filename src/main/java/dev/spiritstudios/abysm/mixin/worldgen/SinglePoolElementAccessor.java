package dev.spiritstudios.abysm.mixin.worldgen;

import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.pool.SinglePoolElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SinglePoolElement.class)
public interface SinglePoolElementAccessor {
	@Invoker("getStructure")
	StructureTemplate abysm$invokeGetStructure(StructureTemplateManager structureTemplateManager);
}
