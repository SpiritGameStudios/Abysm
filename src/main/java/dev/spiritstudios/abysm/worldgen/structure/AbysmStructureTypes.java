package dev.spiritstudios.abysm.worldgen.structure;

import dev.spiritstudios.abysm.worldgen.structure.ruins.DeepSeaRuinsStructure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class AbysmStructureTypes {
	/**
	 * Note: all entries are automatically registered in {@link dev.spiritstudios.abysm.Abysm}
	 */

	public static final StructureType<DeepSeaRuinsStructure> DEEP_SEA_RUINS = () -> DeepSeaRuinsStructure.CODEC;
}
