package dev.spiritstudios.abysm.registry.tags;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class AbysmItemTags {
	public static final TagKey<Item> BLOOMED_FLOROPUMICE = of("bloomed_floropumice");
	public static final TagKey<Item> FLOWERY_SPRIGS = of("flowery_sprigs");
	public static final TagKey<Item> BLOOM_PETALS = of("bloom_petals");
	public static final TagKey<Item> SMALL_BLOOMSHROOMS = of("small_bloomshrooms");
	public static final TagKey<Item> BLOOMSHROOM_STEMS = of("bloomshroom_stems");
	public static final TagKey<Item> ROSY_BLOOMSHROOM_STEMS = of("rosy_bloomshroom_stems");
	public static final TagKey<Item> SUNNY_BLOOMSHROOM_STEMS = of("sunny_bloomshroom_stems");
	public static final TagKey<Item> MAUVE_BLOOMSHROOM_STEMS = of("mauve_bloomshroom_stems");
	public static final TagKey<Item> BLOOMSHROOM_CAPS = of("bloomshroom_caps");
	public static final TagKey<Item> BLOOMING_CROWNS = of("blooming_crowns");
	public static final TagKey<Item> SCABIOSAS = of("scabiosas");

	public static final TagKey<Item> HARPOON_ENCHANTABLE = of("enchantable/harpoon");

	private static TagKey<Item> of(String id) {
		return TagKey.of(RegistryKeys.ITEM, Abysm.id(id));
	}
}
