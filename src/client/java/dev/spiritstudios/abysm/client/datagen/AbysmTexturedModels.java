package dev.spiritstudios.abysm.client.datagen;

import net.minecraft.client.data.TexturedModel;

public class AbysmTexturedModels {
	public static final TexturedModel.Factory BLOOMING_CROWN = TexturedModel.makeFactory(AbysmTextureMaps::bloomingCrown, AbysmModels.BLOOMING_CROWN);
	public static final TexturedModel.Factory BLOSSOM = TexturedModel.makeFactory(AbysmTextureMaps::blossom, AbysmModels.BLOSSOM);
}
