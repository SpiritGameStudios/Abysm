package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.world.level.block.AbysmBlockFamilies;
import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import java.util.concurrent.CompletableFuture;

public class AbysmRecipeProvider extends FabricRecipeProvider {
	@Override
	public String getName() {
		return "Recipes";
	}

	public AbysmRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput exporter) {
		return new RecipeProvider(wrapperLookup, exporter) {
			@Override
			public void buildRecipes() {
				// region crafting
				AbysmBlockFamilies
					.getAllAbysmBlockFamilies()
					.filter(BlockFamily::shouldGenerateRecipe)
					.forEach(blockFamily -> generateRecipes(blockFamily, FeatureFlagSet.of(FeatureFlags.VANILLA)));

				// 1 prismarine crystal, 4 sandstone, 4 basalt -> 8 floropumice
				this.shapeless(RecipeCategory.BUILDING_BLOCKS, AbysmBlocks.FLOROPUMICE, 8)
					.requires(Items.PRISMARINE_CRYSTALS)
					.requires(Blocks.SANDSTONE, 4)
					.requires(Blocks.BASALT, 4)
					.unlockedBy("has_prismarine", this.has(Items.PRISMARINE_CRYSTALS))
					.unlockedBy("has_sandstone", this.has(Blocks.SANDSTONE))
					.unlockedBy("has_basalt", this.has(Blocks.BASALT))
					.save(this.output);

				// floropumice recipes not covered by block families
				offerBricklikeRecipe(AbysmBlocks.FLOROPUMICE_BRICKS, AbysmBlocks.POLISHED_FLOROPUMICE);
				offerBricklikeRecipe(AbysmBlocks.SMOOTH_FLOROPUMICE_BRICKS, AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE);
				offerPillarRecipe(AbysmBlocks.SMOOTH_FLOROPUMICE_PILLAR, AbysmBlocks.SMOOTH_FLOROPUMICE);

				// bloomshroom bark recipes
				woodFromLogs(AbysmBlocks.ROSY_BLOOMSHROOM_HYPHAE, AbysmBlocks.ROSY_BLOOMSHROOM_STEM);
				woodFromLogs(AbysmBlocks.SUNNY_BLOOMSHROOM_HYPHAE, AbysmBlocks.SUNNY_BLOOMSHROOM_STEM);
				woodFromLogs(AbysmBlocks.MAUVE_BLOOMSHROOM_HYPHAE, AbysmBlocks.MAUVE_BLOOMSHROOM_STEM);

				// petaleaves recipes
				twoByTwoPacker(RecipeCategory.DECORATIONS, AbysmBlocks.ROSEBLOOM_PETALEAVES, AbysmBlocks.ROSEBLOOM_PETALS);
				twoByTwoPacker(RecipeCategory.DECORATIONS, AbysmBlocks.SUNBLOOM_PETALEAVES, AbysmBlocks.SUNBLOOM_PETALS);
				twoByTwoPacker(RecipeCategory.DECORATIONS, AbysmBlocks.MALLOWBLOOM_PETALEAVES, AbysmBlocks.MALLOWBLOOM_PETALS);

				// dregloam recipes
				twoByTwoPacker(RecipeCategory.BUILDING_BLOCKS, AbysmBlocks.DREGLOAM_OOZE, AbysmItems.DREGLOAM_OOZEBALL);

				this.shapeless(RecipeCategory.BUILDING_BLOCKS, AbysmItems.DREGLOAM_OOZEBALL, 4)
					.requires(AbysmBlocks.DREGLOAM_OOZE)
					.unlockedBy("has_ooze", this.has(AbysmBlocks.DREGLOAM_OOZE))
					.save(this.output);

				this.shapeless(RecipeCategory.BUILDING_BLOCKS, AbysmBlocks.OOZING_DREGLOAM)
					.requires(AbysmItems.DREGLOAM_OOZEBALL)
					.requires(AbysmBlocks.DREGLOAM)
					.unlockedBy("has_oozeball", this.has(AbysmItems.DREGLOAM_OOZEBALL))
					.unlockedBy("has_dregloam", this.has(AbysmBlocks.DREGLOAM))
					.save(this.output);

				// harpoon
				this.shaped(RecipeCategory.COMBAT, AbysmItems.HARPOON)
					.define('c', AbysmBlocks.OOZETRICKLE_CORD)
					.define('h', Items.HEART_OF_THE_SEA)
					.define('g', Items.GOLD_INGOT)
					.define('f', AbysmBlocks.SMOOTH_FLOROPUMICE)
					.pattern("ff ")
					.pattern("hcg")
					.pattern("f  ")
					.unlockedBy("has_cord", this.has(AbysmBlocks.OOZETRICKLE_CORD))
					.save(this.output);
				// endregion crafting

				// region smelting etc
				offerSmelting(AbysmBlocks.FLOROPUMICE, RecipeCategory.BUILDING_BLOCKS, AbysmBlocks.SMOOTH_FLOROPUMICE, 0.1F, 200);
				// endregion smelting etc

				// region stonecutter
				// region floropumice
				scFamily(AbysmBlockFamilies.FLOROPUMICE, 1, AbysmBlocks.FLOROPUMICE);
				scFamily(AbysmBlockFamilies.FLOROPUMICE_BRICKS, 1, AbysmBlocks.FLOROPUMICE_BRICKS, AbysmBlocks.POLISHED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);
				scFamily(AbysmBlockFamilies.FLOROPUMICE_TILES, 1, AbysmBlocks.FLOROPUMICE_TILES, AbysmBlocks.FLOROPUMICE_BRICKS, AbysmBlocks.POLISHED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);

				scBlocks(AbysmBlocks.FLOROPUMICE_BRICKS, 1, AbysmBlocks.POLISHED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);

				scFamily(AbysmBlockFamilies.SMOOTH_FLOROPUMICE, 1, AbysmBlocks.SMOOTH_FLOROPUMICE);
				scFamily(AbysmBlockFamilies.SMOOTH_FLOROPUMICE_BRICKS, 1, AbysmBlocks.SMOOTH_FLOROPUMICE_BRICKS, AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE, AbysmBlocks.SMOOTH_FLOROPUMICE);
				scFamily(AbysmBlockFamilies.CUT_SMOOTH_FLOROPUMICE, 1, AbysmBlocks.CUT_SMOOTH_FLOROPUMICE, AbysmBlocks.SMOOTH_FLOROPUMICE_BRICKS, AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE, AbysmBlocks.SMOOTH_FLOROPUMICE);

				scBlocks(AbysmBlocks.SMOOTH_FLOROPUMICE_BRICKS, 1, AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE, AbysmBlocks.SMOOTH_FLOROPUMICE);
				scBlocks(AbysmBlocks.SMOOTH_FLOROPUMICE_PILLAR, 1, AbysmBlocks.SMOOTH_FLOROPUMICE);
				// endregion floropumice
				// endregion stonecutter
			}

			private void offerSmelting(ItemLike input, RecipeCategory category, ItemLike output, float experience, int cookingTime) {
				SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), category, output, experience, cookingTime)
					.unlockedBy("has_input", this.has(input))
					.save(this.output);
			}

			private void offerBricklikeRecipe(ItemLike out, ItemLike in) {
				this.shaped(RecipeCategory.BUILDING_BLOCKS, out, 4)
					.define('#', in)
					.pattern("##")
					.pattern("##")
					.unlockedBy("has_input", this.has(in))
					.save(this.output);
			}

			private void offerPillarRecipe(ItemLike pillar, ItemLike in) {
				this.shaped(RecipeCategory.BUILDING_BLOCKS, pillar, 2)
					.define('#', in)
					.pattern("#")
					.pattern("#")
					.unlockedBy("has_input", this.has(in))
					.save(this.output);
			}

			private void scFamily(BlockFamily family, int multiplier, ItemLike... materials) {
				Block stairs = family.get(BlockFamily.Variant.STAIRS);
				if (stairs != null) {
					scBlocks(stairs, multiplier, materials);
				}
				Block slab = family.get(BlockFamily.Variant.SLAB);
				if (slab != null) {
					scBlocks(slab, 2 * multiplier, materials);
				}
				Block wall = family.get(BlockFamily.Variant.WALL);
				if (wall != null) {
					scWalls(wall, multiplier, materials);
				}
				Block polished = family.get(BlockFamily.Variant.POLISHED);
				if (polished != null) {
					scBlocks(polished, multiplier, materials);
				}
				Block cut = family.get(BlockFamily.Variant.CUT);
				if (cut != null) {
					scBlocks(cut, multiplier, materials);
				}
				Block chiseled = family.get(BlockFamily.Variant.CHISELED);
				if (chiseled != null) {
					scBlocks(chiseled, multiplier, materials);
				}
			}

			private void scBlocks(ItemLike result, int amount, ItemLike... materials) {
				// use this for non-walls
				for (ItemLike material : materials) {
					scBuildingBlock(result, material, amount);
				}
			}

			private void scWalls(ItemLike result, int amount, ItemLike... materials) {
				// use this for walls
				for (ItemLike material : materials) {
					scDecoration(result, material, amount);
				}
			}

			private void scBuildingBlock(ItemLike result, ItemLike material, int amount) {
				// use this for non-walls
				this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, result, material, amount);
			}

			private void scDecoration(ItemLike result, ItemLike material, int amount) {
				// use this for walls
				this.stonecutterResultFromBase(RecipeCategory.DECORATIONS, result, material, amount);
			}
		};
	}
}
