package dev.spiritstudios.abysm.client;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.AbysmConfig;
import dev.spiritstudios.abysm.client.registry.AbysmParticles;
import dev.spiritstudios.abysm.client.render.AbysmDebugRenderers;
import dev.spiritstudios.abysm.client.render.AbysmRenderPipelines;
import dev.spiritstudios.abysm.client.render.HarpoonLoadedProperty;
import dev.spiritstudios.abysm.client.render.entity.AbysmEntityLayers;
import dev.spiritstudios.abysm.client.render.entity.renderer.BigFloralFishEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.BloomrayEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.ElectricOoglyBooglyRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.FlippersRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.HarpoonEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.LehydrathanEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.ManOWarEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.MysteriousBlobEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.PaddlefishEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.SmallFloralFishEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.lectorfin.LectorfinEntityRenderer;
import dev.spiritstudios.abysm.client.sound.AbysmAL;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.networking.EntityFinishedEatingS2CPayload;
import dev.spiritstudios.specter.api.config.client.ModMenuHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.property.bool.BooleanProperties;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class AbysmClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BooleanProperties.ID_MAPPER.put(Abysm.id("harpoon_loaded"), HarpoonLoadedProperty.CODEC);

		EntityRendererRegistry.register(AbysmEntityTypes.SMALL_FLORAL_FISH, SmallFloralFishEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.BIG_FLORAL_FISH, BigFloralFishEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.BLOOMRAY, BloomrayEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY, ElectricOoglyBooglyRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.FLYING_HARPOON, HarpoonEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.MAN_O_WAR, ManOWarEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.LECTORFIN, LectorfinEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.MYSTERIOUS_BLOB, MysteriousBlobEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.TEST_LEVIATHAN, LehydrathanEntityRenderer::new);
		EntityRendererRegistry.register(AbysmEntityTypes.PADDLEFISH, PaddlefishEntityRenderer::new);

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
				double velocityX = random.nextGaussian() * 0.02;
				double velocityY = random.nextGaussian() * 0.02;
				double velocityZ = random.nextGaussian() * 0.02;
				world.addParticleClient(parameters, entity.getParticleX(1.0), entity.getRandomBodyY() + 0.5, entity.getParticleZ(1.0), velocityX, velocityY, velocityZ);
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			ClientPlayerEntity player = client.player;
			if (player == null) return;

			if (player.isSubmergedIn(FluidTags.WATER)) AbysmAL.enable();
			else AbysmAL.disable();
		});

		ModMenuHelper.addConfig(Abysm.MODID, AbysmConfig.HOLDER.id());
	}
}
