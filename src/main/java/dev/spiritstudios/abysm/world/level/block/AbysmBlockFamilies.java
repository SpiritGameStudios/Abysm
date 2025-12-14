package dev.spiritstudios.abysm.world.level.block;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.level.block.Block;

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
		.getFamily();

	public static final BlockFamily FLOROPUMICE_BRICKS = familyBuilder(AbysmBlocks.FLOROPUMICE_BRICKS)
		.stairs(AbysmBlocks.FLOROPUMICE_BRICK_STAIRS)
		.slab(AbysmBlocks.FLOROPUMICE_BRICK_SLAB)
		.wall(AbysmBlocks.FLOROPUMICE_BRICK_WALL)
		.polished(AbysmBlocks.FLOROPUMICE_TILES)
		.cracked(AbysmBlocks.CRACKED_FLOROPUMICE_BRICKS)
		.getFamily();

	public static final BlockFamily FLOROPUMICE_TILES = familyBuilder(AbysmBlocks.FLOROPUMICE_TILES)
		.stairs(AbysmBlocks.FLOROPUMICE_TILE_STAIRS)
		.slab(AbysmBlocks.FLOROPUMICE_TILE_SLAB)
		.wall(AbysmBlocks.FLOROPUMICE_TILE_WALL)
		.cracked(AbysmBlocks.CRACKED_FLOROPUMICE_TILES)
		.getFamily();

	public static final BlockFamily SMOOTH_FLOROPUMICE = familyBuilder(AbysmBlocks.SMOOTH_FLOROPUMICE)
		.stairs(AbysmBlocks.SMOOTH_FLOROPUMICE_STAIRS)
		.slab(AbysmBlocks.SMOOTH_FLOROPUMICE_SLAB)
		.wall(AbysmBlocks.SMOOTH_FLOROPUMICE_WALL)
		.polished(AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE)
		.chiseled(AbysmBlocks.CHISELED_SMOOTH_FLOROPUMICE)
		.getFamily();

	public static final BlockFamily SMOOTH_FLOROPUMICE_BRICKS = familyBuilder(AbysmBlocks.SMOOTH_FLOROPUMICE_BRICKS)
		.stairs(AbysmBlocks.SMOOTH_FLOROPUMICE_BRICK_STAIRS)
		.slab(AbysmBlocks.SMOOTH_FLOROPUMICE_BRICK_SLAB)
		.wall(AbysmBlocks.SMOOTH_FLOROPUMICE_BRICK_WALL)
		.cut(AbysmBlocks.CUT_SMOOTH_FLOROPUMICE)
		.cracked(AbysmBlocks.CRACKED_SMOOTH_FLOROPUMICE_BRICKS)
		.getFamily();

	public static final BlockFamily CUT_SMOOTH_FLOROPUMICE = familyBuilder(AbysmBlocks.CUT_SMOOTH_FLOROPUMICE)
		.stairs(AbysmBlocks.CUT_SMOOTH_FLOROPUMICE_STAIRS)
		.slab(AbysmBlocks.CUT_SMOOTH_FLOROPUMICE_SLAB)
		.cracked(AbysmBlocks.CRACKED_CUT_SMOOTH_FLOROPUMICE)
		.getFamily();

	public static BlockFamily.Builder familyBuilder(Block baseBlock) {
		BlockFamily.Builder builder = new BlockFamily.Builder(baseBlock);
		BlockFamily blockFamily = ABYSM_MAP.put(baseBlock, builder.getFamily()); // "build" here is just a getter, so it's fine to get it twice
		if (blockFamily != null) {
			throw new IllegalStateException("Duplicate family definition for " + BuiltInRegistries.BLOCK.getResourceKey(baseBlock));
		} else {
			return builder;
		}
	}

	public static Stream<BlockFamily> getAllAbysmBlockFamilies() {
		return ABYSM_MAP.values().stream();
	}
}
