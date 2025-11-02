package dev.spiritstudios.abysm.client.render.entity.renderer;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.AbysmEntityLayers;
import dev.spiritstudios.abysm.client.render.entity.renderer.feature.ManOWarTentaclesFeatureRenderer;
import dev.spiritstudios.abysm.client.render.entity.model.GarbageBagModel;
import dev.spiritstudios.abysm.client.render.entity.state.ManOWarRenderState;
import dev.spiritstudios.abysm.entity.floralreef.ManOWarEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.HitboxRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;

public class ManOWarEntityRenderer extends MobRenderer<ManOWarEntity, ManOWarRenderState, GarbageBagModel> {

	protected static final ResourceLocation TEXTURE = Abysm.id("textures/entity/man_o_war.png");

	public ManOWarEntityRenderer(EntityRendererProvider.Context ctx) {
		this(ctx, new GarbageBagModel(ctx.bakeLayer(AbysmEntityLayers.MAN_O_WAR)), 0.4f);
	}

	protected ManOWarEntityRenderer(EntityRendererProvider.Context ctx, GarbageBagModel model, float shadowRadius) {
		super(ctx, model, shadowRadius);

		this.addLayer(new ManOWarTentaclesFeatureRenderer(this));
	}


	@Override
	public ResourceLocation getTextureLocation(ManOWarRenderState state) {
		return TEXTURE;
	}

	@Override
	public ManOWarRenderState createRenderState() {
		return new ManOWarRenderState();
	}

	@Override
	public void extractRenderState(ManOWarEntity manOWar, ManOWarRenderState state, float tickProgress) {
		super.extractRenderState(manOWar, state, tickProgress);
		state.velocity = manOWar.getPrevVelocity().lerp(manOWar.getDeltaMovement(), tickProgress);
		state.tentacleData = manOWar.tentacleData;
		state.tentacleBox = manOWar.getTentacleBox();
		state.centerBoxPos = manOWar.getBoundingBox().getBottomCenter();
	}

	@Override
	public boolean shouldRender(ManOWarEntity entity, Frustum frustum, double x, double y, double z) {
		return super.shouldRender(entity, frustum, x, y, z) || frustum.isVisible(entity.getTentacleBox());
	}

	@Override
	protected void extractAdditionalHitboxes(ManOWarEntity entity, ImmutableList.Builder<HitboxRenderState> builder, float f) {
		super.extractAdditionalHitboxes(entity, builder, f);

		AABB tentacleBox = entity.getTentacleBox();

		builder.add(new HitboxRenderState(
			tentacleBox.minX - entity.getX(), tentacleBox.minY - entity.getY(), tentacleBox.minZ - entity.getZ(),
			tentacleBox.maxX - entity.getX(), tentacleBox.maxY - entity.getY(), tentacleBox.maxZ - entity.getZ(),
			0, 1, 0
		));
	}
}
