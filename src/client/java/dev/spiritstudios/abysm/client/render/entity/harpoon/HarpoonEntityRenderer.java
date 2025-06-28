package dev.spiritstudios.abysm.client.render.entity.harpoon;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.state.HarpoonEntityRenderState;
import dev.spiritstudios.abysm.component.BlessedComponent;
import dev.spiritstudios.abysm.entity.harpoon.HarpoonEntity;
import dev.spiritstudios.abysm.item.HarpoonItem;
import dev.spiritstudios.abysm.registry.AbysmDataComponentTypes;
import dev.spiritstudios.abysm.registry.AbysmEntityTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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
	public void render(HarpoonEntityRenderState state, MatrixStack matrices, VertexConsumerProvider consumers, int light) {
		if (state.handPos == Vec3d.ZERO) {
			super.render(state, matrices, consumers, light);
			return;
		}

		Vec3d start = state.handPos;

		Vec3d anchorOffset = Vec3d.fromPolar(state.pitch, 180 - state.yaw).multiply(0.7).subtract(0, 0.1, 0);

		Vec3d end = new Vec3d(state.x, state.y, state.z).add(anchorOffset);

		Vec3d difference = start.subtract(end);
		double distance = start.distanceTo(end) + 0.1;

		float horizontalAngle = (float) MathHelper.atan2(end.z - start.z, end.x - start.x);
		horizontalAngle += MathHelper.ceil(-horizontalAngle / MathHelper.TAU) * MathHelper.TAU;

		float verticalAngle = (float) Math.asin(difference.y / distance);

		float length = (float) (distance);

		matrices.push();
		matrices.translate(anchorOffset.x, anchorOffset.y, anchorOffset.z);
		{
			matrices.multiply(RotationAxis.POSITIVE_Y.rotation(-horizontalAngle - MathHelper.HALF_PI));
			matrices.multiply(RotationAxis.POSITIVE_X.rotation(-verticalAngle));

			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45));
			VertexConsumer consumer = consumers.getBuffer(RENDER_LAYER);

			renderPart(state.startLight, state.endLight, consumer, matrices.peek(), length, false);

			matrices.translate(0.1875, 0.1875, 0);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));

			renderPart(state.startLight, state.endLight, consumer, matrices.peek(), length, true);
		}
		matrices.pop();

		super.render(state, matrices, consumers, light);
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
				.add(this.dispatcher.camera.getProjection().getPosition(arm * 0.525F, -0.1F)
					.multiply(960.0F / this.dispatcher.gameOptions.getFov().getValue())
					.rotateY(angle * 0.5F)
					.rotateX(-angle * 0.7F)).subtract(0, 0.6, 0);
		} else {
			float yaw = (MathHelper.lerp(tickProgress, player.lastBodyYaw, player.bodyYaw) + 10) * MathHelper.RADIANS_PER_DEGREE;

			double sinYaw = MathHelper.sin(yaw);
			double cosYaw = MathHelper.cos(yaw);

			float scale = player.getScale();
			float horizontalScale = arm * 0.5F * scale;
			float yOffset = player.isInSneakingPose() ? -0.1875F : 0.0F;

			return player.getCameraPosVec(tickProgress)
				.add(-cosYaw * horizontalScale, yOffset - 0.75F * scale, -sinYaw * horizontalScale);
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

		state.handPos = player == null ? Vec3d.ZERO : this.getHandPos(
			player,
			MathHelper.sin(MathHelper.sqrt(player.getHandSwingProgress(tickProgress)) * MathHelper.PI),
			tickProgress
		);

		BlockPos start = BlockPos.ofFloored(state.handPos);
		state.startLight = LightmapTextureManager.pack(getBlockLight(harpoon, start), getSkyLight(harpoon, start));

		BlockPos end = BlockPos.ofFloored(state.x, state.y, state.z);
		state.endLight = LightmapTextureManager.pack(getBlockLight(harpoon, end), getSkyLight(harpoon, end));
	}

	public static void renderInStack(MinecraftClient client, ClientWorld clientWorld, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, boolean thirdPerson) {
		HarpoonEntity harpoon = AbysmEntityTypes.FLYING_HARPOON.create(clientWorld, SpawnReason.COMMAND);
		if (harpoon == null) {
			return;
		}
		matrices.push();
		if (thirdPerson) {
			matrices.translate(0, 3.75 * 0.0625, 7.5 * 0.0625);
		}
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
		matrices.translate(0, -0.05, 1.7);
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45));
		EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
		dispatcher.setRenderShadows(false);
		boolean hitbox = dispatcher.shouldRenderHitboxes();
		dispatcher.setRenderHitboxes(false);
		dispatcher.render(harpoon, 0, 0, 0, 1.0F, matrices, vertexConsumers, light);
		dispatcher.setRenderShadows(true);
		if (hitbox) {
			dispatcher.setRenderHitboxes(true);
		}
		matrices.pop();
	}
}
