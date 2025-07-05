package dev.spiritstudios.abysm.block;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.registry.Registries;

import java.util.Map;
import java.util.stream.Stream;

public class AbysmBlockFamilies {
	private static final Map<Block, BlockFamily> ABYSM_MAP = Maps.newHashMap();
	private static final String RECIPE_GROUP_PREFIX_WOODEN = "wooden";
	private static final String RECIPE_UNLOCKED_BY_HAS_PLANKS = "has_planks";

	public static final BlockFamily FLOROPUMICE = familyBuilder(AbysmBlocks.FLOROPUMICE)
		.stairs(AbysmBlocks.FLOROPUMICE_STAIRS)
		.slab(AbysmBlocks.FLOROPUMICE_SLAB)
		.wall(AbysmBlocks.FLOROPUMICE_WALL)
		.polished(AbysmBlocks.POLISHED_FLOROPUMICE)
		.chiseled(AbysmBlocks.CHISLED_FLOROPUMICE)
		.build();

	public static final BlockFamily FLOROPUMICE_BRICKS = familyBuilder(AbysmBlocks.FLOROPUMICE_BRICKS)
		.stairs(AbysmBlocks.FLOROPUMICE_BRICK_STAIRS)
		.slab(AbysmBlocks.FLOROPUMICE_BRICK_SLAB)
		.wall(AbysmBlocks.FLOROPUMICE_BRICK_WALL)
		.polished(AbysmBlocks.FLOROPUMICE_TILES)
		.build();

	public static final BlockFamily FLOROPUMICE_TILES = familyBuilder(AbysmBlocks.FLOROPUMICE_TILES)
		.stairs(AbysmBlocks.FLOROPUMICE_TILE_STAIRS)
		.slab(AbysmBlocks.FLOROPUMICE_TILE_SLAB)
		.wall(AbysmBlocks.FLOROPUMICE_TILE_WALL)
		.build();

	public static final BlockFamily SMOOTH_FLOROPUMICE = familyBuilder(AbysmBlocks.SMOOTH_FLOROPUMICE)
		.stairs(AbysmBlocks.SMOOTH_FLOROPUMICE_STAIRS)
		.slab(AbysmBlocks.SMOOTH_FLOROPUMICE_SLAB)
		.wall(AbysmBlocks.SMOOTH_FLOROPUMICE_WALL)
		.polished(AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE)
		.chiseled(AbysmBlocks.CHISELED_SMOOTH_FLOROPUMICE)
		.build();

	public static final BlockFamily SMOOTH_FLOROPUMICE_BRICKS = familyBuilder(AbysmBlocks.SMOOTH_FLOROPUMICE_BRICKS)
		.stairs(AbysmBlocks.SMOOTH_FLOROPUMICE_BRICK_STAIRS)
		.slab(AbysmBlocks.SMOOTH_FLOROPUMICE_BRICK_SLAB)
		.wall(AbysmBlocks.SMOOTH_FLOROPUMICE_BRICK_WALL)
		.cut(AbysmBlocks.CUT_SMOOTH_FLOROPUMICE)
		.build();

	public static final BlockFamily CUT_SMOOTH_FLOROPUMICE = familyBuilder(AbysmBlocks.CUT_SMOOTH_FLOROPUMICE)
		.stairs(AbysmBlocks.CUT_SMOOTH_FLOROPUMICE_STAIRS)
		.slab(AbysmBlocks.CUT_SMOOTH_FLOROPUMICE_SLAB)
		.build();

	public static BlockFamily.Builder familyBuilder(Block baseBlock) {
		BlockFamily.Builder builder = new BlockFamily.Builder(baseBlock);
		BlockFamily blockFamily = ABYSM_MAP.put(baseBlock, builder.build()); // "build" here is just a getter, so it's fine to get it twice
		if (blockFamily != null) {
			throw new IllegalStateException("Duplicate family definition for " + Registries.BLOCK.getKey(baseBlock));
		} else {
			return builder;
		}
	}

	public static Stream<BlockFamily> getAllAbysmBlockFamilies() {
		return ABYSM_MAP.values().stream();
	}
}
