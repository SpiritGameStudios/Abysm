package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.block.AbysmBlockFamilies;
import dev.spiritstudios.abysm.registry.tags.AbysmBlockTags;
import dev.spiritstudios.abysm.registry.tags.AbysmEntityTypeTags;
import dev.spiritstudios.abysm.registry.tags.AbysmItemTags;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.abysm.registry.AbysmDamageTypes;
import dev.spiritstudios.abysm.registry.AbysmEntityTypes;
import dev.spiritstudios.abysm.registry.AbysmItems;
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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.*;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AbysmTagProviders {
	public static void addAll(FabricDataGenerator.Pack pack) {
		BlockTagProvider blockTagProvider = pack.addProvider(BlockTagProvider::new);
		pack.addProvider(((output, registriesFuture) -> new ItemTagProvider(output, registriesFuture, blockTagProvider)));
		pack.addProvider(BiomeTagProvider::new);
		pack.addProvider(EntityTypeTagProvider::new);
		pack.addProvider(DamageTypeTagProvider::new);
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
					AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE,
					AbysmBlocks.CHISELED_SMOOTH_FLOROPUMICE,
					AbysmBlocks.SMOOTH_FLOROPUMICE_PILLAR
				)
				.addOptionalTag(AbysmBlockTags.BLOOMED_FLOROPUMICE);

			getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
				.addOptionalTag(AbysmBlockTags.BLOOMSHROOM_STEMS)
				.addOptionalTag(AbysmBlockTags.BLOOMSHROOM_CAPS);

			getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
				.add(
					AbysmBlocks.BLOOMSHROOM_GOOP
				);

			getOrCreateTagBuilder(BlockTags.HOE_MINEABLE)
				.add(
					AbysmBlocks.BLOOMSHROOM_GOOP
				)
				.addOptionalTag(AbysmBlockTags.BLOOMSHROOM_STEMS)
				.addOptionalTag(AbysmBlockTags.BLOOMSHROOM_CAPS);

			getOrCreateTagBuilder(BlockTags.REPLACEABLE_BY_TREES)
				.addOptionalTag(AbysmBlockTags.FLOWERY_SPRIGS)
				.addOptionalTag(AbysmBlockTags.BLOOM_PETALS);

			getOrCreateTagBuilder(BlockTags.REPLACEABLE_BY_MUSHROOMS)
				.addOptionalTag(AbysmBlockTags.FLOWERY_SPRIGS)
				.addOptionalTag(AbysmBlockTags.BLOOM_PETALS);

			getOrCreateTagBuilder(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)
				.addOptionalTag(AbysmBlockTags.FLOWERY_SPRIGS);

			getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
				.addOptionalTag(AbysmBlockTags.BLOOM_PETALS);

			getOrCreateTagBuilder(BlockTags.ENDERMAN_HOLDABLE)
				.addOptionalTag(AbysmBlockTags.SMALL_BLOOMSHROOMS)
				.addOptionalTag(AbysmBlockTags.FLOWERY_SPRIGS);

			getOrCreateTagBuilder(BlockTags.SCULK_REPLACEABLE)
				.add(
					AbysmBlocks.FLOROPUMICE
				)
				.addOptionalTag(AbysmBlockTags.BLOOMED_FLOROPUMICE);

			getOrCreateTagBuilder(BlockTags.SCULK_REPLACEABLE_WORLD_GEN)
				.add(
					AbysmBlocks.FLOROPUMICE
				)
				.addOptionalTag(AbysmBlockTags.BLOOMED_FLOROPUMICE);

			getOrCreateTagBuilder(BlockTags.LEAVES)
				.add(
					AbysmBlocks.ROSEBLOOM_PETALEAVES,
					AbysmBlocks.SUNBLOOM_PETALEAVES,
					AbysmBlocks.MALLOWBLOOM_PETALEAVES
				);

			getOrCreateTagBuilder(BlockTags.FLOWERS)
				.addOptionalTag(AbysmBlockTags.BLOOM_PETALS)
				.addOptionalTag(AbysmBlockTags.SMALL_BLOOMSHROOMS)
				.addOptionalTag(AbysmBlockTags.BLOOMING_CROWNS)
				.addOptionalTag(AbysmBlockTags.SCABIOSAS);

			getOrCreateTagBuilder(BlockTags.SMALL_FLOWERS)
				.addOptionalTag(AbysmBlockTags.SMALL_BLOOMSHROOMS);

			getOrCreateTagBuilder(BlockTags.FLOWER_POTS)
				.add(
					AbysmBlocks.POTTED_ROSY_SPRIGS,
					AbysmBlocks.POTTED_SUNNY_SPRIGS,
					AbysmBlocks.POTTED_MAUVE_SPRIGS,

					AbysmBlocks.POTTED_ROSY_BLOOMSHROOM,
					AbysmBlocks.POTTED_SUNNY_BLOOMSHROOM,
					AbysmBlocks.POTTED_MAUVE_BLOOMSHROOM
				);

			// conventional tags
			getOrCreateTagBuilder(ConventionalBlockTags.FLOWERS)
				.addOptionalTag(AbysmBlockTags.SMALL_BLOOMSHROOMS)
				.addOptionalTag(AbysmBlockTags.BLOOMING_CROWNS)
				.addOptionalTag(AbysmBlockTags.SCABIOSAS);

			getOrCreateTagBuilder(ConventionalBlockTags.SMALL_FLOWERS)
				.addOptionalTag(AbysmBlockTags.SMALL_BLOOMSHROOMS);

			// abysm tags
			getOrCreateTagBuilder(AbysmBlockTags.BLOOMSHROOM_PLANTABLE_ON)
				.add(
					AbysmBlocks.FLOROPUMICE,
					Blocks.SAND
				)
				.addOptionalTag(AbysmBlockTags.BLOOMED_FLOROPUMICE);

			getOrCreateTagBuilder(AbysmBlockTags.ALSO_PRESERVES_LEAVES)
				.addOptionalTag(AbysmBlockTags.BLOOMSHROOM_CAPS)
				.addOptionalTag(AbysmBlockTags.BLOOMSHROOM_STEMS)
				.add(AbysmBlocks.BLOOMSHROOM_GOOP);

			getOrCreateTagBuilder(AbysmBlockTags.BLOOMED_FLOROPUMICE)
				.add(
					AbysmBlocks.ROSEBLOOMED_FLOROPUMICE,
					AbysmBlocks.SUNBLOOMED_FLOROPUMICE,
					AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE
				);

			getOrCreateTagBuilder(AbysmBlockTags.FLOWERY_SPRIGS)
				.add(
					AbysmBlocks.ROSY_SPRIGS,
					AbysmBlocks.SUNNY_SPRIGS,
					AbysmBlocks.MAUVE_SPRIGS
				);

			getOrCreateTagBuilder(AbysmBlockTags.BLOOM_PETALS)
				.add(
					AbysmBlocks.ROSEBLOOM_PETALS,
					AbysmBlocks.SUNBLOOM_PETALS,
					AbysmBlocks.MALLOWBLOOM_PETALS
				);

			getOrCreateTagBuilder(AbysmBlockTags.SMALL_BLOOMSHROOMS)
				.add(
					AbysmBlocks.ROSY_BLOOMSHROOM,
					AbysmBlocks.SUNNY_BLOOMSHROOM,
					AbysmBlocks.MAUVE_BLOOMSHROOM
				);

			getOrCreateTagBuilder(AbysmBlockTags.BLOOMSHROOM_STEMS)
				.addOptionalTag(AbysmBlockTags.ROSY_BLOOMSHROOM_STEMS)
				.addOptionalTag(AbysmBlockTags.SUNNY_BLOOMSHROOM_STEMS)
				.addOptionalTag(AbysmBlockTags.MAUVE_BLOOMSHROOM_STEMS);

			getOrCreateTagBuilder(AbysmBlockTags.ROSY_BLOOMSHROOM_STEMS)
				.add(
					AbysmBlocks.ROSY_BLOOMSHROOM_STEM,
					AbysmBlocks.ROSY_BLOOMSHROOM_HYPHAE
				);

			getOrCreateTagBuilder(AbysmBlockTags.SUNNY_BLOOMSHROOM_STEMS)
				.add(
					AbysmBlocks.SUNNY_BLOOMSHROOM_STEM,
					AbysmBlocks.SUNNY_BLOOMSHROOM_HYPHAE
				);

			getOrCreateTagBuilder(AbysmBlockTags.MAUVE_BLOOMSHROOM_STEMS)
				.add(
					AbysmBlocks.MAUVE_BLOOMSHROOM_STEM,
					AbysmBlocks.MAUVE_BLOOMSHROOM_HYPHAE
				);

			getOrCreateTagBuilder(AbysmBlockTags.BLOOMSHROOM_CAPS)
				.add(
					AbysmBlocks.ROSY_BLOOMSHROOM_CAP,
					AbysmBlocks.SUNNY_BLOOMSHROOM_CAP,
					AbysmBlocks.MAUVE_BLOOMSHROOM_CAP
				);

			getOrCreateTagBuilder(AbysmBlockTags.BLOOMING_CROWNS)
				.add(
					AbysmBlocks.BLOOMING_SODALITE_CROWN,
					AbysmBlocks.BLOOMING_ANYOLITE_CROWN,
					AbysmBlocks.BLOOMING_MELILITE_CROWN
				);

			getOrCreateTagBuilder(AbysmBlockTags.SCABIOSAS)
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

			getOrCreateTagBuilder(AbysmItemTags.HARPOON_ONLY).add(AbysmItems.NOOPRAH);

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
			getOrCreateTagBuilder(BiomeTags.IS_OVERWORLD)
				.add(AbysmBiomes.FLORAL_REEF);

			// Using the conventional tag here since the vanilla one overrides the music
			getOrCreateTagBuilder(ConventionalBiomeTags.IS_OCEAN)
				.add(AbysmBiomes.FLORAL_REEF);
		}
	}

	private static class EntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
		public EntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			getOrCreateTagBuilder(EntityTypeTags.AXOLOTL_HUNT_TARGETS)
				.add(
					AbysmEntityTypes.SMALL_FLORAL_FISH,
					AbysmEntityTypes.BIG_FLORAL_FISH
				);

			getOrCreateTagBuilder(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
				.add(
					AbysmEntityTypes.SMALL_FLORAL_FISH,
					AbysmEntityTypes.BIG_FLORAL_FISH
				);

			getOrCreateTagBuilder(EntityTypeTags.AQUATIC)
				.add(
					AbysmEntityTypes.SMALL_FLORAL_FISH,
					AbysmEntityTypes.BIG_FLORAL_FISH
				);

			getOrCreateTagBuilder(EntityTypeTags.NOT_SCARY_FOR_PUFFERFISH)
				.add(
					AbysmEntityTypes.SMALL_FLORAL_FISH,
					AbysmEntityTypes.BIG_FLORAL_FISH
				);

			getOrCreateTagBuilder(AbysmEntityTypeTags.MAN_O_WAR_FRIEND)
				.add(EntityType.PUFFERFISH).add(AbysmEntityTypes.MAN_O_WAR);

			getOrCreateTagBuilder(AbysmEntityTypeTags.HARPOON_UNHAULABLE)
				.forceAddTag(ConventionalEntityTypeTags.BOSSES)
				.add(EntityType.ENDER_DRAGON)
				.add(EntityType.GIANT)
				//.add(EntityType.SHULKER)
				.add(EntityType.WARDEN)
				.add(EntityType.WITHER);
		}
	}

	private static class DamageTypeTagProvider extends FabricTagProvider<DamageType> {

		public DamageTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			getOrCreateTagBuilder(DamageTypeTags.IS_PROJECTILE).add(AbysmDamageTypes.HARPOON);
			getOrCreateTagBuilder(DamageTypeTags.ALWAYS_KILLS_ARMOR_STANDS).add(AbysmDamageTypes.HARPOON);
			getOrCreateTagBuilder(DamageTypeTags.PANIC_CAUSES).add(AbysmDamageTypes.CNIDOCYTE_STING).add(AbysmDamageTypes.HARPOON);

			getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR).add(AbysmDamageTypes.CNIDOCYTE_STING).add(AbysmDamageTypes.PRESSURE);
			getOrCreateTagBuilder(DamageTypeTags.BYPASSES_EFFECTS).add(AbysmDamageTypes.PRESSURE);
			getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(AbysmDamageTypes.CNIDOCYTE_STING).add(AbysmDamageTypes.PRESSURE);
		}
	}
}
