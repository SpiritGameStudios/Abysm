package dev.spiritstudios.abysm.client.render.entity.harpoon;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.state.HarpoonEntityRenderState;
import dev.spiritstudios.abysm.entity.harpoon.HarpoonEntity;
import dev.spiritstudios.abysm.item.HarpoonItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class HarpoonEntityRenderer extends ProjectileEntityRenderer<HarpoonEntity, HarpoonEntityRenderState> {

	protected static final Identifier TEXTURE = Abysm.id("textures/entity/harpoon.png");
	public static final Identifier CHAINS = Abysm.id("textures/entity/chains.png");

	private static final RenderLayer RENDER_LAYER = RenderLayer.getEntityCutoutNoCull(CHAINS);

	public HarpoonEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	protected Identifier getTexture(HarpoonEntityRenderState state) {
		return TEXTURE;
	}

	@Override
	public void render(HarpoonEntityRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		// lashing potato go (okay maybe it was too much mojank, thanks Echo)

		if (state.player == null) {
			super.render(state, matrixStack, vertexConsumerProvider, light);
			return;
		}

		Vec3d start = state.handPos;
		Vec3d end = new Vec3d(state.x, state.y, state.z);

		Vec3d difference = start.subtract(end);
		double distance = start.distanceTo(end);

		float horizontalAngle = (float) MathHelper.atan2(end.z - start.z, end.x - start.x);
		horizontalAngle += MathHelper.ceil(-horizontalAngle / MathHelper.TAU) * MathHelper.TAU;

		float verticalAngle = (float) Math.asin(difference.y / distance);

		float length = (float) (difference.length() - 1);
		matrixStack.push();
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(-horizontalAngle - MathHelper.HALF_PI));
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(-verticalAngle));

		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45));
		VertexConsumer consumer = vertexConsumerProvider.getBuffer(RENDER_LAYER);

		matrixStack.push();
		renderPart(light, consumer, matrixStack.peek(), length, false);
		matrixStack.pop();

		matrixStack.translate(0.1875, 0.1875, 0);
		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));

		renderPart(light, consumer, matrixStack.peek(), length, true);
		matrixStack.pop();

		super.render(state, matrixStack, vertexConsumerProvider, light);
	}

	private static void renderPart(int light, VertexConsumer consumer, MatrixStack.Entry entry, float length, boolean shift) {
		float minU = shift ? 0.5F : 0.0F;
		float maxU = shift ? 1.0F : 0.5F;
		float v = length / 10.0F;

		consumer.vertex(entry, 0, 0.25F, 0)
			.color(255, 255, 255, 255)
			.texture(minU, 0)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(light)
			.normal(entry, 0, -1, 0);

		consumer.vertex(entry, 0, 0.25F, length)
			.color(255, 255, 255, 255)
			.texture(minU, v)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(light)
			.normal(entry, 0, -1, 0);

		consumer.vertex(entry, 0, 0.125F, length)
			.color(255, 255, 255, 255)
			.texture(maxU, v)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(light)
			.normal(entry, 0, -1, 0);

		consumer.vertex(entry, 0, 0.125F, 0)
			.color(255, 255, 255, 255)
			.texture(maxU, 0)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(light)
			.normal(entry, 0, -1, 0);
	}

	public static Arm getArmHoldingRod(PlayerEntity player) {
		return player.getMainHandStack().getItem() instanceof HarpoonItem ? player.getMainArm() : player.getMainArm().getOpposite();
	}

	private Vec3d getHandPos(PlayerEntity player, float angle, float tickProgress) {
		int arm = getArmHoldingRod(player) == Arm.RIGHT ? 1 : -1;
		if (this.dispatcher.gameOptions.getPerspective().isFirstPerson() && player == MinecraftClient.getInstance().player) {
			float m = 960.0F / this.dispatcher.gameOptions.getFov().getValue();

			Vec3d vec3d = this.dispatcher.camera.getProjection().getPosition((float) arm * 0.525F, -0.1F)
				.multiply(m)
				.rotateY(angle * 0.5F)
				.rotateX(-angle * 0.7F);

			return player.getCameraPosVec(tickProgress).add(vec3d).subtract(0, 0.3, 0);
		} else {
			float yaw = MathHelper.lerp(tickProgress, player.lastBodyYaw, player.bodyYaw) * MathHelper.RADIANS_PER_DEGREE;

			double sinYaw = MathHelper.sin(yaw);
			double cosYaw = MathHelper.cos(yaw);

			float scale = player.getScale();
			float horizontalScale = arm * 0.35F * scale;
			float yOffset = player.isInSneakingPose() ? -0.1875F : 0.0F;

			return player.getCameraPosVec(tickProgress)
				.add(-cosYaw * horizontalScale, yOffset - 0.45F * scale, -sinYaw * horizontalScale);
		}
	}

	public static void vertex(VertexConsumer vertexConsumer, MatrixStack.Entry matrix, float x, float y, float z, float u, float v, int light) {
		vertexConsumer.vertex(matrix, x, y, z)
			.color(255, 255, 255, 255)
			.texture(u, v)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(light)
			.normal(0.0F, 1.0F, 0.0F);
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
	public void updateRenderState(HarpoonEntity harpoon, HarpoonEntityRenderState state, float f) {
		super.updateRenderState(harpoon, state, f);
		state.player = harpoon.getPlayer();

		if (state.player == null) {
			state.handPos = Vec3d.ZERO;
		} else {
			float swingProgress = state.player.getHandSwingProgress(f);
			float angle = MathHelper.sin(MathHelper.sqrt(swingProgress) * MathHelper.PI);
			state.handPos = this.getHandPos(state.player, angle, f);
		}

		state.prevX = harpoon.lastX;
		state.prevY = harpoon.lastY;
		state.prevZ = harpoon.lastZ;
	}
}
