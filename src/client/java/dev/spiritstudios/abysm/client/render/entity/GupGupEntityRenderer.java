package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.state.VariantState;
import dev.spiritstudios.abysm.data.variant.GupGupVariant;
import dev.spiritstudios.abysm.world.entity.generic.GupGupEntity;
import dev.spiritstudios.spectre.api.client.model.animation.AnimationLocation;
import dev.spiritstudios.spectre.api.client.model.animation.SpectreKeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class GupGupEntityRenderer extends VariantMobRenderer<GupGupEntity, GupGupVariant, VariantState<GupGupVariant>, GupGupEntityRenderer.Model> {

	public GupGupEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new Model(context), 1F);
	}

	@Override
	public VariantState<GupGupVariant> createRenderState() {
		return new VariantState<>();
	}

	@Override
	public Identifier getTextureLocation(VariantState<GupGupVariant> renderState) {
		return renderState.variant.texture;
	}

	@Override
	public void extractRenderState(GupGupEntity entity, VariantState<GupGupVariant> renderState, float partialTick) {
		super.extractRenderState(entity, renderState, partialTick);

		renderState.query.set(entity, partialTick);
	}

	public static class Model extends EntityModel<VariantState<GupGupVariant>> {
		private final SpectreKeyframeAnimation swim;

		protected Model(EntityRendererProvider.Context context) {
			super(
				context.bakeLayer(new ModelLayerLocation(Abysm.id("gup_gup"), "main"))
			);

			this.swim = context.bakeAnimation(new AnimationLocation(Abysm.id("gup_gup"), "swim"), root());
		}

		@Override
		public void setupAnim(VariantState<GupGupVariant> renderState) {
			super.setupAnim(renderState);

			var query = renderState.query;

			swim.apply(query, renderState.ageInTicks, 1F);
		}
	}
}
