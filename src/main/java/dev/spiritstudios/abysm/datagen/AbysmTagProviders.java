package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.block.AbysmBlockFamilies;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.abysm.registry.AbysmItems;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AbysmTagProviders {
	public static void addAll(FabricDataGenerator.Pack pack) {
		pack.addProvider(BlockTagProvider::new);
		pack.addProvider(ItemTagProvider::new);
		pack.addProvider(BiomeTagProvider::new);
	}

	private static class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
		public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			// region Family tags
			addTagsForFamilies(false, true,
				AbysmBlockFamilies.FLOROPUMICE,
				AbysmBlockFamilies.FLOROPUMICE_BRICKS,
				AbysmBlockFamilies.FLOROPUMICE_TILES,
				AbysmBlockFamilies.SMOOTH_FLOROPUMICE,
				AbysmBlockFamilies.SMOOTH_FLOROPUMICE_BRICKS,
				AbysmBlockFamilies.CUT_SMOOTH_FLOROPUMICE
			);

			addFamiliesToTag(BlockTags.PICKAXE_MINEABLE,
				AbysmBlockFamilies.FLOROPUMICE,
				AbysmBlockFamilies.FLOROPUMICE_BRICKS,
				AbysmBlockFamilies.FLOROPUMICE_TILES,
				AbysmBlockFamilies.SMOOTH_FLOROPUMICE,
				AbysmBlockFamilies.SMOOTH_FLOROPUMICE_BRICKS,
				AbysmBlockFamilies.CUT_SMOOTH_FLOROPUMICE
			);
			// endregion

			// region Block tags
			getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
				.add(
					AbysmBlocks.POLISHED_FLOROPUMICE,
					AbysmBlocks.CHISLED_FLOROPUMICE,
					AbysmBlocks.ROSEBLOOMED_FLOROPUMICE,
					AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE,
					AbysmBlocks.CHISELED_SMOOTH_FLOROPUMICE,
					AbysmBlocks.SMOOTH_FLOROPUMICE_PILLAR
				);
			// endregion

			getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
				.add(
					AbysmBlocks.ROSY_BLOOMSHROOM_STEM,
					AbysmBlocks.ROSY_BLOOMSHROOM_CAP
				);

			getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
				.add(
					AbysmBlocks.BLOOMSHROOM_GOOP
				);

			getOrCreateTagBuilder(BlockTags.HOE_MINEABLE)
				.add(
					AbysmBlocks.ROSY_BLOOMSHROOM_STEM,
					AbysmBlocks.ROSY_BLOOMSHROOM_CAP,
					AbysmBlocks.BLOOMSHROOM_GOOP
				);
		}


		public void addFamiliesToTag(TagKey<Block> tag, BlockFamily... families) {
			for(BlockFamily family : families) {
				addFamilyToTag(family, tag);
			}
		}

		public void addFamilyToTag(BlockFamily family, TagKey<Block> tag) {
			FabricTagBuilder builder = getOrCreateTagBuilder(tag);
			builder.add(family.getBaseBlock());
			for(Block block : family.getVariants().values()) {
				builder.add(block);
			}
		}

		public void addTagsForFamilies(boolean isWooden, boolean isStone, BlockFamily... families) {
			for(BlockFamily family : families) {
				addTagsForFamily(family, isWooden, isStone);
			}
		}

		public void addTagsForFamily(BlockFamily family, boolean isWooden, boolean isStone) {
			addBlockToTags(family, BlockFamily.Variant.BUTTON, isWooden, isStone, BlockTags.BUTTONS, BlockTags.WOODEN_BUTTONS, BlockTags.STONE_BUTTONS);
			addBlockToTags(family, BlockFamily.Variant.PRESSURE_PLATE, isWooden, isStone, BlockTags.PRESSURE_PLATES, BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.STONE_PRESSURE_PLATES);

			addBlockToTags(family, BlockFamily.Variant.STAIRS, isWooden, BlockTags.STAIRS, BlockTags.WOODEN_STAIRS);
			addBlockToTags(family, BlockFamily.Variant.SLAB, isWooden, BlockTags.SLABS, BlockTags.WOODEN_SLABS);
			addBlockToTags(family, BlockFamily.Variant.FENCE, isWooden, BlockTags.FENCES, BlockTags.WOODEN_FENCES);
			addBlockToTags(family, BlockFamily.Variant.FENCE, isWooden, ConventionalBlockTags.FENCES, ConventionalBlockTags.WOODEN_FENCES);
			addBlockToTags(family, BlockFamily.Variant.DOOR, isWooden, BlockTags.DOORS, BlockTags.WOODEN_DOORS);
			addBlockToTags(family, BlockFamily.Variant.TRAPDOOR, isWooden, BlockTags.TRAPDOORS, BlockTags.WOODEN_TRAPDOORS);

			addBlockToTags(family, BlockFamily.Variant.FENCE_GATE, BlockTags.FENCE_GATES);
			addBlockToTags(family, BlockFamily.Variant.FENCE_GATE, isWooden, ConventionalBlockTags.FENCE_GATES, ConventionalBlockTags.WOODEN_FENCE_GATES);
			addBlockToTags(family, BlockFamily.Variant.WALL, BlockTags.WALLS);
		}

		public void addBlockToTags(BlockFamily family, BlockFamily.Variant variant, TagKey<Block> tag) {
			Block block = family.getVariant(variant);
			if(block != null) {
				getOrCreateTagBuilder(tag).add(block);
			}
		}

		public void addBlockToTags(BlockFamily family, BlockFamily.Variant variant, boolean isWooden, TagKey<Block> baseTag, TagKey<Block> woodTag) {
			addBlockToTags(family.getVariant(variant), isWooden, baseTag, woodTag);
		}

		public void addBlockToTags(@Nullable Block block, boolean isWooden, TagKey<Block> baseTag, TagKey<Block> woodTag) {
			if(block != null) {
				getOrCreateTagBuilder(baseTag).add(block);
				if (isWooden) {
					getOrCreateTagBuilder(woodTag).add(block);
				}
			}
		}

		public void addBlockToTags(BlockFamily family, BlockFamily.Variant variant, boolean isWooden, boolean isStone, TagKey<Block> baseTag, TagKey<Block> woodTag, TagKey<Block> stoneTag) {
			addBlockToTags(family.getVariant(variant), isWooden, isStone, baseTag, woodTag, stoneTag);
		}

		public void addBlockToTags(@Nullable Block block, boolean isWooden, boolean isStone, TagKey<Block> baseTag, TagKey<Block> woodTag, TagKey<Block> stoneTag) {
			if(block != null) {
				getOrCreateTagBuilder(baseTag).add(block);
				if (isWooden) {
					getOrCreateTagBuilder(woodTag).add(block);
				}
				if(isStone) {
					getOrCreateTagBuilder(stoneTag).add(block);
				}
			}
		}
	}

	private static class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
		public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			getOrCreateTagBuilder(ItemTags.FOOT_ARMOR)
				.add(AbysmItems.FLIPPERS);
		}
	}

	private static class BiomeTagProvider extends FabricTagProvider<Biome> {
		public BiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, RegistryKeys.BIOME, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			getOrCreateTagBuilder(BiomeTags.IS_OVERWORLD)
				.add(AbysmBiomes.FLORAL_REEF);
		}
	}
}
