package dev.spiritstudios.abysm.client.datagen;

import net.minecraft.block.Block;
import net.minecraft.client.data.TextureMap;

import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.BLOSSOM_BASE_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.BLOSSOM_FLOWER_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.CROWN_INNER_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.CROWN_PETAL_KEY;
import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.LANTERN_OVERLAY;
import static net.minecraft.client.data.TextureKey.BOTTOM;
import static net.minecraft.client.data.TextureKey.LANTERN;
import static net.minecraft.client.data.TextureKey.SIDE;
import static net.minecraft.client.data.TextureKey.TOP;

public class AbysmTextureMaps {
	public static TextureMap bloomingCrown(Block block) {
		return new TextureMap()
			.put(TOP, TextureMap.getSubId(block, "_top"))
			.put(BOTTOM, TextureMap.getSubId(block, "_bottom"))
			.put(SIDE, TextureMap.getSubId(block, "_side"))
			.put(CROWN_PETAL_KEY, TextureMap.getSubId(block, "_petal"))
			.put(CROWN_INNER_KEY, TextureMap.getSubId(block, "_inner"));
	}

	public static TextureMap blossom(Block block) {
		return new TextureMap()
			.put(BLOSSOM_FLOWER_KEY, TextureMap.getId(block))
			.put(BLOSSOM_BASE_KEY, TextureMap.getSubId(block, "_base"));
	}

	public static TextureMap layeredLantern(Block block) {
		return new TextureMap()
			.put(LANTERN, TextureMap.getId(block))
			.put(LANTERN_OVERLAY, TextureMap.getSubId(block, "_overlay"));
	}
}
