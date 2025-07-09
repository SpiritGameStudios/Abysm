package dev.spiritstudios.abysm.client;

import dev.spiritstudios.abysm.client.registry.AbysmParticles;
import dev.spiritstudios.abysm.client.render.AbysmDebugRenderers;
import dev.spiritstudios.abysm.client.render.AbysmRenderPipelines;
import dev.spiritstudios.abysm.client.render.entity.AbysmEntityLayers;
import dev.spiritstudios.abysm.client.render.entity.BigFloralFishEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.BloomrayEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.ElectricOoglyBooglyRenderer;
import dev.spiritstudios.abysm.client.render.entity.FlippersRenderer;
import dev.spiritstudios.abysm.client.render.entity.LectorfinEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.ManOWarEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.MysteriousBlobEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.SmallFloralFishEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.harpoon.HarpoonEntityRenderer;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.networking.EntityFinishedEatingS2CPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class AbysmClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(AbysmEntityTypes.SMALL_FLORAL_FISH, SmallFloralFishEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.BIG_FLORAL_FISH, BigFloralFishEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.BLOOMRAY, BloomrayEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY, ElectricOoglyBooglyRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.FLYING_HARPOON, HarpoonEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.MAN_O_WAR, ManOWarEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.LECTORFIN, LectorfinEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.MYSTERIOUS_BLOB, MysteriousBlobEntityRenderer::new);

		ArmorRenderer.register(new FlippersRenderer(), AbysmItems.FLIPPERS);

		AbysmEntityLayers.init();
		AbysmParticles.init();
		AbysmRenderPipelines.init();
		AbysmDebugRenderers.init();

		ClientPlayNetworking.registerGlobalReceiver(EntityFinishedEatingS2CPayload.ID, (payload, context) -> {
			World world = context.player().getWorld();
			Entity entity = world.getEntityById(payload.entityId());
			if (entity == null) {
				return;
			}
			ParticleEffect parameters = payload.particleEffect();
			Random random = entity.getRandom();
			for (int i = 0; i < 5; i++) {
				double d = random.nextGaussian() * 0.02;
				double e = random.nextGaussian() * 0.02;
				double f = random.nextGaussian() * 0.02;
				world.addParticleClient(parameters, entity.getParticleX(1.0), entity.getRandomBodyY() + 0.5, entity.getParticleZ(1.0), d, e, f);
			}
		});
	}
}
