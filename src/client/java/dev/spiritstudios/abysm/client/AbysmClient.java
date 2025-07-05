package dev.spiritstudios.abysm.client;

import dev.spiritstudios.abysm.client.registry.AbysmParticles;
import dev.spiritstudios.abysm.client.render.AbysmDebugRenderers;
import dev.spiritstudios.abysm.client.render.AbysmRenderPipelines;
import dev.spiritstudios.abysm.client.render.entity.AbysmEntityLayers;
import dev.spiritstudios.abysm.client.render.entity.BigFloralFishEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.BloomrayEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.ElectricOoglyBooglyRenderer;
import dev.spiritstudios.abysm.client.render.entity.FlippersRenderer;
import dev.spiritstudios.abysm.client.render.entity.ManOWarEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.SmallFloralFishEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.harpoon.HarpoonEntityRenderer;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.item.AbysmItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class AbysmClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(AbysmEntityTypes.SMALL_FLORAL_FISH, SmallFloralFishEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.BIG_FLORAL_FISH, BigFloralFishEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.BLOOMRAY, BloomrayEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY, ElectricOoglyBooglyRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.FLYING_HARPOON, HarpoonEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.MAN_O_WAR, ManOWarEntityRenderer::new);

		ArmorRenderer.register(new FlippersRenderer(), AbysmItems.FLIPPERS);

		AbysmEntityLayers.init();
		AbysmParticles.init();
		AbysmRenderPipelines.init();
		AbysmDebugRenderers.init();
	}
}
