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
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;
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
		Vec3 end = new Vec3(state.x, state.y, state.z);

		Vec3 vector = start.subtract(end);
		float length = (float) vector.length();
		vector = vector.normalize();

		float pitch = (float) Math.acos(vector.y);
		float yaw = Mth.HALF_PI - (float) Mth.atan2(vector.z, vector.x);

		int startLight = LightTexture.pack(state.startBlockLight, state.startSkyLight);
		int endLight = LightTexture.pack(state.endBlockLight, state.endSkyLight);

		// lots of finals here to let javac inline stuff
		final float scale = 0.1F;

		final float x1 = scale * -1; // cos(pi) = -1
		final float z1 = scale * 0; // sin(pi) = 0

		final float x2 = scale * 1; // cos(0) = 1
		final float z2 = scale * 0; // sin(0) = 0

		final float x3 = scale * 0; // cos(pi / 2) = 0
		final float z3 = scale * 1; // sin(pi / 2) = 1

		final float x4 = scale * 0; // cos(pi + (pi / 2)) = 0
		final float z4 = scale * -1; // sin(pi + (pi / 2)) = -1

		float ropeAB = length + 1;

		final float textureSize = 16;

		final float firstChainU = 3 / textureSize;
		final float secondChainU = (3 + 3) / textureSize;

		poseStack.pushPose();
		{
			poseStack.mulPose(Axis.YP.rotation(yaw));
			poseStack.mulPose(Axis.XP.rotation(pitch));

			for (RenderType renderType : ItemRenderer.getFoilRenderTypes(RenderTypes.entityCutoutNoCull(CHAINS), false, state.isFoil)) {
				nodeCollector.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
					vertex(buffer, pose, x1, length, z1, 0, 1, 0, firstChainU, ropeAB, startLight);
					vertex(buffer, pose, x1, 0, z1, 0, 1, 0, firstChainU, 1, endLight);
					vertex(buffer, pose, x2, 0, z2, 0, 1, 0, 0F, 1, endLight);
					vertex(buffer, pose, x2, length, z2, 0, 1, 0, 0F, ropeAB, startLight);

					vertex(buffer, pose, x3, length, z3, 1, 0, 0, secondChainU, ropeAB, startLight);
					vertex(buffer, pose, x3, 0, z3, 1, 0, 0, secondChainU, 1, endLight);
					vertex(buffer, pose, x4, 0, z4, 1, 0, 0, firstChainU, 1, endLight);
					vertex(buffer, pose, x4, length, z4, 1, 0, 0, firstChainU, ropeAB, startLight);
				});
			}

			poseStack.mulPose(Axis.XP.rotation(-Mth.HALF_PI));
			poseStack.mulPose(Axis.YP.rotation(Mth.HALF_PI));

			poseStack.translate(0.6, 0, 0);

			this.model.setupAnim(state);

			for (RenderType renderType : ItemRenderer.getFoilRenderTypes(this.model.renderType(TEXTURE), false, state.isFoil)) {
				nodeCollector.submitModel(model, state, poseStack, renderType, endLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
			}
		}
		poseStack.popPose();
	}

	private void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float z, float normalX, float normalY, float normalZ, float u, float v, int lightCoords) {
		buffer
			.addVertex(pose.pose(), x, y, z)
			.setColor(CommonColors.WHITE)
			.setUv(u, v)
			.setOverlay(OverlayTexture.NO_OVERLAY)
			.setLight(lightCoords)
			.setNormal(pose, normalX, normalY, normalZ);
	}

	public static HumanoidArm getArmHoldingRod(Player player) {
		return player.getMainHandItem().getItem() instanceof HarpoonItem ? player.getMainArm() : player.getMainArm().getOpposite();
	}

	private Vec3 getHandPos(Player player, float handAngle, float tickProgress) {
		int arm = getArmHoldingRod(player) == HumanoidArm.RIGHT ? 1 : -1;
		if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && player == Minecraft.getInstance().player) {
			return player.getEyePosition(tickProgress)
				.add(this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane(arm * 0.525F, -1F)
					.scale(960.0F / this.entityRenderDispatcher.options.fov().get())
					.yRot(handAngle * 0.5F).xRot(-handAngle * 0.7F));
		} else {
			float yaw = (Mth.lerp(tickProgress, player.yHeadRotO, player.yHeadRot)) * Mth.DEG_TO_RAD;
			float pitch = (Mth.lerp(tickProgress, player.xRotO, player.getXRot())) * Mth.DEG_TO_RAD;

			yaw -= 0.2F;

			yaw += handAngle * 0.5F;
			pitch += handAngle * 0.7F;

			Vec3 origin = player.getPosition(tickProgress).add(0.35, player.getBoundingBox().getYsize() - 0.5, 0);

			Vec3 vec = origin.add(
					new Vec3(0, 0, 1)
						.xRot(-pitch)
						.yRot(-yaw)
				);
			Gizmos.arrow(origin, vec, 0xFFFFFFFF, 5);
			return vec;
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
			state.handPos = this.getHandPos(player, Mth.sin(Mth.sqrt(player.getAttackAnim(partialTick)) * Mth.PI), partialTick);
		}

		BlockPos start = BlockPos.containing(state.handPos);
		state.startBlockLight = getBlockLightLevel(harpoon, start);
		state.startSkyLight = getSkyLightLevel(harpoon, start);

		BlockPos end = BlockPos.containing(harpoon.getLightProbePosition(partialTick));
		state.endBlockLight = getBlockLightLevel(harpoon, end);
		state.endSkyLight = getSkyLightLevel(harpoon, end);

		state.yRot = harpoon.getYRot(partialTick);
		state.xRot = harpoon.getXRot(partialTick);
		state.isFoil = harpoon.isEnchanted();
	}
}
