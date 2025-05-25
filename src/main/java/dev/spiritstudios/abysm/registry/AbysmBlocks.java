package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.ScabiosaBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;

import java.util.function.Function;

public final class AbysmBlocks {
	public static final Block FLOROPUMICE = register(
		"floropumice",
		Block::new,
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.LIGHT_BLUE)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresTool()
			.strength(1.0F, 4.0F)
	);

	public static final Block POLISHED_FLOROPUMICE = register(
		"polished_floropumice",
		Block::new,
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.LIGHT_BLUE)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresTool()
			.strength(1.5F, 6.0F)
	);

	public static final Block FLOROPUMICE_BRICKS = register(
		"floropumice_bricks",
		Block::new,
		AbstractBlock.Settings.copy(POLISHED_FLOROPUMICE)
	);

	public static final Block FLOROPUMICE_TILES = register(
		"floropumice_tiles",
		Block::new,
		AbstractBlock.Settings.copy(POLISHED_FLOROPUMICE)
	);

	public static final Block CHISLED_FLOROPUMICE = register(
		"chiseled_floropumice",
		Block::new,
		AbstractBlock.Settings.copy(POLISHED_FLOROPUMICE)
	);

	public static final Block SMOOTH_FLOROPUMICE = register(
		"smooth_floropumice",
		Block::new,
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.BLUE)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresTool()
			.strength(1.0F, 4.0F)
	);

	public static final Block POLISHED_SMOOTH_FLOROPUMICE = register(
		"polished_smooth_floropumice",
		Block::new,
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.BLUE)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresTool()
			.strength(1.5F, 6.0F)
	);

	public static final Block CUT_SMOOTH_FLOROPUMICE = register(
		"cut_smooth_floropumice",
		Block::new,
		AbstractBlock.Settings.copy(POLISHED_SMOOTH_FLOROPUMICE)
	);

	public static final Block SMOOTH_FLOROPUMICE_BRICKS = register(
		"smooth_floropumice_bricks",
		Block::new,
		AbstractBlock.Settings.copy(POLISHED_SMOOTH_FLOROPUMICE)
	);

	public static final Block CHISELED_SMOOTH_FLOROPUMICE = register(
		"chiseled_smooth_floropumice",
		Block::new,
		AbstractBlock.Settings.copy(POLISHED_SMOOTH_FLOROPUMICE)
	);

	public static final Block SMOOTH_FLOROPUMICE_PILLAR = register(
		"smooth_floropumice_pillar",
		PillarBlock::new,
		AbstractBlock.Settings.copy(POLISHED_SMOOTH_FLOROPUMICE)
	);

	public static final Block PURPLE_SCABIOSA = register(
		"purple_scabiosa",
		ScabiosaBlock::new,
		AbstractBlock.Settings.create()
			.mapColor(MapColor.PURPLE)
			.breakInstantly()
			.noCollision()
			.sounds(BlockSoundGroup.SPORE_BLOSSOM)
			.pistonBehavior(PistonBehavior.DESTROY)
	);

	public static <T extends Block> T register(RegistryKey<Block> key, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings, boolean item) {
		T block = factory.apply(settings.registryKey(key));

		if (item) {
			RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, key.getValue());
			BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey());
			Registry.register(
				Registries.ITEM,
				itemKey,
				blockItem
			);
			blockItem.appendBlocks(Item.BLOCK_ITEMS, blockItem);
		}

		return Registry.register(Registries.BLOCK, key, block);
	}

	private static <T extends Block> T register(String id, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings) {
		return register(keyOf(id), factory, settings, true);
	}

	private static <T extends Block> T register(String id, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings, boolean item) {
		return register(keyOf(id), factory, settings, item);
	}

	private static RegistryKey<Block> keyOf(String id) {
		return RegistryKey.of(RegistryKeys.BLOCK, Abysm.id(id));
	}

	public static void init() {
		// NO-OP
	}
}
