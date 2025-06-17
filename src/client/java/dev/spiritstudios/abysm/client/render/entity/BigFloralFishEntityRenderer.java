package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.feature.EntityPatternFeatureRenderer;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.entity.floral_reef.BigFloralFishEntity;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class BigFloralFishEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends AbstractFishEntityRenderer<BigFloralFishEntity, R> {
	public BigFloralFishEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new BigFloralFishEntityModel());

		this.addRenderLayer(new EntityPatternFeatureRenderer<>(this));
	}

	@Override
	public Identifier getTextureLocation(R renderState) {
		EntityPattern pattern = renderState.getGeckolibData(EntityPatternFeatureRenderer.DATA_TICKET);
		if(pattern != null) {
			EntityPatternVariant variant = pattern.variant();
			if(variant != null) {
				if(variant.baseTexture().isPresent()) return variant.baseTexture().get();
			}
		}
		return super.getTextureLocation(renderState);
	}

	@Override
	public int getRenderColor(BigFloralFishEntity animatable, Void relatedObject, float partialTick) {
		EntityPattern pattern = animatable.getEntityPattern();
		if(pattern != null) {
			return pattern.baseColor();
		}
		return super.getRenderColor(animatable, relatedObject, partialTick);
	}

	public static class BigFloralFishEntityModel extends AbstractFishEntityModel<BigFloralFishEntity> {
		public BigFloralFishEntityModel() {
			super(Abysm.id("floral_fish_big"), true);
		}

		@Override
		public void addAdditionalStateData(BigFloralFishEntity animatable, GeoRenderState renderState) {
			super.addAdditionalStateData(animatable, renderState);
			renderState.addGeckolibData(EntityPatternFeatureRenderer.DATA_TICKET, animatable.getEntityPattern());
		}
	}
}
