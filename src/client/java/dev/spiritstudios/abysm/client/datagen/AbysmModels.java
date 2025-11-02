package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.Abysm;
import java.util.Optional;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;

import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.BLOSSOM_BASE_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.BLOSSOM_FLOWER_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.CROWN_INNER_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.CROWN_PETAL_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.LANTERN_OVERLAY;
import static net.minecraft.client.data.models.model.TextureSlot.BOTTOM;
import static net.minecraft.client.data.models.model.TextureSlot.LANTERN;
import static net.minecraft.client.data.models.model.TextureSlot.SIDE;
import static net.minecraft.client.data.models.model.TextureSlot.TOP;

public class AbysmModels {
	public static final ModelTemplate BLOOMING_CROWN = block("blooming_crown", TOP, SIDE, BOTTOM, CROWN_PETAL_KEY, CROWN_INNER_KEY);
	public static final ModelTemplate BLOSSOM = block("blossom", BLOSSOM_FLOWER_KEY, BLOSSOM_BASE_KEY);

	public static final ModelTemplate TEMPLATE_OOZETRICKLE_LANTERN = block("template_oozetrickle_lantern", LANTERN, LANTERN_OVERLAY);
	public static final ModelTemplate TEMPLATE_HANGING_OOZETRICKLE_LANTERN = block("template_hanging_oozetrickle_lantern", "_hanging", LANTERN, LANTERN_OVERLAY);

	private static ModelTemplate block(String parent, TextureSlot... requiredTextureKeys) {
		return new ModelTemplate(
			Optional.of(Abysm.id("block/" + parent)),
			Optional.empty(),
			requiredTextureKeys
		);
	}


	private static ModelTemplate block(String parent, String variant, TextureSlot... requiredTextureKeys) {
		return new ModelTemplate(
			Optional.of(Abysm.id("block/" + parent)),
			Optional.of(variant),
			requiredTextureKeys
		);
	}
}
