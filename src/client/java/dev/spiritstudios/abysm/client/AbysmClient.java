package dev.spiritstudios.abysm.client;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.AbysmConfig;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.block.OozetrickleLanternBlock;
import dev.spiritstudios.abysm.client.registry.AbysmParticles;
import dev.spiritstudios.abysm.client.render.AbysmDebugRenderers;
import dev.spiritstudios.abysm.client.render.AbysmRenderPipelines;
import dev.spiritstudios.abysm.client.render.HarpoonLoadedProperty;
import dev.spiritstudios.abysm.client.render.entity.AbysmEntityLayers;
import dev.spiritstudios.abysm.client.render.entity.renderer.BigFloralFishEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.BloomrayEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.ElectricOoglyBooglyRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.FlippersRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.FliprayEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.HarpoonEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.LehydrathanEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.ManOWarEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.MysteriousBlobEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.SimpleFishRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.SmallFloralFishEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.renderer.lectorfin.LectorfinEntityRenderer;
import dev.spiritstudios.abysm.client.sound.AbysmAL;
import dev.spiritstudios.abysm.duck.LivingEntityDuck;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.networking.EntityUpdateBlueS2CPayload;
import dev.spiritstudios.abysm.networking.HappyEntityParticlesS2CPayload;
import dev.spiritstudios.specter.api.config.client.ModMenuHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
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
		EntityRendererRegistry.register(AbysmEntityTypes.PADDLEFISH, SimpleFishRenderer.factory("paddlefish", false));
		EntityRendererRegistry.register(AbysmEntityTypes.SNAPPER, SimpleFishRenderer.factory("snapper", false));
		EntityRendererRegistry.register(AbysmEntityTypes.GUP_GUP, SimpleFishRenderer.factory("gup_gup", false));
		EntityRendererRegistry.register(AbysmEntityTypes.RETICULATED_FLIPRAY, FliprayEntityRenderer::new);

		ArmorRenderer.register(new FlippersRenderer(), AbysmItems.FLIPPERS);

		AbysmEntityLayers.init();
		AbysmParticles.init();
		AbysmRenderPipelines.init();
		AbysmDebugRenderers.init();

		ClientPlayNetworking.registerGlobalReceiver(HappyEntityParticlesS2CPayload.ID, (payload, context) -> {
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

		ClientPlayNetworking.registerGlobalReceiver(EntityUpdateBlueS2CPayload.ID, (payload, context) -> {
			World world = context.player().getWorld();
			Entity entity = world.getEntityById(payload.entityId());
			if (entity == null) {
				return;
			}
			if (entity instanceof LivingEntityDuck duck) {
				duck.abysm$setBlue(payload.isBlue());
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			ClientPlayerEntity player = client.player;
			if (player == null) return;

			if (player.isSubmergedIn(FluidTags.WATER)) AbysmAL.enable();
			else AbysmAL.disable();
		});

		ModMenuHelper.addConfig(Abysm.MODID, AbysmConfig.HOLDER.id());

		BlockRenderLayerMap.INSTANCE.putBlocks(
			RenderLayer.getCutout(),
			AbysmBlocks.ROSY_SPRIGS,
			AbysmBlocks.POTTED_ROSY_SPRIGS,
			AbysmBlocks.SUNNY_SPRIGS,
			AbysmBlocks.POTTED_SUNNY_SPRIGS,
			AbysmBlocks.MAUVE_SPRIGS,
			AbysmBlocks.POTTED_MAUVE_SPRIGS,

			AbysmBlocks.ROSEBLOOM_PETALS,
			AbysmBlocks.SUNBLOOM_PETALS,
			AbysmBlocks.MALLOWBLOOM_PETALS,

			AbysmBlocks.ROSY_BLOOMSHROOM,
			AbysmBlocks.POTTED_ROSY_BLOOMSHROOM,
			AbysmBlocks.SUNNY_BLOOMSHROOM,
			AbysmBlocks.POTTED_SUNNY_BLOOMSHROOM,
			AbysmBlocks.MAUVE_BLOOMSHROOM,
			AbysmBlocks.POTTED_MAUVE_BLOOMSHROOM,

			AbysmBlocks.BLOOMING_SODALITE_CROWN,
			AbysmBlocks.BLOOMING_ANYOLITE_CROWN,
			AbysmBlocks.BLOOMING_MELILITE_CROWN,

			AbysmBlocks.WHITE_SCABIOSA,
			AbysmBlocks.ORANGE_SCABIOSA,
			AbysmBlocks.MAGENTA_SCABIOSA,
			AbysmBlocks.LIGHT_BLUE_SCABIOSA,
			AbysmBlocks.YELLOW_SCABIOSA,
			AbysmBlocks.LIME_SCABIOSA,
			AbysmBlocks.PINK_SCABIOSA,
			AbysmBlocks.GREY_SCABIOSA,
			AbysmBlocks.LIGHT_GREY_SCABIOSA,
			AbysmBlocks.CYAN_SCABIOSA,
			AbysmBlocks.PURPLE_SCABIOSA,
			AbysmBlocks.BLUE_SCABIOSA,
			AbysmBlocks.BROWN_SCABIOSA,
			AbysmBlocks.GREEN_SCABIOSA,
			AbysmBlocks.RED_SCABIOSA,
			AbysmBlocks.BLACK_SCABIOSA,

			AbysmBlocks.ANTENNAE_PLANT,
			AbysmBlocks.POTTED_ANTENNAE_PLANT,

			AbysmBlocks.OOZETRICKLE_FILAMENTS,
			AbysmBlocks.POTTED_OOZETRICKLE_FILAMENTS,
			AbysmBlocks.TALL_OOZETRICKLE_FILAMENTS,

			AbysmBlocks.GOLDEN_LAZULI_OREFURL,
			AbysmBlocks.GOLDEN_LAZULI_OREFURL_PLANT,

			AbysmBlocks.OOZETRICKLE_LANTERN
		);

		BlockRenderLayerMap.INSTANCE.putBlocks(
			RenderLayer.getCutoutMipped(),
			AbysmBlocks.ROSEBLOOM_PETALEAVES,
			AbysmBlocks.SUNBLOOM_PETALEAVES,
			AbysmBlocks.MALLOWBLOOM_PETALEAVES,

			AbysmBlocks.OOZETRICKLE_CORD
		);

		BlockRenderLayerMap.INSTANCE.putBlocks(
			RenderLayer.getTranslucent(),
			AbysmBlocks.SWEET_NECTARSAP,
			AbysmBlocks.SOUR_NECTARSAP,
			AbysmBlocks.BITTER_NECTARSAP
		);

		ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> OozetrickleLanternBlock.getColor(state.get(OozetrickleLanternBlock.LIGHT)), AbysmBlocks.OOZETRICKLE_LANTERN);
	}
}
