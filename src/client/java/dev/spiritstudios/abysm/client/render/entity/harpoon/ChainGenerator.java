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

	private static final int HEIGHT = 32;
	private static final int WIDTH = 32;

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
			throw new RuntimeException(e);
		}
		if (original == null) {
			return;
		}
		if (original.getHeight() != 16 || original.getWidth() != 16) {
			return;
		}
		NativeImage chains = new NativeImage(WIDTH, HEIGHT, false);
		for (int height = 0; height < HEIGHT; height++) {
			for (int width = 0; width < WIDTH; width++) {
				chains.setColorArgb(height, width, original.getColorArgb(height % 16, width % 16));
			}
		}
		MinecraftClient.getInstance().getTextureManager().registerTexture(HarpoonEntityRenderer.CHAINS, new NativeImageBackedTexture(HarpoonEntityRenderer.CHAINS::toString, chains));
	}
}
