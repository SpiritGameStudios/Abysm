package dev.spiritstudios.abysm.client;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.registry.AbysmParticles;
import dev.spiritstudios.abysm.client.render.entity.BigFloralFishEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.BloomrayEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.ElectricOoglyBooglyRenderer;
import dev.spiritstudios.abysm.client.render.entity.harpoon.ChainGenerator;
import dev.spiritstudios.abysm.client.render.entity.harpoon.HarpoonEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.SmallFloralFishEntityRenderer;
import dev.spiritstudios.abysm.registry.AbysmEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;

public class AbysmClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
		EntityRendererRegistry.register(AbysmEntityTypes.SMALL_FLORAL_FISH, SmallFloralFishEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.BIG_FLORAL_FISH, BigFloralFishEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.BLOOMRAY, BloomrayEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY, ElectricOoglyBooglyRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.FLYING_HARPOON, HarpoonEntityRenderer::new);

		AbysmParticles.init();

		FabricLoader.getInstance().getModContainer(Abysm.MODID).ifPresent(modContainer -> {
			ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(ChainGenerator.INSTANCE);
		});

    }
}
