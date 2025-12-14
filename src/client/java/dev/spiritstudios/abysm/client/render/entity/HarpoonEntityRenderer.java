package dev.spiritstudios.abysm.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.state.HarpoonRenderState;
import dev.spiritstudios.abysm.world.entity.harpoon.HarpoonEntity;
import dev.spiritstudios.abysm.world.item.HarpoonItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.ArrowModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class HarpoonEntityRenderer extends EntityRenderer<HarpoonEntity, HarpoonRenderState> {
	protected static final Identifier TEXTURE = Abysm.id("textures/item/harpoon_arrow.png");
	public static final Identifier CHAINS = Abysm.id("textures/entity/chains.png");

	private final ArrowModel model;

	public HarpoonEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new ArrowModel(context.bakeLayer(ModelLayers.ARROW));
	}

	@Override
	public void submit(HarpoonRenderState state, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
		if (state.handPos == Vec3.ZERO) {
			super.submit(state, poseStack, nodeCollector, cameraRenderState);
			return;
		}

		Vec3 start = state.handPos;

		Vec3 anchorOffset = Vec3.directionFromRotation(state.xRot, 180 - state.yRot).scale(0.7).subtract(0, 0.1, 0);

		Vec3 end = new Vec3(state.x, state.y, state.z).add(anchorOffset);

		Vec3 difference = start.subtract(end);

		double distZ = end.z - start.z;
		double distX = end.x - start.x;
		float horizontalAngle = (float) Mth.atan2(distZ, distX);
		horizontalAngle += Mth.ceil(-horizontalAngle / Mth.TWO_PI) * Mth.TWO_PI;

		float verticalAngle = (float) Mth.atan2(difference.y, Math.sqrt(distZ * distZ + distX * distX));

		float length = (float) (start.distanceTo(end) + 0.1);

		int startLight = LightTexture.pack(state.startBlockLight, state.startSkyLight);
		int endLight = LightTexture.pack(state.endBlockLight, state.endSkyLight);

		poseStack.pushPose();
		poseStack.translate(anchorOffset.x, anchorOffset.y, anchorOffset.z);
		{
			poseStack.mulPose(Axis.YP.rotation(-horizontalAngle - Mth.HALF_PI));
			poseStack.mulPose(Axis.XP.rotation(-verticalAngle));

			poseStack.mulPose(Axis.ZP.rotationDegrees(45));


			for (RenderType renderType : ItemRenderer.getFoilRenderTypes(this.model.renderType(CHAINS), false, state.isFoil)) {
				nodeCollector.submitCustomGeometry(
					poseStack,
					renderType,
					(pose, buffer) -> {
						renderPart(startLight, endLight, buffer, pose, length, false);

						pose.translate(0.1875F, 0.1875F, 0F);
						pose.rotate(Axis.ZP.rotationDegrees(90));

						renderPart(startLight, endLight, buffer, pose, length, true);
					}
				);
			}
		}
		poseStack.popPose();

		poseStack.pushPose();
		{
			poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
			poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot));
			this.model.setupAnim(state);

			for (RenderType renderType : ItemRenderer.getFoilRenderTypes(this.model.renderType(TEXTURE), false, state.isFoil)) {
				nodeCollector.submitModel(
					model,
					state,
					poseStack,
					renderType,
					endLight,
					OverlayTexture.NO_OVERLAY,
					state.outlineColor,
					null
				);
			}
		}
		poseStack.popPose();
	}


	private static void renderPart(int startLight, int endLight, VertexConsumer consumer, PoseStack.Pose entry, float length, boolean shift) {
		float minU = shift ? 0.5F : 0.0F;
		float maxU = shift ? 1.0F : 0.5F;
		float minV = 0.0F;
		float maxV = length / 10.0F;

		consumer.addVertex(entry, 0, 0.25F, 0)
			.setColor(255, 255, 255, 255)
			.setUv(minU, minV)
			.setOverlay(OverlayTexture.NO_OVERLAY)
			.setLight(endLight)
			.setNormal(entry, 0, -1, 0);

		consumer.addVertex(entry, 0, 0.25F, length)
			.setColor(255, 255, 255, 255)
			.setUv(minU, maxV)
			.setOverlay(OverlayTexture.NO_OVERLAY)
			.setLight(startLight)
			.setNormal(entry, 0, -1, 0);

		consumer.addVertex(entry, 0, 0.125F, length)
			.setColor(255, 255, 255, 255)
			.setUv(maxU, maxV)
			.setOverlay(OverlayTexture.NO_OVERLAY)
			.setLight(startLight)
			.setNormal(entry, 0, -1, 0);

		consumer.addVertex(entry, 0, 0.125F, 0)
			.setColor(255, 255, 255, 255)
			.setUv(maxU, minV)
			.setOverlay(OverlayTexture.NO_OVERLAY)
			.setLight(endLight)
			.setNormal(entry, 0, -1, 0);
	}

	public static HumanoidArm getArmHoldingRod(Player player) {
		return player.getMainHandItem().getItem() instanceof HarpoonItem ? player.getMainArm() : player.getMainArm().getOpposite();
	}

	private Vec3 getHandPos(Player player, float angle, float tickProgress) {
		int arm = getArmHoldingRod(player) == HumanoidArm.RIGHT ? 1 : -1;
		if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() &&
			player == Minecraft.getInstance().player) {
			return player.getEyePosition(tickProgress)
				.add(this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane(arm * 0.525F, -1F)
					.scale(960.0F / this.entityRenderDispatcher.options.fov().get())
					.yRot(angle * 0.5F)
					.xRot(-angle * 0.7F));
		} else {
			float yaw = (Mth.lerp(tickProgress, player.yBodyRotO, player.yBodyRot) + 10) * Mth.DEG_TO_RAD;

			double sinYaw = Mth.sin(yaw);
			double cosYaw = Mth.cos(yaw);

			float scale = player.getScale();
			float sideOffset = arm * 0.35F * scale;
			double forwardOffset = 0.3 * scale;
			float verticalOffset = player.isCrouching() ? -0.1875F : 0.0F;

			return player.getEyePosition(tickProgress)
				.add(-cosYaw * sideOffset - sinYaw * forwardOffset, verticalOffset - 0.45F * scale, -sinYaw * sideOffset + cosYaw * forwardOffset);
		}
	}

	@Override
	public HarpoonRenderState createRenderState() {
		return new HarpoonRenderState();
	}

	@Override
	public boolean shouldRender(HarpoonEntity entity, Frustum frustum, double x, double y, double z) {
		return true;
	}

	@Override
	public void extractRenderState(HarpoonEntity harpoon, HarpoonRenderState state, float partialTick) {
		super.extractRenderState(harpoon, state, partialTick);
		Player player = harpoon.getPlayer();

		if (player == null || harpoon.isRemoved()) {
			state.handPos = Vec3.ZERO;
		} else {
			state.handPos = this.getHandPos(
				player,
				Mth.sin(Mth.sqrt(player.getAttackAnim(partialTick)) * Mth.PI),
				partialTick
			);
		}

		BlockPos start = BlockPos.containing(state.handPos);
		state.startBlockLight = getBlockLightLevel(harpoon, start);
		state.startSkyLight = getSkyLightLevel(harpoon, start);

		BlockPos end = BlockPos.containing(harpoon.getLightProbePosition(partialTick));
		state.endSkyLight = getBlockLightLevel(harpoon, end);
		state.startSkyLight = getSkyLightLevel(harpoon, end);

		state.yRot = harpoon.getYRot(partialTick);
		state.xRot = harpoon.getXRot(partialTick);
		state.isFoil = harpoon.isEnchanted();
	}
}
