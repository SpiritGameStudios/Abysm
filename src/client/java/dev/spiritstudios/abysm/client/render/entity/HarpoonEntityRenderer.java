package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.state.HarpoonEntityRenderState;
import dev.spiritstudios.abysm.entity.HarpoonEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.render.entity.GuardianEntityRenderer;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class HarpoonEntityRenderer extends ProjectileEntityRenderer<HarpoonEntity, HarpoonEntityRenderState> {

	protected static final Identifier TEXTURE = Abysm.id("textures/entity/harpoon.png");

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
		/*if (state.player != null) {
			final float g = MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false);
			matrixStack.push();
			Vec3d vec3d = FishingBobberEntityRenderer.getPlayerHandPos(playerEntity, g, Items.LASHING_POTATO, this.dispatcher);
			Vec3d vec3d2 = new Vec3d(
				MathHelper.lerp((double)g, state.prevX, state.x),
				MathHelper.lerp((double)g, state.prevY, state.y) + state.standingEyeHeight,
				MathHelper.lerp((double)g, state.prevZ, state.z)
			);
			float h = state.age + g;
			float j = h * 0.15F % 1.0F;
			Vec3d vec3d3 = vec3d.subtract(vec3d2);
			float k = (float)(vec3d3.length() + 0.1);
			vec3d3 = vec3d3.normalize();
			float l = (float)Math.acos(vec3d3.y);
			float m = (float)Math.atan2(vec3d3.z, vec3d3.x);
			matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((float) (Math.PI / 2) - m) * (180.0F / (float)Math.PI)));
			matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(l * (180.0F / (float)Math.PI)));
			float n = h * 0.05F * -1.5F;
			float o = 0.2F;
			float p = MathHelper.cos(n + (float) Math.PI) * 0.2F;
			float q = MathHelper.sin(n + (float) Math.PI) * 0.2F;
			float r = MathHelper.cos(n + 0.0F) * 0.2F;
			float s = MathHelper.sin(n + 0.0F) * 0.2F;
			float t = MathHelper.cos(n + (float) (Math.PI / 2)) * 0.2F;
			float u = MathHelper.sin(n + (float) (Math.PI / 2)) * 0.2F;
			float v = MathHelper.cos(n + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
			float w = MathHelper.sin(n + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
			float y = 0.0F;
			float z = 0.4999F;
			float aa = -1.0F + j;
			float ab = k * 2.5F + aa;
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(GuardianEntityRenderer.));
			MatrixStack.Entry entry = matrixStack.peek();
			vertex(vertexConsumer, entry, p, k, q, 0.4999F, ab);
			vertex(vertexConsumer, entry, p, 0.0F, q, 0.4999F, aa);
			vertex(vertexConsumer, entry, r, 0.0F, s, 0.0F, aa);
			vertex(vertexConsumer, entry, r, k, s, 0.0F, ab);
			vertex(vertexConsumer, entry, t, k, u, 0.4999F, ab);
			vertex(vertexConsumer, entry, t, 0.0F, u, 0.4999F, aa);
			vertex(vertexConsumer, entry, v, 0.0F, w, 0.0F, aa);
			vertex(vertexConsumer, entry, v, k, w, 0.0F, ab);
			matrixStack.pop();
		}
		 */
		super.render(state, matrixStack, vertexConsumerProvider, i);
	}

	protected static void vertex(VertexConsumer vertexConsumer, MatrixStack.Entry matrix, float x, float y, float z, float u, float v) {
		vertexConsumer.vertex(matrix, x, y, z)
			.color(128, 255, 128, 255)
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
	public void updateRenderState(HarpoonEntity harpoon, HarpoonEntityRenderState state, float f) {
		super.updateRenderState(harpoon, state, f);
		state.player = harpoon.getPlayer();
		state.prevX = harpoon.lastX;
		state.prevY = harpoon.lastY;
		state.prevZ = harpoon.lastZ;
	}
}
