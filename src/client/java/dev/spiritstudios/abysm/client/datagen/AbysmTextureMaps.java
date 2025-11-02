package dev.spiritstudios.abysm.client.datagen;

import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.BLOSSOM_BASE_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.BLOSSOM_FLOWER_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.CROWN_INNER_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.CROWN_PETAL_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.LANTERN_OVERLAY;
import static net.minecraft.client.data.models.model.TextureSlot.BOTTOM;
import static net.minecraft.client.data.models.model.TextureSlot.LANTERN;
import static net.minecraft.client.data.models.model.TextureSlot.SIDE;
import static net.minecraft.client.data.models.model.TextureSlot.TOP;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.world.level.block.Block;

public class AbysmTextureMaps {
	public static TextureMapping bloomingCrown(Block block) {
		return new TextureMapping()
			.put(TOP, TextureMapping.getBlockTexture(block, "_top"))
			.put(BOTTOM, TextureMapping.getBlockTexture(block, "_bottom"))
			.put(SIDE, TextureMapping.getBlockTexture(block, "_side"))
			.put(CROWN_PETAL_KEY, TextureMapping.getBlockTexture(block, "_petal"))
			.put(CROWN_INNER_KEY, TextureMapping.getBlockTexture(block, "_inner"));
	}

	public static TextureMapping blossom(Block block) {
		return new TextureMapping()
			.put(BLOSSOM_FLOWER_KEY, TextureMapping.getBlockTexture(block))
			.put(BLOSSOM_BASE_KEY, TextureMapping.getBlockTexture(block, "_base"));
	}

	public static TextureMapping layeredLantern(Block block) {
		return new TextureMapping()
			.put(LANTERN, TextureMapping.getBlockTexture(block))
			.put(LANTERN_OVERLAY, TextureMapping.getBlockTexture(block, "_overlay"));
	}
}
