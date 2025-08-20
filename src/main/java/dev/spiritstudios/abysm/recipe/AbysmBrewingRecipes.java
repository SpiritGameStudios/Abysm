package dev.spiritstudios.abysm.recipe;

import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.item.AbysmPotions;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;

public class AbysmBrewingRecipes {

	public static void init() {
		FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
			builder.registerPotionRecipe(Potions.AWKWARD, AbysmItems.LAPIS_BULB, AbysmPotions.BLUE);
			builder.registerPotionRecipe(AbysmPotions.BLUE, Items.REDSTONE, AbysmPotions.LONG_BLUE);
		});
	}
}
