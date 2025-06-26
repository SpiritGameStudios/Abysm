package dev.spiritstudios.abysm.client;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.registry.AbysmParticles;
import dev.spiritstudios.abysm.client.render.AbysmRenderPipelines;
import dev.spiritstudios.abysm.client.render.entity.*;
import dev.spiritstudios.abysm.client.render.entity.harpoon.HarpoonEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.model.GarbageBagModel;
import dev.spiritstudios.abysm.registry.AbysmEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class AbysmClient implements ClientModInitializer {

	public static final EntityModelLayer MAN_O_WAR_LAYER = new EntityModelLayer(Abysm.id("man_o_war"), "main");

    @Override
    public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(MAN_O_WAR_LAYER, GarbageBagModel::getTexturedModelData);

		EntityRendererRegistry.register(AbysmEntityTypes.SMALL_FLORAL_FISH, SmallFloralFishEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.BIG_FLORAL_FISH, BigFloralFishEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.BLOOMRAY, BloomrayEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY, ElectricOoglyBooglyRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.FLYING_HARPOON, HarpoonEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.MAN_O_WAR, ManOWarEntityRenderer::new);

		AbysmParticles.init();
		AbysmRenderPipelines.init();
    }
}
