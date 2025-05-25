package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.ScabiosaBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
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

public class AbysmBlocks {
	public static final Block FLOROPUMICE = register(
		"floropumice",
		Block::new,
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.LIGHT_BLUE)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresTool()
			.strength(1.5F, 6.0F)
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
