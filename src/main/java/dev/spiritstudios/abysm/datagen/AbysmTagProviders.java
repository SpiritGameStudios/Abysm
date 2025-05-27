package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.block.AbysmBlockFamilies;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.abysm.registry.AbysmItems;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
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
		BlockTagProvider blockTagProvider = pack.addProvider(BlockTagProvider::new);
		pack.addProvider(((output, registriesFuture) -> new ItemTagProvider(output, registriesFuture, blockTagProvider)));
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

			getOrCreateTagBuilder(BlockTags.REPLACEABLE_BY_TREES)
				.add(
					AbysmBlocks.ROSY_SPRIGS
				);

			getOrCreateTagBuilder(BlockTags.REPLACEABLE_BY_MUSHROOMS)
				.add(
					AbysmBlocks.ROSY_SPRIGS
				);

			getOrCreateTagBuilder(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)
				.add(
					AbysmBlocks.ROSY_SPRIGS
				);

			getOrCreateTagBuilder(BlockTags.ENDERMAN_HOLDABLE)
				.add(
					AbysmBlocks.ROSY_SPRIGS,
					AbysmBlocks.ROSY_BLOOMSHROOM
				);

			getOrCreateTagBuilder(BlockTags.FLOWERS)
				.add(
					AbysmBlocks.ROSY_BLOOMSHROOM
				);

			getOrCreateTagBuilder(BlockTags.SMALL_FLOWERS)
				.add(
					AbysmBlocks.ROSY_BLOOMSHROOM
				);

			getOrCreateTagBuilder(BlockTags.FLOWER_POTS)
				.add(
					AbysmBlocks.POTTED_ROSY_SPRIGS,
					AbysmBlocks.POTTED_ROSY_BLOOMSHROOM
				);

			getOrCreateTagBuilder(ConventionalBlockTags.FLOWERS)
				.add(
					AbysmBlocks.ROSY_BLOOMSHROOM
				);

			getOrCreateTagBuilder(ConventionalBlockTags.SMALL_FLOWERS)
				.add(
					AbysmBlocks.ROSY_BLOOMSHROOM
				);
			// endregion
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
		public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, @Nullable FabricTagProvider.BlockTagProvider blockTagProvider) {
			super(output, completableFuture, blockTagProvider);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			getOrCreateTagBuilder(ItemTags.FOOT_ARMOR)
				.add(AbysmItems.FLIPPERS);

			// region copy block tags
			this.copy(BlockTags.BUTTONS, ItemTags.BUTTONS);
			this.copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
			this.copy(BlockTags.STONE_BUTTONS, ItemTags.STONE_BUTTONS);
			this.copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
			this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
			this.copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
			this.copy(BlockTags.SLABS, ItemTags.SLABS);
			this.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
			this.copy(BlockTags.FENCES, ItemTags.FENCES);
			this.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
			this.copy(BlockTags.DOORS, ItemTags.DOORS);
			this.copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
			this.copy(BlockTags.TRAPDOORS, ItemTags.TRAPDOORS);
			this.copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
			this.copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
			this.copy(BlockTags.WALLS, ItemTags.WALLS);

			this.copy(BlockTags.FLOWERS, ItemTags.FLOWERS);
			this.copy(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS);

			this.copy(ConventionalBlockTags.WOODEN_FENCES, ConventionalItemTags.WOODEN_FENCES);
			this.copy(ConventionalBlockTags.WOODEN_FENCE_GATES, ConventionalItemTags.WOODEN_FENCE_GATES);

			this.copy(ConventionalBlockTags.FLOWERS, ConventionalItemTags.FLOWERS);
			this.copy(ConventionalBlockTags.SMALL_FLOWERS, ConventionalItemTags.SMALL_FLOWERS);
			// endregion
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
