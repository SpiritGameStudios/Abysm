package dev.spiritstudios.abysm.client.render.entity.renderer;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.state.HarpoonEntityRenderState;
import dev.spiritstudios.abysm.entity.harpoon.HarpoonEntity;
import dev.spiritstudios.abysm.item.HarpoonItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.ArrowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class HarpoonEntityRenderer extends EntityRenderer<HarpoonEntity, HarpoonEntityRenderState> {
	protected static final Identifier TEXTURE = Abysm.id("textures/item/harpoon_arrow.png");
	public static final Identifier CHAINS = Abysm.id("textures/entity/chains.png");

	private static final RenderLayer RENDER_LAYER = RenderLayer.getEntityCutoutNoCull(CHAINS);

	private final ArrowEntityModel model;

	public HarpoonEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.model = new ArrowEntityModel(context.getPart(EntityModelLayers.ARROW));
	}

	@Override
	public void render(HarpoonEntityRenderState state, MatrixStack matrices, VertexConsumerProvider consumers, int light) {
		if (state.handPos == Vec3d.ZERO) {
			super.render(state, matrices, consumers, light);
			return;
		}

		Vec3d start = state.handPos;

		Vec3d anchorOffset = Vec3d.fromPolar(state.pitch, 180 - state.yaw).multiply(0.7).subtract(0, 0.1, 0);

		Vec3d end = new Vec3d(state.x, state.y, state.z).add(anchorOffset);

		Vec3d difference = start.subtract(end);

		double distZ = end.z - start.z;
		double distX = end.x - start.x;
		float horizontalAngle = (float) MathHelper.atan2(distZ, distX);
		horizontalAngle += MathHelper.ceil(-horizontalAngle / MathHelper.TAU) * MathHelper.TAU;

		float verticalAngle = (float) MathHelper.atan2(difference.y, Math.sqrt(distZ * distZ + distX * distX));

		float length = (float) (start.distanceTo(end) + 0.1);

		matrices.push();
		matrices.translate(anchorOffset.x, anchorOffset.y, anchorOffset.z);
		{
			matrices.multiply(RotationAxis.POSITIVE_Y.rotation(-horizontalAngle - MathHelper.HALF_PI));
			matrices.multiply(RotationAxis.POSITIVE_X.rotation(-verticalAngle));

			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45));
			VertexConsumer consumer = ItemRenderer.getItemGlintConsumer(consumers, RENDER_LAYER, false, state.enchanted);

			renderPart(state.startLight, state.endLight, consumer, matrices.peek(), length, false);

			matrices.translate(0.1875, 0.1875, 0);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));

			renderPart(state.startLight, state.endLight, consumer, matrices.peek(), length, true);
		}
		matrices.pop();

		matrices.push();
		{
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.yaw - 90.0F));
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(state.pitch));
			VertexConsumer consumer = ItemRenderer.getItemGlintConsumer(
				consumers, this.model.getLayer(TEXTURE), false, state.enchanted
			);
			this.model.setAngles(state);
			this.model.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
		}
		matrices.pop();
	}

	private static void renderPart(int startLight, int endLight, VertexConsumer consumer, MatrixStack.Entry entry, float length, boolean shift) {
		float minU = shift ? 0.5F : 0.0F;
		float maxU = shift ? 1.0F : 0.5F;
		float minV = 0.0F;
		float maxV = length / 10.0F;

		consumer.vertex(entry, 0, 0.25F, 0)
			.color(255, 255, 255, 255)
			.texture(minU, minV)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(endLight)
			.normal(entry, 0, -1, 0);

		consumer.vertex(entry, 0, 0.25F, length)
			.color(255, 255, 255, 255)
			.texture(minU, maxV)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(startLight)
			.normal(entry, 0, -1, 0);

		consumer.vertex(entry, 0, 0.125F, length)
			.color(255, 255, 255, 255)
			.texture(maxU, maxV)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(startLight)
			.normal(entry, 0, -1, 0);

		consumer.vertex(entry, 0, 0.125F, 0)
			.color(255, 255, 255, 255)
			.texture(maxU, minV)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(endLight)
			.normal(entry, 0, -1, 0);
	}

	public static Arm getArmHoldingRod(PlayerEntity player) {
		return player.getMainHandStack().getItem() instanceof HarpoonItem ? player.getMainArm() : player.getMainArm().getOpposite();
	}

	private Vec3d getHandPos(PlayerEntity player, float angle, float tickProgress) {
		int arm = getArmHoldingRod(player) == Arm.RIGHT ? 1 : -1;
		if (this.dispatcher.gameOptions.getPerspective().isFirstPerson() &&
			player == MinecraftClient.getInstance().player) {
			return player.getCameraPosVec(tickProgress)
				.add(this.dispatcher.camera.getProjection().getPosition(arm * 0.525F, -1F)
					.multiply(960.0F / this.dispatcher.gameOptions.getFov().getValue())
					.rotateY(angle * 0.5F)
					.rotateX(-angle * 0.7F));
		} else {
			float yaw = (MathHelper.lerp(tickProgress, player.lastBodyYaw, player.bodyYaw) + 10) * MathHelper.RADIANS_PER_DEGREE;

			double sinYaw = MathHelper.sin(yaw);
			double cosYaw = MathHelper.cos(yaw);

			float scale = player.getScale();
			float sideOffset = arm * 0.35F * scale;
			double forwardOffset = 0.3 * scale;
			float verticalOffset = player.isInSneakingPose() ? -0.1875F : 0.0F;

			return player.getCameraPosVec(tickProgress)
				.add(-cosYaw * sideOffset - sinYaw * forwardOffset, verticalOffset - 0.45F * scale, -sinYaw * sideOffset + cosYaw * forwardOffset);
		}
	}

	@Override
	public HarpoonEntityRenderState createRenderState() {
		return new HarpoonEntityRenderState();
	}

	@Override
	public boolean shouldRender(HarpoonEntity entity, Frustum frustum, double x, double y, double z) {
		return true;
	}

	@Override
	public void updateRenderState(HarpoonEntity harpoon, HarpoonEntityRenderState state, float tickProgress) {
		super.updateRenderState(harpoon, state, tickProgress);
		PlayerEntity player = harpoon.getPlayer();

		if (player == null || harpoon.isRemoved()) {
			state.handPos = Vec3d.ZERO;
		} else {
			state.handPos = this.getHandPos(
				player,
				MathHelper.sin(MathHelper.sqrt(player.getHandSwingProgress(tickProgress)) * MathHelper.PI),
				tickProgress
			);
		}

		BlockPos start = BlockPos.ofFloored(state.handPos);

		state.startLight = LightmapTextureManager.pack(getBlockLight(harpoon, start), getSkyLight(harpoon, start));
		state.endLight = getLight(harpoon, tickProgress);

		state.yaw = harpoon.getLerpedYaw(tickProgress);
		state.pitch = harpoon.getLerpedPitch(tickProgress);
		state.enchanted = harpoon.isEnchanted();
	}
}
