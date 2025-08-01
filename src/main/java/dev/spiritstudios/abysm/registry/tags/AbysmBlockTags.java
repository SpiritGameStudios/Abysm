package dev.spiritstudios.abysm.registry.tags;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class AbysmBlockTags {
	public static final TagKey<Block> BLOOMSHROOM_PLANTABLE_ON = of("bloomshroom_plantable_on");
	public static final TagKey<Block> OOZE_VEGETATION_PLANTABLE_ON = of("ooze_vegetation_plantable_on");
	// blocks that preserve leaves (like logs), but are not logs themselves. e.g. bloomshroom caps, stems, goop
	public static final TagKey<Block> ALSO_PRESERVES_LEAVES = of("also_preserves_leaves");
	public static final TagKey<Block> IS_AIR_OR_WATER = of("is_air_or_water");
	public static final TagKey<Block> OOZE_REPLACEABLE = of("ooze_replaceable");
	public static final TagKey<Block> BLOOMED_FLOROPUMICE_REPLACEABLE = of("bloomed_floropumice_replaceable");

	public static final TagKey<Block> BLOOMED_FLOROPUMICE = of("bloomed_floropumice");
	public static final TagKey<Block> FLOWERY_SPRIGS = of("flowery_sprigs");
	public static final TagKey<Block> BLOOM_PETALS = of("bloom_petals");
	public static final TagKey<Block> SMALL_BLOOMSHROOMS = of("small_bloomshrooms");
	public static final TagKey<Block> BLOOMSHROOM_STEMS = of("bloomshroom_stems");
	public static final TagKey<Block> ROSY_BLOOMSHROOM_STEMS = of("rosy_bloomshroom_stems");
	public static final TagKey<Block> SUNNY_BLOOMSHROOM_STEMS = of("sunny_bloomshroom_stems");
	public static final TagKey<Block> MAUVE_BLOOMSHROOM_STEMS = of("mauve_bloomshroom_stems");
	public static final TagKey<Block> BLOOMSHROOM_CAPS = of("bloomshroom_caps");
	public static final TagKey<Block> BLOOMING_CROWNS = of("blooming_crowns");
	public static final TagKey<Block> SCABIOSAS = of("scabiosas");

	public static final TagKey<Block> DEEP_SEA_RUINS_REPLACEABLE = of("deep_sea_ruins_replacable");

	private static TagKey<Block> of(String id) {
		return TagKey.of(RegistryKeys.BLOCK, Abysm.id(id));
	}
}
