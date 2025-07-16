package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.feature.ManOWarTentaclesFeatureRenderer;
import dev.spiritstudios.abysm.client.render.entity.model.GarbageBagModel;
import dev.spiritstudios.abysm.client.render.entity.state.ManOWarRenderState;
import dev.spiritstudios.abysm.entity.floralreef.ManOWarEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class ManOWarEntityRenderer extends MobEntityRenderer<ManOWarEntity, ManOWarRenderState, GarbageBagModel> {

	protected static final Identifier TEXTURE = Abysm.id("textures/entity/man_o_war.png");

	public ManOWarEntityRenderer(EntityRendererFactory.Context ctx) {
		this(ctx, new GarbageBagModel(ctx.getPart(AbysmEntityLayers.MAN_O_WAR)), 0.4f);
	}

	protected ManOWarEntityRenderer(EntityRendererFactory.Context ctx, GarbageBagModel model, float shadowRadius) {
		super(ctx, model, shadowRadius);

		this.addFeature(new ManOWarTentaclesFeatureRenderer(this));
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
	public void updateRenderState(ManOWarEntity manOWar, ManOWarRenderState state, float tickProgress) {
		super.updateRenderState(manOWar, state, tickProgress);
		state.velocity = manOWar.getPrevVelocity().lerp(manOWar.getVelocity(), tickProgress);
		state.tentacleData = manOWar.tentacleData;
		state.tentacleBox = manOWar.getTentacleBox();
		state.centerBoxPos = manOWar.getBoundingBox().getHorizontalCenter();
	}

	@Override
	public boolean shouldRender(ManOWarEntity entity, Frustum frustum, double x, double y, double z) {
		return super.shouldRender(entity, frustum, x, y, z) || frustum.isVisible(entity.getTentacleBox());
	}

	public static void renderTentacleBox(MatrixStack matrices, VertexConsumer lines, ManOWarRenderState state) {
		matrices.push();
		{
			Vec3d center = state.centerBoxPos;
			matrices.translate(-center.x, -center.y, -center.z);
			VertexRendering.drawBox(matrices, lines, state.tentacleBox, 0, 1, 0, 1);
		}
		matrices.pop();
	}
}
