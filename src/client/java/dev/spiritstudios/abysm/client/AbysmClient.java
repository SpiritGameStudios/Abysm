package dev.spiritstudios.abysm.client;

import dev.spiritstudios.abysm.client.render.entity.BigFloralFishEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.SmallFloralFishEntityRenderer;
import dev.spiritstudios.abysm.registry.AbysmEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class AbysmClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
		EntityRendererRegistry.register(AbysmEntityTypes.SMALL_FLORAL_FISH, SmallFloralFishEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.BIG_FLORAL_FISH, BigFloralFishEntityRenderer::new);
    }
}
