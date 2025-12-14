package dev.spiritstudios.abysm.world.level.block;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.item.PlaceableOnWaterOrBlockItem;
import dev.spiritstudios.abysm.core.particles.AbysmParticleTypes;
import dev.spiritstudios.abysm.world.level.levelgen.feature.AbysmConfiguredFeatures;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class AbysmBlocks {

	// region floropumice blocks
	public static final Block FLOROPUMICE = register(
		"floropumice",
		FloropumiceBlock::new,
		BlockBehaviour.Properties.of()
			.mapColor(DyeColor.LIGHT_BLUE)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresCorrectToolForDrops()
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
		BlockBehaviour.Properties.of()
			.mapColor(DyeColor.LIGHT_BLUE)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresCorrectToolForDrops()
			.strength(1.5F, 6.0F)
	);

	public static final Block FLOROPUMICE_BRICKS = register(
		"floropumice_bricks",
		Block::new,
		BlockBehaviour.Properties.ofFullCopy(POLISHED_FLOROPUMICE)
	);
	public static final Block CRACKED_FLOROPUMICE_BRICKS = register(
		"cracked_floropumice_bricks",
		Block::new,
		BlockBehaviour.Properties.ofFullCopy(POLISHED_FLOROPUMICE)
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
		BlockBehaviour.Properties.ofFullCopy(POLISHED_FLOROPUMICE)
	);
	public static final Block CRACKED_FLOROPUMICE_TILES = register(
		"cracked_floropumice_tiles",
		Block::new,
		BlockBehaviour.Properties.ofFullCopy(POLISHED_FLOROPUMICE)
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
		BlockBehaviour.Properties.ofFullCopy(POLISHED_FLOROPUMICE)
	);

	public static final Block SMOOTH_FLOROPUMICE = register(
		"smooth_floropumice",
		Block::new,
		BlockBehaviour.Properties.of()
			.mapColor(DyeColor.BLUE)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresCorrectToolForDrops()
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
		BlockBehaviour.Properties.of()
			.mapColor(DyeColor.BLUE)
			.instrument(NoteBlockInstrument.BASEDRUM)
			.requiresCorrectToolForDrops()
			.strength(1.5F, 6.0F)
	);

	public static final Block SMOOTH_FLOROPUMICE_BRICKS = register(
		"smooth_floropumice_bricks",
		Block::new,
		BlockBehaviour.Properties.ofFullCopy(POLISHED_SMOOTH_FLOROPUMICE)
	);
	public static final Block CRACKED_SMOOTH_FLOROPUMICE_BRICKS = register(
		"cracked_smooth_floropumice_bricks",
		Block::new,
		BlockBehaviour.Properties.ofFullCopy(POLISHED_SMOOTH_FLOROPUMICE)
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
		BlockBehaviour.Properties.ofFullCopy(POLISHED_SMOOTH_FLOROPUMICE)
	);
	public static final Block CRACKED_CUT_SMOOTH_FLOROPUMICE = register(
		"cracked_cut_smooth_floropumice",
		Block::new,
		BlockBehaviour.Properties.ofFullCopy(POLISHED_SMOOTH_FLOROPUMICE)
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
		BlockBehaviour.Properties.ofFullCopy(POLISHED_SMOOTH_FLOROPUMICE)
	);

	public static final Block SMOOTH_FLOROPUMICE_PILLAR = register(
		"smooth_floropumice_pillar",
		RotatedPillarBlock::new,
		BlockBehaviour.Properties.ofFullCopy(POLISHED_SMOOTH_FLOROPUMICE)
	);
	// endregion floropumice

	// region bloomshroom blocks
	public static final Block ROSEBLOOMED_FLOROPUMICE = register(
		"rosebloomed_floropumice",
		settings -> new BloomedFloropumiceBlock(AbysmConfiguredFeatures.ROSEBLOOMED_PATCH_BONEMEAL, settings),
		BlockBehaviour.Properties.of()
			.mapColor(DyeColor.RED)
			.instrument(NoteBlockInstrument.BASS)
			.strength(1.2F, 4.5F)
			.requiresCorrectToolForDrops()
			.randomTicks()
	);
	public static final Block SUNBLOOMED_FLOROPUMICE = register(
		"sunbloomed_floropumice",
		settings -> new BloomedFloropumiceBlock(AbysmConfiguredFeatures.SUNBLOOMED_PATCH_BONEMEAL, settings),
		BlockBehaviour.Properties.ofFullCopy(ROSEBLOOMED_FLOROPUMICE)
			.mapColor(DyeColor.YELLOW)
	);
	public static final Block MALLOWBLOOMED_FLOROPUMICE = register(
		"mallowbloomed_floropumice",
		settings -> new BloomedFloropumiceBlock(AbysmConfiguredFeatures.MALLOWBLOOMED_PATCH_BONEMEAL, settings),
		BlockBehaviour.Properties.ofFullCopy(ROSEBLOOMED_FLOROPUMICE)
			.mapColor(DyeColor.PURPLE)
	);

	public static final Block ROSY_SPRIGS = register(
		"rosy_sprigs",
		settings -> new BloomshroomSprigsBlock(AbysmParticleTypes.ROSEBLOOM_GLIMMER, settings),
		BlockBehaviour.Properties.of()
			.mapColor(DyeColor.RED)
			.sound(SoundType.GRASS)
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XZ)
			.lightLevel(state -> 3)
			.instabreak()
			.noCollision()
			.replaceable()
	);
	public static final Block POTTED_ROSY_SPRIGS = register(
		"potted_rosy_sprigs",
		settings -> new FlowerPotBlock(ROSY_SPRIGS, settings),
		BlockBehaviour.Properties.of()
			.pushReaction(PushReaction.DESTROY)
			.lightLevel(state -> 3)
			.instabreak()
			.noOcclusion(),
		false
	);
	public static final Block SUNNY_SPRIGS = register(
		"sunny_sprigs",
		settings -> new BloomshroomSprigsBlock(AbysmParticleTypes.SUNBLOOM_GLIMMER, settings),
		BlockBehaviour.Properties.ofFullCopy(ROSY_SPRIGS)
			.mapColor(DyeColor.YELLOW)
	);
	public static final Block POTTED_SUNNY_SPRIGS = register(
		"potted_sunny_sprigs",
		settings -> new FlowerPotBlock(SUNNY_SPRIGS, settings),
		BlockBehaviour.Properties.ofFullCopy(POTTED_ROSY_SPRIGS),
		false
	);
	public static final Block MAUVE_SPRIGS = register(
		"mauve_sprigs",
		settings -> new BloomshroomSprigsBlock(AbysmParticleTypes.MALLOWBLOOM_GLIMMER, settings),
		BlockBehaviour.Properties.ofFullCopy(ROSY_SPRIGS)
			.mapColor(DyeColor.PURPLE)
	);
	public static final Block POTTED_MAUVE_SPRIGS = register(
		"potted_mauve_sprigs",
		settings -> new FlowerPotBlock(MAUVE_SPRIGS, settings),
		BlockBehaviour.Properties.ofFullCopy(POTTED_ROSY_SPRIGS),
		false
	);

	public static final Block ROSY_BLOOMSHROOM = register(
		"rosy_bloomshroom",
		settings -> new SmallBloomshroomBlock(AbysmConfiguredFeatures.ROSY_BLOOMSHROOM, AbysmParticleTypes.ROSEBLOOM_GLIMMER, settings),
		BlockBehaviour.Properties.of()
			.mapColor(DyeColor.RED)
			.sound(SoundType.GRASS)
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XZ)
			.lightLevel(state -> 8)
			.instabreak()
			.noCollision()
	);
	public static final Block POTTED_ROSY_BLOOMSHROOM = register(
		"potted_rosy_bloomshroom",
		settings -> new FlowerPotBlock(ROSY_BLOOMSHROOM, settings),
		BlockBehaviour.Properties.of()
			.pushReaction(PushReaction.DESTROY)
			.lightLevel(state -> 8)
			.instabreak()
			.noOcclusion(),
		false
	);
	public static final Block ROSY_BLOOMSHROOM_STEM = register(
		"rosy_bloomshroom_stem",
		RotatedPillarBlock::new,
		BlockBehaviour.Properties.of()
			.mapColor(DyeColor.RED)
			.instrument(NoteBlockInstrument.BASS)
			.sound(SoundType.WOOD)
			.strength(1.1F, 1.5F)
	);
	public static final Block ROSY_BLOOMSHROOM_HYPHAE = register(
		"rosy_bloomshroom_hyphae",
		RotatedPillarBlock::new,
		BlockBehaviour.Properties.of()
			.mapColor(DyeColor.RED)
			.instrument(NoteBlockInstrument.BASS)
			.sound(SoundType.WOOD)
			.strength(1.1F, 1.5F)
	);
	public static final Block ROSY_BLOOMSHROOM_CAP = register(
		"rosy_bloomshroom_cap",
		settings -> new BloomshroomCapBlock(AbysmParticleTypes.ROSEBLOOM_PETALS, settings),
		BlockBehaviour.Properties.of()
			.mapColor(DyeColor.RED)
			.instrument(NoteBlockInstrument.BASS)
			.sound(SoundType.WOOD)
			.strength(0.7F, 0.9F)
	);
	public static final Block ROSEBLOOM_PETALEAVES = register(
		"rosebloom_petaleaves",
		settings -> new BloomPetaleavesBlock(AbysmParticleTypes.ROSEBLOOM_PETALS, 0.25F, settings),
		BlockBehaviour.Properties.of()
			.mapColor(MapColor.COLOR_RED)
			.strength(0.2F)
			.sound(SoundType.CHERRY_LEAVES)
			.pushReaction(PushReaction.DESTROY)
			.isValidSpawn(Blocks::never)
			.isSuffocating(Blocks::never)
			.isViewBlocking(Blocks::never)
			.isRedstoneConductor(Blocks::never)
			.randomTicks()
			.noOcclusion()
	);
	public static final Block ROSEBLOOM_PETALS = register(
		"rosebloom_petals",
		settings -> new BloomPetalsBlock(AbysmParticleTypes.ROSEBLOOM_PETALS, settings),
		BlockBehaviour.Properties.of()
			.mapColor(MapColor.COLOR_RED)
			.sound(SoundType.PINK_PETALS)
			.pushReaction(PushReaction.DESTROY)
			.noCollision(),
		PlaceableOnWaterOrBlockItem::new
	);

	public static final Block SUNNY_BLOOMSHROOM = register(
		"sunny_bloomshroom",
		settings -> new SmallBloomshroomBlock(AbysmConfiguredFeatures.SUNNY_BLOOMSHROOM, AbysmParticleTypes.SUNBLOOM_GLIMMER, settings),
		BlockBehaviour.Properties.ofFullCopy(ROSY_BLOOMSHROOM)
			.mapColor(DyeColor.YELLOW)
	);
	public static final Block POTTED_SUNNY_BLOOMSHROOM = register(
		"potted_sunny_bloomshroom",
		settings -> new FlowerPotBlock(SUNNY_BLOOMSHROOM, settings),
		BlockBehaviour.Properties.ofFullCopy(POTTED_ROSY_BLOOMSHROOM),
		false
	);
	public static final Block SUNNY_BLOOMSHROOM_STEM = register(
		"sunny_bloomshroom_stem",
		RotatedPillarBlock::new,
		BlockBehaviour.Properties.ofFullCopy(ROSY_BLOOMSHROOM_STEM)
			.mapColor(DyeColor.YELLOW)
	);
	public static final Block SUNNY_BLOOMSHROOM_HYPHAE = register(
		"sunny_bloomshroom_hyphae",
		RotatedPillarBlock::new,
		BlockBehaviour.Properties.ofFullCopy(ROSY_BLOOMSHROOM_HYPHAE)
			.mapColor(DyeColor.YELLOW)
	);
	public static final Block SUNNY_BLOOMSHROOM_CAP = register(
		"sunny_bloomshroom_cap",
		settings -> new BloomshroomCapBlock(AbysmParticleTypes.SUNBLOOM_PETALS, settings),
		BlockBehaviour.Properties.ofFullCopy(ROSY_BLOOMSHROOM_CAP)
			.mapColor(DyeColor.YELLOW)
	);
	public static final Block SUNBLOOM_PETALEAVES = register(
		"sunbloom_petaleaves",
		settings -> new BloomPetaleavesBlock(AbysmParticleTypes.SUNBLOOM_PETALS, 0.25F, settings),
		BlockBehaviour.Properties.ofFullCopy(ROSEBLOOM_PETALEAVES)
			.mapColor(MapColor.COLOR_YELLOW)
	);
	public static final Block SUNBLOOM_PETALS = register(
		"sunbloom_petals",
		settings -> new BloomPetalsBlock(AbysmParticleTypes.SUNBLOOM_PETALS, settings),
		BlockBehaviour.Properties.ofFullCopy(ROSEBLOOM_PETALS)
			.mapColor(MapColor.COLOR_YELLOW),
		PlaceableOnWaterOrBlockItem::new
	);

	public static final Block MAUVE_BLOOMSHROOM = register(
		"mauve_bloomshroom",
		settings -> new SmallBloomshroomBlock(AbysmConfiguredFeatures.MAUVE_BLOOMSHROOM, AbysmParticleTypes.MALLOWBLOOM_GLIMMER, settings),
		BlockBehaviour.Properties.ofFullCopy(ROSY_BLOOMSHROOM)
			.mapColor(DyeColor.PURPLE)
	);
	public static final Block POTTED_MAUVE_BLOOMSHROOM = register(
		"potted_mauve_bloomshroom",
		settings -> new FlowerPotBlock(MAUVE_BLOOMSHROOM, settings),
		BlockBehaviour.Properties.ofFullCopy(POTTED_ROSY_BLOOMSHROOM),
		false
	);
	public static final Block MAUVE_BLOOMSHROOM_STEM = register(
		"mauve_bloomshroom_stem",
		RotatedPillarBlock::new,
		BlockBehaviour.Properties.ofFullCopy(ROSY_BLOOMSHROOM_STEM)
			.mapColor(DyeColor.PURPLE)
	);
	public static final Block MAUVE_BLOOMSHROOM_HYPHAE = register(
		"mauve_bloomshroom_hyphae",
		RotatedPillarBlock::new,
		BlockBehaviour.Properties.ofFullCopy(ROSY_BLOOMSHROOM_HYPHAE)
			.mapColor(DyeColor.PURPLE)
	);
	public static final Block MAUVE_BLOOMSHROOM_CAP = register(
		"mauve_bloomshroom_cap",
		settings -> new BloomshroomCapBlock(AbysmParticleTypes.MALLOWBLOOM_PETALS, settings),
		BlockBehaviour.Properties.ofFullCopy(ROSY_BLOOMSHROOM_CAP)
			.mapColor(DyeColor.PURPLE)
	);
	public static final Block MALLOWBLOOM_PETALEAVES = register(
		"mallowbloom_petaleaves",
		settings -> new BloomPetaleavesBlock(AbysmParticleTypes.MALLOWBLOOM_PETALS, 0.25F, settings),
		BlockBehaviour.Properties.ofFullCopy(ROSEBLOOM_PETALEAVES)
			.mapColor(MapColor.COLOR_PURPLE)
	);
	public static final Block MALLOWBLOOM_PETALS = register(
		"mallowbloom_petals",
		settings -> new BloomPetalsBlock(AbysmParticleTypes.MALLOWBLOOM_PETALS, settings),
		BlockBehaviour.Properties.ofFullCopy(ROSEBLOOM_PETALS)
			.mapColor(MapColor.COLOR_PURPLE),
		PlaceableOnWaterOrBlockItem::new
	);

	public static final Block SWEET_NECTARSAP = register(
		"sweet_nectarsap",
		settings -> new NectarsapBlock(AbysmParticleTypes.ROSEBLOOM_GLIMMER, settings),
		BlockBehaviour.Properties.of()
			.mapColor(DyeColor.PINK)
			.instrument(NoteBlockInstrument.BASS)
			.sound(SoundType.HONEY_BLOCK)
			.strength(0.7F, 1.1F)
			.lightLevel(state -> 11)
			.noOcclusion()
	);

	public static final Block SOUR_NECTARSAP = register(
		"sour_nectarsap",
		settings -> new NectarsapBlock(AbysmParticleTypes.SUNBLOOM_GLIMMER, settings),
		BlockBehaviour.Properties.ofFullCopy(SWEET_NECTARSAP)
			.mapColor(DyeColor.YELLOW)
	);

	public static final Block BITTER_NECTARSAP = register(
		"bitter_nectarsap",
		settings -> new NectarsapBlock(AbysmParticleTypes.MALLOWBLOOM_GLIMMER, settings),
		BlockBehaviour.Properties.ofFullCopy(SWEET_NECTARSAP)
			.mapColor(DyeColor.PURPLE)
	);

	public static final Block BLOOMING_SODALITE_CROWN = register(
		"blooming_sodalite_crown",
		settings -> new BloomshroomCrownBlock(AbysmParticleTypes.ROSEBLOOM_GLIMMER, AbysmParticleTypes.SODALITE_THORNS, settings),
		BlockBehaviour.Properties.of()
			.mapColor(MapColor.LAPIS)
			.sound(SoundType.SPORE_BLOSSOM)
			.pushReaction(PushReaction.DESTROY)
			.lightLevel(state -> 9)
			.instabreak()
			.noCollision()
	);
	public static final Block BLOOMING_ANYOLITE_CROWN = register(
		"blooming_anyolite_crown",
		settings -> new BloomshroomCrownBlock(AbysmParticleTypes.SUNBLOOM_GLIMMER, AbysmParticleTypes.ANYOLITE_THORNS, settings),
		BlockBehaviour.Properties.ofFullCopy(BLOOMING_SODALITE_CROWN)
			.mapColor(MapColor.COLOR_LIGHT_GREEN)
	);
	public static final Block BLOOMING_MELILITE_CROWN = register(
		"blooming_melilite_crown",
		settings -> new BloomshroomCrownBlock(AbysmParticleTypes.MALLOWBLOOM_GLIMMER, AbysmParticleTypes.MELILITE_THORNS, settings),
		BlockBehaviour.Properties.ofFullCopy(BLOOMING_SODALITE_CROWN)
			.mapColor(MapColor.COLOR_ORANGE)
	);
	// endregion bloomshroom

	// region scabiosas
	public static final Block WHITE_SCABIOSA = register(
		"white_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.of()
			.mapColor(MapColor.TERRACOTTA_WHITE)
			.forceSolidOn()
			.instabreak()
			.noCollision()
			.sound(SoundType.SPORE_BLOSSOM)
			.pushReaction(PushReaction.DESTROY)
	);

	public static final Block ORANGE_SCABIOSA = register(
		"orange_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.COLOR_ORANGE)
	);

	public static final Block MAGENTA_SCABIOSA = register(
		"magenta_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.COLOR_MAGENTA)
	);

	public static final Block LIGHT_BLUE_SCABIOSA = register(
		"light_blue_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.COLOR_LIGHT_BLUE)
	);

	public static final Block YELLOW_SCABIOSA = register(
		"yellow_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.COLOR_YELLOW)
	);

	public static final Block LIME_SCABIOSA = register(
		"lime_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.COLOR_LIGHT_GREEN)
	);

	public static final Block PINK_SCABIOSA = register(
		"pink_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.COLOR_PINK)
	);

	public static final Block GREY_SCABIOSA = register(
		"grey_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.COLOR_GRAY)
	);

	public static final Block LIGHT_GREY_SCABIOSA = register(
		"light_grey_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.CLAY)
	);

	public static final Block CYAN_SCABIOSA = register(
		"cyan_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.COLOR_CYAN)
	);

	public static final Block PURPLE_SCABIOSA = register(
		"purple_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.COLOR_PURPLE)
	);

	public static final Block BLUE_SCABIOSA = register(
		"blue_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.LAPIS)
	);

	public static final Block BROWN_SCABIOSA = register(
		"brown_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.TERRACOTTA_BROWN)
	);

	public static final Block GREEN_SCABIOSA = register(
		"green_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.TERRACOTTA_GREEN)
	);

	public static final Block RED_SCABIOSA = register(
		"red_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.COLOR_RED)
	);

	public static final Block BLACK_SCABIOSA = register(
		"black_scabiosa",
		RotatableWaterloggableFlowerBlock::new,
		BlockBehaviour.Properties.ofFullCopy(WHITE_SCABIOSA).mapColor(MapColor.COLOR_BLACK)
	);
	// endregion scabiosas

	// region misc plants
	public static final Block ANTENNAE_PLANT = register(
		"antennae_plant",
		AntennaePlantBlock::new,
		BlockBehaviour.Properties.ofFullCopy(ROSY_SPRIGS)
			.lightLevel(state -> 7)
			.mapColor(DyeColor.PINK)
	);
	public static final Block POTTED_ANTENNAE_PLANT = register(
		"potted_antennae_plant",
		settings -> new FlowerPotBlock(ANTENNAE_PLANT, settings),
		BlockBehaviour.Properties.ofFullCopy(POTTED_ROSY_SPRIGS),
		false
	);
	// endregion misc plants

	// region dregloam
	public static final Block DREGLOAM = register(
		"dregloam",
		Block::new,
		BlockBehaviour.Properties.of()
			.strength(0.85F)
			.mapColor(MapColor.TERRACOTTA_BROWN)
			.sound(SoundType.MUD)
	);

	public static final Block OOZING_DREGLOAM = register(
		"oozing_dregloam",
		OozingDregloamBlock::new,
		BlockBehaviour.Properties.of()
			.strength(0.95F)
			.mapColor(MapColor.TERRACOTTA_CYAN)
			.sound(SoundType.MUD)
			.randomTicks()
	);

	public static final Block DREGLOAM_OOZE = register(
		"dregloam_ooze",
		DregloamOozeBlock::new,
		BlockBehaviour.Properties.of()
			.strength(0.7F)
			.mapColor(MapColor.TERRACOTTA_CYAN)
			.sound(SoundType.MUD)
			.randomTicks()
	);

	public static final Block DREGLOAM_GOLDEN_LAZULI_ORE = register(
		"dregloam_golden_lazuli_ore",
		Block::new,
		BlockBehaviour.Properties.of()
			.strength(1.1F)
			.mapColor(MapColor.TERRACOTTA_BROWN)
			.sound(SoundType.MUD)
	);
	// endregion

	// region dregloam plants
	public static final Block OOZETRICKLE_FILAMENTS = register(
		"oozetrickle_filaments",
		OozetrickleFilamentsBlock::new,
		BlockBehaviour.Properties.of()
			.mapColor(DyeColor.LIGHT_BLUE)
			.sound(SoundType.ROOTS)
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XZ)
			.lightLevel(state -> 2)
			.instabreak()
			.noCollision()
			.replaceable()
	);

	public static final Block POTTED_OOZETRICKLE_FILAMENTS = register(
		"potted_oozetrickle_filaments",
		settings -> new FlowerPotBlock(OOZETRICKLE_FILAMENTS, settings),
		BlockBehaviour.Properties.of()
			.pushReaction(PushReaction.DESTROY)
			.lightLevel(state -> 2)
			.instabreak()
			.noOcclusion(),
		false
	);

	public static final Block TALL_OOZETRICKLE_FILAMENTS = register(
		"tall_oozetrickle_filaments",
		TallOozetrickleFilamentsBlock::new,
		BlockBehaviour.Properties.of()
			.mapColor(DyeColor.LIGHT_BLUE)
			.sound(SoundType.ROOTS)
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XZ)
			.lightLevel(state -> 3)
			.instabreak()
			.noCollision(),
		DoubleHighBlockItem::new
	);

	// the random-ticked head of the plant
	public static final GrowingPlantHeadBlock GOLDEN_LAZULI_OREFURL = register(
		"golden_lazuli_orefurl",
		OrefurlBlock::new,
		BlockBehaviour.Properties.of()
			.mapColor(MapColor.GOLD)
			.sound(SoundType.SWEET_BERRY_BUSH)
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XZ)
			.lightLevel(state -> 2)
			.noCollision()
			.randomTicks()
			.instabreak(),
		false
	);

	// the not-random-ticked body of the plant
	public static final Block GOLDEN_LAZULI_OREFURL_PLANT = register(
		"golden_lazuli_orefurl_plant",
		OrefurlPlantBlock::new,
		BlockBehaviour.Properties.of()
			.mapColor(MapColor.GOLD)
			.sound(SoundType.SWEET_BERRY_BUSH)
			.pushReaction(PushReaction.DESTROY)
			.offsetType(BlockBehaviour.OffsetType.XZ)
			.lightLevel(state -> 2)
			.noCollision()
			.instabreak(),
		false
	);
	// endregion

	// region oozetrickle deco blocks
	public static final Block OOZETRICKLE_CORD = register(
		"oozetrickle_cord",
		ChainBlock::new,
		BlockBehaviour.Properties.of()
			.strength(2.5F, 3.0F)
			.sound(SoundType.ROOTS)
			.forceSolidOn()
			.requiresCorrectToolForDrops()
			.noOcclusion()
	);

	public static final Block OOZETRICKLE_LANTERN = register(
		"oozetrickle_lantern",
		OozetrickleLanternBlock::new,
		BlockBehaviour.Properties.of()
			.strength(1.9F)
			.mapColor(MapColor.COLOR_LIGHT_BLUE)
			.sound(SoundType.FROGLIGHT)
			.pushReaction(PushReaction.DESTROY)
			.lightLevel(state -> state.getValue(OozetrickleLanternBlock.LIGHT))
			.forceSolidOn()
			.noOcclusion()
			.randomTicks()
	);
	// endregion

	// region silt
	public static final Block SILT = register(
		"silt",
		Block::new,
		BlockBehaviour.Properties.of()
			.strength(0.6F)
			.mapColor(MapColor.WOOD)
			.sound(SoundType.MUD)
	);

	public static final Block CHISELED_SILT = register(
		"chiseled_silt",
		Block::new,
		BlockBehaviour.Properties.of()
			.strength(0.8F)
			.mapColor(MapColor.WOOD)
	);

	public static final Block CUT_SILT = register(
		"cut_silt",
		Block::new,
		BlockBehaviour.Properties.of()
			.strength(0.8F)
			.mapColor(MapColor.WOOD)
	);
	// endregion

	// region technical blocks
	public static final Block DENSITY_BLOB_BLOCK = register(
		"density_blob_block",
		DensityBlobBlock::new,
		BlockBehaviour.Properties.of()
			.mapColor(MapColor.COLOR_LIGHT_GRAY)
			.requiresCorrectToolForDrops()
			.strength(-1.0F, 3600000.0F)
			.noLootTable(),
		(block, settings) -> new BlockItem(block, settings.rarity(Rarity.EPIC))
	);
	// endregion

	public static <T extends Block> T register(ResourceKey<Block> key, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties settings, @Nullable BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemConstructor) {
		T block = factory.apply(settings.setId(key));

		if (blockItemConstructor != null) {
			ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, key.identifier());
			Item.Properties itemSettings = new Item.Properties().setId(itemKey).useBlockDescriptionPrefix();
			BlockItem blockItem = blockItemConstructor.apply(block, itemSettings);
			Registry.register(
				BuiltInRegistries.ITEM,
				itemKey,
				blockItem
			);
			blockItem.registerBlocks(Item.BY_BLOCK, blockItem);
		}

		return Registry.register(BuiltInRegistries.BLOCK, key, block);
	}

	public static <T extends Block> T register(ResourceKey<Block> key, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties settings, boolean item) {
		return register(key, factory, settings, item ? BlockItem::new : null);
	}

	private static <T extends Block> T register(String id, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties settings, boolean item) {
		return register(keyOf(id), factory, settings, item);
	}

	private static <T extends Block> T register(String id, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties settings, @Nullable BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemConstructor) {
		return register(keyOf(id), factory, settings, blockItemConstructor);
	}

	private static <T extends Block> T register(String id, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties settings) {
		return register(keyOf(id), factory, settings, true);
	}

	@SuppressWarnings("deprecation")
	private static Block registerStairsOf(String id, Block block) {
		return register(id, settings -> new StairBlock(block.defaultBlockState(), settings), BlockBehaviour.Properties.ofLegacyCopy(block));
	}

	@SuppressWarnings("deprecation")
	private static Block registerSlabOf(String id, Block block) {
		return register(id, SlabBlock::new, BlockBehaviour.Properties.ofLegacyCopy(block));
	}

	@SuppressWarnings("deprecation")
	private static Block registerWallOf(String id, Block block) {
		return register(id, WallBlock::new, BlockBehaviour.Properties.ofLegacyCopy(block).forceSolidOn());
	}

	private static ResourceKey<Block> keyOf(String id) {
		return ResourceKey.create(Registries.BLOCK, Abysm.id(id));
	}

	public static void init() {
		// NO-OP
	}
}
