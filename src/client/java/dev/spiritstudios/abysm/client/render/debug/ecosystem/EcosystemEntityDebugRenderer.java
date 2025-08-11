package dev.spiritstudios.abysm.client.render.debug.ecosystem;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Colors;
import net.minecraft.util.NameGenerator;
import net.minecraft.util.Util;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EcosystemEntityDebugRenderer implements DebugRenderer.Renderer {
	private static final int DARK_RED = 0xFF640000;

	public final MinecraftClient client;
	private double lastUpdateTime = Double.MIN_VALUE;
	@Nullable
	private EcosystemEntityDebugRenderer.EntityLoadingStatus loadingStatus;

	public EcosystemEntityDebugRenderer(MinecraftClient client) {
		this.client = client;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
		double time = Util.getMeasuringTimeNano();
		if (time - this.lastUpdateTime > 3.0E9) {
			this.lastUpdateTime = time;
			this.updateStatus();
		}

		if (this.loadingStatus == null) return;

		List<EntityStatus> entityStatuses = this.loadingStatus.serverStates.getNow(null);
		if(entityStatuses == null) return;

		Vec3d cameraPos = new Vec3d(cameraX, cameraY, cameraZ);

		for (EntityStatus entityStatus : entityStatuses) {
			List<StringInfo> stringInfos = new ArrayList<>();

			if(entityStatus.shouldHunt) stringInfos.add(new StringInfo("Should Hunt", Colors.GREEN));
			if(entityStatus.shouldRepopulate) stringInfos.add(new StringInfo("Should Repopulate", Colors.GREEN));

			if(entityStatus.isHunting || entityStatus.isBeingHunted) {
				boolean favored = entityStatus.favoredInHunt;
				int huntColor = favored ? Colors.LIGHT_RED : DARK_RED;

				// In theory, both of these should never be active, but it isn't impossible as of now
				if(entityStatus.isHunting) stringInfos.add(new StringInfo("Hunting - " + entityStatus.huntTicks, huntColor));
				if(entityStatus.isBeingHunted) stringInfos.add(new StringInfo("Being Hunted", huntColor));
			}

			if(stringInfos.isEmpty()) continue;
			stringInfos.add(new StringInfo(entityStatus.name, Colors.WHITE));


			Vec3d renderPos = entityStatus.entity.getBlockPos().toCenterPos().add(1);
			float yOffsetAmount = 0.35f;
			float yOffset = stringInfos.size() * yOffsetAmount;

			// Render top to bottom
			for (StringInfo stringInfo : stringInfos) {
				drawString(matrices, vertexConsumers, stringInfo.string, renderPos, yOffset, stringInfo.color);
				yOffset -= yOffsetAmount;
			}
		}
	}

	private void drawString(MatrixStack matrices, VertexConsumerProvider vertexConsumers, String string, Vec3d pos, float yOffset, int color) {
		double x = pos.getX();
		double y = pos.getY() + yOffset;
		double z = pos.getZ();

		float size = 0.025f;
		float offset = 0;
		DebugRenderer.drawString(
			matrices, vertexConsumers, string,
			x, y, z,
			color, size, true,
			offset, true
		);

//		return yOffset -= 1;
	}

	private void updateStatus() {
		IntegratedServer integratedServer = this.client.getServer();
		if (integratedServer == null) {
			this.loadingStatus = null;
			return;
		}

//		int searchRadius = this.client.player.isHolding(Items.ENDER_EYE) ? this.client.options.getClampedViewDistance() : 1;
		this.loadingStatus = new EntityLoadingStatus(integratedServer, this.client.player.getBoundingBox().expand(30));
	}

	@Environment(EnvType.CLIENT)
	final class EntityLoadingStatus {
		final CompletableFuture<List<EntityStatus>> serverStates;

		EntityLoadingStatus(IntegratedServer server, Box searchBox) {
			ClientWorld clientWorld = EcosystemEntityDebugRenderer.this.client.world;
			assert clientWorld != null;

			RegistryKey<World> worldKey = clientWorld.getRegistryKey();

			this.serverStates = server.submit(() -> {
				ServerWorld serverWorld = server.getWorld(worldKey);
				if(serverWorld == null) return ImmutableList.of();

				ImmutableList.Builder<EntityStatus> statusBuilder = ImmutableList.builder();

				serverWorld.getEntitiesByClass(MobEntity.class, searchBox, LivingEntity::isAlive).forEach(entity -> {
					if(!(entity instanceof EcologicalEntity ecologicalEntity)) return;

					// Names are completely useless, but I think it's fun that Minecraft uses them for debug anyways
					statusBuilder.add(new EntityStatus(entity, NameGenerator.name(entity),
						ecologicalEntity.canHunt(), ecologicalEntity.canRepopulate(),
						ecologicalEntity.isHunting(), ecologicalEntity.isBeingHunted(), ecologicalEntity.isFavoredInHunt(), ecologicalEntity.getHuntTicks()
					));
				});

				return statusBuilder.build();
			});
		}
	}

	private static record EntityStatus (
		MobEntity entity, String name,
		boolean shouldHunt, boolean shouldRepopulate,
		boolean isHunting, boolean isBeingHunted, boolean favoredInHunt, int huntTicks
	) {}

	private static record StringInfo(String string, int color) {}
}
