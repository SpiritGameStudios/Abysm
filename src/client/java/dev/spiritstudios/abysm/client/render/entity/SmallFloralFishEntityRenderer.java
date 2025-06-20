package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.client.render.entity.feature.EntityPatternFeatureRenderer;
import dev.spiritstudios.abysm.data.pattern.EntityPatternVariant;
import dev.spiritstudios.abysm.entity.floralreef.SmallFloralFishEntity;
import dev.spiritstudios.abysm.entity.pattern.EntityPattern;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class SmallFloralFishEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends AbstractFishEntityRenderer<SmallFloralFishEntity, R> {
	public SmallFloralFishEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new SmallFloralFishEntityModel());

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
	public int getRenderColor(SmallFloralFishEntity animatable, Void relatedObject, float partialTick) {
		EntityPattern pattern = animatable.getEntityPattern();
		if(pattern != null) {
			return pattern.baseColor();
		}
		return super.getRenderColor(animatable, relatedObject, partialTick);
	}

	public static class SmallFloralFishEntityModel extends AbstractFishEntityModel<SmallFloralFishEntity> {
		public SmallFloralFishEntityModel() {
			super(Abysm.id("floral_fish_small"), true);
		}

		@Override
		public void addAdditionalStateData(SmallFloralFishEntity animatable, GeoRenderState renderState) {
			super.addAdditionalStateData(animatable, renderState);
			renderState.addGeckolibData(EntityPatternFeatureRenderer.DATA_TICKET, animatable.getEntityPattern());
		}
	}
}
