package dev.spiritstudios.abysm.registry.tags;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

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
	public static final TagKey<Item> NECTARSAP = of("nectarsap");
	public static final TagKey<Item> BLOOMING_CROWNS = of("blooming_crowns");
	public static final TagKey<Item> SCABIOSAS = of("scabiosas");

	public static final TagKey<Item> HARPOON_ENCHANTABLE = of("enchantable/harpoon");

	public static final TagKey<Item> DIVING_SUIT_HELMETS = of("diving_suit/helmets");
	public static final TagKey<Item> DIVING_SUIT_CHESTPLATES = of("diving_suit/chestplates");
	public static final TagKey<Item> DIVING_SUIT_LEGGINGS = of("diving_suit/leggings");
	public static final TagKey<Item> DIVING_SUIT_BOOTS = of("diving_suit/boots");

	private static TagKey<Item> of(String id) {
		return TagKey.create(Registries.ITEM, Abysm.id(id));
	}
}
