package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.client.render.entity.state.VariantState;
import dev.spiritstudios.abysm.data.variant.AbstractEntityVariant;
import dev.spiritstudios.abysm.world.entity.variant.Variantable;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;

public abstract class VariantMobRenderer<T extends Mob & Variantable<V>, V extends AbstractEntityVariant, S extends VariantState<V>,  M extends EntityModel<S>> extends MobRenderer<T, S, M> {
	public VariantMobRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
		super(context, model, shadowRadius);
	}

	@Override
	public void extractRenderState(T entity, S renderState, float partialTick) {
		super.extractRenderState(entity, renderState, partialTick);

		renderState.variant = entity.getVariant();
	}

	@Override
	public Identifier getTextureLocation(S renderState) {
		return renderState.variant.texture;
	}
}
