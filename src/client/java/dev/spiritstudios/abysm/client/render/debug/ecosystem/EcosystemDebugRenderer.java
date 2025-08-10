package dev.spiritstudios.abysm.client.render.debug.ecosystem;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemArea;
import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemArea.PopInfo;
import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemAreaManager;
import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemAreaPos;
import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemChunk;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Displays each EcosystemType form all nearby created {@link EcosystemChunk}. Format:<br><br>
 * {EntityType}: {NearbyPopulation} / {TargetPopulation} ({PopulationInChunk})<br><br>
 *
 * The displayed colors are based on the {@link EcosystemChunk.PopStatus} of {@link EcosystemChunk#getPopStatus(EcosystemType)}<br>
 * Dark Green = Overpopulated<br>
 * Green = Maintained<br>
 * Orange/yellow = Underpopulated<br>
 * Light red = Near extinct<br>
 * Dark red = Extinct<br><br>
 *
 * Hold a Spyglass to have text rendering stay a few blocks beneath sea level instead of following your y level.<br>
 * Hold a Heart of The Sea to see only the chunks of the EcosystemType's search radius, as your current chunk in the center.
 */
public class EcosystemDebugRenderer implements DebugRenderer.Renderer {
	private static final int DARK_RED = 0x640000;

	public final MinecraftClient client;
	private double lastUpdateTime = Double.MIN_VALUE;
	@Nullable
	private EcosystemDebugRenderer.EcosystemLoadingStatus loadingStatus;

	public EcosystemDebugRenderer(MinecraftClient client) {
		this.client = client;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
		// Strange updating system ~~stolen~~ borrowed from ChunkLoadingDebugRenderer
		double time = Util.getMeasuringTimeNano();
		if (time - this.lastUpdateTime > 3.0E9) {
			this.lastUpdateTime = time;
			this.updateStatus();
		}

		if (this.loadingStatus == null) return;

		List<EcosystemArea> ecosystemAreas = this.loadingStatus.serverStates.getNow(null);
		if (ecosystemAreas == null) return;

		ClientPlayerEntity player = this.client.player;
		ClientWorld world = player.clientWorld;
		boolean renderPositions = player.isHolding(Items.COMPASS);
		double initY = this.client.gameRenderer.getCamera().getPos().y * 0.95;

		if (player.isHolding(Items.SPYGLASS)) {
			initY = player.getWorld().getSeaLevel() - 3;
		}

//		ChunkPos playerChunk = player.getChunkPos();
//		boolean onlyNearbyChunks = player.isHolding(Items.HEART_OF_THE_SEA);

		for (EcosystemArea ecosystemArea : ecosystemAreas) {
			EcosystemAreaPos ecosystemAreaPos = ecosystemArea.pos;
			ChunkPos areaCenterPos = ecosystemAreaPos.getCenterChunkPos();
			int yOffset = 0;

			// Outlines
			int offsetAmount = 16;
			double centerMinX = ChunkSectionPos.getOffsetPos(areaCenterPos.x, 0) - cameraX;
			double centerMinZ = ChunkSectionPos.getOffsetPos(areaCenterPos.z, 0) - cameraZ;
			double centerMaxX = ChunkSectionPos.getOffsetPos(areaCenterPos.x, offsetAmount) - cameraX;
			double centerMaxZ = ChunkSectionPos.getOffsetPos(areaCenterPos.z, offsetAmount) - cameraZ;
			double minY = -30;
			double maxY = 20;
			if(player.isHolding(Items.SPYGLASS)) {
				minY = -60 - cameraY;
				maxY = world.getSeaLevel() + 10 - cameraY;
			}
			VertexRendering.drawBox(matrices, vertexConsumers.getBuffer(RenderLayer.getLines()), centerMinX, minY, centerMinZ, centerMaxX, maxY, centerMaxZ, 1f, 1f, 1f, 1f);
			VertexRendering.drawBox(matrices, vertexConsumers.getBuffer(RenderLayer.getLines()), centerMinX - offsetAmount, minY, centerMinZ - offsetAmount, centerMaxX + offsetAmount, maxY, centerMaxZ + offsetAmount, 1f, 0f, 0f, 1f);

			// EcosystemAreaPos position
			if(renderPositions) {
				String areaPosString = ecosystemAreaPos.toString();
				drawString(matrices, vertexConsumers, areaPosString, areaCenterPos, initY, 1, Colors.WHITE);
			}

			// Populations
			if (ecosystemArea instanceof EmptyEcosystemArea || ecosystemArea.populations.isEmpty()) {
				String empty = "Empty!";
				int color = Colors.LIGHT_GRAY;
				drawString(matrices, vertexConsumers, empty, areaCenterPos, initY, yOffset, color);
				continue;
			}

			for (Map.Entry<EcosystemType<?>, PopInfo> popEntry : ecosystemArea.populations.entrySet()) {
				EcosystemType<?> ecosystemType = popEntry.getKey();
				PopInfo popInfo = popEntry.getValue();
				Text typeName = ecosystemType.entityType().getName();

				int areaPopulation = popInfo.getEntityCount();
				int nearbyPopulation = ecosystemArea.getNearbyPopulationSize(ecosystemType);
				int targetPopulation = ecosystemType.targetPopulation();

				// EntityName: NearbyPopulation/TargetPopulation (ChunkPopulation)
				String stringedInfo = String.format("%s: %s/%s (%s)", typeName.getString(), nearbyPopulation, targetPopulation, areaPopulation);
				int color = nearbyPopulation >= targetPopulation ? Colors.GREEN : Colors.LIGHT_RED;
				drawString(matrices, vertexConsumers, stringedInfo, areaCenterPos, initY, yOffset, color);
				yOffset -= 1;
			}

		}
	}

	private void drawString(MatrixStack matrices, VertexConsumerProvider vertexConsumers, String string, ChunkPos chunkPos, double initY, int yOffset, int color) {
		double x = ChunkSectionPos.getOffsetPos(chunkPos.x, 8);
		double y = initY + yOffset;
		double z = ChunkSectionPos.getOffsetPos(chunkPos.z, 8);

		float size = 0.05f;
		float offset = 0;
		DebugRenderer.drawString(
			matrices, vertexConsumers, string,
			x, y, z,
			color, size, true,
			offset, true
		);
	}

	private void updateStatus() {
		IntegratedServer integratedServer = this.client.getServer();
		if (integratedServer == null) {
			this.loadingStatus = null;
			return;
		}

		int searchRadius = this.client.player.isHolding(Items.ENDER_EYE) ? this.client.options.getClampedViewDistance() : 1;
		this.loadingStatus = new EcosystemLoadingStatus(integratedServer, getPlayerChunkPos(), searchRadius);
	}

	private ChunkPos getPlayerChunkPos() {
		assert this.client.player != null;
		return this.client.player.getChunkPos();
	}

	@Environment(EnvType.CLIENT)
	final class EcosystemLoadingStatus {
		final CompletableFuture<List<EcosystemArea>> serverStates;

		@SuppressWarnings("UnstableApiUsage")
		EcosystemLoadingStatus(IntegratedServer server, ChunkPos playerChunkPos, int searchRadius) {
			ClientWorld clientWorld = EcosystemDebugRenderer.this.client.world; // how???
			// you can do that because this class isn't static
			// - echo
			// Huh, makes sense but looks very goofy xD ~ kat

			assert clientWorld != null;
			RegistryKey<World> worldKey = clientWorld.getRegistryKey();

			// This is beyond cursed, but it's what Minecraft does ¯\_(ツ)_/¯
			this.serverStates = server.submit(() -> {
				ServerWorld serverWorld = server.getWorld(worldKey);
				if (serverWorld == null) return ImmutableList.of();

				ImmutableList.Builder<EcosystemArea> areaBuilder = ImmutableList.builder();

				EcosystemAreaManager ecosystemAreaManager = EcosystemAreaManager.getEcosystemAreaManagerForWorld(serverWorld);
				EcosystemAreaPos playerEcosystemPos = new EcosystemAreaPos(playerChunkPos);
				for (EcosystemAreaPos areaPos : EcosystemAreaPos.stream(playerEcosystemPos, searchRadius).toList()) {
					EcosystemArea area = ecosystemAreaManager.areas.getOrDefault(areaPos, new EmptyEcosystemArea(serverWorld, areaPos));
					areaBuilder.add(area);
				}

				return areaBuilder.build();
			});
		}
	}

	// Used to define a chunk doesn't have any attached EcosystemChunk
	private static class EmptyEcosystemArea extends EcosystemArea {
		public EmptyEcosystemArea(ServerWorld world, EcosystemAreaPos pos) {
			super(world, pos);
		}
	}
}
