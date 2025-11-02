package dev.spiritstudios.abysm.client.render.entity.renderer;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.variant.GupGupEntityVariant;
import dev.spiritstudios.abysm.entity.generic.GupGupEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class GupGupEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<GupGupEntity, R> {
	public static final DataTicket<GupGupEntityVariant> VARIANT_TICKET = DataTicket.create("gup_gup_variant_ticket", GupGupEntityVariant.class);

	public GupGupEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new GupGupEntityModel());
	}

	@Override
	public ResourceLocation getTextureLocation(R renderState) {
		GupGupEntityVariant variant = renderState.getGeckolibData(VARIANT_TICKET);
		if (variant != null && variant.getTexture() != null) return variant.getTexture();

		return MissingTextureAtlasSprite.getLocation();
	}

	public static final class GupGupEntityModel extends DefaultedEntityGeoModel<GupGupEntity> {
		public GupGupEntityModel() {
			super(Abysm.id("gup_gup"), false);
		}

		@Override
		public void addAdditionalStateData(GupGupEntity animatable, GeoRenderState renderState) {
			super.addAdditionalStateData(animatable, renderState);
			renderState.addGeckolibData(VARIANT_TICKET, animatable.getVariant());
		}
	}
}
