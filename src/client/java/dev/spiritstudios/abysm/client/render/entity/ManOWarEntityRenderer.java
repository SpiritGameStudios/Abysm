package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.AbysmClient;
import dev.spiritstudios.abysm.client.render.entity.state.ManOWarRenderState;
import dev.spiritstudios.abysm.entity.ManOWar;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ManOWarEntityRenderer extends MobEntityRenderer<ManOWar, ManOWarRenderState, GarbageBagModel> {

	protected static final Identifier TEXTURE = Abysm.id("textures/entity/man_o_war.png");

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
	}

	@Override
	public void render(ManOWarRenderState livingEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		super.render(livingEntityRenderState, matrixStack, vertexConsumerProvider, i);
	}
}
