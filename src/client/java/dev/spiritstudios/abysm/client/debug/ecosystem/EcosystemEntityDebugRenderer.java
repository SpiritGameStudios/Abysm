package dev.spiritstudios.abysm.client.debug.ecosystem;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.world.ecosystem.entity.EcologicalEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo;
import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Util;
import net.minecraft.util.debug.DebugValueAccess;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EcosystemEntityDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
	private static final int DARK_RED = 0xFF640000;

	public final Minecraft minecraft;
	private double lastUpdateTime = Double.MIN_VALUE;
	@Nullable
	private EcosystemEntityDebugRenderer.EntityLoadingStatus loadingStatus;

	public EcosystemEntityDebugRenderer(Minecraft minecraft) {
		this.minecraft = minecraft;
	}

	@Override
	public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValueAccess, Frustum frustum, float partialTick) {
		double time = Util.getNanos();
		if (time - this.lastUpdateTime > 3.0E9) {
			this.lastUpdateTime = time;
			this.updateStatus();
		}

		if (this.loadingStatus == null) return;

		List<EntityStatus> entityStatuses = this.loadingStatus.serverStates.getNow(null);
		if (entityStatuses == null) return;

		for (EntityStatus entityStatus : entityStatuses) {
			List<StringInfo> stringInfos = new ArrayList<>();

			if (entityStatus.shouldHunt) stringInfos.add(new StringInfo("Should Hunt", CommonColors.GREEN));
			if (entityStatus.shouldRepopulate) stringInfos.add(new StringInfo("Should Repopulate", CommonColors.GREEN));
			if (entityStatus.shouldScavenge) stringInfos.add(new StringInfo("Should Scavenge", CommonColors.GREEN));

			if (entityStatus.isHunting || entityStatus.isBeingHunted) {
				boolean favored = entityStatus.favoredInHunt;
				int huntColor = favored ? CommonColors.SOFT_RED : DARK_RED;

				// In theory, both of these should never be active, but it isn't impossible as of now
				if (entityStatus.isHunting)
					stringInfos.add(new StringInfo("Hunting - " + entityStatus.huntTicks, huntColor));
				if (entityStatus.isBeingHunted) stringInfos.add(new StringInfo("Being Hunted", huntColor));
			}

			if (stringInfos.isEmpty()) continue;
			stringInfos.add(new StringInfo(entityStatus.name, CommonColors.WHITE));


			float yOffsetAmount = 0.35f;
			float yOffset = stringInfos.size() * yOffsetAmount;

			Vec3 renderPos = entityStatus.entity.blockPosition().getCenter().add(1);

			// Render top to bottom
			for (StringInfo stringInfo : stringInfos) {
				Gizmos.billboardText(
					stringInfo.string,
					renderPos.add(0, yOffset, 0),
					TextGizmo.Style.forColorAndCentered(stringInfo.color)
				);

				yOffset -= yOffsetAmount;
			}
		}
	}

	private void updateStatus() {
		IntegratedServer integratedServer = this.minecraft.getSingleplayerServer();
		if (integratedServer == null) {
			this.loadingStatus = null;
			return;
		}

//		int searchRadius = this.client.player.isHolding(Items.ENDER_EYE) ? this.client.options.getClampedViewDistance() : 1;
		this.loadingStatus = new EntityLoadingStatus(integratedServer, this.minecraft.player.getBoundingBox().inflate(30));
	}

	@Environment(EnvType.CLIENT)
	final class EntityLoadingStatus {
		final CompletableFuture<List<EntityStatus>> serverStates;

		EntityLoadingStatus(IntegratedServer server, AABB searchBox) {
			ClientLevel clientWorld = EcosystemEntityDebugRenderer.this.minecraft.level;
			assert clientWorld != null;

			ResourceKey<Level> worldKey = clientWorld.dimension();

			this.serverStates = server.submit(() -> {
				ServerLevel serverWorld = server.getLevel(worldKey);
				if (serverWorld == null) return ImmutableList.of();

				ImmutableList.Builder<EntityStatus> statusBuilder = ImmutableList.builder();

				serverWorld.getEntitiesOfClass(Mob.class, searchBox, LivingEntity::isAlive).forEach(entity -> {
					if (!(entity instanceof EcologicalEntity ecologicalEntity)) return;

					// Names are completely useless, but I think it's fun that Minecraft uses them for debug anyways
					statusBuilder.add(new EntityStatus(entity, DebugEntityNameGenerator.getEntityName(entity),
						ecologicalEntity.shouldHunt(), ecologicalEntity.shouldRepopulate(), ecologicalEntity.shouldScavenge(),
						ecologicalEntity.isHunting(), ecologicalEntity.isBeingHunted(), ecologicalEntity.isFavoredInHunt(), ecologicalEntity.getHuntTicks()
					));
				});

				return statusBuilder.build();
			});
		}
	}

	private record EntityStatus(
		Mob entity, String name,
		boolean shouldHunt, boolean shouldRepopulate, boolean shouldScavenge,
		boolean isHunting, boolean isBeingHunted, boolean favoredInHunt, int huntTicks
	) {
	}

	private static record StringInfo(String string, int color) {
	}
}
