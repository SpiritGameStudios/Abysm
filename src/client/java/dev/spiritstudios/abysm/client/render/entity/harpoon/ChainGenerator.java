package dev.spiritstudios.abysm.client.render.entity.harpoon;

import dev.spiritstudios.abysm.Abysm;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureContents;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class ChainGenerator implements SimpleSynchronousResourceReloadListener {

	public static final Identifier ID = Abysm.id("chain_generator");
	public static final Identifier VANILLA_CHAIN = Identifier.ofVanilla("textures/item/chain.png");

	public static final ChainGenerator INSTANCE = new ChainGenerator();

	@Override
	public Identifier getFabricId() {
		return ID;
	}

	@Override
	public void reload(ResourceManager manager) {
		// thanks MissingSprite
		NativeImage original;
		try {
			original = TextureContents.load(manager, VANILLA_CHAIN).image();
		} catch (IOException e) {
			Abysm.LOGGER.debug("An error occurred when generating the chain texture!", e);
			return;
		}
		if (original == null) {
			return;
		}
		int originalHeight = original.getHeight();
		int originalWidth = original.getWidth();
		NativeImage chains = new NativeImage(originalWidth * 2, originalHeight * 2, false);
		for (int height = 0; height < originalHeight * 2; height++) {
			for (int width = 0; width < originalWidth * 2; width++) {
				chains.setColorArgb(height, width, original.getColorArgb(height % originalHeight, width % originalWidth));
			}
		}
		MinecraftClient.getInstance().getTextureManager().registerTexture(HarpoonEntityRenderer.CHAINS, new NativeImageBackedTexture(HarpoonEntityRenderer.CHAINS::toString, chains));
	}
}
