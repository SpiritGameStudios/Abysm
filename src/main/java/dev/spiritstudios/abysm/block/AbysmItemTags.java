package dev.spiritstudios.abysm.block;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class AbysmItemTags {
	public static final TagKey<Item> ROSY_BLOOMSHROOM_STEMS = of("rosy_bloomshroom_stems");
	public static final TagKey<Item> SCABIOSAS = of("scabiosas");

	private static TagKey<Item> of(String id) {
		return TagKey.of(RegistryKeys.ITEM, Abysm.id(id));
	}
}
