package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.state.MysteriousBlobRenderState;
import dev.spiritstudios.abysm.world.entity.depths.MysteriousBlobEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class MysteriousBlobEntityRenderer extends MobRenderer<MysteriousBlobEntity, MysteriousBlobRenderState, MysteriousBlobEntityRenderer.Model> {
	public static final Identifier HAPPY_TEXTURE_LOCATION = Abysm.id("textures/entity/mysterious_blob.png");
	public static final Identifier UNHAPPY_TEXTURE_LOCATION = Abysm.id("textures/entity/mysterious_blob_unhappy.png");

	public MysteriousBlobEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new Model(context), 1F);
	}

	@Override
	public MysteriousBlobRenderState createRenderState() {
		return new MysteriousBlobRenderState();
	}

	@Override
	public Identifier getTextureLocation(MysteriousBlobRenderState renderState) {
		return renderState.isHappy ? HAPPY_TEXTURE_LOCATION : UNHAPPY_TEXTURE_LOCATION;
	}

	@Override
	public void extractRenderState(MysteriousBlobEntity entity, MysteriousBlobRenderState renderState, float partialTick) {
		super.extractRenderState(entity, renderState, partialTick);
		renderState.isHappy = entity.isHappy();

		renderState.xScale = entity.lerpScaleXZ(partialTick);
		renderState.yScale = entity.lerpScaleY(partialTick);
		renderState.zScale = entity.lerpScaleXZ(partialTick);

	}

	public static class Model extends EntityModel<MysteriousBlobRenderState> {
		protected Model(EntityRendererProvider.Context context) {
			super(
				context.bakeLayer(new ModelLayerLocation(Abysm.id("mysterious_blob"), "main"))
			);
		}

		@Override
		public void setupAnim(MysteriousBlobRenderState renderState) {
			super.setupAnim(renderState);

			root.xScale += renderState.xScale;
			root.yScale += renderState.yScale;
			root.zScale += renderState.zScale;
		}
	}
}
