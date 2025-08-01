package dev.spiritstudios.abysm.component;

import net.minecraft.component.type.FoodComponent;

public final class AbysmFoodComponents {
	public static final FoodComponent SMALL_FLORAL_FISH = new FoodComponent.Builder().nutrition(1).saturationModifier(0.1F).build();

	public static final FoodComponent BIG_FLORAL_FISH = new FoodComponent.Builder().nutrition(3).saturationModifier(0.3F).build();
}
