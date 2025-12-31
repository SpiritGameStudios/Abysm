package dev.spiritstudios.abysm.client;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.registry.AbysmParticles;
import dev.spiritstudios.abysm.client.render.AbysmDebugRenderers;
import dev.spiritstudios.abysm.client.render.AbysmRenderPipelines;
import dev.spiritstudios.abysm.client.render.HarpoonLoadedProperty;
import dev.spiritstudios.abysm.client.render.entity.AbysmEntityLayers;
import dev.spiritstudios.abysm.client.render.entity.BloomrayEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.ElectricOoglyBooglyRenderer;
import dev.spiritstudios.abysm.client.render.entity.FishEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.FlippersRenderer;
import dev.spiritstudios.abysm.client.render.entity.FliprayEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.FloralFishRenderer;
import dev.spiritstudios.abysm.client.render.entity.GupGupEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.HarpoonEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.ManOWarEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.MysteriousBlobEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.SkeletonSharkEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.SnapperEntityRenderer;
import dev.spiritstudios.abysm.client.render.entity.lectorfin.LectorfinEntityRenderer;
import dev.spiritstudios.abysm.client.sound.AbysmAL;
import dev.spiritstudios.abysm.duck.LivingEntityDuck;
import dev.spiritstudios.abysm.network.EntityUpdateBlueS2CPayload;
import dev.spiritstudios.abysm.network.HappyEntityParticlesS2CPayload;
import dev.spiritstudios.abysm.network.NowHuntingS2CPayload;
import dev.spiritstudios.abysm.world.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.world.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import dev.spiritstudios.abysm.world.level.block.OozetrickleLanternBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class AbysmClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ConditionalItemModelProperties.ID_MAPPER.put(Abysm.id("harpoon_loaded"), HarpoonLoadedProperty.CODEC);

		EntityRenderers.register(AbysmEntityTypes.SMALL_FLORAL_FISH, FloralFishRenderer.factory("small_floral_fish"));
		EntityRenderers.register(AbysmEntityTypes.BIG_FLORAL_FISH, FloralFishRenderer.factory("big_floral_fish"));
		EntityRenderers.register(AbysmEntityTypes.BLOOMRAY, BloomrayEntityRenderer::new);
		EntityRenderers.register(AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY, ElectricOoglyBooglyRenderer::new);
		EntityRenderers.register(AbysmEntityTypes.FLYING_HARPOON, HarpoonEntityRenderer::new);
		EntityRenderers.register(AbysmEntityTypes.MAN_O_WAR, ManOWarEntityRenderer::new);
		EntityRenderers.register(AbysmEntityTypes.LECTORFIN, LectorfinEntityRenderer::new);
		EntityRenderers.register(AbysmEntityTypes.MYSTERIOUS_BLOB, MysteriousBlobEntityRenderer::new);
//		EntityRenderers.register(AbysmEntityTypes.TEST_LEVIATHAN, LehydrathanEntityRenderer::new);
		EntityRenderers.register(AbysmEntityTypes.PADDLEFISH, FishEntityRenderer.factory("paddlefish"));
		EntityRenderers.register(AbysmEntityTypes.SNAPPER, SnapperEntityRenderer::new);
		EntityRenderers.register(AbysmEntityTypes.GUP_GUP, GupGupEntityRenderer::new);
		EntityRenderers.register(AbysmEntityTypes.AROWANA_MAGICII, FishEntityRenderer.factory("arowana_magicii"));
		EntityRenderers.register(AbysmEntityTypes.SYNTHETHIC_ORNIOTHOPE, FishEntityRenderer.factory("synthethic_orniothope"));

		EntityRenderers.register(AbysmEntityTypes.RETICULATED_FLIPRAY, FliprayEntityRenderer::new);
		EntityRenderers.register(AbysmEntityTypes.SKELETON_SHARK, SkeletonSharkEntityRenderer::new);

		ArmorRenderer.register(new FlippersRenderer(), AbysmItems.FLIPPERS);

		AbysmEntityLayers.init();
		AbysmParticles.init();
		AbysmRenderPipelines.init();
		AbysmDebugRenderers.init();

		ClientPlayNetworking.registerGlobalReceiver(HappyEntityParticlesS2CPayload.ID, (payload, context) -> {
			Level level = context.player().level();
			Entity entity = level.getEntity(payload.entityId());
			if (entity == null) {
				return;
			}
			ParticleOptions options = payload.particleEffect();
			RandomSource random = entity.getRandom();
			for (int i = 0; i < 5; i++) {
				double velocityX = random.nextGaussian() * 0.02;
				double velocityY = random.nextGaussian() * 0.02;
				double velocityZ = random.nextGaussian() * 0.02;
				level.addParticle(options, entity.getRandomX(1.0), entity.getRandomY() + 0.5, entity.getRandomZ(1.0), velocityX, velocityY, velocityZ);
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(EntityUpdateBlueS2CPayload.ID, (payload, context) -> {
			Level level = context.player().level();
			Entity entity = level.getEntity(payload.entityId());
			if (entity == null) {
				return;
			}
			if (entity instanceof LivingEntityDuck duck) {
				duck.abysm$setBlue(payload.isBlue());
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(NowHuntingS2CPayload.ID, (payload, context) -> {
			Level level = context.player().level();
			Entity entity = level.getEntity(payload.entityId());
			if (!(entity instanceof EcologicalEntity ecologicalEntity)) {
				return;
			}
			boolean hunting = payload.hunting();
			ecologicalEntity.setHunting(hunting);
			ecologicalEntity.setHuntTicks(hunting ? Integer.MAX_VALUE : 0);
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			LocalPlayer player = client.player;
			if (player == null) return;

			if (player.isEyeInFluid(FluidTags.WATER)) AbysmAL.enable();
			else AbysmAL.disable();
		});

//		ModMenuHelper.addConfig(Abysm.MODID, AbysmConfig.HOLDER.id());

		BlockRenderLayerMap.putBlocks(
			ChunkSectionLayer.CUTOUT,
			AbysmBlocks.ROSY_SPRIGS,
			AbysmBlocks.POTTED_ROSY_SPRIGS,
			AbysmBlocks.SUNNY_SPRIGS,
			AbysmBlocks.POTTED_SUNNY_SPRIGS,
			AbysmBlocks.MAUVE_SPRIGS,
			AbysmBlocks.POTTED_MAUVE_SPRIGS,

			AbysmBlocks.ROSEBLOOM_PETALS,
			AbysmBlocks.SUNBLOOM_PETALS,
			AbysmBlocks.MALLOWBLOOM_PETALS,

			AbysmBlocks.ROSEBLOOM_PETALEAVES,
			AbysmBlocks.SUNBLOOM_PETALEAVES,
			AbysmBlocks.MALLOWBLOOM_PETALEAVES,

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

			AbysmBlocks.OOZETRICKLE_LANTERN,
			AbysmBlocks.OOZETRICKLE_CORD
		);

		BlockRenderLayerMap.putBlocks(
			ChunkSectionLayer.TRANSLUCENT,
			AbysmBlocks.SWEET_NECTARSAP,
			AbysmBlocks.SOUR_NECTARSAP,
			AbysmBlocks.BITTER_NECTARSAP
		);

		ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> OozetrickleLanternBlock.getColor(state.getValue(OozetrickleLanternBlock.LIGHT)), AbysmBlocks.OOZETRICKLE_LANTERN);
	}
}
