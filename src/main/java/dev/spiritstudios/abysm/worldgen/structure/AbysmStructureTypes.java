package dev.spiritstudios.abysm.worldgen.structure;

import dev.spiritstudios.abysm.worldgen.structure.ruins.DeepSeaRuinsStructure;
import net.minecraft.world.gen.structure.StructureType;

public class AbysmStructureTypes {
	public static final StructureType<DeepSeaRuinsStructure> DEEP_SEA_RUINS = () -> DeepSeaRuinsStructure.CODEC;
}
