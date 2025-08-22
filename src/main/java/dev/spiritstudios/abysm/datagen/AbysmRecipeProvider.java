package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.block.AbysmBlockFamilies;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.item.AbysmItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.data.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;

import java.util.concurrent.CompletableFuture;

public class AbysmRecipeProvider extends FabricRecipeProvider {
	@Override
	public String getName() {
		return "Recipes";
	}

	public AbysmRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter exporter) {
		return new RecipeGenerator(wrapperLookup, exporter) {
			@Override
			public void generate() {
				// region crafting
				AbysmBlockFamilies
					.getAllAbysmBlockFamilies()
					.filter(BlockFamily::shouldGenerateRecipes)
					.forEach(blockFamily -> generateFamily(blockFamily, FeatureSet.of(FeatureFlags.VANILLA)));

				// 1 prismarine crystal, 4 sandstone, 4 basalt -> 8 floropumice
				this.createShapeless(RecipeCategory.BUILDING_BLOCKS, AbysmBlocks.FLOROPUMICE, 8)
					.input(Items.PRISMARINE_CRYSTALS)
					.input(Blocks.SANDSTONE, 4)
					.input(Blocks.BASALT, 4)
					.criterion("has_prismarine", this.conditionsFromItem(Items.PRISMARINE_CRYSTALS))
					.criterion("has_sandstone", this.conditionsFromItem(Blocks.SANDSTONE))
					.criterion("has_basalt", this.conditionsFromItem(Blocks.BASALT))
					.offerTo(this.exporter);

				// floropumice recipes not covered by block families
				offerBricklikeRecipe(AbysmBlocks.FLOROPUMICE_BRICKS, AbysmBlocks.POLISHED_FLOROPUMICE);
				offerBricklikeRecipe(AbysmBlocks.SMOOTH_FLOROPUMICE_BRICKS, AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE);
				offerPillarRecipe(AbysmBlocks.SMOOTH_FLOROPUMICE_PILLAR, AbysmBlocks.SMOOTH_FLOROPUMICE);

				// bloomshroom bark recipes
				offerBarkBlockRecipe(AbysmBlocks.ROSY_BLOOMSHROOM_HYPHAE, AbysmBlocks.ROSY_BLOOMSHROOM_STEM);
				offerBarkBlockRecipe(AbysmBlocks.SUNNY_BLOOMSHROOM_HYPHAE, AbysmBlocks.SUNNY_BLOOMSHROOM_STEM);
				offerBarkBlockRecipe(AbysmBlocks.MAUVE_BLOOMSHROOM_HYPHAE, AbysmBlocks.MAUVE_BLOOMSHROOM_STEM);

				// petaleaves recipes
				offer2x2CompactingRecipe(RecipeCategory.DECORATIONS, AbysmBlocks.ROSEBLOOM_PETALEAVES, AbysmBlocks.ROSEBLOOM_PETALS);
				offer2x2CompactingRecipe(RecipeCategory.DECORATIONS, AbysmBlocks.SUNBLOOM_PETALEAVES, AbysmBlocks.SUNBLOOM_PETALS);
				offer2x2CompactingRecipe(RecipeCategory.DECORATIONS, AbysmBlocks.MALLOWBLOOM_PETALEAVES, AbysmBlocks.MALLOWBLOOM_PETALS);

				// dregloam recipes
				offer2x2CompactingRecipe(RecipeCategory.BUILDING_BLOCKS, AbysmBlocks.DREGLOAM_OOZE, AbysmItems.DREGLOAM_OOZEBALL);

				this.createShapeless(RecipeCategory.BUILDING_BLOCKS, AbysmItems.DREGLOAM_OOZEBALL, 4)
					.input(AbysmBlocks.DREGLOAM_OOZE)
					.criterion("has_ooze", this.conditionsFromItem(AbysmBlocks.DREGLOAM_OOZE))
					.offerTo(this.exporter);

				this.createShapeless(RecipeCategory.BUILDING_BLOCKS, AbysmBlocks.OOZING_DREGLOAM)
					.input(AbysmItems.DREGLOAM_OOZEBALL)
					.input(AbysmBlocks.DREGLOAM)
					.criterion("has_oozeball", this.conditionsFromItem(AbysmItems.DREGLOAM_OOZEBALL))
					.criterion("has_dregloam", this.conditionsFromItem(AbysmBlocks.DREGLOAM))
					.offerTo(this.exporter);

				// harpoon
				this.createShaped(RecipeCategory.COMBAT, AbysmItems.HARPOON)
					.input('c', AbysmBlocks.OOZETRICKLE_CORD)
					.input('h', Items.HEART_OF_THE_SEA)
					.input('g', Items.GOLD_INGOT)
					.input('f', AbysmBlocks.SMOOTH_FLOROPUMICE)
					.pattern("ff ")
					.pattern("hcg")
					.pattern("f  ")
					.criterion("has_cord", this.conditionsFromItem(AbysmBlocks.OOZETRICKLE_CORD))
					.offerTo(this.exporter);
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

			private void offerSmelting(ItemConvertible input, RecipeCategory category, ItemConvertible output, float experience, int cookingTime) {
				CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItem(input), category, output, experience, cookingTime)
					.criterion("has_input", this.conditionsFromItem(input))
					.offerTo(this.exporter);
			}

			private void offerBricklikeRecipe(ItemConvertible out, ItemConvertible in) {
				this.createShaped(RecipeCategory.BUILDING_BLOCKS, out, 4)
					.input('#', in)
					.pattern("##")
					.pattern("##")
					.criterion("has_input", this.conditionsFromItem(in))
					.offerTo(this.exporter);
			}

			private void offerPillarRecipe(ItemConvertible pillar, ItemConvertible in) {
				this.createShaped(RecipeCategory.BUILDING_BLOCKS, pillar, 2)
					.input('#', in)
					.pattern("#")
					.pattern("#")
					.criterion("has_input", this.conditionsFromItem(in))
					.offerTo(this.exporter);
			}

			private void scFamily(BlockFamily family, int multiplier, ItemConvertible... materials) {
				Block stairs = family.getVariant(BlockFamily.Variant.STAIRS);
				if (stairs != null) {
					scBlocks(stairs, multiplier, materials);
				}
				Block slab = family.getVariant(BlockFamily.Variant.SLAB);
				if (slab != null) {
					scBlocks(slab, 2 * multiplier, materials);
				}
				Block wall = family.getVariant(BlockFamily.Variant.WALL);
				if (wall != null) {
					scWalls(wall, multiplier, materials);
				}
				Block polished = family.getVariant(BlockFamily.Variant.POLISHED);
				if (polished != null) {
					scBlocks(polished, multiplier, materials);
				}
				Block cut = family.getVariant(BlockFamily.Variant.CUT);
				if (cut != null) {
					scBlocks(cut, multiplier, materials);
				}
				Block chiseled = family.getVariant(BlockFamily.Variant.CHISELED);
				if (chiseled != null) {
					scBlocks(chiseled, multiplier, materials);
				}
			}

			private void scBlocks(ItemConvertible result, int amount, ItemConvertible... materials) {
				// use this for non-walls
				for (ItemConvertible material : materials) {
					scBuildingBlock(result, material, amount);
				}
			}

			private void scWalls(ItemConvertible result, int amount, ItemConvertible... materials) {
				// use this for walls
				for (ItemConvertible material : materials) {
					scDecoration(result, material, amount);
				}
			}

			private void scBuildingBlock(ItemConvertible result, ItemConvertible material, int amount) {
				// use this for non-walls
				this.offerStonecuttingRecipe(RecipeCategory.BUILDING_BLOCKS, result, material, amount);
			}

			private void scDecoration(ItemConvertible result, ItemConvertible material, int amount) {
				// use this for walls
				this.offerStonecuttingRecipe(RecipeCategory.DECORATIONS, result, material, amount);
			}
		};
	}
}
