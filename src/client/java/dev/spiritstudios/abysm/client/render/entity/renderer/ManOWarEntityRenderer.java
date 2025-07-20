package dev.spiritstudios.abysm.client.render.entity.renderer;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.AbysmEntityLayers;
import dev.spiritstudios.abysm.client.render.entity.renderer.feature.ManOWarTentaclesFeatureRenderer;
import dev.spiritstudios.abysm.client.render.entity.model.GarbageBagModel;
import dev.spiritstudios.abysm.client.render.entity.state.ManOWarRenderState;
import dev.spiritstudios.abysm.entity.floralreef.ManOWarEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.EntityHitbox;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

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

	@Override
	protected void appendHitboxes(ManOWarEntity entity, ImmutableList.Builder<EntityHitbox> builder, float f) {
		super.appendHitboxes(entity, builder, f);

		Box tentacleBox = entity.getTentacleBox();

		builder.add(new EntityHitbox(
			tentacleBox.minX - entity.getX(), tentacleBox.minY - entity.getY(), tentacleBox.minZ - entity.getZ(),
			tentacleBox.maxX - entity.getX(), tentacleBox.maxY - entity.getY(), tentacleBox.maxZ - entity.getZ(),
			0, 1, 0
		));
	}
}
