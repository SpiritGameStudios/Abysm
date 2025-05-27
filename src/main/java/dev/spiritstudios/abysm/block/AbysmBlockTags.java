package dev.spiritstudios.abysm.block;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class AbysmBlockTags {
	public static final TagKey<Block> BLOOMSHROOM_PLANTABLE_ON = of("bloomshroom_plantable_on");

	private static TagKey<Block> of(String id) {
		return TagKey.of(RegistryKeys.BLOCK, Abysm.id(id));
	}
}
