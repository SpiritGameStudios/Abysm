package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.*;
import dev.spiritstudios.abysm.worldgen.feature.AbysmConfiguredFeatures;
import net.minecraft.block.*;
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

	// region floropumice blocks
	public static final Block FLOROPUMICE = register(
		"floropumice",
		FloropumiceBlock::new,
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.LIGHT_BLUE)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresTool()
			.strength(1.0F, 4.0F)
	);
	public static final Block FLOROPUMICE_STAIRS = registerStairsOf(
		"floropumice_stairs",
		FLOROPUMICE
	);
	public static final Block FLOROPUMICE_SLAB = registerSlabOf(
		"floropumice_slab",
		FLOROPUMICE
	);
	public static final Block FLOROPUMICE_WALL = registerWallOf(
		"floropumice_wall",
		FLOROPUMICE
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
	public static final Block FLOROPUMICE_BRICK_STAIRS = registerStairsOf(
		"floropumice_brick_stairs",
		FLOROPUMICE_BRICKS
	);
	public static final Block FLOROPUMICE_BRICK_SLAB = registerSlabOf(
		"floropumice_brick_slab",
		FLOROPUMICE_BRICKS
	);
	public static final Block FLOROPUMICE_BRICK_WALL = registerWallOf(
		"floropumice_brick_wall",
		FLOROPUMICE_BRICKS
	);

	public static final Block FLOROPUMICE_TILES = register(
		"floropumice_tiles",
		Block::new,
		AbstractBlock.Settings.copy(POLISHED_FLOROPUMICE)
	);
	public static final Block FLOROPUMICE_TILE_STAIRS = registerStairsOf(
		"floropumice_tile_stairs",
		FLOROPUMICE_TILES
	);
	public static final Block FLOROPUMICE_TILE_SLAB = registerSlabOf(
		"floropumice_tile_slab",
		FLOROPUMICE_TILES
	);
	public static final Block FLOROPUMICE_TILE_WALL = registerWallOf(
		"floropumice_tile_wall",
		FLOROPUMICE_TILES
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
	public static final Block SMOOTH_FLOROPUMICE_STAIRS = registerStairsOf(
		"smooth_floropumice_stairs",
		SMOOTH_FLOROPUMICE
	);
	public static final Block SMOOTH_FLOROPUMICE_SLAB = registerSlabOf(
		"smooth_floropumice_slab",
		SMOOTH_FLOROPUMICE
	);
	public static final Block SMOOTH_FLOROPUMICE_WALL = registerWallOf(
		"smooth_floropumice_wall",
		SMOOTH_FLOROPUMICE
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

	public static final Block SMOOTH_FLOROPUMICE_BRICKS = register(
		"smooth_floropumice_bricks",
		Block::new,
		AbstractBlock.Settings.copy(POLISHED_SMOOTH_FLOROPUMICE)
	);
	public static final Block SMOOTH_FLOROPUMICE_BRICK_STAIRS = registerStairsOf(
		"smooth_floropumice_brick_stairs",
		SMOOTH_FLOROPUMICE_BRICKS
	);
	public static final Block SMOOTH_FLOROPUMICE_BRICK_SLAB = registerSlabOf(
		"smooth_floropumice_brick_slab",
		SMOOTH_FLOROPUMICE_BRICKS
	);
	public static final Block SMOOTH_FLOROPUMICE_BRICK_WALL = registerWallOf(
		"smooth_floropumice_brick_wall",
		SMOOTH_FLOROPUMICE_BRICKS
	);

	public static final Block CUT_SMOOTH_FLOROPUMICE = register(
		"cut_smooth_floropumice",
		Block::new,
		AbstractBlock.Settings.copy(POLISHED_SMOOTH_FLOROPUMICE)
	);
	public static final Block CUT_SMOOTH_FLOROPUMICE_STAIRS = registerStairsOf(
		"cut_smooth_floropumice_stairs",
		CUT_SMOOTH_FLOROPUMICE
	);
	public static final Block CUT_SMOOTH_FLOROPUMICE_SLAB = registerSlabOf(
		"cut_smooth_floropumice_slab",
		CUT_SMOOTH_FLOROPUMICE
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
	// endregion floropumice

	// region bloomshroom blocks
	public static final Block ROSEBLOOMED_FLOROPUMICE = register(
		"rosebloomed_floropumice",
		BloomedFloropumiceBlock::new,
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.RED)
			.instrument(NoteBlockInstrument.BASS)
			.strength(1.2F, 4.5F)
			.requiresTool()
			.ticksRandomly()
	);
	public static final Block ROSY_BLOOMSHROOM = register(
		"rosy_bloomshroom",
		settings -> new SmallBloomshroomBlock(AbysmConfiguredFeatures.BLOOMSHROOM, settings),
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.RED)
			.sounds(BlockSoundGroup.GRASS)
			.pistonBehavior(PistonBehavior.DESTROY)
			.offset(AbstractBlock.OffsetType.XZ)
			.luminance(state -> 5)
			.breakInstantly()
			.noCollision()
	);
	public static final Block POTTED_ROSY_BLOOMSHROOM = register(
		"potted_rosy_bloomshroom",
		settings -> new FlowerPotBlock(ROSY_BLOOMSHROOM, settings),
		AbstractBlock.Settings.create()
			.pistonBehavior(PistonBehavior.DESTROY)
			.luminance(state -> 5)
			.breakInstantly()
			.nonOpaque(),
		false
	);
	public static final Block ROSY_BLOOMSHROOM_STEM = register(
		"rosy_bloomshroom_stem",
		PillarBlock::new,
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.RED)
			.instrument(NoteBlockInstrument.BASS)
			.sounds(BlockSoundGroup.WOOD)
			.strength(1.1F, 1.5F)
	);
	public static final Block ROSY_BLOOMSHROOM_CAP = register(
		"rosy_bloomshroom_cap",
		Block::new,
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.RED)
			.instrument(NoteBlockInstrument.BASS)
			.sounds(BlockSoundGroup.WOOD)
			.strength(0.7F, 0.9F)
	);
	public static final Block BLOOMSHROOM_GOOP = register(
		"bloomshroom_goop",
		WaterloggableTranslucentBlock::new,
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.PINK)
			.instrument(NoteBlockInstrument.BASS)
			.sounds(BlockSoundGroup.HONEY)
			.strength(0.7F, 1.1F)
			.luminance(state -> 11)
			.nonOpaque()
	);
	// endregion bloomshroom

	// region scabiosas
	public static final Block WHITE_SCABIOSA = register(
		"white_scabiosa",
		ScabiosaBlock::new,
		AbstractBlock.Settings.create()
			.mapColor(MapColor.TERRACOTTA_WHITE)
			.solid()
			.breakInstantly()
			.noCollision()
			.sounds(BlockSoundGroup.SPORE_BLOSSOM)
			.pistonBehavior(PistonBehavior.DESTROY)
	);

	public static final Block ORANGE_SCABIOSA = register(
		"orange_scabiosa",
		ScabiosaBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.ORANGE)
	);

	public static final Block PINK_SCABIOSA = register(
		"pink_scabiosa",
		ScabiosaBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.PINK)
	);

	public static final Block PURPLE_SCABIOSA = register(
		"purple_scabiosa",
		ScabiosaBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.PURPLE)
	);
	// endregion scabiosas

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

	@SuppressWarnings("deprecation")
	private static Block registerStairsOf(String id, Block block) {
		return register(id, settings -> new StairsBlock(block.getDefaultState(), settings), AbstractBlock.Settings.copyShallow(block));
	}

	@SuppressWarnings("deprecation")
	private static Block registerSlabOf(String id, Block block) {
		return register(id, SlabBlock::new, AbstractBlock.Settings.copyShallow(block));
	}

	@SuppressWarnings("deprecation")
	private static Block registerWallOf(String id, Block block) {
		return register(id, WallBlock::new, AbstractBlock.Settings.copyShallow(block).solid());
	}

	public static void init() {
		// NO-OP
	}
}
