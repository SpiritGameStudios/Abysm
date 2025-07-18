package dev.spiritstudios.abysm.block;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.item.PlaceableOnWaterOrBlockItem;
import dev.spiritstudios.abysm.particle.AbysmParticleTypes;
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
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
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
		settings -> new BloomedFloropumiceBlock(AbysmConfiguredFeatures.ROSY_BLOOMSHROOM_VEGETATION_BONEMEAL, settings),
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.RED)
			.instrument(NoteBlockInstrument.BASS)
			.strength(1.2F, 4.5F)
			.requiresTool()
			.ticksRandomly()
	);
	public static final Block SUNBLOOMED_FLOROPUMICE = register(
		"sunbloomed_floropumice",
		settings -> new BloomedFloropumiceBlock(AbysmConfiguredFeatures.SUNNY_BLOOMSHROOM_VEGETATION_BONEMEAL, settings),
		AbstractBlock.Settings.copy(ROSEBLOOMED_FLOROPUMICE)
			.mapColor(DyeColor.YELLOW)
	);
	public static final Block MALLOWBLOOMED_FLOROPUMICE = register(
		"mallowbloomed_floropumice",
		settings -> new BloomedFloropumiceBlock(AbysmConfiguredFeatures.MAUVE_BLOOMSHROOM_VEGETATION_BONEMEAL, settings),
		AbstractBlock.Settings.copy(ROSEBLOOMED_FLOROPUMICE)
			.mapColor(DyeColor.PURPLE)
	);

	public static final Block ROSY_SPRIGS = register(
		"rosy_sprigs",
		settings -> new BloomshroomSprigsBlock(AbysmParticleTypes.ROSEBLOOM_GLIMMER, settings),
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.RED)
			.sounds(BlockSoundGroup.GRASS)
			.pistonBehavior(PistonBehavior.DESTROY)
			.offset(AbstractBlock.OffsetType.XZ)
			.luminance(state -> 3)
			.breakInstantly()
			.noCollision()
			.replaceable()
	);
	public static final Block POTTED_ROSY_SPRIGS = register(
		"potted_rosy_sprigs",
		settings -> new FlowerPotBlock(ROSY_SPRIGS, settings),
		AbstractBlock.Settings.create()
			.pistonBehavior(PistonBehavior.DESTROY)
			.luminance(state -> 3)
			.breakInstantly()
			.nonOpaque(),
		false
	);
	public static final Block SUNNY_SPRIGS = register(
		"sunny_sprigs",
		settings -> new BloomshroomSprigsBlock(AbysmParticleTypes.SUNBLOOM_GLIMMER, settings),
		AbstractBlock.Settings.copy(ROSY_SPRIGS)
			.mapColor(DyeColor.YELLOW)
	);
	public static final Block POTTED_SUNNY_SPRIGS = register(
		"potted_sunny_sprigs",
		settings -> new FlowerPotBlock(SUNNY_SPRIGS, settings),
		AbstractBlock.Settings.copy(POTTED_ROSY_SPRIGS),
		false
	);
	public static final Block MAUVE_SPRIGS = register(
		"mauve_sprigs",
		settings -> new BloomshroomSprigsBlock(AbysmParticleTypes.MALLOWBLOOM_GLIMMER, settings),
		AbstractBlock.Settings.copy(ROSY_SPRIGS)
			.mapColor(DyeColor.PURPLE)
	);
	public static final Block POTTED_MAUVE_SPRIGS = register(
		"potted_mauve_sprigs",
		settings -> new FlowerPotBlock(MAUVE_SPRIGS, settings),
		AbstractBlock.Settings.copy(POTTED_ROSY_SPRIGS),
		false
	);

	public static final Block ROSY_BLOOMSHROOM = register(
		"rosy_bloomshroom",
		settings -> new SmallBloomshroomBlock(AbysmConfiguredFeatures.ROSY_BLOOMSHROOM, AbysmParticleTypes.ROSEBLOOM_GLIMMER, settings),
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.RED)
			.sounds(BlockSoundGroup.GRASS)
			.pistonBehavior(PistonBehavior.DESTROY)
			.offset(AbstractBlock.OffsetType.XZ)
			.luminance(state -> 8)
			.breakInstantly()
			.noCollision()
	);
	public static final Block POTTED_ROSY_BLOOMSHROOM = register(
		"potted_rosy_bloomshroom",
		settings -> new FlowerPotBlock(ROSY_BLOOMSHROOM, settings),
		AbstractBlock.Settings.create()
			.pistonBehavior(PistonBehavior.DESTROY)
			.luminance(state -> 8)
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
	public static final Block ROSY_BLOOMSHROOM_HYPHAE = register(
		"rosy_bloomshroom_hyphae",
		PillarBlock::new,
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.RED)
			.instrument(NoteBlockInstrument.BASS)
			.sounds(BlockSoundGroup.WOOD)
			.strength(1.1F, 1.5F)
	);
	public static final Block ROSY_BLOOMSHROOM_CAP = register(
		"rosy_bloomshroom_cap",
		settings -> new BloomshroomCapBlock(AbysmParticleTypes.ROSEBLOOM_PETALS, settings),
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.RED)
			.instrument(NoteBlockInstrument.BASS)
			.sounds(BlockSoundGroup.WOOD)
			.strength(0.7F, 0.9F)
	);
	public static final Block ROSEBLOOM_PETALEAVES = register(
		"rosebloom_petaleaves",
		settings -> new BloomPetaleavesBlock(AbysmParticleTypes.ROSEBLOOM_PETALS, 0.25F, settings),
		AbstractBlock.Settings.create()
			.mapColor(MapColor.RED)
			.strength(0.2F)
			.sounds(BlockSoundGroup.CHERRY_LEAVES)
			.pistonBehavior(PistonBehavior.DESTROY)
			.allowsSpawning(Blocks::never)
			.suffocates(Blocks::never)
			.blockVision(Blocks::never)
			.solidBlock(Blocks::never)
			.ticksRandomly()
			.nonOpaque()
	);
	public static final Block ROSEBLOOM_PETALS = register(
		"rosebloom_petals",
		settings -> new BloomPetalsBlock(AbysmParticleTypes.ROSEBLOOM_PETALS, settings),
		AbstractBlock.Settings.create()
			.mapColor(MapColor.RED)
			.sounds(BlockSoundGroup.FLOWERBED)
			.pistonBehavior(PistonBehavior.DESTROY)
			.noCollision(),
		PlaceableOnWaterOrBlockItem::new
	);

	public static final Block SUNNY_BLOOMSHROOM = register(
		"sunny_bloomshroom",
		settings -> new SmallBloomshroomBlock(AbysmConfiguredFeatures.SUNNY_BLOOMSHROOM, AbysmParticleTypes.SUNBLOOM_GLIMMER, settings),
		AbstractBlock.Settings.copy(ROSY_BLOOMSHROOM)
			.mapColor(DyeColor.YELLOW)
	);
	public static final Block POTTED_SUNNY_BLOOMSHROOM = register(
		"potted_sunny_bloomshroom",
		settings -> new FlowerPotBlock(SUNNY_BLOOMSHROOM, settings),
		AbstractBlock.Settings.copy(POTTED_ROSY_BLOOMSHROOM),
		false
	);
	public static final Block SUNNY_BLOOMSHROOM_STEM = register(
		"sunny_bloomshroom_stem",
		PillarBlock::new,
		AbstractBlock.Settings.copy(ROSY_BLOOMSHROOM_STEM)
			.mapColor(DyeColor.YELLOW)
	);
	public static final Block SUNNY_BLOOMSHROOM_HYPHAE = register(
		"sunny_bloomshroom_hyphae",
		PillarBlock::new,
		AbstractBlock.Settings.copy(ROSY_BLOOMSHROOM_HYPHAE)
			.mapColor(DyeColor.YELLOW)
	);
	public static final Block SUNNY_BLOOMSHROOM_CAP = register(
		"sunny_bloomshroom_cap",
		settings -> new BloomshroomCapBlock(AbysmParticleTypes.SUNBLOOM_PETALS, settings),
		AbstractBlock.Settings.copy(ROSY_BLOOMSHROOM_CAP)
			.mapColor(DyeColor.YELLOW)
	);
	public static final Block SUNBLOOM_PETALEAVES = register(
		"sunbloom_petaleaves",
		settings -> new BloomPetaleavesBlock(AbysmParticleTypes.SUNBLOOM_PETALS, 0.25F, settings),
		AbstractBlock.Settings.copy(ROSEBLOOM_PETALEAVES)
			.mapColor(MapColor.YELLOW)
	);
	public static final Block SUNBLOOM_PETALS = register(
		"sunbloom_petals",
		settings -> new BloomPetalsBlock(AbysmParticleTypes.SUNBLOOM_PETALS, settings),
		AbstractBlock.Settings.copy(ROSEBLOOM_PETALS)
			.mapColor(MapColor.YELLOW),
		PlaceableOnWaterOrBlockItem::new
	);

	public static final Block MAUVE_BLOOMSHROOM = register(
		"mauve_bloomshroom",
		settings -> new SmallBloomshroomBlock(AbysmConfiguredFeatures.MAUVE_BLOOMSHROOM, AbysmParticleTypes.MALLOWBLOOM_GLIMMER, settings),
		AbstractBlock.Settings.copy(ROSY_BLOOMSHROOM)
			.mapColor(DyeColor.PURPLE)
	);
	public static final Block POTTED_MAUVE_BLOOMSHROOM = register(
		"potted_mauve_bloomshroom",
		settings -> new FlowerPotBlock(MAUVE_BLOOMSHROOM, settings),
		AbstractBlock.Settings.copy(POTTED_ROSY_BLOOMSHROOM),
		false
	);
	public static final Block MAUVE_BLOOMSHROOM_STEM = register(
		"mauve_bloomshroom_stem",
		PillarBlock::new,
		AbstractBlock.Settings.copy(ROSY_BLOOMSHROOM_STEM)
			.mapColor(DyeColor.PURPLE)
	);
	public static final Block MAUVE_BLOOMSHROOM_HYPHAE = register(
		"mauve_bloomshroom_hyphae",
		PillarBlock::new,
		AbstractBlock.Settings.copy(ROSY_BLOOMSHROOM_HYPHAE)
			.mapColor(DyeColor.PURPLE)
	);
	public static final Block MAUVE_BLOOMSHROOM_CAP = register(
		"mauve_bloomshroom_cap",
		settings -> new BloomshroomCapBlock(AbysmParticleTypes.MALLOWBLOOM_PETALS, settings),
		AbstractBlock.Settings.copy(ROSY_BLOOMSHROOM_CAP)
			.mapColor(DyeColor.PURPLE)
	);
	public static final Block MALLOWBLOOM_PETALEAVES = register(
		"mallowbloom_petaleaves",
		settings -> new BloomPetaleavesBlock(AbysmParticleTypes.MALLOWBLOOM_PETALS, 0.25F, settings),
		AbstractBlock.Settings.copy(ROSEBLOOM_PETALEAVES)
			.mapColor(MapColor.PURPLE)
	);
	public static final Block MALLOWBLOOM_PETALS = register(
		"mallowbloom_petals",
		settings -> new BloomPetalsBlock(AbysmParticleTypes.MALLOWBLOOM_PETALS, settings),
		AbstractBlock.Settings.copy(ROSEBLOOM_PETALS)
			.mapColor(MapColor.PURPLE),
		PlaceableOnWaterOrBlockItem::new
	);

	public static final Block BLOOMSHROOM_GOOP = register(
		"bloomshroom_goop",
		BloomshroomGoopBlock::new,
		AbstractBlock.Settings.create()
			.mapColor(DyeColor.PINK)
			.instrument(NoteBlockInstrument.BASS)
			.sounds(BlockSoundGroup.HONEY)
			.strength(0.7F, 1.1F)
			.luminance(state -> 11)
			.nonOpaque()
	);

	public static final Block BLOOMING_SODALITE_CROWN = register(
		"blooming_sodalite_crown",
		settings -> new BloomshroomCrownBlock(AbysmParticleTypes.ROSEBLOOM_GLIMMER, AbysmParticleTypes.SODALITE_THORNS, settings),
		AbstractBlock.Settings.create()
			.mapColor(MapColor.LAPIS_BLUE)
			.sounds(BlockSoundGroup.SPORE_BLOSSOM)
			.pistonBehavior(PistonBehavior.DESTROY)
			.luminance(state -> 9)
			.breakInstantly()
			.noCollision()
	);
	public static final Block BLOOMING_ANYOLITE_CROWN = register(
		"blooming_anyolite_crown",
		settings -> new BloomshroomCrownBlock(AbysmParticleTypes.SUNBLOOM_GLIMMER, AbysmParticleTypes.ANYOLITE_THORNS, settings),
		AbstractBlock.Settings.copy(BLOOMING_SODALITE_CROWN)
			.mapColor(MapColor.LIME)
	);
	public static final Block BLOOMING_MELILITE_CROWN = register(
		"blooming_melilite_crown",
		settings -> new BloomshroomCrownBlock(AbysmParticleTypes.MALLOWBLOOM_GLIMMER, AbysmParticleTypes.MELILITE_THORNS, settings),
		AbstractBlock.Settings.copy(BLOOMING_SODALITE_CROWN)
			.mapColor(MapColor.ORANGE)
	);
	// endregion bloomshroom

	// region scabiosas
	public static final Block WHITE_SCABIOSA = register(
		"white_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
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
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.ORANGE)
	);

	public static final Block MAGENTA_SCABIOSA = register(
		"magenta_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.MAGENTA)
	);

	public static final Block LIGHT_BLUE_SCABIOSA = register(
		"light_blue_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.LIGHT_BLUE)
	);

	public static final Block YELLOW_SCABIOSA = register(
		"yellow_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.YELLOW)
	);

	public static final Block LIME_SCABIOSA = register(
		"lime_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.LIME)
	);

	public static final Block PINK_SCABIOSA = register(
		"pink_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.PINK)
	);

	public static final Block GREY_SCABIOSA = register(
		"grey_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.GRAY)
	);

	public static final Block LIGHT_GREY_SCABIOSA = register(
		"light_grey_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.LIGHT_BLUE_GRAY)
	);

	public static final Block CYAN_SCABIOSA = register(
		"cyan_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.CYAN)
	);

	public static final Block PURPLE_SCABIOSA = register(
		"purple_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.PURPLE)
	);

	public static final Block BLUE_SCABIOSA = register(
		"blue_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.LAPIS_BLUE)
	);

	public static final Block BROWN_SCABIOSA = register(
		"brown_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.TERRACOTTA_BROWN)
	);

	public static final Block GREEN_SCABIOSA = register(
		"green_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.TERRACOTTA_GREEN)
	);

	public static final Block RED_SCABIOSA = register(
		"red_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.RED)
	);

	public static final Block BLACK_SCABIOSA = register(
		"black_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		AbstractBlock.Settings.copy(WHITE_SCABIOSA).mapColor(MapColor.BLACK)
	);
	// endregion scabiosas

	// region misc plants
	public static final Block ANTENNAE_PLANT = register(
		"antennae_plant",
		AntennaePlantBlock::new,
		AbstractBlock.Settings.copy(ROSY_SPRIGS)
			.luminance(state -> 7)
			.mapColor(DyeColor.PINK)
	);
	public static final Block POTTED_ANTENNAE_PLANT = register(
		"potted_antennae_plant",
		settings -> new FlowerPotBlock(ANTENNAE_PLANT, settings),
		AbstractBlock.Settings.copy(POTTED_ROSY_SPRIGS),
		false
	);

	// the random-ticked head of the plant
	public static final AbstractPlantStemBlock GOLDEN_LAZULI_OREFURL = register(
		"golden_lazuli_orefurl",
		OrefurlBlock::new,
		AbstractBlock.Settings.create()
			.mapColor(MapColor.GOLD)
			.sounds(BlockSoundGroup.SWEET_BERRY_BUSH)
			.pistonBehavior(PistonBehavior.DESTROY)
			.offset(AbstractBlock.OffsetType.XZ)
			.luminance(state -> 2)
			.noCollision()
			.ticksRandomly()
			.breakInstantly(),
		false
	);

	// the not-random-ticked body of the plant
	public static final Block GOLDEN_LAZULI_OREFURL_PLANT = register(
		"golden_lazuli_orefurl_plant",
		OrefurlPlantBlock::new,
		AbstractBlock.Settings.create()
			.mapColor(MapColor.GOLD)
			.sounds(BlockSoundGroup.SWEET_BERRY_BUSH)
			.pistonBehavior(PistonBehavior.DESTROY)
			.offset(AbstractBlock.OffsetType.XZ)
			.luminance(state -> 2)
			.noCollision()
			.breakInstantly(),
		false
	);
	// endregion misc plants

	// region dregloam
	public static final Block DREGLOAM = register(
		"dregloam",
		Block::new,
		AbstractBlock.Settings.create()
			.strength(0.85F)
			.mapColor(MapColor.TERRACOTTA_BROWN)
			.sounds(BlockSoundGroup.MUD)
	);

	public static final Block OOZING_DREGLOAM = register(
		"oozing_dregloam",
		OozingDregloamBlock::new,
		AbstractBlock.Settings.create()
			.strength(0.95F)
			.mapColor(MapColor.TERRACOTTA_CYAN)
			.sounds(BlockSoundGroup.MUD)
			.ticksRandomly()
	);

	public static final Block DREGLOAM_OOZE = register(
		"dregloam_ooze",
		DregloamOozeBlock::new,
		AbstractBlock.Settings.create()
			.strength(0.7F)
			.mapColor(MapColor.TERRACOTTA_CYAN)
			.sounds(BlockSoundGroup.MUD)
			.ticksRandomly()
	);

	public static final Block DREGLOAM_GOLDEN_LAZULI_ORE = register(
		"dregloam_golden_lazuli_ore",
		Block::new,
		AbstractBlock.Settings.create()
			.strength(1.1F)
			.mapColor(MapColor.TERRACOTTA_BROWN)
			.sounds(BlockSoundGroup.MUD)
	);
	// endregion

	public static <T extends Block> T register(RegistryKey<Block> key, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings, @Nullable BiFunction<Block, Item.Settings, ? extends BlockItem> blockItemConstructor) {
		T block = factory.apply(settings.registryKey(key));

		if (blockItemConstructor != null) {
			RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, key.getValue());
			Item.Settings itemSettings = new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey();
			BlockItem blockItem = blockItemConstructor.apply(block, itemSettings);
			Registry.register(
				Registries.ITEM,
				itemKey,
				blockItem
			);
			blockItem.appendBlocks(Item.BLOCK_ITEMS, blockItem);
		}

		return Registry.register(Registries.BLOCK, key, block);
	}

	public static <T extends Block> T register(RegistryKey<Block> key, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings, boolean item) {
		return register(key, factory, settings, item ? BlockItem::new : null);
	}

	private static <T extends Block> T register(String id, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings, boolean item) {
		return register(keyOf(id), factory, settings, item);
	}

	private static <T extends Block> T register(String id, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings, @Nullable BiFunction<Block, Item.Settings, ? extends BlockItem> blockItemConstructor) {
		return register(keyOf(id), factory, settings, blockItemConstructor);
	}

	private static <T extends Block> T register(String id, Function<AbstractBlock.Settings, T> factory, AbstractBlock.Settings settings) {
		return register(keyOf(id), factory, settings, true);
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

	private static RegistryKey<Block> keyOf(String id) {
		return RegistryKey.of(RegistryKeys.BLOCK, Abysm.id(id));
	}

	public static void init() {
		// NO-OP
	}
}
