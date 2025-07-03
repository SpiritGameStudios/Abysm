package dev.spiritstudios.abysm.client.render.debug.ecosystem;

import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemChunk;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.registry.AbysmAttachments;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
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
import net.minecraft.world.chunk.Chunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.common.collect.ImmutableBiMap;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

// Displays each EcosystemType from all nearby created EcosystemChunks.
// {EntityType}: {NearbyPopulation} / {TargetPopulation} ({PopulationInChunk})

// Green = nearby population meets target
// Light red = nearby population doesn't meet target
// Dark red = No nearby population (extinct)

// Hold a spyglass in main hand to have text rendering stay a few blocks beneath sea level instead of following you
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

		Map<ChunkPos, EcosystemChunk> ecosystemChunkMap = this.loadingStatus.serverStates.getNow(null);
		if (ecosystemChunkMap == null) return;

		double initY = this.client.gameRenderer.getCamera().getPos().y * 0.95;
		assert this.client.player != null;

		if (this.client.player.isHolding(Items.SPYGLASS)) {
			initY = this.client.player.getWorld().getSeaLevel() - 3;
		}

		for (Map.Entry<ChunkPos, EcosystemChunk> entry : ecosystemChunkMap.entrySet()) {
			ChunkPos chunkPos = entry.getKey();
			EcosystemChunk ecosystemChunk = entry.getValue();
			int yOffset = 0;

			if (ecosystemChunk instanceof EmptyEcosystemChunk || ecosystemChunk.entityPopulation.isEmpty()) {
				String empty = "Empty!";
				int color = Colors.LIGHT_GRAY;
				drawString(matrices, vertexConsumers, empty, chunkPos, initY, yOffset, color);
				continue;
			}

			for (Map.Entry<EcosystemType<?>, EcosystemChunk.PopInfo> popEntry : ecosystemChunk.entityPopulation.entrySet()) {
				EcosystemType<?> ecosystemType = popEntry.getKey();
				EcosystemChunk.PopInfo popInfo = popEntry.getValue();
				Text typeName = ecosystemType.entityType().getName();

				int chunkPopulation = popInfo.getEntityCount();
				int nearbyPopulation = ecosystemChunk.getNearbyEcosystemTypePopulation(ecosystemType);
				int targetPopulation = ecosystemType.targetPopulation();
				boolean okay = nearbyPopulation >= targetPopulation;
				boolean extinctInArea = !okay && (nearbyPopulation <= 0);

				// EntityName: NearbyPopulation/TargetPopulation (ChunkPopulation)
				String stringedInfo = String.format("%s: %s/%s (%s)", typeName.getString(), nearbyPopulation, targetPopulation, chunkPopulation);
				int color = okay ? Colors.GREEN :
					extinctInArea ? DARK_RED : Colors.LIGHT_RED;
				drawString(matrices, vertexConsumers, stringedInfo, chunkPos, initY, yOffset, color);

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

		this.loadingStatus = new EcosystemLoadingStatus(integratedServer, getPlayerChunkPos());
	}

	private ChunkPos getPlayerChunkPos() {
		return this.client.player.getChunkPos();
	}

	@Environment(EnvType.CLIENT)
	final class EcosystemLoadingStatus {
		final CompletableFuture<Map<ChunkPos, EcosystemChunk>> serverStates;

		@SuppressWarnings("UnstableApiUsage")
		EcosystemLoadingStatus(IntegratedServer server, ChunkPos playerChunkPos) {
			ClientWorld clientWorld = EcosystemDebugRenderer.this.client.world; // how???
			// you can do that because this class isn't static
			// - echo

			RegistryKey<World> worldKey = clientWorld.getRegistryKey();

			// This is beyond cursed but it's what Minecraft does ¯\_(ツ)_/¯
			this.serverStates = server.submit(() -> {
				ServerWorld serverWorld = server.getWorld(worldKey);
				if (serverWorld == null) return ImmutableBiMap.of();

				ImmutableMap.Builder<ChunkPos, EcosystemChunk> mapBuilder = ImmutableMap.builder();
				for (ChunkPos chunkPos : ChunkPos.stream(playerChunkPos, 2).toList()) {
					Chunk chunk = serverWorld.getChunk(chunkPos.x, chunkPos.z);
					EcosystemChunk ecosystemChunk = chunk.getAttachedOrElse(AbysmAttachments.ECOSYSTEM_CHUNK, new EmptyEcosystemChunk(serverWorld, chunkPos));
					mapBuilder.put(chunkPos, ecosystemChunk);
				}

				return mapBuilder.build();
			});
		}
	}

	// Used to define a chunk doesn't have any attached EcosystemChunk
	private static class EmptyEcosystemChunk extends EcosystemChunk {
		public EmptyEcosystemChunk(World world, ChunkPos pos) {
			super(world, pos);
		}
	}
}
