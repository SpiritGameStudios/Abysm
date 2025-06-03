package dev.spiritstudios.abysm.block;

import dev.spiritstudios.abysm.Abysm;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class AbysmBlockTags {
	public static final TagKey<Block> BLOOMSHROOM_PLANTABLE_ON = of("bloomshroom_plantable_on");
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

	private static TagKey<Block> of(String id) {
		return TagKey.of(RegistryKeys.BLOCK, Abysm.id(id));
	}
}
