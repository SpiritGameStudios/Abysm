package dev.spiritstudios.abysm.client.datagen;

import net.minecraft.block.Block;
import net.minecraft.client.data.TextureMap;

import static dev.spiritstudios.abysm.client.datagen.AbysmTextureKeys.*;
import static net.minecraft.client.data.TextureKey.*;

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
}
