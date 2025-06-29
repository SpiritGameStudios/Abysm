package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.worldgen.structure.DeepSeaRuinsStructure;
import net.minecraft.world.gen.structure.StructureType;

public class AbysmStructureTypes {
	public static final StructureType<DeepSeaRuinsStructure> DEEP_SEA_RUINS = () -> DeepSeaRuinsStructure.CODEC;
}
