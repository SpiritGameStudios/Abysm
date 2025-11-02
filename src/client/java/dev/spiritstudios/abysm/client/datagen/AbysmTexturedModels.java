package dev.spiritstudios.abysm.client.datagen;

import net.minecraft.client.data.models.model.TexturedModel;

public class AbysmTexturedModels {
	public static final TexturedModel.Provider BLOOMING_CROWN = TexturedModel.createDefault(AbysmTextureMaps::bloomingCrown, AbysmModels.BLOOMING_CROWN);
	public static final TexturedModel.Provider BLOSSOM = TexturedModel.createDefault(AbysmTextureMaps::blossom, AbysmModels.BLOSSOM);

	public static final TexturedModel.Provider TEMPLATE_OOZETRICKLE_LANTERN = TexturedModel.createDefault(AbysmTextureMaps::layeredLantern, AbysmModels.TEMPLATE_OOZETRICKLE_LANTERN);
	public static final TexturedModel.Provider TEMPLATE_OOZETRICKLE_HANGING_LANTERN = TexturedModel.createDefault(AbysmTextureMaps::layeredLantern, AbysmModels.TEMPLATE_HANGING_OOZETRICKLE_LANTERN);
}
