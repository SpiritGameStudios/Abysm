package dev.spiritstudios.abysm.client.render.entity.renderer;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.data.variant.SnapperEntityVariant;
import dev.spiritstudios.abysm.entity.generic.SnapperEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class SnapperEntityRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<SnapperEntity, R> {
	public static final DataTicket<SnapperEntityVariant> VARIANT_TICKET = DataTicket.create("snapper_variant_ticket", SnapperEntityVariant.class);

	public SnapperEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new SnapperEntityModel());
	}

	@Override
	public Identifier getTextureLocation(R renderState) {
		SnapperEntityVariant variant = renderState.getGeckolibData(VARIANT_TICKET);
		if (variant != null && variant.getTexture() != null) return variant.getTexture();

		return MissingSprite.getMissingSpriteId();
	}

	public static final class SnapperEntityModel extends DefaultedEntityGeoModel<SnapperEntity> {
		public SnapperEntityModel() {
			super(Abysm.id("snapper"), false);
		}

		@Override
		public void addAdditionalStateData(SnapperEntity animatable, GeoRenderState renderState) {
			super.addAdditionalStateData(animatable, renderState);
			renderState.addGeckolibData(VARIANT_TICKET, animatable.getVariant());
		}
	}
}
