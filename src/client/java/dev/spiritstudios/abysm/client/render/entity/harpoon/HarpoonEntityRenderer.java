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

		matrixStack.push();
		final float tickProgress = MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false);

		Vec3d anchorAtEnd = Vec3d.fromPolar(state.pitch, 180 - state.yaw);
		matrixStack.translate(anchorAtEnd.x, anchorAtEnd.y, anchorAtEnd.z);

		Vec3d vec3d2 = new Vec3d(
			MathHelper.lerp(tickProgress, state.prevX, state.x),
			MathHelper.lerp(tickProgress, state.prevY, state.y) + state.standingEyeHeight,
			MathHelper.lerp(tickProgress, state.prevZ, state.z)
		).add(anchorAtEnd);

		float age = state.age + tickProgress;
		float j = age * 0.15F % 1.0F;
		Vec3d vec3d3 = state.handPos.subtract(vec3d2);
		float k = (float) (vec3d3.length() + 0.1);
		vec3d3 = vec3d3.normalize();


		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(MathHelper.PI / 2.0F - (float) MathHelper.atan2(vec3d3.z, vec3d3.x)));
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotation((float) Math.acos(vec3d3.y)));

		float theta = age * 0.05F * -1.5F;

		float multiplier = 0.2F;

		float cosTheta = MathHelper.cos(theta);
		float sinTheta = MathHelper.sin(theta);

		// cos(θ + π) = -cos(θ)
		// sin(θ + π) = -sin(θ)
		// cos(θ + π/2) = -sin(θ)
		// sin(θ + π/2) = cos(θ)
		// this was originally using 8 sin/cos calls because mojank:tm:

		float p = -cosTheta * multiplier;
		float q = -sinTheta * multiplier;

		float r = cosTheta * multiplier;
		float s = sinTheta * multiplier;

		float t = -sinTheta * multiplier;
		float u = cosTheta * multiplier;

		float v = sinTheta * multiplier;
		float w = -cosTheta * multiplier;

		float y = 0.0F;
		float z = 0.4999F;
		float aa = -1.0F + j;
		float ab = k * 2.5F + aa;

		VertexConsumer buffer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(CHAINS));
		MatrixStack.Entry entry = matrixStack.peek();

		vertex(buffer, entry, p, k, q, z, ab, light);
		vertex(buffer, entry, p, y, q, z, aa, light);
		vertex(buffer, entry, r, y, s, y, aa, light);
		vertex(buffer, entry, r, k, s, y, ab, light);
		vertex(buffer, entry, t, k, u, z, ab, light);
		vertex(buffer, entry, t, y, u, z, aa, light);
		vertex(buffer, entry, v, y, w, y, aa, light);
		vertex(buffer, entry, v, k, w, y, ab, light);

		matrixStack.pop();

		super.render(state, matrixStack, vertexConsumerProvider, light);
	}

	public static Arm getArmHoldingRod(PlayerEntity player) {
		return player.getMainHandStack().getItem() instanceof HarpoonItem ? player.getMainArm() : player.getMainArm().getOpposite();
	}

	private Vec3d getHandPos(PlayerEntity player, float angle, float tickProgress) {
		int arm = getArmHoldingRod(player) == Arm.RIGHT ? 1 : -1;
		if (this.dispatcher.gameOptions.getPerspective().isFirstPerson() && player == MinecraftClient.getInstance().player) {
			double m = 960.0d / (double) this.dispatcher.gameOptions.getFov().getValue();
			Vec3d vec3d = this.dispatcher.camera.getProjection().getPosition((float) arm * 0.525F, -0.1F).multiply(m).rotateY(angle * 0.5F).rotateX(-angle * 0.7F);
			return player.getCameraPosVec(tickProgress).add(vec3d).subtract(0, 0.3, 0);
		} else {
			float yaw = MathHelper.lerp(tickProgress, player.lastBodyYaw, player.bodyYaw) * MathHelper.DEGREES_PER_RADIAN;
			double d = MathHelper.sin(yaw);
			double e = MathHelper.cos(yaw);

			float scale = player.getScale();
			double j = (double) arm * 0.35 * (double) scale;
			double k = 0;
			float offset = player.isInSneakingPose() ? -0.1875F : 0.0F;
			return player.getCameraPosVec(tickProgress).add(-e * j - d * k, (double) offset - 0.45 * (double) scale, -d * j + e * k);
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
