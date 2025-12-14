package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.state.VariantState;
import dev.spiritstudios.abysm.data.variant.SnapperEntityVariant;
import dev.spiritstudios.abysm.world.entity.generic.SnapperEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SnapperEntityRenderer extends VariantMobRenderer<SnapperEntity, SnapperEntityVariant, VariantState<SnapperEntityVariant>, SnapperEntityRenderer.Model> {
	public SnapperEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new Model(context), 1F);
	}

	@Override
	public VariantState<SnapperEntityVariant> createRenderState() {
		return new VariantState<>();
	}

	@Override
	public void extractRenderState(SnapperEntity entity, VariantState<SnapperEntityVariant> renderState, float partialTick) {
		super.extractRenderState(entity, renderState, partialTick);

		renderState.query.set(entity, partialTick);
	}

	public static class Model extends EntityModel<VariantState<SnapperEntityVariant>> {
		protected Model(EntityRendererProvider.Context context) {
			super(
				context.bakeLayer(new ModelLayerLocation(Abysm.id("snapper"), "main"))
			);
		}
	}
}
