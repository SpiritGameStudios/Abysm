package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.AbysmClient;
import dev.spiritstudios.abysm.client.render.entity.state.ManOWarRenderState;
import dev.spiritstudios.abysm.entity.floralreef.ManOWar;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.Color;

public class ManOWarEntityRenderer extends MobEntityRenderer<ManOWar, ManOWarRenderState, GarbageBagModel> {

	protected static final Identifier TEXTURE = Abysm.id("textures/entity/man_o_war.png");
	public static final int TENTACLE_ARGB = ColorHelper.fullAlpha(new Color(5, 41, 66).getRGB());

	public ManOWarEntityRenderer(EntityRendererFactory.Context ctx) {
		this(ctx, new GarbageBagModel(ctx.getPart(AbysmClient.MAN_O_WAR_LAYER)), 0.4f);
	}

	protected ManOWarEntityRenderer(EntityRendererFactory.Context ctx, GarbageBagModel model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}

	@Override
	public Identifier getTexture(ManOWarRenderState state) {
		return TEXTURE;
	}

	@Override
	public ManOWarRenderState createRenderState() {
		return new ManOWarRenderState();
	}

	@Override
	public void updateRenderState(ManOWar manOWar, ManOWarRenderState state, float tickProgress) {
		super.updateRenderState(manOWar, state, tickProgress);
		state.velocity = manOWar.getPrevVelocity().lerp(manOWar.getVelocity(), tickProgress);
		state.tentacleData = manOWar.tentacleData;
	}

	@Override
	public void render(ManOWarRenderState livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		super.render(livingEntityRenderState, matrixStack, vertexConsumerProvider, i);
	}

	@SuppressWarnings("unused")
	public void renderSpecial(ManOWarRenderState state, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
		if (state.tentacleData == null || state.tentacleData.isEmpty()) {
			return;
		}
		matrixStack.push();
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.LINES);
		Vec3d line = new Vec3d(0, ManOWar.BASE_TENTACLE_LENGTH, MathHelper.clamp(state.velocity.horizontalLengthSquared() * 500, 1.0E-7, 1.2) * 0.5);
		long time = Util.getMeasuringTimeMs();
		state.tentacleData.forEach(tentacle -> {
			matrixStack.push();
			matrixStack.translate(0, 1.5, 0);
			VertexRendering.drawVector(matrixStack,
				vertexConsumer,
				tentacle.relativePosition().toVector3f(),
				line.add(0, 0,
					(MathHelper.sin((time + tentacle.swayOffset()) * ManOWar.INVERSE_MAX_SWAY_OFFSET) + 1) * 0.2),
				TENTACLE_ARGB);
			matrixStack.pop();
		});
		matrixStack.pop();
	}
}
