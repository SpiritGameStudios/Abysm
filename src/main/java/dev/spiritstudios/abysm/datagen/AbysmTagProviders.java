package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.block.AbysmBlockFamilies;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.entity.AbysmDamageTypes;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.registry.tags.AbysmBiomeTags;
import dev.spiritstudios.abysm.registry.tags.AbysmBlockTags;
import dev.spiritstudios.abysm.registry.tags.AbysmEntityTypeTags;
import dev.spiritstudios.abysm.registry.tags.AbysmItemTags;
import dev.spiritstudios.abysm.registry.tags.AbysmSoundEventTags;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.data.tag.ProvidedTagBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AbysmTagProviders {
	public static void addAll(FabricDataGenerator.Pack pack) {
		BlockTagProvider blockTagProvider = pack.addProvider(BlockTagProvider::new);
		pack.addProvider(((output, registriesFuture) -> new ItemTagProvider(output, registriesFuture, blockTagProvider)));
		pack.addProvider(BiomeTagProvider::new);
		pack.addProvider(EntityTypeTagProvider::new);
		pack.addProvider(DamageTypeTagProvider::new);
		pack.addProvider(SoundEventTagProvider::new);
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
			valueLookupBuilder(BlockTags.PICKAXE_MINEABLE)
				.add(
					AbysmBlocks.POLISHED_FLOROPUMICE,
					AbysmBlocks.CHISLED_FLOROPUMICE,
					AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE,
					AbysmBlocks.CHISELED_SMOOTH_FLOROPUMICE,
					AbysmBlocks.SMOOTH_FLOROPUMICE_PILLAR,
					AbysmBlocks.CHISELED_SILT,
					AbysmBlocks.CUT_SILT
				)
				.addOptionalTag(AbysmBlockTags.BLOOMED_FLOROPUMICE)
				.add(AbysmBlocks.OOZETRICKLE_CORD)
				.add(AbysmBlocks.OOZETRICKLE_LANTERN);

			valueLookupBuilder(BlockTags.AXE_MINEABLE)
				.addOptionalTag(AbysmBlockTags.BLOOMSHROOM_STEMS)
				.addOptionalTag(AbysmBlockTags.BLOOMSHROOM_CAPS)
				.add(AbysmBlocks.OOZETRICKLE_CORD)
				.add(AbysmBlocks.OOZETRICKLE_LANTERN)
				.add(AbysmBlocks.MONARE_VASE_BLOCK);

			valueLookupBuilder(BlockTags.SHOVEL_MINEABLE)
				.addOptionalTag(AbysmBlockTags.NECTARSAP)
				.add(
					AbysmBlocks.DREGLOAM,
					AbysmBlocks.OOZING_DREGLOAM,
					AbysmBlocks.DREGLOAM_OOZE,
					AbysmBlocks.DREGLOAM_GOLDEN_LAZULI_ORE,
					AbysmBlocks.SILT
				);

			valueLookupBuilder(BlockTags.HOE_MINEABLE)
				.addOptionalTag(AbysmBlockTags.NECTARSAP)
				.addOptionalTag(AbysmBlockTags.BLOOMSHROOM_STEMS)
				.addOptionalTag(AbysmBlockTags.BLOOMSHROOM_CAPS)
				.add(AbysmBlocks.OOZETRICKLE_CORD)
				.add(AbysmBlocks.OOZETRICKLE_LANTERN)
				.add(AbysmBlocks.MONARE_VASE_PETAL);

			valueLookupBuilder(BlockTags.REPLACEABLE_BY_TREES)
				.addOptionalTag(AbysmBlockTags.FLOWERY_SPRIGS)
				.addOptionalTag(AbysmBlockTags.BLOOM_PETALS)
				.add(
					AbysmBlocks.OOZETRICKLE_FILAMENTS,
					AbysmBlocks.TALL_OOZETRICKLE_FILAMENTS,
					AbysmBlocks.BRINE_BRACKEN,
					AbysmBlocks.BRINE
				);

			valueLookupBuilder(BlockTags.REPLACEABLE_BY_MUSHROOMS)
				.addOptionalTag(AbysmBlockTags.FLOWERY_SPRIGS)
				.addOptionalTag(AbysmBlockTags.BLOOM_PETALS)
				.add(
					AbysmBlocks.OOZETRICKLE_FILAMENTS,
					AbysmBlocks.TALL_OOZETRICKLE_FILAMENTS,
					AbysmBlocks.BRINE_BRACKEN,
					AbysmBlocks.BRINE
				);

			valueLookupBuilder(BlockTags.GEODE_INVALID_BLOCKS)
				.add(AbysmBlocks.BRINE);

			valueLookupBuilder(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)
				.addOptionalTag(AbysmBlockTags.FLOWERY_SPRIGS)
				.add(
					AbysmBlocks.OOZETRICKLE_FILAMENTS
				)
				.add(AbysmBlocks.BRINE_BRACKEN)
				.add(AbysmBlocks.BRINE);

			valueLookupBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
				.addOptionalTag(AbysmBlockTags.BLOOM_PETALS);

			valueLookupBuilder(BlockTags.ENDERMAN_HOLDABLE)
				.addOptionalTag(AbysmBlockTags.SMALL_BLOOMSHROOMS)
				.addOptionalTag(AbysmBlockTags.FLOWERY_SPRIGS)
				.add(
					AbysmBlocks.OOZETRICKLE_FILAMENTS
				)
				.add(AbysmBlocks.BRINE_BRACKEN);

			valueLookupBuilder(BlockTags.SCULK_REPLACEABLE)
				.add(
					AbysmBlocks.FLOROPUMICE
				)
				.addOptionalTag(AbysmBlockTags.BLOOMED_FLOROPUMICE);

			valueLookupBuilder(BlockTags.SCULK_REPLACEABLE_WORLD_GEN)
				.add(
					AbysmBlocks.FLOROPUMICE
				)
				.addOptionalTag(AbysmBlockTags.BLOOMED_FLOROPUMICE);

			valueLookupBuilder(BlockTags.SMALL_DRIPLEAF_PLACEABLE)
				.add(
					AbysmBlocks.DREGLOAM,
					AbysmBlocks.OOZING_DREGLOAM,
					AbysmBlocks.DREGLOAM_OOZE,
					AbysmBlocks.DREGLOAM_GOLDEN_LAZULI_ORE
				);

			valueLookupBuilder(BlockTags.DRAGON_IMMUNE)
				.add(
					AbysmBlocks.DENSITY_BLOB_BLOCK
				);

			valueLookupBuilder(BlockTags.WITHER_IMMUNE)
				.add(
					AbysmBlocks.DENSITY_BLOB_BLOCK
				);


			valueLookupBuilder(BlockTags.DIRT)
				.add(
					AbysmBlocks.DREGLOAM,
					AbysmBlocks.OOZING_DREGLOAM,
					AbysmBlocks.DREGLOAM_OOZE,
					AbysmBlocks.DREGLOAM_GOLDEN_LAZULI_ORE
				);

			valueLookupBuilder(BlockTags.LEAVES)
				.add(
					AbysmBlocks.ROSEBLOOM_PETALEAVES,
					AbysmBlocks.SUNBLOOM_PETALEAVES,
					AbysmBlocks.MALLOWBLOOM_PETALEAVES
				);

			valueLookupBuilder(BlockTags.FLOWERS)
				.addOptionalTag(AbysmBlockTags.BLOOM_PETALS)
				.addOptionalTag(AbysmBlockTags.SMALL_BLOOMSHROOMS)
				.addOptionalTag(AbysmBlockTags.BLOOMING_CROWNS)
				.addOptionalTag(AbysmBlockTags.SCABIOSAS);

			valueLookupBuilder(BlockTags.SMALL_FLOWERS)
				.addOptionalTag(AbysmBlockTags.SMALL_BLOOMSHROOMS);

			valueLookupBuilder(BlockTags.FLOWER_POTS)
				.add(
					AbysmBlocks.POTTED_ROSY_SPRIGS,
					AbysmBlocks.POTTED_SUNNY_SPRIGS,
					AbysmBlocks.POTTED_MAUVE_SPRIGS,

					AbysmBlocks.POTTED_ROSY_BLOOMSHROOM,
					AbysmBlocks.POTTED_SUNNY_BLOOMSHROOM,
					AbysmBlocks.POTTED_MAUVE_BLOOMSHROOM,

					AbysmBlocks.POTTED_ANTENNAE_PLANT,

					AbysmBlocks.POTTED_OOZETRICKLE_FILAMENTS
				);

			// conventional tags
			valueLookupBuilder(ConventionalBlockTags.FLOWERS)
				.addOptionalTag(AbysmBlockTags.SMALL_BLOOMSHROOMS)
				.addOptionalTag(AbysmBlockTags.BLOOMING_CROWNS)
				.addOptionalTag(AbysmBlockTags.SCABIOSAS)
				.add(AbysmBlocks.MONARE_VASE);

			valueLookupBuilder(ConventionalBlockTags.SMALL_FLOWERS)
				.addOptionalTag(AbysmBlockTags.SMALL_BLOOMSHROOMS)
				.add(AbysmBlocks.MONARE_VASE);

			// abysm tags
			valueLookupBuilder(AbysmBlockTags.BLOOMSHROOM_PLANTABLE_ON)
				.add(
					AbysmBlocks.FLOROPUMICE,
					Blocks.SAND
				)
				.addOptionalTag(AbysmBlockTags.BLOOMED_FLOROPUMICE);

			valueLookupBuilder(AbysmBlockTags.OOZE_VEGETATION_PLANTABLE_ON)
				.add(
					AbysmBlocks.OOZING_DREGLOAM,
					AbysmBlocks.DREGLOAM_OOZE
				);

			valueLookupBuilder(AbysmBlockTags.ALSO_PRESERVES_LEAVES)
				.addOptionalTag(AbysmBlockTags.BLOOMSHROOM_CAPS)
				.addOptionalTag(AbysmBlockTags.BLOOMSHROOM_STEMS)
				.addOptionalTag(AbysmBlockTags.NECTARSAP);

			valueLookupBuilder(AbysmBlockTags.IS_AIR_OR_WATER)
				.addOptionalTag(BlockTags.AIR)
				.add(Blocks.WATER);

			valueLookupBuilder(AbysmBlockTags.OOZE_REPLACEABLE)
				.add(AbysmBlocks.DREGLOAM);

			valueLookupBuilder(AbysmBlockTags.BLOOMED_FLOROPUMICE_REPLACEABLE)
				.add(AbysmBlocks.FLOROPUMICE);


			valueLookupBuilder(AbysmBlockTags.BLOOMED_FLOROPUMICE)
				.add(
					AbysmBlocks.ROSEBLOOMED_FLOROPUMICE,
					AbysmBlocks.SUNBLOOMED_FLOROPUMICE,
					AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE
				);

			valueLookupBuilder(AbysmBlockTags.FLOWERY_SPRIGS)
				.add(
					AbysmBlocks.ROSY_SPRIGS,
					AbysmBlocks.SUNNY_SPRIGS,
					AbysmBlocks.MAUVE_SPRIGS
				);

			valueLookupBuilder(AbysmBlockTags.BLOOM_PETALS)
				.add(
					AbysmBlocks.ROSEBLOOM_PETALS,
					AbysmBlocks.SUNBLOOM_PETALS,
					AbysmBlocks.MALLOWBLOOM_PETALS
				);

			valueLookupBuilder(AbysmBlockTags.SMALL_BLOOMSHROOMS)
				.add(
					AbysmBlocks.ROSY_BLOOMSHROOM,
					AbysmBlocks.SUNNY_BLOOMSHROOM,
					AbysmBlocks.MAUVE_BLOOMSHROOM
				);

			valueLookupBuilder(AbysmBlockTags.BLOOMSHROOM_STEMS)
				.addOptionalTag(AbysmBlockTags.ROSY_BLOOMSHROOM_STEMS)
				.addOptionalTag(AbysmBlockTags.SUNNY_BLOOMSHROOM_STEMS)
				.addOptionalTag(AbysmBlockTags.MAUVE_BLOOMSHROOM_STEMS);

			valueLookupBuilder(AbysmBlockTags.ROSY_BLOOMSHROOM_STEMS)
				.add(
					AbysmBlocks.ROSY_BLOOMSHROOM_STEM,
					AbysmBlocks.ROSY_BLOOMSHROOM_HYPHAE
				);

			valueLookupBuilder(AbysmBlockTags.SUNNY_BLOOMSHROOM_STEMS)
				.add(
					AbysmBlocks.SUNNY_BLOOMSHROOM_STEM,
					AbysmBlocks.SUNNY_BLOOMSHROOM_HYPHAE
				);

			valueLookupBuilder(AbysmBlockTags.MAUVE_BLOOMSHROOM_STEMS)
				.add(
					AbysmBlocks.MAUVE_BLOOMSHROOM_STEM,
					AbysmBlocks.MAUVE_BLOOMSHROOM_HYPHAE
				);

			valueLookupBuilder(AbysmBlockTags.BLOOMSHROOM_CAPS)
				.add(
					AbysmBlocks.ROSY_BLOOMSHROOM_CAP,
					AbysmBlocks.SUNNY_BLOOMSHROOM_CAP,
					AbysmBlocks.MAUVE_BLOOMSHROOM_CAP
				);

			valueLookupBuilder(AbysmBlockTags.NECTARSAP)
				.add(
					AbysmBlocks.SWEET_NECTARSAP,
					AbysmBlocks.SOUR_NECTARSAP,
					AbysmBlocks.BITTER_NECTARSAP
				);

			valueLookupBuilder(AbysmBlockTags.BLOOMING_CROWNS)
				.add(
					AbysmBlocks.BLOOMING_SODALITE_CROWN,
					AbysmBlocks.BLOOMING_ANYOLITE_CROWN,
					AbysmBlocks.BLOOMING_MELILITE_CROWN
				);

			valueLookupBuilder(AbysmBlockTags.SCABIOSAS)
				.add(
					AbysmBlocks.WHITE_SCABIOSA,
					AbysmBlocks.ORANGE_SCABIOSA,
					AbysmBlocks.MAGENTA_SCABIOSA,
					AbysmBlocks.LIGHT_BLUE_SCABIOSA,
					AbysmBlocks.YELLOW_SCABIOSA,
					AbysmBlocks.LIME_SCABIOSA,
					AbysmBlocks.PINK_SCABIOSA,
					AbysmBlocks.GREY_SCABIOSA,
					AbysmBlocks.LIGHT_GREY_SCABIOSA,
					AbysmBlocks.CYAN_SCABIOSA,
					AbysmBlocks.PURPLE_SCABIOSA,
					AbysmBlocks.BLUE_SCABIOSA,
					AbysmBlocks.BROWN_SCABIOSA,
					AbysmBlocks.GREEN_SCABIOSA,
					AbysmBlocks.RED_SCABIOSA,
					AbysmBlocks.BLACK_SCABIOSA
				);

			this.valueLookupBuilder(AbysmBlockTags.DEEP_SEA_RUINS_REPLACEABLE)
				.add(
					AbysmBlocks.SMOOTH_FLOROPUMICE,
					AbysmBlocks.SMOOTH_FLOROPUMICE_SLAB,
					AbysmBlocks.SMOOTH_FLOROPUMICE_STAIRS,
					AbysmBlocks.SMOOTH_FLOROPUMICE_WALL,

					AbysmBlocks.SMOOTH_FLOROPUMICE_BRICKS,
					AbysmBlocks.SMOOTH_FLOROPUMICE_BRICK_SLAB,
					AbysmBlocks.SMOOTH_FLOROPUMICE_BRICK_STAIRS,
					AbysmBlocks.SMOOTH_FLOROPUMICE_BRICK_WALL,

					AbysmBlocks.FLOROPUMICE_TILES,
					AbysmBlocks.FLOROPUMICE_TILE_SLAB,
					AbysmBlocks.FLOROPUMICE_TILE_STAIRS,
					AbysmBlocks.FLOROPUMICE_TILE_WALL,

					AbysmBlocks.CUT_SMOOTH_FLOROPUMICE,
					AbysmBlocks.CUT_SMOOTH_FLOROPUMICE_SLAB,
					AbysmBlocks.CUT_SMOOTH_FLOROPUMICE_STAIRS,

					AbysmBlocks.FLOROPUMICE_BRICKS,
					AbysmBlocks.FLOROPUMICE_BRICK_SLAB,
					AbysmBlocks.FLOROPUMICE_BRICK_STAIRS,
					AbysmBlocks.FLOROPUMICE_BRICK_WALL,

					AbysmBlocks.CHISLED_FLOROPUMICE
				);
			// endregion
		}


		public void addFamiliesToTag(TagKey<Block> tag, BlockFamily... families) {
			for (BlockFamily family : families) {
				addFamilyToTag(family, tag);
			}
		}

		public void addFamilyToTag(BlockFamily family, TagKey<Block> tag) {
			ProvidedTagBuilder<Block, Block> builder = valueLookupBuilder(tag);

			// family.getVariants() gives a HashMap, so sort it for consistent ordering
			Collection<Block> blocks = family.getVariants().values();
			List<Block> blocksSorted = blocks.stream().sorted((b1, b2) -> {
				char[] c1 = b1.getTranslationKey().toCharArray();
				char[] c2 = b2.getTranslationKey().toCharArray();
				return Arrays.compare(c1, c2);
			}).toList();

			builder.add(family.getBaseBlock());
			for (Block block : blocksSorted) {
				builder.add(block);
			}
		}

		public void addTagsForFamilies(boolean isWooden, boolean isStone, BlockFamily... families) {
			for (BlockFamily family : families) {
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
			if (block != null) {
				valueLookupBuilder(tag).add(block);
			}
		}

		public void addBlockToTags(BlockFamily family, BlockFamily.Variant variant, boolean isWooden, TagKey<Block> baseTag, TagKey<Block> woodTag) {
			addBlockToTags(family.getVariant(variant), isWooden, baseTag, woodTag);
		}

		public void addBlockToTags(@Nullable Block block, boolean isWooden, TagKey<Block> baseTag, TagKey<Block> woodTag) {
			if (block != null) {
				valueLookupBuilder(baseTag).add(block);
				if (isWooden) {
					valueLookupBuilder(woodTag).add(block);
				}
			}
		}

		public void addBlockToTags(BlockFamily family, BlockFamily.Variant variant, boolean isWooden, boolean isStone, TagKey<Block> baseTag, TagKey<Block> woodTag, TagKey<Block> stoneTag) {
			addBlockToTags(family.getVariant(variant), isWooden, isStone, baseTag, woodTag, stoneTag);
		}

		public void addBlockToTags(@Nullable Block block, boolean isWooden, boolean isStone, TagKey<Block> baseTag, TagKey<Block> woodTag, TagKey<Block> stoneTag) {
			if (block != null) {
				valueLookupBuilder(baseTag).add(block);
				if (isWooden) {
					valueLookupBuilder(woodTag).add(block);
				}
				if (isStone) {
					valueLookupBuilder(stoneTag).add(block);
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
			// region vanilla item tags
			valueLookupBuilder(ItemTags.FOOT_ARMOR_ENCHANTABLE)
				.add(
					AbysmItems.FLIPPERS,
					AbysmItems.DIVING_BOOTS
				);

			valueLookupBuilder(ItemTags.LEG_ARMOR_ENCHANTABLE)
				.add(
					AbysmItems.DIVING_LEGGINGS
				);

			valueLookupBuilder(ItemTags.CHEST_ARMOR_ENCHANTABLE)
				.add(
					AbysmItems.DIVING_CHESTPLATE
				);

			valueLookupBuilder(ItemTags.HEAD_ARMOR_ENCHANTABLE)
				.add(
					AbysmItems.DIVING_HELMET
				);

			valueLookupBuilder(ItemTags.ARMOR_ENCHANTABLE)
				.add(
					AbysmItems.FLIPPERS,
					AbysmItems.DIVING_BOOTS,
					AbysmItems.DIVING_LEGGINGS,
					AbysmItems.DIVING_CHESTPLATE,
					AbysmItems.DIVING_HELMET
				);

			valueLookupBuilder(ItemTags.DURABILITY_ENCHANTABLE)
				.add(
					AbysmItems.FLIPPERS,
					AbysmItems.DIVING_BOOTS,
					AbysmItems.DIVING_LEGGINGS,
					AbysmItems.DIVING_CHESTPLATE,
					AbysmItems.DIVING_HELMET
				);

			valueLookupBuilder(ItemTags.EQUIPPABLE_ENCHANTABLE)
				.add(
					AbysmItems.FLIPPERS,
					AbysmItems.DIVING_BOOTS,
					AbysmItems.DIVING_LEGGINGS,
					AbysmItems.DIVING_CHESTPLATE,
					AbysmItems.DIVING_HELMET
				);

			valueLookupBuilder(ItemTags.VANISHING_ENCHANTABLE)
				.add(
					AbysmItems.HARPOON
				);

			valueLookupBuilder(ItemTags.FISHES)
				.add(
					AbysmItems.SMALL_FLORAL_FISH,
					AbysmItems.BIG_FLORAL_FISH
				);

			valueLookupBuilder(ItemTags.WOLF_FOOD)
				.add(
					AbysmItems.SMALL_FLORAL_FISH,
					AbysmItems.BIG_FLORAL_FISH
				);

			valueLookupBuilder(ConventionalItemTags.BUCKETS).add(AbysmItems.BRINE_BUCKET);
			// endregion

			// region abysm item tags
			valueLookupBuilder(AbysmItemTags.HARPOON_ENCHANTABLE)
				.add(
					AbysmItems.HARPOON
				);

			valueLookupBuilder(AbysmItemTags.DIVING_SUIT_HELMETS).add(AbysmItems.DIVING_HELMET);
			valueLookupBuilder(AbysmItemTags.DIVING_SUIT_CHESTPLATES).add(AbysmItems.DIVING_CHESTPLATE);
			valueLookupBuilder(AbysmItemTags.DIVING_SUIT_LEGGINGS).add(AbysmItems.DIVING_LEGGINGS);
			valueLookupBuilder(AbysmItemTags.DIVING_SUIT_BOOTS).add(AbysmItems.DIVING_BOOTS);
			// endregion

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

			this.copy(BlockTags.DIRT, ItemTags.DIRT);

			this.copy(BlockTags.LEAVES, ItemTags.LEAVES);
			this.copy(BlockTags.FLOWERS, ItemTags.FLOWERS);
			this.copy(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS);

			this.copy(ConventionalBlockTags.WOODEN_FENCES, ConventionalItemTags.WOODEN_FENCES);
			this.copy(ConventionalBlockTags.WOODEN_FENCE_GATES, ConventionalItemTags.WOODEN_FENCE_GATES);

			this.copy(ConventionalBlockTags.FLOWERS, ConventionalItemTags.FLOWERS);
			this.copy(ConventionalBlockTags.SMALL_FLOWERS, ConventionalItemTags.SMALL_FLOWERS);

			this.copy(AbysmBlockTags.BLOOMED_FLOROPUMICE, AbysmItemTags.BLOOMED_FLOROPUMICE);
			this.copy(AbysmBlockTags.FLOWERY_SPRIGS, AbysmItemTags.FLOWERY_SPRIGS);
			this.copy(AbysmBlockTags.BLOOM_PETALS, AbysmItemTags.BLOOM_PETALS);
			this.copy(AbysmBlockTags.SMALL_BLOOMSHROOMS, AbysmItemTags.SMALL_BLOOMSHROOMS);
			this.copy(AbysmBlockTags.BLOOMSHROOM_STEMS, AbysmItemTags.BLOOMSHROOM_STEMS);
			this.copy(AbysmBlockTags.ROSY_BLOOMSHROOM_STEMS, AbysmItemTags.ROSY_BLOOMSHROOM_STEMS);
			this.copy(AbysmBlockTags.SUNNY_BLOOMSHROOM_STEMS, AbysmItemTags.SUNNY_BLOOMSHROOM_STEMS);
			this.copy(AbysmBlockTags.MAUVE_BLOOMSHROOM_STEMS, AbysmItemTags.MAUVE_BLOOMSHROOM_STEMS);
			this.copy(AbysmBlockTags.BLOOMSHROOM_CAPS, AbysmItemTags.BLOOMSHROOM_CAPS);
			this.copy(AbysmBlockTags.NECTARSAP, AbysmItemTags.NECTARSAP);
			this.copy(AbysmBlockTags.BLOOMING_CROWNS, AbysmItemTags.BLOOMING_CROWNS);
			this.copy(AbysmBlockTags.SCABIOSAS, AbysmItemTags.SCABIOSAS);
			// endregion
		}
	}

	private static class BiomeTagProvider extends FabricTagProvider<Biome> {
		public BiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, RegistryKeys.BIOME, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			// region vanilla tags
			builder(BiomeTags.IS_OVERWORLD)
				.add(AbysmBiomes.FLORAL_REEF)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(BiomeTags.MINESHAFT_BLOCKING)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(BiomeTags.WATER_ON_MAP_OUTLINES) // add these biomes here since they aren't in IS_OCEAN
				.add(AbysmBiomes.FLORAL_REEF)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(BiomeTags.SPAWNS_COLD_VARIANT_FROGS)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS)
				.add(AbysmBiomes.FLORAL_REEF);
			// endregion

			// region convention tags
			builder(ConventionalBiomeTags.IS_OVERWORLD)
				.add(AbysmBiomes.FLORAL_REEF)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(ConventionalBiomeTags.IS_TEMPERATE)
				.add(AbysmBiomes.FLORAL_REEF)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(ConventionalBiomeTags.IS_WET)
				.add(AbysmBiomes.FLORAL_REEF)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(ConventionalBiomeTags.IS_WET_OVERWORLD)
				.add(AbysmBiomes.FLORAL_REEF)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(ConventionalBiomeTags.IS_TEMPERATE_OVERWORLD)
				.add(AbysmBiomes.FLORAL_REEF)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(ConventionalBiomeTags.IS_OCEAN) // Using only the conventional tag here since the vanilla one overrides the music
				.add(AbysmBiomes.FLORAL_REEF)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(ConventionalBiomeTags.IS_DEEP_OCEAN)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(ConventionalBiomeTags.IS_UNDERGROUND)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(ConventionalBiomeTags.IS_MAGICAL)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(ConventionalBiomeTags.IS_RARE)
				.add(AbysmBiomes.DEEP_SEA_RUINS);

			builder(ConventionalBiomeTags.IS_FLORAL)
				.add(AbysmBiomes.FLORAL_REEF);

			builder(ConventionalBiomeTags.IS_AQUATIC)
				.add(AbysmBiomes.FLORAL_REEF)
				.add(AbysmBiomes.DEEP_SEA_RUINS);
			// endregion

			// region abysm tags
			builder(AbysmBiomeTags.DEEP_SEA_RUINS_HAS_STRUCTURE)
				.addOptionalTag(BiomeTags.IS_DEEP_OCEAN);

			builder(AbysmBiomeTags.SPAWNS_VARIANT_DEPTH_SNAPPER)
				.add(AbysmBiomes.DEEP_SEA_RUINS);
			// endregion
		}
	}

	private static class EntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
		public EntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			// region vanilla entity tags
			valueLookupBuilder(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
				.add(
					AbysmEntityTypes.SMALL_FLORAL_FISH,
					AbysmEntityTypes.BIG_FLORAL_FISH,
					AbysmEntityTypes.PADDLEFISH
				);

			valueLookupBuilder(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
				.add(
					AbysmEntityTypes.SMALL_FLORAL_FISH,
					AbysmEntityTypes.BIG_FLORAL_FISH,
					AbysmEntityTypes.PADDLEFISH,
					AbysmEntityTypes.SNAPPER,
					AbysmEntityTypes.GUP_GUP,
					AbysmEntityTypes.AROWANA_MAGICII,
					AbysmEntityTypes.BLOOMRAY,
					AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY,
					AbysmEntityTypes.MAN_O_WAR,
					AbysmEntityTypes.LECTORFIN,
					AbysmEntityTypes.MYSTERIOUS_BLOB,
					AbysmEntityTypes.TEST_LEVIATHAN,
					AbysmEntityTypes.RETICULATED_FLIPRAY,
					AbysmEntityTypes.SKELETON_SHARK
				);

			valueLookupBuilder(EntityTypeTags.AQUATIC)
				.add(
					AbysmEntityTypes.SMALL_FLORAL_FISH,
					AbysmEntityTypes.BIG_FLORAL_FISH,
					AbysmEntityTypes.PADDLEFISH,
					AbysmEntityTypes.SNAPPER,
					AbysmEntityTypes.GUP_GUP,
					AbysmEntityTypes.AROWANA_MAGICII,
					AbysmEntityTypes.BLOOMRAY,
					AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY,
					AbysmEntityTypes.MAN_O_WAR,
					AbysmEntityTypes.LECTORFIN,
					AbysmEntityTypes.MYSTERIOUS_BLOB,
					AbysmEntityTypes.TEST_LEVIATHAN,
					AbysmEntityTypes.RETICULATED_FLIPRAY,
					AbysmEntityTypes.SKELETON_SHARK
				);

			valueLookupBuilder(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH)
				.add(
					AbysmEntityTypes.SMALL_FLORAL_FISH,
					AbysmEntityTypes.BIG_FLORAL_FISH,
					AbysmEntityTypes.PADDLEFISH,
					AbysmEntityTypes.GUP_GUP,
					AbysmEntityTypes.AROWANA_MAGICII,
					AbysmEntityTypes.LECTORFIN
				);

			valueLookupBuilder(EntityTypeTags.IMPACT_PROJECTILES)
				.add(
					AbysmEntityTypes.FLYING_HARPOON
				);

			valueLookupBuilder(ConventionalEntityTypeTags.BOSSES)
				.add(AbysmEntityTypes.MYSTERIOUS_BLOB);
			// endregion

			// region abysm entity tags
			valueLookupBuilder(AbysmEntityTypeTags.MAN_O_WAR_FRIEND)
				.add(
					EntityType.PUFFERFISH,
					AbysmEntityTypes.MAN_O_WAR,
					AbysmEntityTypes.SKELETON_SHARK
				);

			valueLookupBuilder(AbysmEntityTypeTags.MAN_O_WAR_PREY)
				.add(
					EntityType.SALMON,
					EntityType.COD,
					EntityType.TROPICAL_FISH,
					AbysmEntityTypes.SMALL_FLORAL_FISH,
					AbysmEntityTypes.BIG_FLORAL_FISH,
					AbysmEntityTypes.LECTORFIN
				);

			valueLookupBuilder(AbysmEntityTypeTags.HARPOON_UNHAULABLE)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.add(
					EntityType.ENDER_DRAGON,
					EntityType.GIANT,
					EntityType.WARDEN,
					EntityType.WITHER,
					AbysmEntityTypes.MYSTERIOUS_BLOB
				);

			valueLookupBuilder(AbysmEntityTypeTags.IMMUNE_TO_SALINATION)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.add(
					EntityType.WARDEN,
					EntityType.CREAKING,
					EntityType.GLOW_SQUID
				);

			valueLookupBuilder(AbysmEntityTypeTags.NO_SALINATION_CONVULSING_MOBS)
				.addTag(AbysmEntityTypeTags.IMMUNE_TO_SALINATION)
				.add(EntityType.IRON_GOLEM);

			valueLookupBuilder(AbysmEntityTypeTags.LEHYDRATHAN_HUNT_TARGETS)
				.add(EntityType.GLOW_SQUID)
				.add(AbysmEntityTypes.SKELETON_SHARK);

			valueLookupBuilder(EntityTypeTags.UNDEAD)
				.add(AbysmEntityTypes.SKELETON_SHARK);
			// endregion
		}
	}

	private static class DamageTypeTagProvider extends FabricTagProvider<DamageType> {

		public DamageTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			builder(DamageTypeTags.IS_PROJECTILE)
				.add(AbysmDamageTypes.HARPOON);

			builder(DamageTypeTags.ALWAYS_KILLS_ARMOR_STANDS)
				.add(AbysmDamageTypes.HARPOON);

			builder(DamageTypeTags.PANIC_CAUSES)
				.add(AbysmDamageTypes.CNIDOCYTE_STING)
				.add(AbysmDamageTypes.HARPOON);

			builder(DamageTypeTags.BYPASSES_ARMOR)
				.add(AbysmDamageTypes.CNIDOCYTE_STING)
				.add(AbysmDamageTypes.SALINATION)
				.add(AbysmDamageTypes.PRESSURE);

			builder(DamageTypeTags.BYPASSES_EFFECTS)
				.add(AbysmDamageTypes.PRESSURE);

			builder(DamageTypeTags.BYPASSES_ENCHANTMENTS)
				.add(AbysmDamageTypes.CNIDOCYTE_STING)
				.add(AbysmDamageTypes.SALINATION)
				.add(AbysmDamageTypes.PRESSURE);
		}
	}

	private static class SoundEventTagProvider extends FabricTagProvider<SoundEvent> {

		public SoundEventTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, RegistryKeys.SOUND_EVENT, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			builder(AbysmSoundEventTags.UNEFFECTED_BY_WATER);
		}
	}
}
