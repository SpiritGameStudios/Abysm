package dev.spiritstudios.abysm.client.render.entity;

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
	public static final Identifier CHAINS = Identifier.ofVanilla("textures/item/chain.png");

	public HarpoonEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	protected Identifier getTexture(HarpoonEntityRenderState state) {
		return TEXTURE;
	}

	@Override
	public void render(HarpoonEntityRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		// lashing potato go
		/*
		if (!MinecraftClient.getInstance().world.getTickManager().isFrozen()) {
			MinecraftClient.getInstance().world.addParticleClient(ParticleTypes.END_ROD, vec3d2.x, vec3d2.y, vec3d2.z, 0, 0, 0);
		}

		 */
		if (state.player != null) {
			matrixStack.push();
			final float tickProgress = MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false);
			Vec3d pleaseGoToTheCorrectEnd = Vec3d.fromPolar(state.pitch, 180 - state.yaw);
			matrixStack.translate(pleaseGoToTheCorrectEnd.x, pleaseGoToTheCorrectEnd.y, pleaseGoToTheCorrectEnd.z);
			Vec3d vec3d2 = new Vec3d(
				MathHelper.lerp(tickProgress, state.prevX, state.x),
				MathHelper.lerp(tickProgress, state.prevY, state.y) + state.standingEyeHeight,
				MathHelper.lerp(tickProgress, state.prevZ, state.z)
			).add(pleaseGoToTheCorrectEnd);
			float h = state.age + tickProgress;
			float j = h * 0.15F % 1.0F;
			Vec3d vec3d3 = state.handPos.subtract(vec3d2);
			float k = (float)(vec3d3.length() + 0.1 + 0.5);
			vec3d3 = vec3d3.normalize();
			float l = (float)Math.acos(vec3d3.y);
			float m = (float)Math.atan2(vec3d3.z, vec3d3.x);
			matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((float) (Math.PI / 2) - m) * (180.0F / (float)Math.PI)));
			matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(l * (180.0F / (float)Math.PI)));
			float n = h * 0.05F * -1.5F;
			float o = 0.2F;
			float p = MathHelper.cos(n + (float) Math.PI) * o;
			float q = MathHelper.sin(n + (float) Math.PI) * o;
			float r = MathHelper.cos(n + 0.0F) * o;
			float s = MathHelper.sin(n + 0.0F) * o;
			float t = MathHelper.cos(n + (float) (Math.PI / 2)) * o;
			float u = MathHelper.sin(n + (float) (Math.PI / 2)) * o;
			float v = MathHelper.cos(n + (float) (Math.PI * 3.0 / 2.0)) * o;
			float w = MathHelper.sin(n + (float) (Math.PI * 3.0 / 2.0)) * o;
			float y = 0.0F;
			float z = 0.4999F;
			float aa = -1.0F + j;
			float ab = k * 2.5F + aa;
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(CHAINS));
			MatrixStack.Entry entry = matrixStack.peek();
			vertex(vertexConsumer, entry, p, k, q, z, ab);
			vertex(vertexConsumer, entry, p, y, q, z, aa);
			vertex(vertexConsumer, entry, r, y, s, y, aa);
			vertex(vertexConsumer, entry, r, k, s, y, ab);
			vertex(vertexConsumer, entry, t, k, u, z, ab);
			vertex(vertexConsumer, entry, t, y, u, z, aa);
			vertex(vertexConsumer, entry, v, y, w, y, aa);
			vertex(vertexConsumer, entry, v, k, w, y, ab);
			matrixStack.pop();
		}
		super.render(state, matrixStack, vertexConsumerProvider, i);
	}

	public static Arm getArmHoldingRod(PlayerEntity player) {
		return player.getMainHandStack().getItem() instanceof HarpoonItem ? player.getMainArm() : player.getMainArm().getOpposite();
	}

	private Vec3d getHandPos(PlayerEntity player, float f, float tickProgress) {
		int i = getArmHoldingRod(player) == Arm.RIGHT ? 1 : -1;
		if (this.dispatcher.gameOptions.getPerspective().isFirstPerson() && player == MinecraftClient.getInstance().player) {
			double m = 960.0d / (double) this.dispatcher.gameOptions.getFov().getValue();
			Vec3d vec3d = this.dispatcher.camera.getProjection().getPosition((float)i * 0.525F, -0.1F).multiply(m).rotateY(f * 0.5F).rotateX(-f * 0.7F);
			return player.getCameraPosVec(tickProgress).add(vec3d);
		} else {
			float g = MathHelper.lerp(tickProgress, player.lastBodyYaw, player.bodyYaw) * ((float)Math.PI / 180F);
			double d = MathHelper.sin(g);
			double e = MathHelper.cos(g);
			float h = player.getScale();
			double j = (double)i * 0.35 * (double)h;
			double k = 0.8 * (double)h;
			float l = player.isInSneakingPose() ? -0.1875F : 0.0F;
			return player.getCameraPosVec(tickProgress).add(-e * j - d * k, (double)l - 0.45 * (double)h, -d * j + e * k);
		}
	}

	public static void vertex(VertexConsumer vertexConsumer, MatrixStack.Entry matrix, float x, float y, float z, float u, float v) {
		vertexConsumer.vertex(matrix, x, y, z)
			.color(255, 255, 255, 255)
			.texture(u, v)
			.overlay(OverlayTexture.DEFAULT_UV)
			.light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
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
			float g = state.player.getHandSwingProgress(f);
			float h = MathHelper.sin(MathHelper.sqrt(g) * (float)Math.PI);
			state.handPos = this.getHandPos(state.player, h, f);
		}
		state.prevX = harpoon.lastX;
		state.prevY = harpoon.lastY;
		state.prevZ = harpoon.lastZ;
	}
}
