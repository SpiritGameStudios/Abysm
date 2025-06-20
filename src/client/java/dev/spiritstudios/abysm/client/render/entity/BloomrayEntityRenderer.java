package dev.spiritstudios.abysm.client.render.entity;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import dev.spiritstudios.abysm.entity.floralreef.BloomrayEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class BloomrayEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<BloomrayEntity, R> {
	public static final DataTicket<BloomrayEntityVariant> VARIANT_TICKET = DataTicket.create("bloomray_variant_ticket", BloomrayEntityVariant.class);
	public BloomrayEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new BloomrayEntityModel());
		this.withScale(3f);
	}

	@Override
	public Identifier getTextureLocation(R renderState) {
		BloomrayEntityVariant variant = renderState.getGeckolibData(VARIANT_TICKET);
		if(variant != null && variant.getTexture() != null) return variant.getTexture();
		return BloomrayEntityVariant.DEFAULT.getTexture();
	}

	public static class BloomrayEntityModel extends DefaultedEntityGeoModel<BloomrayEntity> {
		public BloomrayEntityModel() {
			super(Abysm.id("bloomray"), true);
		}

		@Override
		public void addAdditionalStateData(BloomrayEntity animatable, GeoRenderState renderState) {
			super.addAdditionalStateData(animatable, renderState);
			renderState.addGeckolibData(VARIANT_TICKET, animatable.getVariant());
		}
	}
}
