package dev.spiritstudios.abysm.client.debug.ecosystem;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.world.ecosystem.chunk.EcosystemArea;
import dev.spiritstudios.abysm.world.ecosystem.chunk.EcosystemAreaManager;
import dev.spiritstudios.abysm.world.ecosystem.chunk.EcosystemAreaPos;
import dev.spiritstudios.abysm.world.ecosystem.registry.EcosystemData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.SectionPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Util;
import net.minecraft.util.debug.DebugValueAccess;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Displays each EcosystemType form all nearby created {@link EcosystemArea}. Format:<br><br>
 * {EntityType}: {NearbyPopulation} / {TargetPopulation} ({PopulationInChunk})<br><br>
 * <p>
 * The displayed colors are based on the {@link EcosystemArea.PopInfo} of {@link EcosystemArea#populations}<br>
 * Dark Green = Overpopulated<br>
 * Green = Maintained<br>
 * Orange/yellow = Underpopulated<br>
 * Light red = Near extinct<br>
 * Dark red = Extinct<br><br>
 * <p>
 * Hold a Spyglass to have text rendering stay a few blocks beneath sea clientLevel instead of following your y clientLevel.<br>
 * Hold a Heart of The Sea to see only the chunks of the EcosystemType's search radius, as your current chunk in the center.
 */
public class EcosystemPopulationDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
	private static final int DARK_RED = 0xFF640000;
	private static final int DARK_GREEN = 0xFF006400;

	public final Minecraft minecraft;
	private double lastUpdateTime = Double.MIN_VALUE;
	@Nullable
	private EcosystemPopulationDebugRenderer.EcosystemLoadingStatus loadingStatus;

	public EcosystemPopulationDebugRenderer(Minecraft minecraft) {
		this.minecraft = minecraft;
	}

	private void updateStatus() {
		IntegratedServer integratedServer = this.minecraft.getSingleplayerServer();
		if (integratedServer == null) {
			this.loadingStatus = null;
			return;
		}

		int searchRadius = this.minecraft.player.isHolding(Items.ENDER_EYE) ? this.minecraft.options.getEffectiveRenderDistance() : 1;
		this.loadingStatus = new EcosystemLoadingStatus(integratedServer, getPlayerChunkPos(), searchRadius);
	}

	private ChunkPos getPlayerChunkPos() {
		assert this.minecraft.player != null;
		return this.minecraft.player.chunkPosition();
	}

	private void emitString(String string, ChunkPos chunkPos, double initY, int yOffset, int color) {
		double x = SectionPos.sectionToBlockCoord(chunkPos.x, 8);
		double y = initY + yOffset;
		double z = SectionPos.sectionToBlockCoord(chunkPos.z, 8);

		Gizmos.billboardText(
			string,
			new Vec3(x, y, z),
			TextGizmo.Style.forColorAndCentered(color)
		);
	}

	@Override
	public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValueAccess, Frustum frustum, float partialTick) {
		// Strange updating system ~~stolen~~ borrowed from ChunkLoadingDebugRenderer
		double time = Util.getNanos();
		if (time - this.lastUpdateTime > 3.0E9) {
			this.lastUpdateTime = time;
			this.updateStatus();
		}

		if (this.loadingStatus == null) return;

		List<EcosystemArea> ecosystemAreas = this.loadingStatus.serverStates.getNow(null);
		if (ecosystemAreas == null) return;

		LocalPlayer player = this.minecraft.player;
		var level = player.level();
		boolean renderPositions = player.isHolding(Items.COMPASS);
		double initY = this.minecraft.gameRenderer.getMainCamera().position().y;

		if (player.isHolding(Items.SPYGLASS)) {
			initY = player.level().getSeaLevel() - 3;
		}

//		ChunkPos playerChunk = player.getChunkPos();
//		boolean onlyNearbyChunks = player.isHolding(Items.HEART_OF_THE_SEA);

		for (EcosystemArea ecosystemArea : ecosystemAreas) {
			EcosystemAreaPos ecosystemAreaPos = ecosystemArea.pos;
			ChunkPos areaCenterPos = ecosystemAreaPos.getCenterChunkPos();
			int yOffset = 0;

			// Outlines
			int offsetAmount = 16;
			double centerMinX = SectionPos.sectionToBlockCoord(areaCenterPos.x, 0) - camX;
			double centerMinZ = SectionPos.sectionToBlockCoord(areaCenterPos.z, 0) - camZ;
			double centerMaxX = SectionPos.sectionToBlockCoord(areaCenterPos.x, offsetAmount) - camX;
			double centerMaxZ = SectionPos.sectionToBlockCoord(areaCenterPos.z, offsetAmount) - camZ;
			double minY = -30;
			double maxY = 20;
			if (player.isHolding(Items.SPYGLASS)) {
				minY = -60 - camY;
				maxY = level.getSeaLevel() + 10 - camY;
			}

			Gizmos.cuboid(
				new AABB(
					new Vec3(centerMinX, minY, centerMinZ),
					new Vec3(centerMaxX, maxY, centerMaxZ)
				),
				GizmoStyle.stroke(CommonColors.WHITE, 1F)
			);

			Gizmos.cuboid(
				new AABB(
					new Vec3(centerMinX - offsetAmount, minY, centerMinZ - offsetAmount),
					new Vec3(centerMaxX + offsetAmount, maxY, centerMaxZ + offsetAmount)
				),
				GizmoStyle.stroke(0xFFFF0000, 1F)
			);

			// EcosystemAreaPos position
			if (renderPositions) {
				String areaPosString = ecosystemAreaPos.toString();
				emitString(areaPosString, areaCenterPos, initY, 1, CommonColors.WHITE);
			}

			// Populations
			if (ecosystemArea instanceof EmptyEcosystemArea || ecosystemArea.populations.isEmpty()) {
				String empty = "Empty!";
				int color = CommonColors.LIGHT_GRAY;
				emitString(empty, areaCenterPos, initY, yOffset, color);
				continue;
			}

			for (EcosystemArea.PopInfo popInfo : ecosystemArea.populations.values()) {
				EcosystemData ecosystemData = popInfo.data;
				Component typeName = popInfo.type.getDescription();

				int areaPopulation = popInfo.getEntityCount();
				int nearbyPopulation = ecosystemArea.getNearbyPopulationSize(popInfo.type);
				int targetPopulation = ecosystemData.targetPopulation();

				// EntityName: NearbyPopulation/TargetPopulation (ChunkPopulation)
				String stringedInfo = String.format("%s: %s/%s (%s)", typeName.getString(), nearbyPopulation, targetPopulation, areaPopulation);
				int color;
				if (nearbyPopulation <= 0) {
					color = DARK_RED;
				} else if (nearbyPopulation < ecosystemData.targetPopulation()) {
					color = CommonColors.SOFT_RED;
				} else {
					color = CommonColors.GREEN;
				}
				emitString(stringedInfo, areaCenterPos, initY, yOffset, color);
				yOffset -= 1;
			}

		}
	}

	@Environment(EnvType.CLIENT)
	final class EcosystemLoadingStatus {
		final CompletableFuture<List<EcosystemArea>> serverStates;

		EcosystemLoadingStatus(IntegratedServer server, ChunkPos playerChunkPos, int searchRadius) {
			ClientLevel clientLevel = EcosystemPopulationDebugRenderer.this.minecraft.level; // how???
			// you can do that because this class isn't static
			// - echo
			// Huh, makes sense but looks very goofy xD ~ kat

			assert clientLevel != null;
			ResourceKey<Level> dimension = clientLevel.dimension();

			// This is beyond cursed, but it's what Minecraft does ¯\_(ツ)_/¯
			this.serverStates = server.submit(() -> {
				ServerLevel serverLevel = server.getLevel(dimension);
				if (serverLevel == null) return ImmutableList.of();

				ImmutableList.Builder<EcosystemArea> areaBuilder = ImmutableList.builder();

				EcosystemAreaManager ecosystemAreaManager = EcosystemAreaManager.getEcosystemAreaManagerForWorld(serverLevel);
				EcosystemAreaPos playerEcosystemPos = new EcosystemAreaPos(playerChunkPos);
				for (EcosystemAreaPos areaPos : EcosystemAreaPos.stream(playerEcosystemPos, searchRadius).toList()) {
					EcosystemArea area = ecosystemAreaManager.areas.getOrDefault(areaPos, new EmptyEcosystemArea(serverLevel, areaPos));
					areaBuilder.add(area);
				}

				return areaBuilder.build();
			});
		}
	}

	// Used to define a chunk doesn't have any attached EcosystemChunk
	private static class EmptyEcosystemArea extends EcosystemArea {
		public EmptyEcosystemArea(ServerLevel level, EcosystemAreaPos pos) {
			super(level, pos);
		}
	}
}
